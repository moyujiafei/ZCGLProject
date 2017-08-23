package org.lf.wx.menu;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lf.wx.WXProperties;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

public class MenuManagerTest {
	private MenuItem m1 = MenuItem.createMenuItem(MenuType.click, "今日歌曲", "V1001_TODAY_MUSIC");
	private MenuItem m2 = MenuItem.createViewMenuItem("搜索", "http://www.soso.com/");
	private MenuItem m3 = MenuItem.createViewMenuItem("视频", "http://v.qq.com/");
	private MenuItem m4 = MenuItem.createMenuItem(MenuType.click, "赞一下我们", "V1001_GOOD");
	private MenuItem m5 = MenuItem.createMenuItem(MenuType.scancode_waitmsg, "扫码带提示", "rselfmenu_0_0");
	private MenuItem m6 = MenuItem.createMenuItem(MenuType.scancode_push, "扫码推事件", "rselfmenu_0_1");
	private MenuItem m7 = MenuItem.createMenuItem(MenuType.pic_sysphoto, "系统拍照发图", "rselfmenu_1_0");
	private MenuItem m8 = MenuItem.createMenuItem(MenuType.pic_photo_or_album, "拍照或者相册发图", "rselfmenu_1_1");
	private MenuItem m9 = MenuItem.createMenuItem(MenuType.pic_weixin, "微信相册发图", "rselfmenu_1_2");
	private MenuItem m10 = MenuItem.createMenuItem(MenuType.location_select, "发送位置", "rselfmenu_2_0");
	private MenuItem m13 = MenuItem.createViewMenuItem("WeUI", WXProperties.WX_SERVER_URL + "/example/");
	private MenuItem m14 = MenuItem.createViewMenuItem("JSSDK", "http://demo.open.weixin.qq.com/jssdk");

	@Test
	public void testMenu1() throws WXException {
		assertTrue(WXUtils.getLocalToken(), MenuManager.delMenu(WXUtils.getLocalToken()));
		
		Menu mb = new Menu();
		mb.append(m1).append(m2).append(m13);
		
		assertTrue(MenuManager.createMenu(WXUtils.getLocalToken(), mb));
	}

	@Test
	public void testMenu2() throws WXException {
		assertTrue(MenuManager.delMenu(WXUtils.getLocalToken()));
		
		Menu mb = new Menu();
		SubMenu sm1 = new SubMenu("菜单");
		sm1.addMenuItem(m5);
		sm1.addMenuItem(m6);
		sm1.addMenuItem(m7);
		sm1.addMenuItem(m8);
		sm1.addMenuItem(m9);
		
		mb.append(m1).append(sm1).append(m13);
		
		assertTrue(MenuManager.createMenu(WXUtils.getLocalToken(), mb));
	}
	
	@Test
	public void createXFGL() throws WXException {
		// 建立管理菜单
		MenuItem a1 = MenuItem.createMenuItem(MenuType.click, "微助手", "V1001_WE");
		MenuItem a2 = MenuItem.createViewMenuItem("测试", WXProperties.WX_SERVER_URL + "/wx/test.do");
		MenuItem a3 = MenuItem.createViewMenuItem("电子商务", WXProperties.WX_SERVER_URL + "/../shop/one/home/home.html");
		Menu mb = new Menu();
		
		SubMenu sm1 = new SubMenu("学习");
		sm1.addMenuItem(m13);
		sm1.addMenuItem(m14);
//		sm1.addMenuItem(a2);
		sm1.addMenuItem(a3);
		
		SubMenu sm2 = new SubMenu("扫一扫");
		MenuItem b1 = MenuItem.createMenuItem(MenuType.scancode_waitmsg, "故障查看", "gzck");
		MenuItem b2 = MenuItem.createMenuItem(MenuType.scancode_waitmsg, "紧急上报", "jjsb");

		sm2.addMenuItem(b1);
		sm2.addMenuItem(b2);
		
		mb.append(sm1).append(a1).append(sm2);
		assertTrue(MenuManager.createMenu(WXUtils.getLocalToken(), mb));
	}
}
