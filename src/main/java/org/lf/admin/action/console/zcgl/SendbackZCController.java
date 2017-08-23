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
 * 资产上交审批。
 * 部门管理员对资产使用人（保管人）提交的“上交中”的资产进行审批。审批通过，资产重新入库变为“未使用”资产。
 * 审批不通过，资产退还给使用人（保管人），变为“使用中”资产。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/sendback/")
public class SendbackZCController extends BaseController {
	private final String ROOT = "/console/zcgl/sendback";

	@Autowired
	private ZCFPService zcfpService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产上交界面
	 * @return
	 */
	@RequestMapping("sendbackZCListUI.do")
	public String sendbackListUI() {
		return ROOT + "/sendbackZCListUI";
	}

	/**
	 * 查询v_zc，获取当前资产管理员名下所有资产状态为“上交中”的资产列表。
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("sendbackZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> sendbackZCList(HttpSession session, String zcmc, String zclx, Integer deptNo, int page, int rows) {
		if(StringUtils.isEmpty(zcmc)){
			zcmc = null;
		}
		if(StringUtils.isEmpty(zclx)){
			zclx = null;
		}
		Integer appId = getAppId(session);
		String glr = getCurrUser(session).getWxUsername();
		List<Integer> zcztList = new ArrayList<Integer>();
		zcztList.add(ZCZT.上交中.getValue());
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclx, null, null, deptNo, glr, zcztList, page, rows);
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
	 * 同意资产上交申请。
	 * 
	 * @return
	 */
	@RequestMapping("agreeSendbackSQ.do")
	@ResponseBody
	public String agreeSendbackSQ(HttpSession session,String zcidString) {
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] tempList = zcidString.split(",");
		List<Integer> zcidList = new ArrayList<Integer>();
		try {
			for(String str : tempList){
				zcidList.add(Integer.parseInt(str));
			}
			zcfpService.agreeSendbackSQ(appId, cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 拒绝资产上交申请页面
	 * @return
	 */
	@RequestMapping("refuseSendbackSQUI.do")
	public String refuseSendbackSQUI() {
		return ROOT + "/refuseSendbackSQUI";
	}
	
	/**
	 * 同意资产上交申请。
	 * 
	 * @return
	 */
	@RequestMapping("refuseSendbackSQ.do")
	@ResponseBody
	public String refuseSendbackSQ(HttpSession session,String zcidString, String refuseRemark) {
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] tempList = zcidString.split(",");
		List<Integer> zcidList = new ArrayList<Integer>();
		try {
			for(String str : tempList){
				zcidList.add(Integer.parseInt(str));
			}
			zcfpService.refuseSendbackSQ(appId, refuseRemark, cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
