package org.lf.admin.action.console.catalog;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.VJZW;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.JZLXService;
import org.lf.admin.service.catalog.JZWService;
import org.lf.admin.service.catalog.XQService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.admin.service.utils.TYBZService;
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

@Controller
@RequestMapping("/console/catalog/jzwgl/")
public class JZWController extends BaseController {
	private final String ROOT = "/console/catalog/jzwgl";
	
	@Autowired
	private JZWService jzwService;
	
	@Autowired
	private XQService xqService;
	
	@Autowired
	private JZLXService jzlxService;
	
	@Autowired
	private TYBZService tybzService;
	
	@RequestMapping("jzwListUI.do")
	public String jzwListUI() {
		return ROOT + "/jzwListUI";
	}
	
	/**
	 * 当参数都是null时显示所有的
	 * 建筑物使用前后模糊查询
	 * 
		select * from c_jzw where app_id = ? xq_id = ? and lx_id = ? and mc like '%...%';

	 * @return
	 */
	@RequestMapping("jzwList.do")
	@ResponseBody
	public EasyuiDatagrid<VJZW> jzwList(HttpSession session,String xqmc, String lxmc, String mc,int page, int rows) {
		if(xqmc == null && lxmc == null && mc == null){
			Integer appId = getAppId(session);
			VJZW param = new VJZW();
			param.setAppId(appId);
			return jzwService.getPageJZWList(param, rows, page);
		}else{
			if(StringUtils.isEmpty(xqmc)){
				xqmc = null;
			}
			if(StringUtils.isEmpty(lxmc)){
				lxmc = null;
			} 
			return jzwService.queryJZW(getAppId(session), xqmc, lxmc,mc,page,rows);
		}
	}
	
	/**
	 * 获得校区下拉框信息(默认为全部)
	 * 
	 * @return
	 */
	@RequestMapping("getXQComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getXQComboWithAll(HttpSession session) {
		return xqService.getXQMCComboWithAll(getAppId(session));
	}
	
	/**
	 * 获得校区下拉框信息(不包括全部)
	 * 
	 * @return
	 */
	@RequestMapping("getXQCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getXQCombo(HttpSession session) {
		return xqService.getXQCombo(getAppId(session));
	}
	
	/**
	 * 获得建筑类型下拉框信息(默认为全部)
	 * 
	 * @return
	 */
	@RequestMapping("getJZLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getJZLXComboWithAll() {
		return jzlxService.getJZLXMCComboWithAll();
	}
	
	/**
	 * 获得建筑类型下拉框信息(不包括全部)
	 * 
	 * @return
	 */
	@RequestMapping("getJZLXCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getJZLXCombo() {  //HttpSession session
		return jzlxService.getJZLXCombo();
	}
	
	/**
	 * 获得停用标志下拉框信息，停用（1）/启用（0）
	 * 
	 * @return
	 */
	@RequestMapping("getTYBZCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getTYBZCombo() {
		return tybzService.getTYBZCombo();
	}
	
	
	/**
	 * 跳转更新建筑物页面
	 * @param id
	 * @param m
	 * @return
	 */
	@RequestMapping("updateJZWUI.do")
	public String updateJZWUI(Integer id, Model m) {
		m.addAttribute("id", id);
		return ROOT + "/updateJZWUI";
	}
	/**
	 * 编辑建筑物
	 * 
	 * @param id
	 * @param xqmc
	 * @param lxmc
	 * @param mc
	 * @param dz
	 * @param tybz
	 * @return
	 */
	@RequestMapping("updateJZW.do")
	@ResponseBody
	public String updateJZW(Integer id, Integer xqid, Integer lxid, String mc, String dz, Integer tybz) {
		try {
			jzwService.updateJZW(id, xqid, lxid, mc, dz, null, tybz);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("checkJZWByUpdateMC.do")
	@ResponseBody
	public boolean checkJZWByUpdateMC(HttpSession session, String oldJZWMC, String newJZWMC) { 
		try {
			return jzwService.checkJZWByUpdateMC(getAppId(session),oldJZWMC, newJZWMC);
		} catch (OperException e) {
			return false;
		}
		
	}
	/**
	 * 删除建筑物
	 * @param id
	 * @return
	 */
	@RequestMapping("delJZW.do")
	@ResponseBody
	public String delJZW(Integer id) {
		CJZW param = new CJZW();
		param.setId(id);
		try {
			jzwService.delJZW(param);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 跳转新增建筑物页面
	 * @return
	 */
	@RequestMapping("insertJZWUI.do")
	public String insertJZWUI() {
		return ROOT + "/insertJZWUI";
	}
	
	/**
	 * 唯一性检查
	 * @param session
	 * @param mc
	 * @return
	 */
	@RequestMapping("checkJZWByMC.do")
	@ResponseBody
	public boolean checkJZWByMC(HttpSession session, String mc) {
		return jzwService.checkJZWByMC(getAppId(session),mc);
	}
	
	/**
	 * 新增建筑物
	 * @return
	 */
	@RequestMapping("insertJZW.do")
	@ResponseBody
	public String insertJZW(HttpSession session,Integer xqId,Integer lxId, String jzw, String dz, Integer tybz) {
		try {
			jzwService.insertJZW(getAppId(session), xqId, lxId, jzw, dz, tybz);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("importJZWExcelUI.do")
	public String importJZWExcelUI() {
		return ROOT + "/importJZWExcelUI";
	}

	/**
	 * 上传文件
	 * 
	 * @param session
	 * @param file_upload
	 * @return
	 */
	@RequestMapping("importJZWExcel.do")
	@ResponseBody
	public String importJZWExcel(HttpSession session, @RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		try {
			jzwService.insertJZWList(getAppId(session), file_upload);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("exportJZWExcel.do")
	@ResponseBody
	public String exportJZWExcel(HttpSession session, HttpServletResponse response) {
		VJZW param = new VJZW();
		param.setAppId(getAppId(session));
		String fileName = "jzw.xls";
		List<VJZW> jzwList = jzwService.getJZWList(param);
		HSSFWorkbook wb = null;
		try {
			wb = jzwService.exportJZWList(jzwList);
			ExcelFileUtils.exportExcel(wb,response, fileName);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("exportJZWTempExcel.do")
	@ResponseBody
	public String exportJZWTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName  = "jzw_temp.xls";
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
