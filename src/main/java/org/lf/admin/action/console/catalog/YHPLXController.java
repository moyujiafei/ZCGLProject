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
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 易耗品类型管理
 *
 */
@Controller
@RequestMapping("/console/catalog/yhplxgl/")
public class YHPLXController extends BaseController {
	private final String ROOT = "/console/catalog/yhplxgl";

	@Autowired
	private YHPLXService yhplxService;
	@Autowired
	private WXMediaService wxMediaService;
	
	/**
	 * 打开测试页面
	 */
	@RequestMapping("testUI.do")
	public String testUI(){
		return "/test/test";
	}
	
	/**
	 * 打开查找易耗品类型界面
	 */
	@RequestMapping("queryYhplxUI.do")
	public String queryYhplxUI(Model m,@RequestParam(required=false,defaultValue="false")boolean isEdit){
		m.addAttribute("isEdit", isEdit);
		return ROOT+"/queryYHPLXUI";
	}
	
	/**
	 * 获取易耗品类型 easyuiTree
	 */
	@RequestMapping("getYhplxTree.do")
	@ResponseBody
	public List<EasyuiTree> getYhplxTree(HttpSession session,String yhplx_mc){
		if(yhplx_mc!=null){
			yhplx_mc=yhplx_mc.trim();
		}
		return yhplxService.getYHPLXTree(getAppId(session), yhplx_mc);
	}
	
	/**
	 * 根据主键查找易耗品类型
	 */
	@RequestMapping("queryYhplx.do")
	@ResponseBody
	public CYHPLX queryYhplx(HttpSession session,Integer id){
		CYHPLX param=new CYHPLX();
		param.setAppId(getAppId(session));
		param.setId(id);
		return yhplxService.getYHPLX(param);
	}

	/**
	 * 打开易耗品类型列表界面
	 */
	@RequestMapping("yhplxListUI.do")
	public String yhplxListUI() {
		return ROOT + "/yhplxListUI";
	}

	/**
	 * 显示v_yhplx中所有plx为空的记录
	 * 
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("yhplxList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHPLX> yhplxList(HttpSession session, int page, int rows) {
		return yhplxService.getLevel1PageYHPLXList(getAppId(session), rows, page);
	}

	/**
	 * 新建易耗品分类
	 * 
	 * @return
	 */
	@RequestMapping("insertYHPLXUI.do")
	public String insertYHPLXUI() {
		return ROOT + "/insertYHPLXUI";
	}

	/**
	 * 插入易耗品类型记录
	 * 
	 * @param session
	 * @param lxid
	 * @param mc
	 * @param remark
	 * @param imageFile
	 * @return
	 */
	@RequestMapping("insertYHPLX.do")
	@ResponseBody
	public String insertYHPLX(HttpSession session, String lxid, String mc, String remark, @RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			Integer appid = getAppId(session);
			// 根据appid生成文件前缀
			String prePath = ZCGLProperties.URL_YHPLX_TARGET_DIR + "/" + appid;
			String pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			yhplxService.insertYHPLX(getAppId(session), lxid, null, mc, remark, pic_url, 1);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	/**
	 * 批量导入易耗品分类
	 * 
	 * @return
	 */
	@RequestMapping("insertYHPLXListUI.do")
	public String insertZCLXListUI() {
		return ROOT + "/insertYHPLXListUI";
	}

	/**
	 * 通过Excel批量导入数据
	 */
	@RequestMapping("insertYHPLXList.do")
	@ResponseBody
	public String insertYHPLXList(HttpSession session, @RequestParam(value = "excel_upload", required = false) MultipartFile[] excelFile) {
		try {
			yhplxService.insertYHPLXList(getAppId(session), excelFile[0], YHPEXCELTYPE.易耗品类型);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	/**
	 * 新建易耗品分类，分类号非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkYHPLXByLXID.do")
	@ResponseBody
	public boolean checkYHPLXByLXID(HttpSession session, String lxid) {
		return yhplxService.checkYHPLXByLXID(getAppId(session), lxid);
	}

	/**
	 * 编辑资产品种
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("updateYHPLXUI.do")
	public String updateYHPLXUI(Integer id, Model model) {
		CYHPLX param = new CYHPLX();
		param.setId(id);
		CYHPLX Oldyhplx = yhplxService.getYHPLX(param);

		model.addAttribute("Oldyhplx", Oldyhplx);
		return ROOT + "/updateYHPLXUI";
	}

	/**
	 * 更新一个易耗品类型记录
	 * 
	 * @param session
	 * @param id
	 * @param mc
	 * @param remark
	 * @param imageFile
	 * @return
	 */
	@RequestMapping("updateYHPLX.do")
	@ResponseBody
	public String updateYHPLX(HttpSession session, Integer id, String mc, String remark, @RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
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
			yhplxService.updateYHPLX(id, yhplx.getLxId(), yhplx.getLxPid(), mc, remark, pic_url, imgVersion);
		} catch (OperException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 新建易耗品分类，名称非空唯一性检查
	 * 
	 * @param session
	 * @param mc
	 * @return
	 */
	@RequestMapping("checkYHPLXByMC.do")
	@ResponseBody
	public boolean checkYHPLXByMC(HttpSession session, String mc) {
		Integer appId = getAppId(session);
		return yhplxService.checkYHPLXByMC(appId, mc);
	}

	/**
	 * 编辑易耗品类型，新名称非空唯一性检查
	 * 
	 * @param session
	 * @param oldMC
	 * @param newMC
	 * @return
	 */
	@RequestMapping("checkYHPLXByUpdateMC.do")
	@ResponseBody
	public boolean checkYHPLXByUpdateMC(HttpSession session, String oldMC, String newMC) {

		return yhplxService.checkYHPLXByUpdateMC(getAppId(session), oldMC, newMC);
	}

	/**
	 * 查询易耗品图片
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("getPicUrl.do")
	@ResponseBody
	public String getPicUrl(Integer id) {
		CYHPLX param = new CYHPLX();
		param.setId(id);
		CYHPLX zclx = yhplxService.getYHPLX(param);
		return zclx.getPicUrl();
	}

	/**
	 * 下载易耗品类型表格模板
	 * 
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping("exportYHPLXTempExcel.do")
	@ResponseBody
	public String exportYHPLXTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName = "yhplx_temp.xls";
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
}
