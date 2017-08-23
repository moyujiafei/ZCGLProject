package org.lf.admin.action.console.sys;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuEnv;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.EnvService;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/sys/")
public class EnvController extends BaseController {
	private final String ROOT = "/console/sys";
	
	@Autowired
	private EnvService envService;
	
	@RequestMapping("envListUI.do")
	public String envListUI() {
		return ROOT + "/envListUI";
	}

	@RequestMapping("envList.do")
	@ResponseBody
	public EasyuiDatagrid<ChuEnv> envList(int page, int rows) {
		ChuEnv param = new ChuEnv();
		return envService.getPageEnvList(param, rows, page);
	}
	
	@RequestMapping("updateEnvUI.do")
	public String updateEnvUI(Integer id, Model m) {
		ChuEnv env = envService.getEnv(id);
		m.addAttribute("env", env);
		
		return ROOT + "/updateEnvUI";
	}
	
	@RequestMapping("updateEnv.do")
	@ResponseBody
	public String updateEnv(Integer id, String envValue, String remark) {
		try {
			envService.updateEnv(id, envValue, remark);
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
}
