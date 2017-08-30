package org.lf.admin.action.console.catalog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VFJ;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.JZWService;
import org.lf.admin.service.catalog.XQService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.admin.service.utils.TYBZService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.EasyuiTree;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/console/catalog/fjgl/")
public class FJController extends BaseController {
	private final String ROOT = "/console/catalog/fjgl";
	
	@Autowired
	private FJService fjService;
	
	@Autowired
	private JZWService jzwService;
	
	@Autowired
	private XQService xqService;
	
	@Autowired
	private TYBZService tybzService;
	
	@RequestMapping("fjListUI.do")
	public String fjListUI() {
		return ROOT + "/fjListUI";
	}

	/**
	 * 通过建筑物名称模糊查找
	 * 
		select * from v_fj where jzw like '%...%';
	 * 
	 * @param session
	 * @param jzwmc
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("fjList.do")
	@ResponseBody
	public EasyuiDatagrid<VFJ> fjList(HttpSession session, String jzw, int page, int rows) {
		if(StringUtils.isEmpty(jzw)){
			Integer appId = getAppId(session);
			VFJ param = new VFJ();
			param.setAppId(appId);
			return fjService.getPageFJList(param, rows, page);
		}else{
			return fjService.queryFJ(jzw,page,rows);
		}
		
	}
	
	@RequestMapping("insertFJUI.do")
	public String insertFJUI() {
		return ROOT + "/insertFJUI";
	}

	
	/**
	 * 获得校区下拉框信息，默认为第一个校区
	 * 
	 * @return
	 */
	@RequestMapping("getXQCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getXQCombo(HttpSession session) {
		return xqService.getXQCombo(getAppId(session));
	}
	
	/**
	 * 获得建筑类型下拉框信息。根据校区（xqid）联动获得
	 * 
	 * @return
	 */
	@RequestMapping("getJZWCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getJZWCombo(HttpSession session, Integer xqid) {
		return jzwService.getJZWCombo(getAppId(session),xqid);
	}
	
	/**
	 * 获得楼层下拉框信息。
	 * 
select distinct floor
from c_fj
where app_id = ?
	 * 
	 * @return
	 */
	@RequestMapping("getFloorCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getFloorCombo(HttpSession session) {
		return fjService.getFloorCombo(getAppId(session));
	}
	
	/**
	 * 检查房间号（room）在指定建筑物（jzwid）下是否唯一
	 * @param session
	 * @param fj
	 * @return
	 */
	@RequestMapping("checkFJByRoom.do")
	@ResponseBody
	public boolean checkFJByRoom(HttpSession session, Integer jzwid, String room) {
		return fjService.checkFJByRoom(getAppId(session),jzwid,room);
	}
	
	/**
	 * 获得停用标志下拉框信息，停用（1）/启用（0）
	 * 
	 * @return
	 */
	@RequestMapping("getTYBZCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getTYBZCombo(HttpSession session) {
		return tybzService.getTYBZCombo();
	}
	
	@RequestMapping("insertFJ.do")
	@ResponseBody
	public String insertFJ(HttpSession session, Integer jzwid, String floor,  String room, String deptName, String glr, Integer tybz) {
		try {
			fjService.insertFJ(getAppId(session),jzwid,floor,room,deptName,glr,tybz);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("updateFJUI.do")
	public String updateFJUI(Integer id, Model m) {
		m.addAttribute("id", id);
		return ROOT + "/updateFJUI";
	}
	
	
	@RequestMapping("checkFJByUpdateRoom.do")
	@ResponseBody
	public boolean checkFJByUpdateRoom(HttpSession session, Integer jzwid, String newRoom, String oldRoom) {
		return fjService.checkFJByUpdateRoom(getAppId(session),jzwid,newRoom,oldRoom);
	}
	
	@RequestMapping("updateFJ.do")
	@ResponseBody
	public String updateFJ(Integer id,Integer jzwId ,String floor, String room, String deptName, String glr, Integer tybz) {
		try {
			fjService.updateFJ(id, jzwId, floor, room, deptName, glr, tybz);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("delFJ.do")
	@ResponseBody
	public String delFJ(Integer id) {
		try {
			fjService.delFJ(id);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("importFJExcelUI.do")
	public String importFJExcelUI() {
		return ROOT + "/importFJExcelUI";
	}
	
	@RequestMapping("importFJExcel.do")
	@ResponseBody
	public String importFJExcel(HttpSession session, @RequestParam(value = "file_upload", required = false) MultipartFile file_upload) {
		try {
			fjService.insertFJList(getAppId(session), file_upload);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("exportFJExcel.do")
	@ResponseBody
	public String exportFJExcel(HttpSession session, HttpServletResponse response) {
		String fileName  = "fj.xls";
		HSSFWorkbook wb = null;
		VFJ param = new VFJ();
		List<VFJ> fjList = new ArrayList<VFJ>();
		try {
			param.setAppId(getAppId(session));
			fjList = fjService.getFJList(param);
			wb = fjService.exportFJList(fjList);
			ExcelFileUtils.exportExcel(wb, response, fileName);
			return SUCCESS;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 查询存放地点
	 * @return
	 */
	@RequestMapping("queryCFDDUI.do")
	public String queryCFDDUI() {
		return ROOT + "/queryCFDDUI";
	}
	
	@RequestMapping("queryJZW.do")
	@ResponseBody
	public List<EasyuiTree> queryJZW(HttpSession session, Integer xqid,Integer jzwid) {
		VFJ param = new VFJ();
		Integer appId = getAppId(session);
		param.setAppId(appId);
		param.setXqId(xqid);
		param.setJzwId(jzwid);
		return fjService.queryJZW(param);
	}
	
	@RequestMapping("queryCFDD.do")
	@ResponseBody
	public VFJ queryCFDD(HttpSession session,Integer fjid) {
		VFJ param = new VFJ();
		Integer appId = getAppId(session);
		param.setAppId(appId);
		param.setFjId(fjid);
		return fjService.getFJ(param);
	}
	
	@RequestMapping("exportFJTempExcel.do")
	@ResponseBody
	public String exportFJTempExcel(HttpSession session, HttpServletResponse response) {
		String fileName  = "fj_temp.xls";
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
