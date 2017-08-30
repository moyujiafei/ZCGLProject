package org.lf.admin.action.console.catalog;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.CJZLX;
import org.lf.admin.service.catalog.JZLXService;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/catalog/jzlxgl/")
public class JZLXController extends BaseController {
	private final String ROOT = "/console/catalog/jzlxgl";
	
	@Autowired
	private JZLXService jzlxService;
	
	@RequestMapping("jzlxListUI.do")
	public String jzlxListUI() {
		return ROOT + "/jzlxListUI";
	}

	@RequestMapping("jzlxList.do")
	@ResponseBody
	public EasyuiDatagrid<CJZLX> jzlxList(int page, int rows) {
		return jzlxService.getPageJZLXList(rows, page);
	}
	
}
