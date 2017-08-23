package org.lf.admin.action.console.rwgl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.OperException;
import org.lf.admin.service.utils.FINISHService;
import org.lf.admin.service.zcgl.RWGLService;
import org.lf.admin.service.zcgl.RWLX;
import org.lf.admin.service.zcgl.RWService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.DateUtils;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * 日常巡检任务管理
 * 
 * @author 杨靖
 *
 */
@Controller
@RequestMapping("/console/rwgl/rcxj/")
public class RCXJController extends BaseController {
	private final String ROOT = "/console/rwgl/rcxj";
	
	@Autowired
	private RWService rwService;
	
	@Autowired
	private RWGLService rwglService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private FINISHService finishService;
	 
	@RequestMapping("rcxjListUI.do")
	public String rcxjListUI() {
		return ROOT + "/rcxjListUI";
	}

	/**
	 * 得到当前正在执行的日常巡检任务。
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("rcxjList.do")
	@ResponseBody
	public EasyuiDatagrid<JRW> rcxjList(HttpSession session, int page, int rows) {
		
		return rwglService.getPageCurrRWList(getAppId(session), RWLX.日常巡检.getValue(), null, null, 0, rows, page);
	}
	
	@RequestMapping("delRCXJ.do")
	@ResponseBody
	public String delRCXJ(Integer id,HttpSession session) {
		try {
			rwglService.delRCXJ(id, getCurrUser(session).getWxUsername());
			return SUCCESS;
		} catch (OperException e) {
			e.printStackTrace();
			return "failed";
		}
	}
	
	/**
	 * 根据操作人、验收人，查询现有的日常巡检任务
	 * @return
	 */
	@RequestMapping("queryRCXJ.do")
	@ResponseBody
	public EasyuiDatagrid<JRW> queryRCXJ(HttpSession session, Integer finish, String czr, String ysr, int page, int rows) {
		if(StringUtils.isEmpty(czr)){
			czr=null;
		}
		if(StringUtils.isEmpty(ysr)){
			ysr=null;
		}
		return rwglService.getPageCurrRWList(getAppId(session), RWLX.日常巡检.getValue(), czr, ysr, finish, rows, page);
	}
	
	@RequestMapping("insertRCXJUI.do")
	public String insertRCXJUI() {
		return ROOT + "/insertRCXJUI";
	}
	
	@RequestMapping("getFinishCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getFinishCombo(){
		return finishService.getFinishComboWithAll();
	}
	/**
	 * 查询V_ZC中，资产状态为“使用中”的资产，作为要巡检的资产列表
	 * 
	 * @return
	 */
	@RequestMapping("getZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getZCList(HttpSession session,int page, int rows) {
		VZC param=new VZC();
		param.setAppId(getAppId(session));
		param.setZczt(ZCZT.使用中.getValue());
		return zcService.getPageZCList(param, rows, page);
	}

	
	/**
	 * 根据rwid，查询此任务下的所有资产，返回给编辑日常巡检任务datagrid表格显示
	 * @return
	 */
	@RequestMapping("getZCListByRWID.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getZCListByRWID(HttpSession session,Integer rwid,int page, int rows){
		return zcService.getPageRWZCList(rwid,rows,page);
	}
	
