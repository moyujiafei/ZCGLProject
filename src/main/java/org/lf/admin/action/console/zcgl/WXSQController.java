package org.lf.admin.action.console.zcgl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.catalog.ZCLXService;
import org.lf.admin.service.zcgl.RWGLService;
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
 * 资产维修申请。
 * 后勤管理人员同意、拒绝提交的资产维修申请。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcgl/wxsq/")
public class WXSQController extends BaseController {
	private final String ROOT = "/console/zcgl/wxsq";

	@Autowired
	private ZCYWService zcywService;
	
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private RWGLService rwglService;
	
	@Autowired
	private ZCService zcService;
	
	/**
	 * 资产维修界面
	 * @return
	 */
	@RequestMapping("wxsqListUI.do")
	public String wxsqListUI() {
		return ROOT + "/wxsqListUI";
	}

	/**
	 * 查询v_zc，获取所有资产状态为“申请维修”的资产列表。
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("wxsqList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> wxsqList(HttpSession session, String zcmc, String zclxid, Integer deptNo, int page, int rows) {
		if(StringUtils.isEmpty(zcmc)){
			zcmc = null;
		}
		if(StringUtils.isEmpty(zclxid)){
			zclxid = null;
		}
		Integer appId = getAppId(session);
		List<Integer> zcztList=new ArrayList<>();
		zcztList.add(ZCZT.申请维修.getValue());
		return zcService.getPageVZCListByDeptNoAndZCLX(appId, null, zcmc, zclxid, null, null, deptNo, null, zcztList, page, rows);
	}
	
	/**
	 * 资产类型下拉列表框，默认为“全部”。
	 * @param session
	 * @return
	 */
	@RequestMapping("getZCLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getZCLXComboWithAll(HttpSession session) {
		return zclxService.getZCLXComboWithAll(getAppId(session));
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
	 * 拒绝资产维修申请页面
	 * @return
	 */
	@RequestMapping("agreeWXSQUI.do")
	public String agreeWXSQUI(Model m) {
		return ROOT + "/agreeWXSQUI";
	}
	
	/**
	 * 同意资产维修申请。
	 * 创建维修任务
	 * 
	 * @return
	 */
	@RequestMapping("agreeWXSQ.do")
	@ResponseBody
	public String agreeWXSQ(HttpSession session, String zcidString, String kssj, String jssj, String czr, String ysr, String yssj) {
		String resultString = SUCCESS;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date ksDate = null;
		Date jsDate = null;
		Date ysDate = null;
		try {
			ksDate = df.parse(kssj);
			jsDate = df.parse(jssj);
			ysDate = df.parse(yssj);
		} catch (ParseException e1) {
			resultString = "时间格式有误。";
			return resultString;
		}
		//判断操作人使用人是否为空。
		if (StringUtils.isEmpty(czr) || StringUtils.isEmpty(ysr)) {
			resultString = "操作人、验收人不能为空";
			return resultString;
		}
		//验收时间要大于结束时间并且结束时间要大于开始时间
		if (!(ysDate.getTime() > jsDate.getTime() && jsDate.getTime() > ksDate.getTime())) {
			resultString="验收时间要大于结束时间并且结束时间要大于开始时间。";
			return resultString;
		}
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] tempList = zcidString.split(",");
		List<Integer> zcidList = new ArrayList<Integer>();
		try {
			for(String str : tempList){
				zcidList.add(Integer.parseInt(str));
			}
			zcywService.agreeWXSQ(appId, cjr, zcidList);
			rwglService.createGZWX(appId, ksDate, jsDate, czr, null, ysr, ysDate, cjr, zcidList);
			return resultString;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 拒绝资产维修申请页面
	 * @return
	 */
	@RequestMapping("refuseWXSQUI.do")
	public String refuseWXSQUI(Model m) {
		return ROOT + "/refuseWXSQUI";
	}
	
	/**
	 * 拒绝资产维修申请。
	 * 
	 * @return
	 */
	@RequestMapping("refuseWXSQ.do")
	@ResponseBody
	public String refuseWXSQ(HttpSession session, String zcidString, String refuseRemark) {
		String cjr = getCurrUser(session).getWxUsername();
		Integer appId = getAppId(session);
		String[] tempList = zcidString.split(",");
		List<Integer> zcidList = new ArrayList<Integer>();
		try {
			for(String str : tempList){
				zcidList.add(Integer.parseInt(str));
			}
			zcywService.refuseWXSQ(appId, refuseRemark, cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
