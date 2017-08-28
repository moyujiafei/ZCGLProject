package org.lf.admin.action.console.zcpd;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.VLYHP;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 企业（部门）易耗品出入库记录查询
 * 
 */
@Controller
@RequestMapping("/console/zcpd/lyhpcx/")
public class LYHPCXController extends BaseController {
	private final String ROOT = "/console/zcpd/lyhpcx";
	
	/**
	 * 部门易耗品出入库记录查询
	 * @return
	 */
	@RequestMapping("BMLYHPListUI.do")
	public String BMLYHPListUI() {
		return ROOT + "/BMLYHPListUI";
	}
	
	/**
	 * 企业易耗品出入库记录查询
	 * @return
	 */
	@RequestMapping("LYHPListUI.do")
	public String LYHPListUI() {
		return ROOT + "/LYHPListUI";
	}

	@RequestMapping("getBMLYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VLYHP> getBMLYHPList(HttpSession session, int page, int rows) {
		return null;
	}
	
	@RequestMapping("getLYHPList.do")
	@ResponseBody
	public EasyuiDatagrid<VLYHP> getLYHPList(HttpSession session, int page, int rows) {
		return null;
	}
}
