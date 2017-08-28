package org.lf.admin.action.console.catalog;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CYHPLX;
import org.lf.admin.db.pojo.VYHPLX;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.catalog.YHPLXService;
import org.lf.admin.service.catalog.YHPLXService.YHPEXCELTYPE;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.admin.service.utils.WXMediaService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 易耗品品种管理
 *
 */
@Controller
@RequestMapping("/console/catalog/yhppzgl/")
public class YHPPZController extends BaseController {

	private final String ROOT = "/console/catalog/yhppzgl";

	@Autowired
	private YHPLXService yhplxService;
	@Autowired
	private WXMediaService wxMediaService;

	/**
	 * 返回易耗品品种列表jsp页面
	 * 
	 * @return
	 */
	@RequestMapping("yhppzListUI.do")
	public String yhppzListUI() {
		return ROOT + "/yhppzListUI";
	}

	/**
	 * 显示v_yhplx中所有plx不为空的记录
	 * 
	 * @param session
	 * @param page
	 * @param rows
	 * @param pzmc
	 * @param flmc
	 * @return
	 */
	@RequestMapping("yhppzList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHPLX> yhppzList(HttpSession session, int page, int rows, String pzmc, String flmc) {
		VYHPLX param = new VYHPLX();
		param.setLx(pzmc);
		param.setPlx(flmc);
		param.setAppId(getAppId(session));
		return yhplxService.getLevel2PageZCLXList(param, rows, page);
	}

	/**
	 * 易耗品分类名称下拉列表框，默认为“全部”。
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("getYHPLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getYHPLXComboWithAll(HttpSession session) {
		return yhplxService.getLevel1YHPLXMCComboWithAll(getAppId(session));
	}

	/**
	 * 易耗品分类名称下拉列表框。
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("getYHPPZCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getYHPPZCombo(HttpSession session) {
		return yhplxService.getLevel1YHPLXMCCombo(getAppId(session));
	}

	/**
	 * 返回易耗品品种 插入 jsp页面
	 * 
	 * @return
	 */
	@RequestMapping("insertYHPPZUI.do")
	public String insertZCPZUI() {
		return ROOT + "/insertYHPPZUI";
	}

	/**
	 * 新建易耗品品种，名称非空唯一性检查
	 * 
	 * @param session
	 * @param mc
	 * @return
	 */
	@RequestMapping("checkYHPPZByMC.do")
	@ResponseBody
	public boolean checkYHPPZByMC(HttpSession session, String mc) {
		return yhplxService.checkYHPLXByMC(getAppId(session), mc);
	}

	/**
	 * 编辑易耗品品种，新名称非空唯一性检查
	 * 
	 * @param session
	 * @param oldMC
	 * @param newMC
	 * @return
	 */
	@RequestMapping("checkYHPPZByUpdateMC.do")
	@ResponseBody
	public boolean checkYHPPZByUpdateMC(HttpSession session, String oldMC, String newMC) {
		return yhplxService.checkYHPLXByUpdateMC(getAppId(session), oldMC, newMC);
	}

	/**
	 * 插入易耗品品种
	 * 
	 * @param session
	 * @param lxid
	 * @param lxpid
	 * @param mc
	 * @param remark
	 * @param imageFile
	 * @return
	 */
	@RequestMapping("insertYHPPZ.do")
	@ResponseBody
	public String insertYHPPZ(HttpSession session, String lxid, String lxpid, String mc, String remark,
			@RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			Integer appid = getAppId(session);
			// 根据appid生成文件前缀
			String prePath = ZCGLProperties.URL_YHPLX_TARGET_DIR + "/" + appid;
			String pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			yhplxService.insertYHPLX(getAppId(session), lxid != null ? lxid : lxpid, lxpid, mc, remark, pic_url, 0);

			// 通过mc,重新查询此记录，目的为了获得此记录的ID值
			CYHPLX param = new CYHPLX();
			param.setMc(mc);
			CYHPLX yhplx = yhplxService.getYHPLX(param);

			// 格式化lxid后更新记录
			String formatLXID = generateLXID(yhplx.getLxPid(), yhplx.getId() + "");
			yhplx.setLxId(formatLXID);
			yhplxService.updateYHPLX(yhplx.getId(), formatLXID, yhplx.getLxPid(), yhplx.getMc(), yhplx.getRemark(), pic_url, yhplx.getImgVersion() - 1);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	/**
	 * 生成8位的品种类型ID
	 * 
	 * @param lxid
	 * @return
	 */
	private String generateLXID(String lxpid, String id) {
		int length = 8 - id.length();
		lxpid = StringUtils.rpad(lxpid, '0', length);
		String formatLXID = lxpid + id;
		return formatLXID;
	}

	/**
	 * 编辑易耗品品种
	 * 
	 * @param id
	 * @param m
	 * @return
	 */
	@RequestMapping("updateYHPPZUI.do")
	public String updateYHPPZUI(Integer id, Model m) {
		CYHPLX param = new CYHPLX();
		param.setId(id);
		m.addAttribute("currYHPLXInfo", yhplxService.getYHPLX(param));
		return ROOT + "/updateYHPPZUI";
	}

	/**
	 * 更新一个易耗品品种
	 * 
	 * @param session
	 * @param id
	 * @param lxid
	 * @param lxpid
	 * @param mc
	 * @param remark
	 * @param imageFile
	 * @return
	 */
	@RequestMapping("updateYHPPZ.do")
	@ResponseBody
	public String updateYHPPZ(HttpSession session, Integer id, String lxid, String lxpid, String mc, String remark,
			@RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			// 先查出zclx表中的记录
			CYHPLX oldParam = new CYHPLX();
			oldParam.setId(id);

			CYHPLX yhplx = yhplxService.getYHPLX(oldParam);

			String pic_url = null;
			Integer imgVersion = yhplx.getImgVersion() == null ? 0 : yhplx.getImgVersion();// 批量插入的记录图片版本是null
			if (imageFile[0].getSize() != 0) {
				Integer appid = getAppId(session);
				// 根据appid生成文件前缀
				String prePath = ZCGLProperties.URL_YHPLX_TARGET_DIR + "/" + appid;
				pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
				imgVersion++;// 只要更新了图片，就增加版本号
			}
			yhplxService.updateYHPLX(id, lxid, lxpid, mc, remark, pic_url, imgVersion);
		} catch (OperException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 导出易耗品Excel模板
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping("exportYHPPZTempExcel.do")
	@ResponseBody
	public String exportYHPPZTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName = "yhppz_temp.xls";
		String filePath = session.getServletContext().getRealPath("") + "/upload/template/";
		File excel = new File(filePath + fileName);
		HSSFWorkbook wb;
		try {
			wb = new HSSFWorkbook(new POIFSFileSystem(excel));
			ExcelFileUtils.exportExcel(wb, response, fileName);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 易耗品品种 批量导入 jsp
	 * 
	 * @return
	 */
	@RequestMapping("insertYHPPZListUI.do")
	public String insertZCPZListUI() {
		return ROOT + "/insertYHPPZListUI";
	}

	/**
	 * 根据excel文件内容批量插入易耗品品种记录
	 * 
	 * @param excelFile
	 *            上传的excel文件
	 * @return
	 */
	@RequestMapping("insertYHPPZList.do")
	@ResponseBody
	public String insertYHPPZList(HttpSession session, @RequestParam(value = "excel_upload", required = false) MultipartFile[] excelFile) {
		try {
			yhplxService.insertYHPLXList(getAppId(session), excelFile[0], YHPEXCELTYPE.易耗品品种);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
