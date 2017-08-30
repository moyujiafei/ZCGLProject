package org.lf.admin.action.console.yhpgl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.JYHPSQ;
import org.lf.admin.db.pojo.VYHPSQXZ;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 部门（个人）易耗品报损
 * 
 */
@Controller
@RequestMapping("/console/yhpgl/yhpbs/")
public class LossingYHPController extends BaseController {
	private static final String ROOT = "/console/yhpgl/yhpbs";

	/**
	 * 部门易耗品报损
	 */
	@RequestMapping("lossingBMYHPListUI.do")
	public String lossingBMYHPListUI(){
		return ROOT +"/lossingBMYHPListUI";
	}
	
	/**
	 * 获取部门易耗品报损单列表
	 * 查询J_YHP_SQ表，获得指定appId，SQ_TYPE为部门报损，申请人（sqr）为当前用户的申请单列表
	 */
	@RequestMapping("getBMYHPSQList.do")
	@ResponseBody
	public EasyuiDatagrid<JYHPSQ> getBMYHPSQList(HttpSession session) {
		return null;
	}
	
	/**
	 * 个人易耗品报损
	 */
	@RequestMapping("lossingGRYHPListUI.do")
	public String lossingGRYHPListUI(){
		return ROOT +"/lossingGRYHPListUI";
	}
	
	/**
	 * 获取个人易耗品报损单列表
	 * 查询J_YHP_SQ表，获得指定appId，SQ_TYPE为个人报损，申请人（sqr）为当前用户的申请单列表
	 */
	@RequestMapping("getGRYHPSQList.do")
	@ResponseBody
	public EasyuiDatagrid<JYHPSQ> getGRYHPSQList(HttpSession session) {
		return null;
	}
	
	@RequestMapping("delYHPSQ.do")
	@ResponseBody
	public boolean delYHPSQ(String sqdm) {
		return false;
	}
	
	/**
	 * 新增低值易耗品部门报损
	 */
	@RequestMapping("insertBMYHPSQUI.do")
	public String insertBMYHPSQUI(){
		return ROOT +"/insertBMYHPSQUI";
	}
	
	/**
	 * 1. 向J_YHP_SQ表中插入一条记录
	 * 	申请单代码（DM）：36位UUID
	 * 	申请人（SQR）：当前用户userid
	 * 	申请类型（SQ_TYPE）：部门报损
	 * 	申请时间（SQSJ）：当前系统时间
	 * 	申请部门（SQBM_ID）：如果用户选中的是“企业”，SQBM_ID为空；否则为部门对应C_ZCGL中的ID号。
	 * 	审批结果（STATUS）为：未提交（0）
	 * 
	 * 2. J_YHP_SQXZ中插入一组记录
	 * 		申请数量SQ_NUM值来自sqNumMap<Integer, Integer>中指定yhpId对应的值。
	 * 		审批数量SP_NUM为0
	 * 
	 * @param sqdm
	 * @return
	 */
	@RequestMapping("insertBMYHPSQ.do")
	@ResponseBody
	public boolean insertBMYHPSQ(HttpSession session, Map<Integer, Integer> sqNumMap) {
		return false;
	}
	
	/**
	 * 查询V_YHP_SQXZ表，显示指定APP_ID和deptNo下的易耗品列表
	 * 如果用户选择“企业”，zcglId传入值为空。即查询存放在后勤仓库尚未分配给部门的易耗品信息。
	 * 
	 * @return
	 */
	@RequestMapping("getYHPSQXZList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHPSQXZ> getYHPSQXZList(HttpSession session, Integer deptNo, String sqdm) {
		return null;
	}
	
	/**
	 * 新增低值易耗品个人报损
	 */
	@RequestMapping("insertGRYHPSQUI.do")
	public String insertGRYHPSQUI(){
		return ROOT +"/insertGRYHPSQUI";
	}
	
	/**
	 * 1. 向J_YHP_SQ表中插入一条记录
	 * 	申请单代码（DM）：36位UUID
	 * 	申请人（SQR）：当前用户userid
	 * 	申请类型（SQ_TYPE）：个人报损
	 * 	申请时间（SQSJ）：当前系统时间
	 * 	申请部门（SQBM_ID）：如果用户选中的是“企业”，SQBM_ID为空；否则为部门对应C_ZCGL中的ID号。
	 * 	审批结果（STATUS）为：未提交（0）
	 * 
	 * 2. J_YHP_SQXZ中插入一组记录
	 * 		申请数量SQ_NUM值来自sqNumMap<Integer, Integer>中指定yhpId对应的值。
	 * 		审批数量SP_NUM为0
	 * 
	 * @param sqdm
	 * @return
	 */
	@RequestMapping("insertGRYHPSQ.do")
	@ResponseBody
	public boolean insertGRYHPSQ(HttpSession session, Map<Integer, Integer> sqNumMap) {
		return false;
	}
	
	/**
	 * 编辑低值易耗品报损单
	 */
	@RequestMapping("updateYHPSQUI.do")
	public String updateYHPSQUI(){
		return ROOT +"/updateYHPSQUI";
	}
	
	/**
	 * 编辑低值易耗品报损单，更新用户申请数量。
	 * 
	 * 1. 将J_YHP_SQXZ中指的SQ_DM下的记录全部删除
	 * 2. 新的申请数量SQ_NUM值来自sqNumMap<Integer, Integer>中指定yhpId对应的值。
	 * 
	 * @return
	 */
	@RequestMapping("updateYHPSQ.do")
	@ResponseBody
	public boolean updateYHPSQ(String sqdm, Map<Integer, Integer> sqNumMap) {
		return false;
	}
	
	/**
	 * 提交低值易耗品报损单
	 */
	@RequestMapping("submitYHPSQUI.do")
	public String submitYHPSQUI(){
		return ROOT +"/submitYHPSQUI";
	}
	
	/**
	 * 将J_YHP_SQ相应记录的STATUS更新为待审批（1）
	 */
	@RequestMapping("submitYHPSQ.do")
	@ResponseBody
	public boolean submitYHPSQ(Integer sqId) {
		return false;
	}
}
