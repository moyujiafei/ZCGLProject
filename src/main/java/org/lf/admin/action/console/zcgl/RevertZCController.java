package org.lf.admin.action.console.zcgl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.zcgl.ZCDJService;
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
 * 资产归还。
 * 后勤管理人员同意、拒绝部门资产管理员提交的资产归还申请。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/revert/")
public class RevertZCController extends BaseController {
	private final String ROOT = "/console/zcgl/revert";

	@Autowired
	private ZCDJService revertService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产归还审批界面
	 * @return
	 */
	@RequestMapping("revertZCListUI.do")
	public String revertListUI() {
		return ROOT + "/revertZCListUI";
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
	 * 查询v_zc，获取资产状态为“归还中”的资产列表。
	 * 其中，资产名称支持前后模糊查找。
	 * 
	 * @param appId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("queryRevertZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> queryRevertZCList(HttpSession session, String zcmc, String zclx, Integer deptNo, int page, int rows) {
		Integer appId=getAppId(session);
		if(StringUtils.isEmpty(zclx)){
			zclx=null;
		}
		if(StringUtils.isEmpty(zcmc)){
			zcmc=null;
		}
		List<Integer> zcztList=new ArrayList<>();
		zcztList.add(ZCZT.归还中.getValue());
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclx, null, null, deptNo, null, zcztList, page, rows);
	}
	
	/**
	 * 同意资产归还申请。
	 * 
	 * @return
	 */
	@RequestMapping("agreeRevertSQ.do")
	@ResponseBody
	public String agreeRevertSQ(HttpSession session, String zcidStr) {
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] arr=zcidStr.split(",");
	    List<Integer> zcidList=new ArrayList<>();
	    for(int i=0;i<arr.length;i++){
	    	zcidList.add(Integer.parseInt(arr[i]));
	    }
		try {
			revertService.agreeRevertSQ(appId, cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 拒绝资产归还申请页面
	 * @return
	 */
	@RequestMapping("refuseRevertSQUI.do")
	public String refuseRevertSQUI() {
		return ROOT + "/refuseRevertSQUI";
	}
	
	/**
	 * 拒绝资产归还申请。
	 * 
	 * @return
	 */
	@RequestMapping("refuseRevertSQ.do")
	@ResponseBody
	public String refuseRevertSQ(HttpSession session, String zcidStr, String refuseRemark) {
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] arr=zcidStr.split(",");
	    List<Integer> zcidList=new ArrayList<>();
	    for(int i=0;i<arr.length;i++){
	    	zcidList.add(Integer.parseInt(arr[i]));
	    }
		try {
			revertService.refuseRevertSQ(appId, refuseRemark, cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
