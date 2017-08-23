package org.lf.admin.action.console.sys;

import java.util.List;

import org.apache.log4j.Logger;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.BackupService;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.zcgl.XTYJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("xtyjSchedule")
@EnableScheduling  //开启对计划任务的支持
public class XTYJSchedule {
	private Logger logger = Logger.getLogger(XTYJSchedule.class);
	
	@Autowired
	private XTYJService xtyjService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private BackupService backupService;
	/**
	 * 每周1-5，上午9点自动发送预警消息
	 */
	@Scheduled(cron = "0 0 9 ? * MON-FRI")  
//    @Scheduled(cron="0/5 * * * * ? ")   //每5秒执行一次  
	public void sendAlertMsg() {
		List<ChuApp> appList = appService.getAppList(null);
		
		try {
			Integer appId;
			for (ChuApp app : appList) {
				appId = app.getAppId();
				xtyjService.sendDeprecatedZCMsg(appId);
				xtyjService.sendExpiredMsg(appId);
				xtyjService.sendExpiringMsg(appId);
			}
		} catch (OperException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Scheduled(cron = "0 0 0 1 * ?")  //每月1号00：00触发
 // @Scheduled(cron="0/5 * * * * ? ")   //每5秒执行一次  
	public void autoBackup() {
		List<ChuApp> appList = appService.getAppList(null);
		
		try {
			Integer appId;
			for (ChuApp app : appList) {
				appId = app.getAppId();
				if(app.getLevel() == 1 || app.getLevel() == 2){
					backupService.backup(appId, null);  // 系统自动备份，操作人员为空
				}
			}
		} catch (OperException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
