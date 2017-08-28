package org.lf.admin.action.console.zcpd;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.LYHP;
import org.lf.admin.db.pojo.VYHP;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 易耗品查询
 * 
 * 部门资产管理员查询本部门易耗品。
 * 后勤管理人员查询整个企业易耗品。
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/console/zcpd/yhpcx/")
public class YHPCXController extends BaseController {
	private final String ROOT = "/console/zcpd/yhpcx";
	
	/**
	 * 部门易耗品查询
	 * @return
	 */
	@RequestMapping("BMYHPListUI.do")
	public String BMYHPListUI() {
		return ROOT + "/BMYHPListUI";
	}
	
	/**
	 * 企业易耗品查询
	 * @return
	 */
	@RequestMapping("YHPListUI.do")
	public String YHPListUI() {
		return ROOT + "/YHPListUI";
	}

	@RequestMapping("getYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VYHP> getYHPList(HttpSession session, String lx, String fzr, Integer deptNo, int page, int rows) {
		return null;
	}
	
	/**
	 * 易耗品操作记录
	 * @return
	 */
	@RequestMapping("detailYHPUI.do")
	public String detailYHPUI() {
		return ROOT + "/detailYHPUI";
	}
	
	@RequestMapping("getYHPDetail.do")
	@ResponseBody
	public EasyuiDatagrid<LYHP> getYHPDetail(HttpSession session, Integer czlx, int page, int rows) {
		return null;
	}	
}
