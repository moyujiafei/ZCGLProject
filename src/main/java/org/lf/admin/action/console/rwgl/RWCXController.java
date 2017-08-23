package org.lf.admin.action.console.rwgl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.zcgl.RWGLService;
import org.lf.admin.service.zcgl.RWLXService;
import org.lf.admin.service.zcgl.RWService;
import org.lf.admin.service.zcgl.ZCService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 任务查询，对系统中所有任务进行查询浏览。
 * 
 * @author 王登
 *
 */
@Controller
@RequestMapping("/console/rwgl/rwcx/")
public class RWCXController extends BaseController {
	private final String ROOT = "/console/rwgl/rwcx";
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private RWService rwService;
	
	@Autowired
	private RWGLService rwglService;
	
	@Autowired
	private RWLXService rwlxService;
	
	@RequestMapping("rwcxListUI.do")
	public String rwcxListUI() {
		return ROOT + "/rwcxListUI";
	}

	/**
	 * 得到所有任务。
	 * @param session
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("rwcxList.do")
	@ResponseBody
	public EasyuiDatagrid<JRW> rwcxList(HttpSession session, Integer rwlx, String czr, String ysr, int page, int rows) {
		Integer appId = getAppId(session);
		JRW param = new JRW();
		param.setAppId(appId);
		param.setLx(rwlx);
		if(czr!=null&&czr.length()>0)
			param.setCzr(czr);
		if(ysr!=null&&ysr.length()>0)
			param.setYsr(ysr);
		return rwService.getPageRWList(param, rows, page);
	}
	
	/**
	 * 任务类型下拉列表框（包含全部）。
	 * @param session
	 * @return
	 */
	@RequestMapping("getRWLXComboWithAll.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getRWLXComboWithAll(HttpSession session) {
		return rwlxService.getRWLXComboWithAll();
	}
	
	/**
	 * 任务详情页面
	 * @return
	 */
	@RequestMapping("rwcxInfoUI.do")
	public String rwcxInfoUI(Integer id, Model m) {
		JRW rw = rwService.getRW(id);
		m.addAttribute("rw", rw);
		return ROOT + "/rwcxInfoUI";
	}
	
	/**
	 * 查询V_ZC中与任务相关的资产列表
	 * 
select zc.*
from v_zc zc
join j_rwxz rwxz on (rwxz.zcid = zc.zcid and rwxz.rwid = ?)
	 * 
	 * @return
	 */
	@RequestMapping("getRWZCList.do")
	@ResponseBody
	public EasyuiDatagrid<VZC> getRWZCList(Integer rwid, int page, int rows) {
		int total = zcService.countRWZCList(rwid);

		EasyuiDatagrid<VZC> pageDatas = new EasyuiDatagrid<VZC>();
		if (total == 0) {
			pageDatas.setRows(new ArrayList<VZC>());
		} else {
			List<VZC> list = zcService.getRWZCList(rwid, rows, page);
			pageDatas.setRows(list);
		}
		pageDatas.setTotal(total);
		return pageDatas;
	}
	
}
