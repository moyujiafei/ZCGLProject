package org.lf.admin.action.console.sys;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.lf.admin.action.console.BaseController;
import org.lf.admin.db.pojo.ChuBackup;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.BackupService;
import org.lf.utils.EasyuiDatagrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/sys/backup/")
public class BackupController extends BaseController {
	private final String ROOT = "/console/sys/backup";
	
	@Autowired
	private BackupService backupService;
	
	@RequestMapping("myBackupListUI.do")
	public String myBackupListUI() {
		return ROOT + "/myBackupListUI";
	}

	/**
	 * 当前appId下STATUS为未删除（0）的备份列表
	 */
	@RequestMapping("myBackupList.do")
	@ResponseBody
	public EasyuiDatagrid<ChuBackup> myBackupList(HttpSession session, int page, int rows) {
		ChuBackup param=new ChuBackup();
		Integer appId = getAppId(session);
		param.setAppId(appId);
		param.setStatus(0);
		return backupService.getPageZCGLList(param, rows, page);
	}
	
	@RequestMapping("deleteBackup.do")
	@ResponseBody
	public boolean deleteBackup(Integer id, HttpSession session) {
		Integer appId = getAppId(session);
		return backupService.deleteById(id,session,appId);
	}
	
	@RequestMapping("insertBackup.do")
	@ResponseBody
	public String insertBackup(HttpSession session) {
		Integer appId = getAppId(session);
		try {
			String czr = getCurrUser(session).getWxUsername();
			backupService.backup(appId, czr);
			return SUCCESS;
		} catch (OperException e) {		
			return e.getMessage();
		}		
	}
	
	@RequestMapping("downLoad.do")
	@ResponseBody
	public String downLoad(Integer id, HttpSession session, HttpServletResponse response) {	 
		try {
			Integer appId = getAppId(session);
			backupService.downLoadBackup(id, session, response, appId);	
			return SUCCESS;
		} catch (OperException e) {
			return e.getMessage();
		}
	}
	   
}
