package org.lf.admin.service.catalog;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.VJZW;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class JZWServiceTest {
	
	@Autowired 
	private JZWService jzwService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private XQService xqService;
	
	private ChuApp app;
	private CJZW jzw;
	private CXQ xq;
	
	private File jzw1File;
	private File jzw_err1File;
	private File jzw_err2File;
	private File jzw_err3File;
	private File jzw_err4File;
	
	@Before
	public void setUp() throws Exception {
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		
		CXQ newXQ = new CXQ();
		newXQ.setAppId(app.getAppId());
		newXQ.setXqmc("测试校区名称1");
		xqService.insertXQ(newXQ);
		
		xq = xqService.getXQ(newXQ);
		
		jzw1File = new File(JZWServiceTest.class.getResource("/jzw1.xls").getFile());
		jzw_err1File = new File(JZWServiceTest.class.getResource("/jzw_err1.xls").getFile());
		jzw_err2File = new File(JZWServiceTest.class.getResource("/jzw_err2.xls").getFile());
		jzw_err3File = new File(JZWServiceTest.class.getResource("/jzw_err3.xls").getFile());
		jzw_err4File = new File(JZWServiceTest.class.getResource("/jzw_err4.xls").getFile());
		
	}
	
	@After
	public void finish() throws Exception {
		xqService.delXQ(xq);
	}
	
	/*
	 * 测试插入一个建筑物记录的基本操作
	 * */
	@Test
	public void testSimpleOper() throws Exception {
		int total = jzwService.countJZWList(null);
		
		//插入建筑物
		CJZW newJZW = new CJZW();
		newJZW.setAppId(app.getAppId());
		newJZW.setXqId(xq.getId());
		newJZW.setLxId(1);
		newJZW.setMc("测试建筑物名称1");
		jzwService.insertJZW(newJZW);
		
		assertTrue(total + 1 == jzwService.countJZWList(null));
		
		// 查询建筑物
//		assertTrue(jzwService.getJZWList(null).size() == jzwService.countJZWList(null));
		
		// 删除建筑物
		jzw = jzwService.getJZW(newJZW);
		jzwService.delJZW(jzw.getId());
	}

		
		/**
		 * 测试插入一个建筑物文件
		 */
		@Test
		public void testLoadFile() throws Exception {
			int total = jzwService.countJZWList(null);
			FileInputStream in = new FileInputStream(jzw1File);
			Map<Integer, VJZW> map = jzwService.parseFile(in);
			FileInputStream newIn = new FileInputStream(jzw1File);
			// 确认批量插入成功
			jzwService.insertJZWList(app.getAppId(), newIn);
			assertTrue(total + map.size()  == jzwService.countJZWList(null));
			
			// 删除插入的建筑物文件
			CJZW cjzw;
			for (VJZW vjzw : map.values()) {
				cjzw = new CJZW();
				cjzw.setMc(vjzw.getJzw());
				cjzw = jzwService.getJZW(cjzw);
				jzwService.delJZW(cjzw.getId());
			}
		
		// 测试下载excel文件
//		List<VJZW> jzwList = jzwService.getJZWList(null);
//		HSSFWorkbook wb = jzwService.exportJZWList(jzwList);
//		ExcelFileUtils fu = new ExcelFileUtils();
//		fu.exportExcel(wb, new File("e:/jzw.xls"));
	}
	
	private CJZW createJZW(Integer appId, Integer xqId, Integer lxId, String mc) {
		CJZW jzw = new CJZW();
		
		jzw.setAppId(appId);
		jzw.setXqId(xqId);
		jzw.setLxId(lxId);
		jzw.setMc(mc);
		
		return jzw;
	}
	
	/**
	 * 测试单条记录插入错误
	 * 
	 * 建筑物类型不能为空， 建筑物名称不能为空，建筑物名称不能重复，建筑物所在校区不能为空
	 */
	@Test
	public void testSimpleError() {
		int appId = app.getAppId();
		
		// 建筑物类型不能为空
		try {
			jzw = createJZW(appId, 1, null, "sdf");
			jzwService.insertJZW(jzw);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(JZWService.建筑物类型不能为空));
		}
		
		// 建筑物名称不能为空
		try {
			jzw = createJZW(appId, 1, 1, "   ");
			jzwService.insertJZW(jzw);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(JZWService.建筑物名称不能为空));
		}
		
		// 同一个校区id建筑物名称不能重复
		try {
			jzw = createJZW(appId, 1, 1, "西一楼");
			jzwService.insertJZW(jzw);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(JZWService.建筑物名称不能重复));
		}
		
		// 建筑物所在校区不能为空
		try {
			jzw = createJZW(appId, null, 1, "qwdqw");
			jzwService.insertJZW(jzw);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(JZWService.建筑物所在校区不能为空));
		}
	}
	
	
	public void loadErrFile(File errFile, OperErrCode code) throws Exception {
		try {
			jzwService.insertJZWList(app.getAppId(), new FileInputStream(errFile));
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(code));
		}
	}

	
	/**
	 * 测试多条记录插入错误
	 * 
	 * 建筑物类型不能为空， 建筑物名称不能为空，建筑物名称不能重复，建筑物所在校区不能为空
	 */
	@Test
	public void testLoadError() throws Exception {
		// 建筑物类型不能为空
		loadErrFile(jzw_err1File,JZWService.建筑物类型不能为空);
		
		// 建筑物名称不能为空
		loadErrFile(jzw_err2File,JZWService.建筑物名称不能为空);
		
		// 建筑物名称不能重复
		loadErrFile(jzw_err3File,JZWService.建筑物名称不能重复);
		
		// 建筑物所在校区不能为空
		loadErrFile(jzw_err4File,JZWService.建筑物所在校区不能为空);
		
	}
}

