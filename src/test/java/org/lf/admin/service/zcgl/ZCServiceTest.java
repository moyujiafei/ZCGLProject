package org.lf.admin.service.zcgl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.JZC;
import org.lf.admin.db.pojo.VZC;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.utils.DateUtils;
import org.lf.utils.NumberUtils;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
@Service("zcServiceTest")
public class ZCServiceTest {
	@Autowired
	private ZCService zcService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private WXUserService userService;
	
	private ChuApp app;
	private Integer appId;
	private List<WXUser> userList;
	
	@Before
	public void setUp() throws Exception {
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		appId = app.getAppId();
	}
	
	public WXUser getRandomUser(Integer appId) {
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		userList = userService.getWXUserList(param);
		int id = NumberUtils.getRandomNum(userList.size());
		return userList.get(id);
	}
	
	/**
	 * 
	 * @param size
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addZCList(Integer appId, int size, ZCZT zczt) throws Exception {				
		int totalsize = zcService.countZCList(null);
		
		JZC zc;
		for (int i = 0; i < size; i++) {
			zc = new JZC();
			zc.setAppId(appId);
			zc.setDm(StringUtils.lpad(i + 1 + totalsize + "", '0', 5));
			zc.setMc("资产" + zc.getDm());
			// 资产类型C_ZCLX，从1-168随机选取
			zc.setLxId(NumberUtils.getRandomNum(1, 168));
			zc.setXh(StringUtils.lpad(i + 12 + "", '0', 5));
			zc.setNum(NumberUtils.getRandomNum(1, 10));
			zc.setSyr(getRandomUser(appId).getUserid());
			zc.setZcglId(NumberUtils.getRandomNum(1, 2));
			zc.setGzsj(DateUtils.getRandomDate(DateUtils.toDate("yyyy-MM-dd", "2017-01-01"), DateUtils.toDate("yyyy-MM-dd", "2017-12-31")));
			zc.setZjnx(new BigDecimal(NumberUtils.getRandomNum(1, 5)));
			
			// 设置随机状态
			if (zczt == null) {
				zc.setZt(NumberUtils.getRandomNum(0, 12));
			} else {
				zc.setZt(zczt.getValue());
			}
			
			
			zcService.insertZC(zc);
		}
	}
	
	/**
	 * 获取指定资产状态下的一组资产列表
	 * @param appId
	 * @param zczt
	 * @return
	 */
	public List<VZC> getZCList(Integer appId, ZCZT zczt) {
		VZC param = new VZC();
		param.setAppId(appId);
		param.setZczt(zczt.getValue());
		
		return zcService.getZCList(param);
	}
	
	public List<Integer> getZCIDList(List<VZC> zcList) {
		List<Integer> zcidList = new ArrayList<>();
		for (VZC zc : zcList) {
			zcidList.add(zc.getZcid());
		}
		
		return zcidList;
	}

	/**
	 * 随机添加一组资产
	 */
	@Test
	public void addRandomZCList() throws Exception {
		addZCList(appId, 100, null);
	}
	
	/**
	 * 随机添加一组指定状态的资产
	 */
	@Test
	public void addZTZCList() throws Exception {
		addZCList(appId, 10, ZCZT.使用中);
	}
}
