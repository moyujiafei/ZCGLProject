package org.lf.admin.action.console.catalog;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.db.pojo.VZCLX;
import org.lf.admin.service.OperException;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.catalog.ZCLXService.EXCELTYPE;
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
 * 资产品种管理
 *
 */
@Controller
@RequestMapping("/console/catalog/zcpzgl/")
public class ZCPZController extends BaseController {
	private final String ROOT = "/console/catalog/zcpzgl";

	@Autowired
	private ZCLXService zclxService;
	@Autowired
	private WXMediaService wxMediaService;

	/**
	 * 返回资产品种列表jsp页面
	 * @return
	 */
	@RequestMapping("zcpzListUI.do")
	public String zcpzListUI() {
		return ROOT + "/zcpzListUI";
	}

	/**
	 * 显示v_zclx中所有LX_PID不为空的记录
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("zcpzList.do")
	@ResponseBody
	public EasyuiDatagrid<VZCLX> zcpzList(HttpSession session, int page, int rows, String pzmc, String flmc) {
		VZCLX param = new VZCLX();
		param.setLx(pzmc);
		param.setPlx(flmc);
		param.setAppId(getAppId(session));
		return zclxService.getLevel2PageZCLXList(param, rows, page);
	}

	/**
	 * 资产分类名称下拉列表框，默认为“全部”。
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCLXComboWithAll(HttpSession session) {
		return zclxService.getLevel1ZCLXMCComboWithAll(getAppId(session));
	}

	/**
	 * 资产分类名称下拉列表框。
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCPZCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCPZCombo(HttpSession session) {
		return zclxService.getLevel1ZCLXMCCombo(getAppId(session));
	}

	/**
	 * 依据用户输入的分类名称，品种名称，查询v_zcpz中所有LX_PID不为空的记录 select * from v_zclx where plx =
	 * ? and lx like '%...%';
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("queryZCPZList.do")
	@ResponseBody
	public EasyuiDatagrid<VZCLX> queryZCPZList(String plx, String mc, int page, int rows) {
		VZCLX param = new VZCLX();
		param.setPlx(plx);
		param.setLx(mc);
		return zclxService.getLevel2PageZCLXList(param, rows, page);
	}

	/**
	 * 新建资产品种，品种号非空唯一性检查。在appId下唯一性。
	 * 
	 * @return
	 */
	@RequestMapping("checkZCPZByLXID.do")
	@ResponseBody
	public boolean checkZCPZByLXID(HttpSession session, String lxid) {
		return zclxService.checkZCLXByLXID(getAppId(session), lxid);
	}

	/**
	 * 新建资产品种，名称非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkZCPZByMC.do")
	@ResponseBody
	public boolean checkZCPZByMC(HttpSession session, String mc) {
		return zclxService.checkZCLXByMC(getAppId(session), mc);
	}

	/**
	 * 新建资产产品种，zjnx数值型检查。必须为一个大于0的值。
	 * 
	 * @return
	 */
	@RequestMapping("checkZCPZByZJNX.do")
	@ResponseBody
	public boolean checkZCPZByZJNX(HttpSession session, BigDecimal zjnx) {
		return zclxService.checkZCPZByZJNX(getAppId(session), zjnx);
	}

	/**
	 * 返回资产品种 插入 jsp页面
	 * @return
	 */
	@RequestMapping("insertZCPZUI.do")
	public String insertZCPZUI() {
		return ROOT + "/insertZCPZUI";
	}
	
	/**
	 * 返回资产品种 批量导入 jsp页面
	 * @return
	 */
	@RequestMapping("insertZCPZListUI.do")
	public String insertZCPZListUI() {
		return ROOT + "/insertZCPZListUI";
	}

	/**
	 * 
	 * @param session
	 *            取appId
	 * @param lxid
	 *            品种号
	 * @param lxpid
	 * @param mc
	 * @param zjnx
	 * @param remark
	 * @return
	 */
	@RequestMapping("insertZCPZ.do")
	@ResponseBody
	public String insertZCPZ(HttpSession session, String lxid, String lxpid, String mc, BigDecimal zjnx, String remark,
			@RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			Integer appid = getAppId(session);
			// 根据appid生成文件前缀
			String prePath = ZCGLProperties.URL_ZCLX_TARGET_DIR + "/" + appid;
			String pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			zclxService.insertZCLX(getAppId(session), lxid != null ? lxid : lxpid, lxpid, mc, zjnx, remark, pic_url, 0);

			// 通过mc,重新查询此记录，目的为了获得此记录的ID值
			CZCLX param = new CZCLX();
			param.setMc(mc);
			CZCLX zclx = zclxService.getZCLX(param);

			// 格式化lxid后更新记录
			String formatLXID = generateLXID(zclx.getLxPid(), zclx.getId() + "");
			zclx.setLxId(formatLXID);
			zclxService.updateZCLX(zclx.getId(), formatLXID, zclx.getLxPid(), zclx.getMc(), zclx.getZjnx(), zclx.getRemark(), pic_url, zclx.getImgVersion() - 1);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 根据excel文件内容批量插入资产品种记录
	 * @param excelFile 上传的excel文件
	 * @return
	 */
	@RequestMapping("insertZCPZList.do")
	@ResponseBody
	public String insertZCPZList(HttpSession session, @RequestParam(value = "excel_upload", required = false) MultipartFile[] excelFile) {
		try {
			zclxService.insertZCLXList(getAppId(session), excelFile[0], EXCELTYPE.资产品种);
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
	 * 编辑资产品种，新名称非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkZCPZByUpdateMC.do")
	@ResponseBody
	public boolean checkZCPZByUpdateMC(HttpSession session, String oldMC, String newMC) {
		return zclxService.checkZCLXByUpdateMC(getAppId(session), oldMC, newMC);
	}

	/**
	 * 编辑资产品种
	 * 
	 * @return
	 */
	@RequestMapping("updateZCPZUI.do")
	public String updateZCPZUI(Integer id, Model m) {
		CZCLX param = new CZCLX();
		param.setId(id);
		m.addAttribute("currZCLXInfo", zclxService.getZCLX(param));
		return ROOT + "/updateZCPZUI";
	}

	/**
	 * 更新一个资产品种
	 * 
	 * @return
	 */
	@RequestMapping("updateZCPZ.do")
	@ResponseBody
	public String updateZCPZ(HttpSession session, Integer id, String lxid, String lxpid, String mc, BigDecimal zjnx, String remark,
			@RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			String pic_url = null;
			if (imageFile[0].getSize() != 0) {// 如果有文件，即文件大小大于0
				Integer appid = getAppId(session);
				// 根据appid生成文件前缀
				String prePath = ZCGLProperties.URL_ZCLX_TARGET_DIR + "/" + appid;
				pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			}
			if (lxpid != null) {// 更新资产品种
				// 格式化lxid后更新记录
				String formatLXID = generateLXID(lxpid, id + "");
				zclxService.updateZCLX(id, formatLXID, lxpid, mc, zjnx, remark, pic_url, 0);
			} else {// 更新资产分类
				zclxService.updateZCLX(id, lxid, lxpid, mc, zjnx, remark, pic_url, 0);
			}
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("exportZCPZTempExcel.do")
	@ResponseBody
	public String exportZCPZTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName  = "zcpz_temp.xls";
		String filePath = session.getServletContext().getRealPath("") + "/upload/template/";
		File excel = new File(filePath+fileName);
		HSSFWorkbook wb;
		try {
			wb = new HSSFWorkbook(new POIFSFileSystem(excel));
			ExcelFileUtils.exportExcel(wb, response, fileName);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
