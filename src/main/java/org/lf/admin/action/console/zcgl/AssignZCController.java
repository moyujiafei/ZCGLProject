package org.lf.admin.action.console.zcgl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.zcgl.ZCFPService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资产分配
 * 
 * 部门资产管理员进行资产分配和申请归还资产。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/assign/")
public class AssignZCController extends BaseController {
	private final String ROOT = "/console/zcgl/assign";

	@Autowired
	private ZCFPService zcfpService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产分配界面
	 * @return
	 */
	@RequestMapping("assignZCListUI.do")
	public String assignZCListUI() {
		return ROOT + "/assignZCListUI";
	}
	
	/**
	 * 资产类型下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCLXComboWithAll(HttpSession session) {
		return zclxService.getZCLXMCComboWithAll(getAppId(session));
	}
	
	/**
	 * 资产管理部门下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCBMComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCBMComboWithAll(HttpSession session) {
		return zcglService.getZCBMNameComboWithAll(getAppId(session));
	}
	
	/**
	 * 查询v_zc，获取资产状态为“未使用、领用中”的资产列表。
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("zcList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> zcList(HttpSession session, String zcmc, String zclx, Integer deptNo,int page, int rows) {
		String glr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		if (StringUtils.isEmpty(zcmc)) {
			zcmc = null;
		}
		if (StringUtils.isEmpty(zclx)) {
			zclx = null;
		}
		List<Integer> zcztList = new ArrayList<>();
		zcztList.add(ZCZT.领用中.getValue());
		zcztList.add(ZCZT.未使用.getValue());
		
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclx, null, null, deptNo, glr, zcztList, page, rows);
	}
	
	/**
	 * 资产分配
	 * @return
	 */
	@RequestMapping("assignZCUI.do")
	public String assignZCUI() {
		return ROOT + "/assignZCUI";
	}
	
	@RequestMapping("assignZC.do")
	@ResponseBody
	public String assignZC(HttpSession session, Integer zcid, String syr) {
		String czr = getCurrUser(session).getWxUsername();
		try {
			zcfpService.assignZC(zcid, czr, syr);
			return SUCCESS;
		} catch (OperException e) {	
			return e.getMessage();
		}
	}
	
	/**
	 * 申请归还
	 * @return
	 */
	@RequestMapping("revertZCUI.do")
	public String revertZCUI() {
		return ROOT + "/revertZCUI";
	}
	
	@RequestMapping("revertZC.do")
	@ResponseBody
	public String revertZC(HttpSession session, Integer zcid, String remark) {
		String czr = getCurrUser(session).getWxUsername();
		
		try {
			zcfpService.revertZC(zcid, czr, remark.trim());
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 资产重新分配
	 * @return
	 */
	@RequestMapping("reassignZCUI.do")
	public String reassignZCUI() {
		return ROOT + "/reassignZCUI";
	}
	
	@RequestMapping("reassignZC.do")
	@ResponseBody
	public String reassignZC(HttpSession session, Integer zcid, String newSyr, String remark) {
		String czr = getCurrUser(session).getWxUsername();
		
		try {
			zcfpService.reassignZC(zcid, czr, newSyr, remark.trim());
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
