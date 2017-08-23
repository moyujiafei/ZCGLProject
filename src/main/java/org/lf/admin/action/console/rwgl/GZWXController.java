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
import org.lf.admin.service.zcgl.ZCYWService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.utils.DateUtils;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 故障维修任务管理
 * 
 * @author 杨靖
 *
 */
@Controller
@RequestMapping("/console/rwgl/gzwx/")
public class GZWXController extends BaseController {
	private final String ROOT = "/console/rwgl/gzwx";
	
	@Autowired
	private RWService rwService;
	
	@Autowired
	private RWGLService rwglService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZCYWService zcywService;
	
	@Autowired
	private FINISHService finishService;
	
	@RequestMapping("gzwxListUI.do")
	public String gzwxListUI() {
		return ROOT + "/gzwxListUI";
	}

	/**
	 * 得到当前正在执行的故障维修任务。
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("gzwxList.do")
	@ResponseBody
	public EasyuiDatagrid<JRW> gzwxList(HttpSession session, Integer finish,int page, int rows){
		return rwglService.getPageCurrRWList(getAppId(session), RWLX.故障维修.getValue(), null, null, finish, rows, page);
	}
	
	@RequestMapping("delGZWX.do")
	@ResponseBody
	public String delGZWX(HttpSession session,Integer id) {
		String cjr=getCurrUser(session).getWxUsername();
		try {
			rwglService.delGZWX(id, cjr);
			return SUCCESS;
		} catch (OperException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/**
	 * 根据操作人、验收人，查询现有的故障维修任务
	 * @return
	 */
	@RequestMapping("queryGZWX.do")
	@ResponseBody
	public EasyuiDatagrid<JRW> queryGZWX(HttpSession session,Integer finish, String czr, String ysr, int page, int rows) {
		if(StringUtils.isEmpty(czr)){
			czr=null;
		}
		if(StringUtils.isEmpty(ysr)){
			ysr=null;
		}
		return rwglService.getPageCurrRWList(getAppId(session), RWLX.故障维修.getValue(), czr, ysr, finish, rows, page);
	}
	
	@RequestMapping("insertGZWXUI.do")
	public String insertGZWXUI() {
		return ROOT + "/insertGZWXUI";
	}	
	
	
	@RequestMapping("getFinishCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getFinishCombo(){
		return finishService.getFinishComboWithAll();
	}
	
	/**
	 * 查询V_ZC中，资产状态为“申请维修”的资产，作为要故障维修的资产列表
	 * 
	 * @return
	 */
	@RequestMapping("getZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getZCList(HttpSession session, int page, int rows) {
		VZC param=new VZC();
		param.setAppId(getAppId(session));
		param.setZczt(ZCZT.申请维修.getValue());
		return zcService.getPageZCList(param, rows, page);
	}
	
	/**
	 * 根据rwid，查询此任务下的所有资产，返回给编辑故障维修任务datagrid表格显示
	 * @return
	 */
	@RequestMapping("getZCListByRWID.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getZCListByRWID(HttpSession session,Integer rwid,int page, int rows){
		return zcService.getPageRWZCList(rwid,rows,page);
	}
	
	/**
	 * 同意资产维修申请。
	 * 创建维修任务
	 * 
	 * @return
	 */
	@RequestMapping("insertGZWX.do")
	@ResponseBody
	public String insertGZWX(HttpSession session, String kssj, String jssj,  String czr, String ysr, String yssj, @RequestParam(value="zcidArray")Integer[] zcidArray) {
		Integer appId = getAppId(session);
		String cjr = getCurrUser(session).getWxUsername();
		Date yssjDate=DateUtils.toDate("yyyy-MM-dd", yssj);
		Date kssjDate=DateUtils.toDate("yyyy-MM-dd", kssj);
		Date jssjDate=DateUtils.toDate("yyyy-MM-dd", jssj);
		if(!checkGZWXByYSSJ(yssjDate, jssjDate)){
			return "验收时间要晚于任务结束时间";
		}
		if(!checkGZWXByKSSJ(kssjDate, jssjDate)){
			return "开始时间要早于任务结束时间";
		}
		List<Integer> zcidList=new ArrayList<>();
		for(int i=0;i<zcidArray.length;i++){
			zcidList.add(zcidArray[i]);
		}
		try {
			//同意故障维修申请
			zcywService.agreeWXSQ(appId, cjr, zcidList);
			//创建故障维修任务
			rwglService.createGZWX(appId, kssjDate, jssjDate, czr, null, ysr,yssjDate ,cjr, zcidList);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	@RequestMapping("updateGZWXUI.do")
	public String updateGZWXUI(Integer id, Model m) {
		JRW rw = rwService.getRW(id);
		m.addAttribute("rw", rw);
		//获取所有任务资产
		int rows=zcService.countRWZCList(id);
		if(rows!=0){
			List<VZC> zcList=zcService.getRWZCList(rw.getId(),rows , 1);
			JSONArray array=new JSONArray();
			JSONObject json;
			for (VZC vzc : zcList) {
				json=new JSONObject();
				json.put("zcid", vzc.getZcid());
				array.add(json);
			}
			json=new JSONObject();
			json.put("zcidArray", array);
			m.addAttribute("zcidJson", json);
		}
		//获取已完成维修的任务资产
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
		return ROOT + "/updateGZWXUI";
	}
	
	/**
	 * 验收时间要晚于任务结束时间。
	 * @param yssj
	 * @param jssj
	 * @return
	 */
	@RequestMapping("checkGZWXByYSSJ.do")
	@ResponseBody
	public boolean checkGZWXByYSSJ(Date yssj, Date jssj) {
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
	public boolean checkGZWXByKSSJ(Date kssj,Date jssj){
		if(kssj.before(jssj)){
			return true;
		}
		return false;
	}
	
	@RequestMapping("updateGZWX.do")
	@ResponseBody
	public String updateGZWX(HttpSession session, Integer id, String kssj, String jssj, String czr, String ysr, String yssj, String czRemark) {
		String cjr = getCurrUser(session).getWxUsername();
		Date yssjDate=DateUtils.toDate("yyyy-MM-dd", yssj);
		Date kssjDate=DateUtils.toDate("yyyy-MM-dd", kssj);
		Date jssjDate=DateUtils.toDate("yyyy-MM-dd", jssj);
		try {
			JRW rw = rwService.getRW(id);
			if(rw.getCzRemark()==null){
				rw.setCzRemark("");
			}
			if(!checkGZWXByYSSJ(yssjDate, jssjDate)){
				return "验收时间要晚于任务结束时间";
			}
			if(!checkGZWXByKSSJ(kssjDate, jssjDate)){
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
