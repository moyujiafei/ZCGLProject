package org.lf.admin.service.sys;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.service.sys.WXAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class WXAppServiceTest {
	@Autowired 
	WXAppService dao;
	
	@Before  
    public void setUp() throws Exception {  
		dao.startAppList();
    }  
	
	/**
	 * 测试同步微信服务器端的标签、微信用户和组织架构
	 * @throws Exception
	 */
	@Test
	public void testSync() throws Exception {
		ChuApp app = dao.getApp("ww342013a5f3df8c7f", 1000002);
		//ChuApp app = dao.getApp("wx2f5f98d168c4b18e", 26);

		dao.syncDepartmentsAndWXUsers(app.getAppId(), dao.syncTag(app.getAppId()));
	}
	
	public ChuApp createChuApp() throws Exception {
		ChuApp app = new ChuApp();
		app.setAppName("测试沙盒");
		app.setCorpId("ww342013a5f3df8c7f");
		app.setCorpName("潮涌信息科技有限公司");
		app.setCorpIcon("images/Lego_潮涌科技.png");
		app.setAgentId(1000006);
		app.setSecret("D6G1fpB6GwmJ9NWESgNwhVFh-O_6RPhHkWm2EksnEBM");
		app.setToken("gacl");
		app.setAesKey("n0Ox3od6DAjlwodoyhm63hSbrWWTTTBGbNB8QYxW2G5");
		app.setCallbackUrl("tide.natapp4.cc");
		app.setServerUrl("http://tide.natapp4.cc/wx");
		
		dao.insertApp(app);
		
		return app;
	}
	
	public ChuApp getChuApp() throws Exception {
		return dao.getApp("ww342013a5f3df8c7f", 1000006);
	}
	
	@Test
	public void testBaseOper() throws Exception {
		int total = dao.countAppList(null);
		
		createChuApp();	
		assertTrue(total + 1 == dao.countAppList(null));
		
		// 通过同步操作，来判断新的应用线程是否已经启动
		ChuApp newApp = getChuApp();
		dao.syncDepartmentsAndWXUsers(newApp.getAppId(), dao.syncTag(newApp.getAppId()));
		
		newApp.setCorpName("新名字");
		dao.updateApp(newApp);
		// 通过同步操作，来判断新的应用线程是否已经启动
		dao.syncDepartmentsAndWXUsers(newApp.getAppId(), dao.syncTag(newApp.getAppId()));
		
		
		dao.delApp(getChuApp().getAppId());
		assertTrue(total == dao.countAppList(null));
	}
}
