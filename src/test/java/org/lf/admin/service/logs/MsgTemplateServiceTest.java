package org.lf.admin.service.logs;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.zcgl.RWLX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class MsgTemplateServiceTest {
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private MsgService msgService;

	@Autowired
	private MsgTemplateService templateService;
	
	@Autowired
	private WXUserService userService;

	private ChuApp app;

	@Before
	public void setUp() throws Exception {
		appService.startAppList();
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
	}

	@Test
	public void testBaseOper() throws Exception {
		ChuWXUser param = new ChuWXUser();
		param.setAppId(app.getAppId());
		param.setName("尚尉");
		WXUser creator = userService.getWXUser(param);
		
		param.setAppId(app.getAppId());
		param.setName("许庆炜");
		WXUser user = userService.getWXUser(param);
		
		// 创建一个任务
		JRW rw = new JRW();
		rw.setAppId(app.getAppId());
		rw.setKssj(new Date());
		rw.setJssj(new Date());
		rw.setLx(RWLX.日常巡检.getValue());
		rw.setYsr(user.getUserid());
		rw.setCzr(user.getUserid());
		
	}
}
