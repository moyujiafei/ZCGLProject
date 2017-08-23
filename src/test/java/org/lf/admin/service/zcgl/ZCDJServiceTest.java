package org.lf.admin.service.zcgl;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.utils.DateUtils;
import org.lf.utils.NumberUtils;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** * @author  wenchen 
 * @date 创建时间：2017年6月14日 下午2:58:49 
 * @version 1.0 
 * @parameter */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
@Service("zcdjServiceTest")
public class ZCDJServiceTest {
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private ZCDJService zcdjService ;
	
	@Autowired
	private WXUserService userService;
	
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private ZCServiceTest zcServiceTest;
	
	private ChuApp app;
	private Integer appId;
	
	private String djr = "XuQingWei";

	@Before
	public void setUp() throws Exception {
		appService.startAppList();
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		appId = app.getAppId();
	}
	
	/**
	 * 测试资产登记、调拨，重新调拨
	 * @throws Exception
	 */
	@Test
	public void testZCDJ() throws Exception {
		int totalsize = zcService.countZCList(null);
		
		String zcdm = StringUtils.lpad(1 + totalsize + "", '0', 5);
		String zcmc = "资产" + zcdm;
		Integer zclx = NumberUtils.getRandomNum(1, 168);
		String xh = StringUtils.lpad(zcmc + "", '0', 5);
		String ccbh = null;
		Date gzsj = DateUtils.getRandomDate(DateUtils.toDate("yyyy-MM-dd", "2017-01-01"), DateUtils.toDate("yyyy-MM-dd", "2017-12-31"));
		BigDecimal zjnx = new BigDecimal(NumberUtils.getRandomNum(1, 5));
		
		
		// 测试资产登记
//		JZC zc = zcdjService.registZC(appId, zcdm, zcmc, zclx, xh, ccbh, gzsj, zjnx, djr, null);
//		assertTrue(zc.getZt().equals(ZCZT.已登记.getValue()));
//		
//		// 资产调拨
//		Integer zcglId = 1;
//		String cfdd = "老的存放地点";
//		zcdjService.allocateZC(zc.getId(), zcglId, cfdd, djr);
//		zc = zcService.getZC(zc.getId());
//		assertTrue(zc.getZt().equals(ZCZT.未使用.getValue()));
//		
//		// 资产的重新调拨
//		Integer new_zcglId = 2;
//		String new_cfdd = "新的存放地点";
//		zcdjService.reallocateZC(zc.getId(), new_zcglId, new_cfdd, djr);
//		zc = zcService.getZC(zc.getId());
//		assertTrue(zc.getZt().equals(ZCZT.未使用.getValue()));
	}
	
	/**
	 * 测试资产归还
	 * @throws Exception
	 */
	@Test
	public void testRevertZC() throws Exception {
		// 测试拒绝
		zcServiceTest.addZCList(appId, 2, ZCZT.归还中);
		List<VZC> zcList = zcService.getZCList(appId, ZCZT.归还中).subList(1, 2);
		List<Integer> zcidList = zcServiceTest.getZCIDList(zcList);
		zcdjService.refuseRevertSQ(appId, "今天心情不好", djr, zcidList);
		for (Integer zcid : zcidList) {
			zcService.getZC(zcid).getZt().equals(ZCZT.未使用.getValue());
		}
		
		// 测试同意归还
		zcServiceTest.addZCList(appId, 2, ZCZT.归还中);
		zcList = zcService.getZCList(appId, ZCZT.归还中).subList(1, 2);
		zcidList = zcServiceTest.getZCIDList(zcList);
		zcdjService.agreeRevertSQ(appId, djr, zcidList);
		for (Integer zcid : zcidList) {
			zcService.getZC(zcid).getZt().equals(ZCZT.已登记.getValue());
		}
	}

}
