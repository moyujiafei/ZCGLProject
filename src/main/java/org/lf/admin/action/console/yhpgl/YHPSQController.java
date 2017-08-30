package org.lf.admin.action.console.yhpgl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.JYHPSQ;
import org.lf.admin.db.pojo.VYHPSQXZ;
import org.lf.admin.service.yhpgl.YHPSQService;
import org.lf.admin.service.yhpgl.YHPSQXZService;
import org.lf.admin.service.yhpgl.YHPService;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 易耗品申领单（或报损单）进行审批
 * 
 * 后勤管理人员对部门易耗品负责人、个人提交的易耗品申领单（或报损单）进行审批，有同意和拒绝两个结果。
 * 部门易耗品负责人对部门、个人提交的易耗品申领单（或报损单）进行审批，有同意和拒绝两个结果。
 *
 */
@Controller
@RequestMapping("/console/yhpgl/yhpsq/")
public class YHPSQController extends BaseController {
	private static final String ROOT = "/console/yhpgl/yhpsq";
	
	@Autowired
	private YHPService yhpService;
	
	@Autowired
	private YHPSQService yhpSQService;
	
	@Autowired
	private YHPSQXZService yhpSQXZService;

	/**
	 * 企业易耗品申领单审批
	 */
	@RequestMapping("qysqListUI.do")
	public String qysqListUI(){
		return ROOT +"/qysqListUI";
	}
	
	/**
	 * 查询J_YHP_SQ表，获得指定appId，易耗品负责人（FZR）为空，STATUS为待审批（1）的列表
	 */
	@RequestMapping("getQYSQList.do")
	@ResponseBody
	public EasyuiDatagrid<JYHPSQ> getQYSQList(HttpSession session) {
		return null;
	}
	
	/**
	 * 部门易耗品申领单审批
	 */
	@RequestMapping("bmsqListUI.do")
	public String bmsqListUI(){
		return ROOT +"/bmsqListUI";
	}
	
	/**
	 * 查询J_YHP_SQ表，获得指定appId，易耗品负责人（FZR）为当前用户，STATUS为待审批（1）的列表。
	 */
	@RequestMapping("getBMSQList.do")
	@ResponseBody
	public EasyuiDatagrid<JYHPSQ> getBMSQList(HttpSession session) {
		Integer appId = getAppId(session);
		String fzr = getCurrUser(session).getWxUsername();
		
		return null;
	}
	
	@RequestMapping("getYHPSQXZList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHPSQXZ> getYHPSQXZList(HttpSession session, String sqdm) {
		return null;
	}
	
	/**
	 * 同意低值易耗品部门申领
	 */
	@RequestMapping("agreeLeadingSQUI.do")
	public String agreeLeadingSQUI(){
		return ROOT +"/agreeLeadingSQUI";
	}
	
	/**
	 * 获取缺省的审批数量值。
	 * spNumMap：<key>为yhp_id，<value>为min(持有量，申请数量)
	 */
	@RequestMapping("getDefaultSPNum.do")
	@ResponseBody
	public Map<Integer, Integer> getDefaultSPNum(String sqdm) {
		return yhpSQXZService.getDefaultSPNum(sqdm);
	}
	
	/**
	 * 同意低值易耗品部门申领
	 */
	@RequestMapping("agreeLeadingSQ.do")
	@ResponseBody
	public boolean agreeLeadingSQ(HttpSession session, String sqdm, Map<Integer, Integer> spNumMap) {
		return false;
	}
	
	/**
	 * 拒绝低值易耗品部门申领
	 */
	@RequestMapping("refuseLeadingSQUI.do")
	public String refuseLeadingSQUI(){
		return ROOT +"/refuseLeadingSQUI";
	}
	
	/**
	 * 拒绝低值易耗品部门申领
	 */
	@RequestMapping("refuseLeadingSQ.do")
	@ResponseBody
	public boolean refuseLeadingSQ(HttpSession session, String sqdm, String remark) {
		return false;
	}

	/**
	 * 同意低值易耗品部门报损
	 */
	@RequestMapping("agreeLossingSQUI.do")
	public String agreeLossingSQUI(){
		return ROOT +"/agreeLossingSQUI";
	}
	
	/**
	 * 同意低值易耗品部门报损
	 */
	@RequestMapping("agreeLossingSQ.do")
	@ResponseBody
	public boolean agreeLossingSQ(HttpSession session, String sqdm, Map<Integer, Integer> spNumMap) {
		return false;
	}
	
	/**
	 * 拒绝低值易耗品部门报损
	 */
	@RequestMapping("refuseLossingSQUI.do")
	public String refuseLossingSQUI(){
		return ROOT +"/refuseLossingSQUI";
	}
	
	/**
	 * 拒绝低值易耗品部门报损
	 */
	@RequestMapping("refuseLossingSQ.do")
	@ResponseBody
	public boolean refuseLossingSQ(HttpSession session, String sqdm, String remark) {
		return false;
	}
}
