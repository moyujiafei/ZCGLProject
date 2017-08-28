package org.lf.admin.action.console.catalog;

import java.io.File;
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
 * 资产类型管理
 *
 */
@Controller
@RequestMapping("/console/catalog/zclxgl/")
public class ZCLXController extends BaseController {
	private final String ROOT = "/console/catalog/zclxgl";

	@Autowired
	private ZCLXService zclxService;
	@Autowired
	private WXMediaService wxMediaService;

	/**
	 * 打开查询资产类型对话框
	 * @param m
	 * @param isEdit
	 * @return
	 */
	@RequestMapping("queryZCLXUI.do")
	public String queryZCLXUI(Model m,@RequestParam(required=false,defaultValue="false")boolean isEdit){
		m.addAttribute("isEdit", isEdit);
		return ROOT+"/queryZCLXUI";
	}
	
	/**
	 * 获取资产类型树
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXTree.do")
	@ResponseBody
	public List<EasyuiTree> getZCLXTree(HttpSession session,String zclx_mc){
		if (zclx_mc != null) {
			zclx_mc = zclx_mc.trim();
		}
		return zclxService.getZCLXTree(getAppId(session),zclx_mc);
	}
	
	@RequestMapping("queryZCLX.do")
	@ResponseBody
	public CZCLX queryZCLX(HttpSession session,Integer id){
		CZCLX param=new CZCLX();
		param.setAppId(getAppId(session));
		param.setId(id);
		return zclxService.getZCLX(param);
	}
	
	@RequestMapping("zclxListUI.do")
	public String zclxListUI() {
		return ROOT + "/zclxListUI";
	}

	/**
	 * 显示v_zclx中所有LX_PID为空的记录
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("zclxList.do")
	@ResponseBody
	public EasyuiDatagrid<VZCLX> zclxList(HttpSession session, int page, int rows) {
		return zclxService.getLevel1PageZCLXList(getAppId(session), rows, page);
	}

	/**
	 * 新建资产分类
	 * 
	 * @return
	 */
	@RequestMapping("insertZCLXUI.do")
	public String insertZCLXUI() {
		return ROOT + "/insertZCLXUI";
	}
	/**
	 * 批量导入资产分类
	 * 
	 * @return
	 */
	@RequestMapping("insertZCLXListUI.do")
	public String insertZCLXListUI() {
		return ROOT + "/insertZCLXListUI";
	}

	/**
	 * 新建资产分类，分类号非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkZCLXByLXID.do")
	@ResponseBody
	public boolean checkZCLXByLXID(HttpSession session, String lxid) {
		return zclxService.checkZCLXByLXID(getAppId(session), lxid);
	}

	/**
	 * 新建资产分类，名称非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkZCLXByMC.do")
	@ResponseBody
	public boolean checkZCLXByMC(HttpSession session, String mc) {
		Integer appId = getAppId(session);
		return zclxService.checkZCLXByMC(appId, mc);
	}

	@RequestMapping("insertZCLX.do")
	@ResponseBody
	public String insertZCLX(HttpSession session, String lxid, String mc, String remark, @RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			Integer appid = getAppId(session);
			// 根据appid生成文件前缀
			String prePath = ZCGLProperties.URL_ZCLX_TARGET_DIR + "/" + appid;
			String pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			zclxService.insertZCLX(getAppId(session), lxid, null, mc, null, remark, pic_url, 0);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 通过Excel批量导入数据
	 */
	@RequestMapping("insertZCLXList.do")
	@ResponseBody
	public String insertZCLXList(HttpSession session, @RequestParam(value = "excel_upload", required = false) MultipartFile[] excelFile) {
		try {
			zclxService.insertZCLXList(getAppId(session), excelFile[0], EXCELTYPE.资产类型);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	/**
	 * 编辑资产品种
	 * 
	 * @return
	 */
	@RequestMapping("updateZCLXUI.do")
	public String updateZCLXUI(Integer id, Model model) {
		CZCLX param = new CZCLX();
		param.setId(id);
		CZCLX Oldzclx = zclxService.getZCLX(param);

		model.addAttribute("Oldzclx", Oldzclx);
		return ROOT + "/updateZCLXUI";
	}

	/**
	 * 编辑资产品种，新名称非空唯一性检查
	 * 
	 * @return
	 */
	@RequestMapping("checkZCLXByUpdateMC.do")
	@ResponseBody
	public boolean checkZCLXByUpdateMC(HttpSession session, String oldMC, String newMC) {

		return zclxService.checkZCLXByUpdateMC(getAppId(session), oldMC, newMC);
	}

	/**
	 * 更新一个资产品种
	 * 
	 * @return
	 */
	@RequestMapping("updateZCLX.do")
	@ResponseBody
	public String updateZCLX(HttpSession session, Integer id, String mc, String remark, @RequestParam(value = "image_upload", required = false) MultipartFile[] imageFile) {
		try {
			// 先查出zclx表中的记录
			CZCLX oldParam = new CZCLX();
			oldParam.setId(id);

			CZCLX zclx = zclxService.getZCLX(oldParam);

			String pic_url = null;
			if (imageFile[0].getSize() != 0) {
				Integer appid = getAppId(session);
				// 根据appid生成文件前缀
				String prePath = ZCGLProperties.URL_ZCLX_TARGET_DIR + "/" + appid;
				pic_url = wxMediaService.uploadMediaListToPath(session, prePath, WXMediaService.MAX_IMAGE_SIZE, WXMediaService.IMAGE_SUFFIX, imageFile);
			}
			zclxService.updateZCLX(id, zclx.getLxId(), zclx.getLxPid(), mc, zclx.getZjnx(), remark, pic_url, zclx.getImgVersion());
		} catch (OperException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 查询一级资产图片
	 */
	@RequestMapping("getPicUrl.do")
	@ResponseBody
	public String getPicUrl(Integer id) {
		CZCLX param = new CZCLX();
		param.setId(id);
		CZCLX zclx = zclxService.getZCLX(param);
		return zclx.getPicUrl();
	}
	
	/**
	 * 下载资产类型表格模板
	 * @param session
	 * @param response
	 * @return
	 */
	@RequestMapping("exportZCLXTempExcel.do")
	@ResponseBody
	public String exportZCLXTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName  = "zclx_temp.xls";
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
