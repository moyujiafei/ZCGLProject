package org.lf.admin.service.catalog;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.pojo.CZCGL;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class ZCGLServiceTest {
	@Autowired
	private ZCGLService zcglService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private WXUserService userService;
	
	@Autowired
	private ChuWXDeptMapper wxDeptDao;
	
	private ChuApp app;
	private WXUser fzr;
	private WXUser glr;
	
	private InputStream zcgl1File;
	
	@Before  
    public void setUp() throws Exception {  
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		int appId = app.getAppId();
		
		zcgl1File = new FileInputStream(new File(ZCGLServiceTest.class.getResource("/zcgl1.xlsx").getFile()));
		
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		param.setName("许庆炜");
		fzr = userService.getWXUser(param);
		
		param = new ChuWXUser();
		param.setAppId(appId);
		param.setName("尚尉");
		glr = userService.getWXUser(param);
    }

	/**
	 * 测试插入一个资产管理记录
	 */
	@Test
	public void testSimpleOper() throws Exception {
		int total = zcglService.countZCGLList(null);
		
		// 插入一个资产管理
		CZCGL zcgl = new CZCGL();
		zcgl.setAppId(app.getAppId());
		zcgl.setDeptName(fzr.getDeptList().get(0).getDeptName());
		zcgl.setFzr(fzr.getUserid());
		zcgl.setGlr(glr.getUserid());
		zcglService.insertZCGL(zcgl);
		assertTrue(total + 1 == zcglService.countZCGLList(null));
		
		// 查询资产管理
		assertTrue(zcglService.getZCGLList(zcgl).size() == 1);
		
		// 删除资产管理
		zcglService.delZCGL(zcgl);
	}
	
	/**
	 * 测试一个资产管理文件
	 */
	@Test
	public void testLoadFile() throws Exception {
		Map<Integer, CZCGL> map = zcglService.parseFile(zcgl1File);
		// 先删除资产
		for (CZCGL zcgl : map.values()) {
			zcgl.setAppId(app.getAppId());
			zcglService.delZCGL(zcgl);
		}
		
		int total = zcglService.countZCGLList(null);
		
		// 确认批量插入成功
		zcglService.insertZCGLList(app.getAppId(), zcgl1File);
		assertTrue(total + map.size()  == zcglService.countZCGLList(null));
	}
	
	/**
	 * 测试加载错误的资产管理文件
	 */
	@Test
	public void testLoadErrorFile() {
		
	}
}
