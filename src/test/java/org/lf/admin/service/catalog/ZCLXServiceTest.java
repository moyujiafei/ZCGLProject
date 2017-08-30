package org.lf.admin.service.catalog;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.CZCLX;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.VZCLX;
import org.lf.admin.service.OperErrCode;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class ZCLXServiceTest {
	@Autowired
	private ZCLXService zclxService;
	
	@Autowired
	private WXAppService appService;

	private ChuApp app;
	private CZCLX zclx;
	
	
	private File zclx1File;
	private File zclx_err1File;
	private File zclx_err2File;
	private File zclx_err3File;
	
	@Before  
    public void setUp() throws Exception {  
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		zclx1File = new File(ZCLXServiceTest.class.getResource("/zclx1.xls").getFile());
		zclx_err1File = new File(ZCLXServiceTest.class.getResource("/zclx_err1.xls").getFile());
		zclx_err2File = new File(ZCLXServiceTest.class.getResource("/zclx_err2.xls").getFile());
		zclx_err3File = new File(ZCLXServiceTest.class.getResource("/zclx_err3.xls").getFile());
    }

	/**
	 * 测试插入一个资产类型记录的基本操作
	 */
	@Test
	public void testSimpleOper() throws Exception {
		int total = zclxService.countZCLXList(null);
		
		// 插入一个资产类型
		CZCLX newZCLX = new CZCLX();
		newZCLX.setAppId(app.getAppId());
		newZCLX.setMc("测试资产类型");
		newZCLX.setLxId("03FFFFFF");
		newZCLX.setLxPid("03");
		zclxService.insertZCLX(newZCLX);
		assertTrue(total + 1 == zclxService.countZCLXList(null));
		
		// 查询资产类型
		VZCLX param = new VZCLX();
		param.setAppId(app.getAppId());
		param.setLx("测试资产类型");
		param.setPlx("仪器仪表");
		assertTrue(zclxService.getZCLXList(param).size() == 1);
		
		// 删除资产类型
		zclx = zclxService.getZCLX(newZCLX);
		zclxService.delZCLX(zclx.getId());
	}
	
	/**
	 * 测试插入一个资产类型文件
	 */
	@Test
	public void testLoadFile() throws Exception {
		int total = zclxService.countZCLXList(null);
		
		InputStream in = new FileInputStream(zclx1File);
		InputStream newIn = new FileInputStream(zclx1File);
		Map<Integer, CZCLX> map = zclxService.parseFile(in);
		
		// 确认批量插入成功
		zclxService.insertZCLXList(app.getAppId(), newIn);
		assertTrue(total + 5  == zclxService.countZCLXList(null));
		
		// 删除资产类型
		CZCLX czclx;
		for (CZCLX zclx : map.values()) {
			CZCLX param = new CZCLX();
			param.setAppId(app.getAppId());
			param.setMc(zclx.getMc());
			czclx = zclxService.getZCLX(param);
			zclxService.delZCLX(czclx.getId());
		}
		
//		// 测试下载excel文件
//		List<CZCLX> zclxList = zclxService.getZCLXList(null);
//		HSSFWorkbook wb = zclxService.exportZCLXList(zclxList);
//		ExcelFileUtils fu = new ExcelFileUtils();
//		fu.exportExcel(wb, new File("e:/zclx.xls"));
	}
	
	private CZCLX createZCLX(Integer appId, String lxId, String lxPid, String mc) {
		CZCLX zclx = new CZCLX();
		zclx.setAppId(appId);
		zclx.setLxId(lxId);
		zclx.setLxPid(lxPid);
		zclx.setMc(mc);
		return zclx;
	}
	
	/**
	 * 测试单条记录插入错误
	 * 
	 * 资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复，
	 */
	@Test
	public void testSimpleError() throws Exception {
		int appId = app.getAppId();
		
		// 资产分类号不能为空
		try {
			zclx = createZCLX(appId,"","03","电脑");
			zclxService.insertZCLX(zclx);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(ZCLXService.资产分类号不能为空));
		}
		
		// 资产分类名称不能为空
		try {
			zclx = createZCLX(appId,"03010105","03","  ");
			zclxService.insertZCLX(zclx);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(ZCLXService.资产分类名称不能为空));
		}
		
		// 资产分类名称不能重复
		try {
			zclx = createZCLX(appId,"03010105","03","点温计");
			zclxService.insertZCLX(zclx);
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(ZCLXService.资产分类名称不能重复));
		}
	}
				
	public void loadErrFile(File errFile, OperErrCode code) throws Exception {
		try {
			zclxService.insertZCLXList(app.getAppId(), new FileInputStream(errFile));
			assertTrue(false);
		} catch (OperException e) {
			assertTrue(e.getErrCode().equals(code));
		}
	}
	
	/**
	 * 测试多条记录插入错误
	 * 
	 * 资产分类号不能为空， 资产分类名称不能为空，资产分类名称不能重复
	 */
	@Test
	public void testLoadError() throws Exception {
		// 资产分类号不能为空
		loadErrFile(zclx_err1File,zclxService.资产分类号不能为空);
		
		// 资产分类名称不能为空
		loadErrFile(zclx_err2File,zclxService.资产分类名称不能为空);
		
		// 资产分类名称不能重复
		loadErrFile(zclx_err3File,zclxService.资产分类名称不能重复);
	}
}
