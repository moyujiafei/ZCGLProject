package org.lf.admin.service.zcgl;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.JRW;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
@Service("rwglServiceTest")
public class RWGLServiceTest {
	@Autowired
	private RWService rwService;
	
	@Autowired
	private RWGLService rwglService;
	
	@Autowired
	private RCXJService rcxjService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private WXUserService userService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZCServiceTest zcServiceTest;
	
	private ChuApp app;
	private Integer appId;
	
	// 任务创建人
	private String cjr;
	
	@Before
	public void setUp() throws Exception {
		appService.startAppList();
		
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		appId = app.getAppId();
	}

	
	/**
	 * 日常巡检任务创建
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public JRW createRCXJ() throws Exception {
		Date kssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-01-01");
		Date kssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-06-30");
		Date kssj = DateUtils.getRandomDate(kssjStart, kssjEnd);
		
		Date jssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-07-01");
		Date jssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-12-31");
		Date jssj = DateUtils.getRandomDate(jssjStart, jssjEnd);

		String ysr = zcServiceTest.getRandomUser(appId).getUserid();
		Date yssj = DateUtils.toDate("yyyy-MM-dd", "2018-01-01");
		String czr = zcServiceTest.getRandomUser(appId).getUserid();
		cjr = zcServiceTest.getRandomUser(appId).getUserid();
		
		// 创建一组使用中的随机任务
		zcServiceTest.addZCList(appId, 5, ZCZT.使用中);
		// 取处于使用中的资产列表中的5个来创建日常巡检任务
		List<VZC> zcList = zcServiceTest.getZCList(appId, ZCZT.使用中);
		List<Integer> zcidList = zcServiceTest.getZCIDList(zcList);

		return rwglService.createRCXJ(appId, kssj, jssj, czr, "创建一个随机日常巡检任务", ysr, yssj, cjr, zcidList);
	}	
	
	/**
	 * 测试日常巡检任务
	 */
	@Test
	public void testRCXJ() throws Exception {
		// 创建巡检任务，返回要巡检的资产列表
		JRW rw = createRCXJ();
		assertTrue(rwglService.countCurrRWList(appId, RWLX.日常巡检.getValue(), rw.getCzr(), rw.getYsr(), 0) > 0);
		
		// 更新任务，任务创建人保持不变，更新时间，验收人，操作人。
		Date kssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-01-01");
		Date kssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-06-30");
		Date kssj = DateUtils.getRandomDate(kssjStart, kssjEnd);
		
		Date jssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-07-01");
		Date jssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-12-31");
		Date jssj = DateUtils.getRandomDate(jssjStart, jssjEnd);
		String newYsr = zcServiceTest.getRandomUser(appId).getUserid();
		String newCzr = zcServiceTest.getRandomUser(appId).getUserid();
		rwglService.updateRW(rw.getId(), kssj, jssj, newCzr, "更新日常巡检任务信息", newYsr, cjr);
		
		// 测试更新验收时间
		Date newYssj = DateUtils.toDate("yyyy-MM-dd", "2018-01-01");
		rwglService.updateYSSJ(cjr, rw.getId(), newYssj, "更新验收时间");
		
		rwglService.finishRW(rw.getId(), "日常巡检验收不通过", false);
		rw = rwService.getRW(rw.getId());
		assertTrue(rw.getFinish() == 0);
		
		rwglService.finishRW(rw.getId(), "日常巡检验收不通过", true);
		rw = rwService.getRW(rw.getId());
		assertTrue(rw.getFinish() == 1);
	}
	
	@Test
	public void testDelRCXJ() throws Exception {
		// 创建巡检任务，返回要巡检的资产列表
		JRW rw = createRCXJ();
		rwglService.delRCXJ(rw.getId(), cjr);
	}
	
	/**
	 * 故障维修创建
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public JRW createGZWX() throws Exception {
		Date kssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-01-01");
		Date kssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-06-30");
		Date kssj = DateUtils.getRandomDate(kssjStart, kssjEnd);
		
		Date jssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-07-01");
		Date jssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-12-31");
		Date jssj = DateUtils.getRandomDate(jssjStart, jssjEnd);

		String ysr = zcServiceTest.getRandomUser(appId).getUserid();
		String czr = zcServiceTest.getRandomUser(appId).getUserid();
		cjr = zcServiceTest.getRandomUser(appId).getUserid();
		
		// 创建一组使用中的随机任务
		zcServiceTest.addZCList(appId, 5, ZCZT.维修中);
		// 取处于使用中的资产列表中的5个来创建日常巡检任务
		List<VZC> zcList = zcServiceTest.getZCList(appId, ZCZT.维修中).subList(1, 5);
		List<Integer> zcidList = zcServiceTest.getZCIDList(zcList);

		return rwglService.createGZWX(appId, kssj, jssj, czr, "创建一个随机故障维修任务", ysr, jssj, cjr, zcidList);
	}
	
	/**
	 * 测试故障维修
	 */
	@Test
	public void testGZWX() throws Exception {
		JRW rw = createGZWX();
		assertTrue(rwglService.countCurrRWList(appId, RWLX.故障维修.getValue(), rw.getCzr(), rw.getYsr(), 0) > 0);
		
		// 更新任务，任务创建人保持不变，更新时间，验收人，操作人。
		Date kssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-01-01");
		Date kssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-06-30");
		Date kssj = DateUtils.getRandomDate(kssjStart, kssjEnd);
		
		Date jssjStart = DateUtils.toDate("yyyy-MM-dd", "2017-07-01");
		Date jssjEnd = DateUtils.toDate("yyyy-MM-dd", "2017-12-31");
		Date jssj = DateUtils.getRandomDate(jssjStart, jssjEnd);
		String newYsr = zcServiceTest.getRandomUser(appId).getUserid();
		String newCzr = zcServiceTest.getRandomUser(appId).getUserid();
		rwglService.updateRW(rw.getId(), kssj, jssj, newCzr, "更新维修任务信息", newYsr, cjr);
	}
	
	@Test
	public void testDelGZWX() throws Exception {
		// 创建巡检任务，返回要巡检的资产列表
		JRW rw = createGZWX();
		rwglService.delGZWX(rw.getId(), cjr);
	}

}
