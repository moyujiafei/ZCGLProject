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
import org.lf.admin.db.pojo.CFJ;
import org.lf.admin.db.pojo.CJZW;
import org.lf.admin.db.pojo.CXQ;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.VFJ;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.utils.ExcelFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class FJServiceTest {

	@Autowired
	private FJService fjService;
	
	@Autowired
	private XQService xqService;
	
	@Autowired 
	private JZWService jzwService;
	
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private WXUserService userService;
	
	private ChuApp app;
	private CFJ fj;
	private CJZW jzw;
	private WXUser glr;
	private CXQ xq;
	
	private File fj1File;
	private File fj_err1File;
	private File fj_err2File;
	private File fj_err3File;
	private File fj_err4File;
	
	@Before
	public void setUp() throws Exception {
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		int appId = app.getAppId();
		
		// 插入一个校区
		CXQ newXQ = new CXQ();
		newXQ.setAppId(appId);
		newXQ.setXqmc("测试校区名称2");
		xqService.insertXQ(newXQ);
		
		xq = xqService.getXQ(newXQ);
		
		// 插入一个建筑物
		CJZW newJZW = new CJZW();
		newJZW.setAppId(appId);
		newJZW.setXqId(xq.getId());
		newJZW.setLxId(1);
		newJZW.setMc("测试建筑物名称2");
		jzwService.insertJZW(newJZW);
		
		jzw = jzwService.getJZW(newJZW);
		
		// 获得一个管理人
		ChuWXUser param = new ChuWXUser();
		param = new ChuWXUser();
		param.setAppId(appId);
		param.setName("尚尉");
		glr = userService.getWXUser(param);
		
		fj1File = new File(FJServiceTest.class.getResource("/fj1.xls").getFile());
		fj_err1File = new File(FJServiceTest.class.getResource("/fj_err1.xls").getFile());
		fj_err2File = new File(FJServiceTest.class.getResource("/fj_err2.xls").getFile());
		fj_err3File = new File(FJServiceTest.class.getResource("/fj_err3.xls").getFile());
		fj_err4File = new File(FJServiceTest.class.getResource("/fj_err4.xls").getFile());
	}
	
	@After
	public void finish() throws Exception {
		jzwService.delJZW(jzw.getId());
		xqService.delXQ(xq);
	}
	
	/*
	 * 测试插入一个房间记录的基本操作
	 * */
	@Test
	public void testSimpleOper() throws Exception {
		int total = fjService.countFJList(null);
		int appId = app.getAppId();
		
		// 再插入一个房间
		CFJ newfj = new CFJ();
		newfj.setAppId(appId);
		newfj.setJzwId(jzw.getId());
		newfj.setFloor("测试楼层1");
		newfj.setRoom("测试房间1");
		newfj.setDeptName(glr.getDeptList().get(0).getDeptName());
		newfj.setGlr(glr.getUserid());
		fjService.insertFJ(newfj);
		
		assertTrue(total + 1 == fjService.countFJList(null));
		
		// 查询房间
		assertTrue(fjService.getFJList(null).size() == fjService.countFJList(null));
		
		// 删除房间
		fj = fjService.getFJ(newfj);
		fjService.delFJ(fj.getId());
		
	}
	
	/**
	 * 测试插入一个房间文件
	 */
	@Test
	public void testLoadFile() throws Exception {
		int total = fjService.countFJList(null);
		FileInputStream in = new FileInputStream(fj1File);
		Map<Integer, VFJ> map = fjService.parseFile(in);
		FileInputStream newIn = new FileInputStream(fj1File);

		// 确认批量插入成功
		fjService.insertFJList(app.getAppId(), newIn);
		assertTrue(total + map.size()  == fjService.countFJList(null));
		
		// 删除插入的房间文件
		CFJ cfj;
		for (VFJ vfj : map.values()) {
			cfj = new CFJ();
			cfj.setRoom(vfj.getRoom());
			cfj = fjService.getFJ(cfj);
			fjService.delFJ(cfj.getId());
		}
		
		// 测试下载excel文件
		List<VFJ> fjList = fjService.getFJList(null);
		HSSFWorkbook wb = fjService.exportFJList(fjList);
		ExcelFileUtils fu = new ExcelFileUtils();
		fu.exportExcel(wb, new File("e:/fj.xls"));
	}
	
	private CFJ createFJ(Integer appId, Integer jzwId, String floor, String room, String deptName, String glr) {
		CFJ fj = new CFJ();
		fj.setDeptName(deptName);
		fj.setAppId(appId);
		fj.setFloor(floor);
		fj.setRoom(room);
		
		return fj;
	}
	
	/**
	 * 测试单条记录插入错误
	 * 
	 * 楼层不能为空， 房间号不能为空，房间号不能重复，房间所属部门名不存在，房间管理员不存在
	 */
	@Test
	public void testSimpleError() {
		int appId = app.getAppId();
		
		// 楼层不能为空
		try {
			fj = createFJ(appId,1,"  ","103","开发部","XuQingWei");
			fjService.insertFJ(fj);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(FJService.楼层不能为空));
		}
		
		// 房间所属部门名不能为空
		try {
			fj = createFJ(appId,1,"1","103","  ","XuQingWei");
			fjService.insertFJ(fj);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(FJService.房间所属部门名不能为空));
		}
		
		// 房间所属部门名不存在
		try {
			fj = createFJ(appId,1,"1","103","管理部","XuQingWei");
			fjService.insertFJ(fj);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(FJService.房间所属部门名不存在));
		}
		
		// 房间号不能为空
		try {
			fj = createFJ(appId,1,"1","  ","开发部","XuQingWei");
			fjService.insertFJ(fj);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(FJService.房间号不能为空));
		}
		
		// 房间号不能重复
		try {
			fj = createFJ(appId,1,"1","101","开发部","XuQingWei");
			fjService.insertFJ(fj);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(FJService.房间号不能重复));
		}

	}
	
	public void loadErrFile(File errFile, OperErrCode code) throws Exception {
		try {
			fjService.insertFJList(app.getAppId(), new FileInputStream(errFile));
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(code));
		}
	}

	
	/**
	 * 测试多条记录插入错误
	 * 
	 * 楼层不能为空， 房间号不能为空，房间所属部门名不存在，房间号不能重复
	 */
	@Test
	public void testLoadError() throws Exception {
		// 楼层不能为空
		loadErrFile(fj_err1File,FJService.楼层不能为空);
		
		// 房间号不能为空
		loadErrFile(fj_err2File,FJService.房间号不能为空);
		
		// 房间所属部门名不存在
		loadErrFile(fj_err3File,FJService.房间所属部门名不存在);
		
		// 房间号不能重复
		loadErrFile(fj_err4File,FJService.房间号不能重复);
	}
}
