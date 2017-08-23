package org.lf.admin.action.console.zcgl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCYWService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资产报废申请。
 * 后勤管理人员同意、拒绝提交的资产报废申请。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/bfsq/")
public class BFSQController extends BaseController {
	private final String ROOT = "/console/zcgl/bfsq";

	@Autowired
	private ZCYWService zcywService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产报废界面
	 * @return
	 */
	@RequestMapping("bfsqListUI.do")
	public String bfsqListUI() {
		return ROOT + "/bfsqListUI";
	}

	/**
	 * 查询v_zc，获取所有资产状态为“申请报废”的资产列表。
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("bfsqList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> bfsqList(HttpSession session, String zcmc, String zclx, Integer deptNo, int page, int rows) {
		Integer appId=getAppId(session);
		if(StringUtils.isEmpty(zclx)){
			zclx=null;
		}
		if(StringUtils.isEmpty(zcmc)){
			zcmc=null;
		}
		List<Integer> zcztList=new ArrayList<>();
		zcztList.add(ZCZT.申请报废.getValue());
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclx, null, null, deptNo, null, zcztList, page, rows);
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
	 * 同意资产报废申请页面
	 * @return
	 */
	@RequestMapping("agreeBFSQUI.do")
	public String agreeBFSQUI(Model m) {
		return ROOT + "/agreeBFSQUI";
	}
	
	/**
	 * 同意资产报废申请。
	 * 
	 * @return
	 */
	@RequestMapping("agreeBFSQ.do")
	@ResponseBody
	public String agreeBFSQ(HttpSession session, String zcidStr, String newCFDD) {
		String spr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] arr=zcidStr.split(",");
		List<Integer> zcidList=new ArrayList<>();
		for(int i=0;i<arr.length;i++){
			zcidList.add(Integer.parseInt(arr[i]));
		}
		try {
			zcywService.agreeBFSQ(appId, zcidList, newCFDD, spr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 拒绝资产报废申请页面
	 * @return
	 */
	@RequestMapping("refuseBFSQUI.do")
	public String refuseBFSQUI(Model m) {
		return ROOT + "/refuseBFSQUI";
	}
	
	/**
	 * 拒绝资产报废申请。
	 * 
	 * @return
	 */
	@RequestMapping("refuseBFSQ.do")
	@ResponseBody
	public String refuseBFSQ(HttpSession session, String zcidStr, String refuseRemark) {
		String spr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] arr=zcidStr.split(",");
		List<Integer> zcidList=new ArrayList<>();
		for(int i=0;i<arr.length;i++){
			zcidList.add(Integer.parseInt(arr[i]));
		}
		try {
			zcywService.refuseBFSQ(appId, zcidList, refuseRemark, spr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
