package org.lf.admin.action.console.sys;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.lf.utils.EasyuiComboBoxItem;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 微信应用管理
 * 
 * @author 付卓
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/console/sys/wxapp/")
public class WXAppController extends BaseController {
	private final String ROOT = "/console/sys/wxapp";
	
	@Autowired
	private WXAppService appService;
	
	@RequestMapping("getAppThreadList.do")
	@ResponseBody
	public List<Integer> getAppThreadList() {
		return appService.getAppThreadList();
	}
	
	/**
	 * 微信应用列表初始化
	 * 
	 * @return
	 */
	@RequestMapping("wxAppListUI.do")
	public String wxAppListUI() {
		return ROOT + "/wxAppListUI";
	}


	/**
	 * 查找所有微信应用的信息
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("wxAppList.do")
	@ResponseBody
	public EasyuiDatagrid<ChuApp> wxAppList(int page, int rows) {
		return appService.getPagedAppList(rows, page);
	}
	
	
	
	/**
	 * 根据appId同步应用信息
	 * 根据微信返回的信息同步三张表 chu_tag,chu_wxdept,chu_wxuser
	 * 然后同步本地表 c_zcgl
	 * 
	 * @param appId
	 * @return
	 */
	@RequestMapping("sync.do")
	@ResponseBody 
	public String sync(Integer appId) {
		String result = null;
		try {
			appService.syncDepartmentsAndWXUsers(appId, appService.syncTag(appId));
			appService.syncZTAction(appId);
			result = "success";
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}
	
	/**
	 * 新增微信应用初始化界面
	 * 
	 * @return
	 */
	@RequestMapping("insertWXAPPUI.do")
	public String insertWXAPPUI() {
		return ROOT + "/insertWXAPPUI";
	}

	/**
	 * 新增微信应用
	 * 表单中有文件上传，提交类型为multipart/form-data，不能用@RequestBody处理
	 * 
	 * @return
	 */
	@RequestMapping("insertWXAPP.do")
	@ResponseBody
	public String insertWXAPP(HttpSession session, String corpId, String corpName, String appName, Integer agentId, String secret, String token,
			String aesKey, String callbackUrl, String serverUrl, @RequestParam(value = "corpIcon", required = false) MultipartFile[] fileList) {
		String result = null;
		if (fileList == null || fileList[0].getSize() == 0) {
			fileList = null;
		}
		try {
			appService.insertApp(session, corpId, corpName, appName, agentId, secret, token, aesKey, callbackUrl, serverUrl, fileList);
			result = SUCCESS;
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}
	
	
	/**
	 * 根据appId删除微信应用
	 * 
	 * @param appId
	 * @return
	 */
	@RequestMapping("delWXAPP.do")
	@ResponseBody 
	public String delWXAPP(Integer appId) {
		String result = null;
		try {
			appService.delApp(appId);
			result = "success";
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}
	
	
	/**
	 * 微信应用开启状态下拉框
	 * 
	 * @return
	 */
	@RequestMapping("getStopCombo.do")
	@ResponseBody
	public List<EasyuiComboBoxItem> getStopCombo() {
		return appService.getWxAppZtCombo();
	}
	
	/**
	 * 编辑微信应用初始化界面
	 * 
	 * @param appId
	 * @param m
	 * @return
	 */
	@RequestMapping("updateWXAPPUI.do")
	public String updateWXAPPUI(Integer appId, Model m) {
		try {
			m.addAttribute("curWxAppInfo", appService.getApp(appId));
		} catch (OperException e) {
			e.printStackTrace();
		}
		return ROOT + "/updateWXAPPUI";
	}
	
	/**
	 * 编辑微信应用
	 * 表单中有文件上传，提交类型为multipart/form-data，不能用@RequestBody处理
	 * 
	 * @return
	 */
	@RequestMapping("updateWXAPP.do")
	@ResponseBody
	public String updateWXAPP(HttpSession session, String appId, String corpId, String corpName, String appName, Integer agentId, String secret,
			String token, String aesKey, String callbackUrl, String serverUrl, String stop,
			@RequestParam(value = "corpIcon", required = false) MultipartFile[] fileList) {

		String result = null;
		if (fileList == null || fileList[0].getSize() == 0) {
			fileList = null;
		}
		try {
			appService.updateApp(session, appId, corpId, corpName, appName, agentId, secret, token, aesKey, callbackUrl, serverUrl, stop, fileList);
			result = SUCCESS;
		} catch (OperException e) {
			result = e.getMessage();
		}
		return result;
	}
}
