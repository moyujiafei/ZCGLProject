package org.lf.admin.action.console.zcpd;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.BMZC_GZSJStat;
import org.lf.admin.db.pojo.BMZC_ZTStat;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.lf.admin.service.zcgl.BMZC_GZSJStatService;
import org.lf.admin.service.zcgl.BMZC_ZCFLStatService;
import org.lf.admin.service.zcgl.BMZC_ZTStatService;
import org.lf.utils.EasyuiComboBoxItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后勤管理人员，统计各部门资产情况
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcpd/bmzcStat/")
public class BMZCStatController extends BaseController {
	private final String ROOT = "/console/zcpd/bmzcStat";
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;

	@Autowired
	private BMZC_ZTStatService bmzcStatService;
			
	@Autowired
	private BMZC_GZSJStatService bmzc_GZSJStatService;
	
	@Autowired
	private BMZC_ZCFLStatService bmzc_ZCFLStatService;
	
	/**
	 * 选择要查看的部门：默认为全部。
	 * @param session
	 * @return
	 */
	@RequestMapping("getDeptNameComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getDeptNameComboWithAll(HttpSession session) {
		return zcglService.getZCBMNameComboWithAll(getAppId(session));
	}
	
	/**
	 * 	部门资产——资产状态统计
	 * @return
	 */
	@RequestMapping("BMZC_ZTStatUI.do")
	public String BMZC_ZTStatUI() {
		return ROOT + "/BMZC_ZTStatUI";
	}
						
	/**
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("getZCZTStat.do")
	@ResponseBody
	public BMZC_ZTStat getZCZTStat(HttpSession session, Integer deptNo) {			
		Integer appId=getAppId(session);	
		return bmzcStatService.getZCZTStat(appId,deptNo);
	}		
	
//	@RequestMapping("exportZCZTStat.do")
//	@ResponseBody
//	public String exportZCZTStat(HttpSession session, HttpServletResponse response) {
//		String fileName  = "bmzc_zt.xls";
//		HSSFWorkbook wb = null;
//		VZC param=new VZC();
//		param.setAppId(getAppId(session));
//		List<BMZC_ZTStat> bmzcStatList=bmzcStatService.getZCZTStatList(param);
//		try{
//			wb=bmzcStatService.exportBmzcStatList(bmzcStatList);
//			ExcelFileUtils.exportExcel(wb, response, fileName);
//			return SUCCESS;
//		}catch (Exception e){
//			return e.getMessage();
//		}
//	}
	
	/**
	 * 部门资产——购置时间统计
	 * @return
	 */
	@RequestMapping("BMZC_GZSJStatUI.do")
	public String BMZC_GZSJStatUI() {
		return ROOT + "/BMZC_GZSJStatUI";
	}

	/**
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("getGZSJStat.do")
	@ResponseBody
	public BMZC_GZSJStat getGZSJStat(HttpSession session, Integer deptNo) {
		Integer appId=getAppId(session);
		return bmzc_GZSJStatService.getGZSJStat(appId, deptNo);
	}
	
	@RequestMapping("exportGZSJStat.do")
	@ResponseBody
	public String exportGZSJStat(HttpSession session, HttpServletResponse response) {
		String fileName  = "bmzc_gzsj.xls";
		HSSFWorkbook wb = null;
        VZC param=new VZC();
        param.setAppId(getAppId(session));
        List<BMZC_GZSJStat> gzsjStatList=bmzc_GZSJStatService.getGZSJStatList(param);
        try{
        	wb=bmzc_GZSJStatService.exportGzsjStatList(gzsjStatList);
			ExcelFileUtils.exportExcel(wb, response, fileName);
			return SUCCESS;        	
        }catch(Exception e){
        	return e.getMessage();
        }
	}
	
	/**
	 * 部门资产——资产分类统计
	 * @return
	 */
	@RequestMapping("BMZC_ZCFLStatUI.do")
	public String BMZC_ZCFLStatUI() {
		return ROOT + "/BMZC_ZCFLStatUI";
	}

	/**
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("getZCFLStat.do")
	@ResponseBody
	public ConcurrentHashMap<String, Object> getZCFLStat(HttpSession session, Integer deptNo) {
		Integer appId=getAppId(session);
		try {
			return bmzc_ZCFLStatService.getZCFLStat(appId, deptNo);
		} catch (OperException e) {
			return new ConcurrentHashMap<>();
		}
	}
		
//	@RequestMapping("exportZCFLStat.do")
//	@ResponseBody
//	public String exportZCFLStat(HttpSession session, HttpServletResponse response) {
//		String fileName  = "bmzc_zcfl.xls";
//		HSSFWorkbook wb = null;
//		CZCGL zcgl=new CZCGL();
//		zcgl.setAppId(getAppId(session));
//		List<ConcurrentHashMap<String, Object>> zcflStatList=bmzc_ZCFLStatService.getZCFLStatList(zcgl);
//		 try{
//	        	wb=bmzc_ZCFLStatService.exportZcflStatList(zcflStatList);
//				ExcelFileUtils.exportExcel(wb, response, fileName);
//				return SUCCESS;        	
//	        }catch(Exception e){
//	        	return e.getMessage();
//	        }				
//	}
	
}
