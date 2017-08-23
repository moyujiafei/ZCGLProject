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
 * 闲置资产管理。
 * 
 * 后勤管理人员允许将闲置资产报废处理。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/xzzc/")
public class XZZCController extends BaseController {
	private final String ROOT = "/console/zcgl/xzzc";

	@Autowired
	private ZCYWService zcywService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产闲置界面
	 * @return
	 */
	@RequestMapping("xzzcListUI.do")
	public String xzzcListUI() {
		return ROOT + "/xzzcListUI";
	}

	/**
	 * 查询v_zc，获取所有资产状态为“闲置”的资产列表。
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("xzzcList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> xzzcList(HttpSession session, String zcmc, String zclx, Integer deptNo,int page, int rows) {	
		Integer appId = getAppId(session);	
		if(StringUtils.isEmpty(zclx)){
			zclx = null;
		}
		if(StringUtils.isEmpty(zcmc)){
			zcmc=null;
		}
		List<Integer> zcztList=new ArrayList<>();
		zcztList.add(ZCZT.闲置.getValue());
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
	 * 同意报废资产页面
	 * @return
	 */
	@RequestMapping("agreeBFZCUI.do")
	public String agreeBFZCUI(Model m) {
		return ROOT + "/agreeBFZCUI";
	}
	
	/**
	 * 同意报废资产。
	 * 
	 * @return
	 */
	@RequestMapping("agreeBFZC.do")
	@ResponseBody
	public String agreeBFZC(HttpSession session, Integer zcid, String remark) {
		String spr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		List<Integer> zcidList = new ArrayList<>();
		zcidList.add(zcid);		
		try {
			zcywService.agreeBFZC(appId, zcidList, remark, spr);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
