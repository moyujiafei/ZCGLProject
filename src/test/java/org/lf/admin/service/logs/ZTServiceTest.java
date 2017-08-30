package org.lf.admin.service.logs;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.LZT;
import org.lf.admin.db.pojo.LZTXZ;
import org.lf.admin.db.pojo.VZT;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.lf.admin.service.zcgl.ZCZT;
import org.lf.wx.media.MediaType;
import org.lf.wx.media.TempMedia;
import org.lf.wx.media.TempMediaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class ZTServiceTest {
	@Autowired
	private ZTService ztService;
	
	@Autowired
	private WXUserService userService;
	
	@Autowired
	private WXAppService appService;
	
	private ChuApp app;
	private String image1MediaId;
	private String image2MediaId;
	private String voice1MediaId;
	
	@Before  
    public void setUp() throws Exception {  
		File image1 = new File(ZTServiceTest.class.getResource("/image1.jpg").getFile());
		File image2 = new File(ZTServiceTest.class.getResource("/image2.jpg").getFile());
		File voice1 = new File(ZTServiceTest.class.getResource("/voice1.mp3").getFile());
		
		appService.startAppList();
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
		
		image1MediaId = TempMediaManager.uploadMedia(appService.getAccessToken(app.getAppId()), image1, MediaType.image).getString(TempMedia.MEDIA_ID);
		image2MediaId = TempMediaManager.uploadMedia(appService.getAccessToken(app.getAppId()), image2, MediaType.image).getString(TempMedia.MEDIA_ID);
		voice1MediaId = TempMediaManager.uploadMedia(appService.getAccessToken(app.getAppId()), voice1, MediaType.video).getString(TempMedia.MEDIA_ID);
    } 
	
	/**
	 * 测试一个不带细则的L_ZT
	 * @throws Exception
	 */
	@Test
	public void testSimpleZT() throws Exception {
		int total = ztService.countZTList(null);
		int appId = app.getAppId();
		
		ChuWXUser pu = new ChuWXUser();
		pu.setAppId(appId);
		pu.setName("许庆炜");
		WXUser user = userService.getWXUser(pu);
		
		String zcdm = UUID.randomUUID().toString();
		
		LZT zt = new LZT();
		zt.setAppId(app.getAppId());
		zt.setZcdm(zcdm);
		zt.setJlsj(new Date());
		zt.setJlr(user.getUserid());
		zt.setNewZt(ZCZT.未使用.getValue());
		ztService.insertZT(zt);
		assertTrue(ztService.countZTList(null) == total + 1);
		
		// 查看资产代码对应的资产信息
		VZT pz = new VZT();
		pz.setZcdm(zcdm);
		List<VZT> ztList = ztService.getZTList(pz);
		// 资产编码是唯一的
		assertTrue(ztList.size() == 1);
		// 确认无资产细则信息
//		assertTrue(ztList.get(0).getMediaId() == null);
	}

	/**
	 * 测试一个带细则的L_ZT
	 * @throws Exception
	 */
	@Test
	public void testComplexZT() throws Exception {
		int total = ztService.countZTList(null);
		int appId = app.getAppId();
		
		ChuWXUser pu = new ChuWXUser();
		pu.setAppId(appId);
		pu.setName("许庆炜");
		WXUser user = userService.getWXUser(pu);
		
		String zcdm = UUID.randomUUID().toString();

		// 准备一个带细则资产状态
		LZT zt = new LZT();
		zt.setAppId(appId);
		
		zt.setZcdm(zcdm);
		zt.setJlsj(new Date());
		zt.setJlr(user.getUserid());
		zt.setOldZt(ZCZT.未使用.getValue());
		zt.setNewZt(ZCZT.使用中.getValue());
		
		
		List<LZTXZ> xzList = new ArrayList<>();
		LZTXZ xz1 = new LZTXZ();
		xz1.setMediaType(MediaType.image.toString());
		xz1.setWxMediaId(image1MediaId);
		xzList.add(xz1);
		LZTXZ xz2 = new LZTXZ();
		xz2.setMediaType(MediaType.image.toString());
		xz2.setWxMediaId(image2MediaId);
		xzList.add(xz2);
		LZTXZ xz3 = new LZTXZ();
		xz3.setMediaType(MediaType.voice.toString());
		xz3.setWxMediaId(voice1MediaId);
		xzList.add(xz3);
		
		// 测试插入
		ztService.insertZT(zt, xzList);
		assertTrue(ztService.countZTList(null) == total + 3);
		
		// 测试查询
		VZT pz = new VZT();
		pz.setZcdm(zcdm);
		List<VZT> vztList = ztService.getZTList(pz);
		// 外连接查询视图
		assertTrue(vztList.size() == xzList.size());
		// getZTList的大小与countZTList总保持一致。
		assertTrue(vztList.size() == ztService.countZTList(pz));
		
		// 测试相应细则的资源是否可以定位到. 如果定位不到，系统将抛“微信素材不存在”异常
		File file;
		for (VZT vzt : vztList) {
//			file = new File(ztService.getWXMedia(vzt.getJlsj(), vzt.getMediaType(), vzt.getMediaId()).toString());
//			assertTrue(file.exists());
		}
	}

}
