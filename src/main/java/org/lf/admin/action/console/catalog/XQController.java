package org.lf.admin.action.console.catalog;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.service.OperException;
import org.lf.admin.service.catalog.XQService;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 校区管理控制层
 * 
 * @author 王登
 *
 */
@Controller
@RequestMapping("/console/catalog/xqgl/")
public class XQController extends BaseController {
	private final String ROOT = "/console/catalog/xqgl";

	@Autowired
	private XQService xqService;
	/**
	 * 初始化校区管理界面
	 * @return
	 */
	@RequestMapping("xqListUI.do")
	public String xqListUI() {
		return ROOT + "/xqListUI";
	}

	@RequestMapping("xqList.do")
	@ResponseBody
	public EasyuiDatagrid<CXQ> xqList(HttpSession session, int page, int rows) {
		return xqService.getPageXQList(getAppId(session), rows, page);
	}

	@RequestMapping("updateXQUI.do")
	public String updateXQUI(Integer xqid, Model m) {
		CXQ cxq = new CXQ();
		cxq = xqService.getXQ(xqid);
		m.addAttribute("cxq", cxq);
		return ROOT + "/updateXQUI";
	}

	@RequestMapping("updateXQ.do")
	@ResponseBody
	public String updateXQ(Integer xqid, String xqmc, String xqdz, String yb) {
		try {
			xqService.updateXQ(xqid, xqmc, xqdz, yb);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	@RequestMapping("delXQ.do")
	@ResponseBody
	public String delXQ(Integer xqid) {
		try {
			xqService.delXQ(xqid);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}

	@RequestMapping("insertXQUI.do")
	public String addXQUI() {
		return ROOT + "/insertXQUI";
	}

	@RequestMapping("insertXQ.do")
	@ResponseBody
	public String insertXQ(HttpSession session, String xqmc, String xqdz, String yb) {
		try {
			xqService.insertXQ(getAppId(session), xqmc, xqdz, yb);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 插入操作，检查校区名唯一。
	 * @param session
	 * @param xqmc
	 * @return
	 */
	@RequestMapping("checkXQByMC.do")
	@ResponseBody
	public boolean checkXQByMC(HttpSession session, String newXQMC) {
		return xqService.checkXQMC(getAppId(session), newXQMC);
	}

	@RequestMapping("checkXQByUpdateMC.do")
	@ResponseBody
	public boolean checkXQByUpdateMC(HttpSession session, String oldXQMC, String newXQMC) {
		return xqService.checkXQMC(getAppId(session), newXQMC, oldXQMC);
	}
}
