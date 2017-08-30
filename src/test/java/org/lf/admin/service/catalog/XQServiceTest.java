package org.lf.admin.service.catalog;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class XQServiceTest {
	@Autowired
	private XQService xqService;

	@Autowired
	private WXAppService appService;
	
	private ChuApp app;
	private CXQ xq;
	
	@Before
	public void setUp() throws Exception {
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
	}
	
	// 插入一个校区
	CXQ createXQ(Integer appId) throws Exception {
		xq = new CXQ();
		xq.setAppId(appId);
		xq.setXqmc("测试校区名称1");
		xqService.insertXQ(xq);
		return xq;
	}
	
	/*
	 * 测试插入一条记录的基本操作
	 * */
	@Test
	public void testBaseOper() throws Exception {
		int appId = app.getAppId();
		int total = xqService.countXQList(appId);
		
		// 插入校区
		createXQ(appId);
		assertTrue(total + 1 == xqService.countXQList(appId));
		
		// 查询校区
		assertTrue(xqService.getXQList(appId).size() == xqService.countXQList(appId));
		
		CXQ result = xqService.getXQ(xq);
		assertTrue(result != null);
		
		// 删除校区
		xqService.delXQ(xq);
	}
	
	private CXQ createXQ(Integer appId, String xqmc) {
		xq = new CXQ();
		
		xq.setAppId(appId);
		xq.setXqmc(xqmc);
		
		return xq;
	}
	
	/*
	 * 测试单条记录插入错误
	 * 
	 * 校区名称不能为空, 校区名称不能重复
	 * */
	@Test
	public void testSimpleError() {
		int appId = app.getAppId();
		
		// 校区名称不能为空
		try {
			xq = createXQ(appId,"   ");
			xqService.insertXQ(xq);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(XQService.校区名称不能为空));
		}
		
		// 校区名称不能重复
		try {
			xq = createXQ(appId,"光谷校区");
			xqService.insertXQ(xq);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(XQService.校区名称不能重复));
		}
	}
}