	@RequestMapping("insertRCXJ.do")
	@ResponseBody
	public String insertRCXJ(HttpSession session, String kssj, String jssj,  String czr, String ysr, String yssj, String zcidStr) {
		Integer appId = getAppId(session);
		String cjr = getCurrUser(session).getWxUsername();
		Date yssjDate=DateUtils.toDate("yyyy-MM-dd", yssj);
		Date kssjDate=DateUtils.toDate("yyyy-MM-dd", kssj);
		Date jssjDate=DateUtils.toDate("yyyy-MM-dd", jssj);
		if(!checkRCXJByYSSJ(yssjDate, jssjDate)){
			return "验收时间要晚于任务结束时间";
		}
		if(!checkRCXJByKSSJ(kssjDate, jssjDate)){
			return "开始时间要早于任务结束时间";
		}
		String[] arr=zcidStr.split(",");
		List<Integer> zcidList=new ArrayList<>();
		for(int i=0;i<arr.length;i++){
			zcidList.add(Integer.parseInt(arr[i]));
		}
		try {
			rwglService.createRCXJ(appId, kssjDate, jssjDate, czr, null, ysr,yssjDate ,cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	@RequestMapping("updateRCXJUI.do")
	public String updateRCXJUI(Integer id, Model m) {
		JRW rw = rwService.getRW(id);
		m.addAttribute("rw", rw);
		//获取所有任务资产
		int rows=zcService.countRWZCList(id);
		if(rows!=0){
			List<VZC> zcList=zcService.getRWZCList(rw.getId(), rows, 1);
			StringBuilder sb=new StringBuilder();
			sb.append("[");
			for (VZC vzc : zcList) {
				sb.append(vzc.getZcid()+",");
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sb.append("]");
			JSONArray json=JSON.parseArray(sb.toString());
			m.addAttribute("zcidList", json.toJSONString());
		}
		//获取已完成巡检的任务资产
		rows=zcService.countRWZCList(id, null, null, null, null, null, null, null, 1);
		if(rows!=0){
			List<VZC> finishedZCList=zcService.getRWZCList(id, rows, 1, 1);
			StringBuilder sb=new StringBuilder();
			sb.append("[");
			for (VZC vzc : finishedZCList) {
				sb.append(vzc.getZcid()+",");
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sb.append("]");
			JSONArray json=JSON.parseArray(sb.toString());
			m.addAttribute("FinishedzcidList", json.toJSONString());
		}
		return ROOT + "/updateRCXJUI";
	}
	
	/**
	 * 验收时间要晚于任务结束时间。
	 * @param yssj
	 * @param jssj
	 * @return
	 */
	@RequestMapping("checkRCXJByYSSJ.do")
	@ResponseBody
	public boolean checkRCXJByYSSJ(Date yssj, Date jssj) {
		if(yssj.after(jssj)){
			return true;
		}
		return false;
	}
	
	/**
	 * 开始时间要早于结束时间
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public boolean checkRCXJByKSSJ(Date kssj,Date jssj){
		if(kssj.before(jssj)){
			return true;
		}
		return false;
	}
	
	@RequestMapping("updateRCXJ.do")
	@ResponseBody
	public String updateRCXJ(HttpSession session, Integer id, String kssj, String jssj, String czr, String ysr, String yssj, String czRemark) {
		String cjr = getCurrUser(session).getWxUsername();
		Date yssjDate=DateUtils.toDate("yyyy-MM-dd", yssj);
		Date kssjDate=DateUtils.toDate("yyyy-MM-dd", kssj);
		Date jssjDate=DateUtils.toDate("yyyy-MM-dd", jssj);
		try {
			JRW rw = rwService.getRW(id);
			if(rw.getCzRemark()==null){
				rw.setCzRemark("");
			}
			if(!checkRCXJByYSSJ(yssjDate, jssjDate)){
				return "验收时间要晚于任务结束时间";
			}
			if(!checkRCXJByKSSJ(kssjDate, jssjDate)){
				return "开始时间要早于任务结束时间";
			}
			boolean flag=false;
			if (! (rw.getKssj().equals(kssjDate) && rw.getJssj().equals(jssjDate) && rw.getCzr().equals(czr) && rw.getYsr().equals(ysr) && rw.getCzRemark().equals(czRemark))) {
				rwglService.updateRW(id, kssjDate, jssjDate, czr, czRemark, ysr, cjr);
				flag=true;
			}
			if (rw.getYssj()==null || ! rw.getYssj().equals(yssjDate)) {
				rwglService.updateYSSJ(cjr, id, yssjDate, czRemark);
				flag=true;
			}
			if(!flag){
				return "没有修改任何信息";
			}
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
}
