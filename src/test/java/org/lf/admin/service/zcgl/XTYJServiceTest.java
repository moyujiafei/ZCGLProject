package org.lf.admin.service.zcgl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class XTYJServiceTest {
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private XTYJService xtyjService;

	private ChuApp app;
	private Integer appId;

	@Before
	public void setUp() throws Exception {
		appService.startAppList();
		
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		appId = app.getAppId();
	}
	
	@Test
	/**
	 * 测试系统发送任务即将到期预警信息,系统发送任务已经过期预警信息,发送资产折旧到期预警
	 */
	public void test() throws Exception{
		xtyjService.sendExpiringMsg(appId);
		xtyjService.sendExpiredMsg(appId);
		xtyjService.sendDeprecatedZCMsg(appId);
	}

}
