package org.lf.admin.action.console;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.db.pojo.ChuMenu;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 后台管理主页控制层
 * @author sunwill
 *
 */
@Controller
@RequestMapping("/console/")
public class HomeController extends BaseController {
//	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private RoleService roleService;
	private static final String ROOT = "/console";

	@RequestMapping("home.do")
	public String home(HttpSession session, Model m) {
		ChuUser user = getCurrUser(session);
		List<ChuMenu> resultList = roleService.getMenusByRole(user.getRoleId(), 0);
		m.addAttribute("currUserMenus", resultList);
		return ROOT + "/home";
	}

	@RequestMapping("welcome.do")
	public String welcome() {
		return ROOT + "/welcome";
	}
}
