package org.lf.admin.service.logs;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuApp;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.ZCGLProperties;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.sys.WXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class MsgServiceTest {
	@Autowired
	private WXAppService appService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private WXUserService userService;
	
	private ChuApp app;
	
	@Before  
    public void setUp() throws Exception {  
		appService.startAppList();
		app = appService.getApp("ww342013a5f3df8c7f", 1000002);
    }  

	/**
	 * 测试向某个指定用户发送消息
	 * @throws Exception
	 */
	@Test
	public void testSendMsgToUser() throws Exception {
		int appId = app.getAppId();
		
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		param.setName("尚尉");
		WXUser user = userService.getWXUser(param);
		
		int total = msgService.countMsgList(null);
		String url = ZCGLProperties.URL_WX_CLIENT + ZCGLProperties.URL_RW_INFO;
		url = String.format(url, 2);
		System.out.println(url);
		msgService.sendUserMsg(app.getAppId(), MsgLX.系统通知, user.getUserid(), "赶紧集合啦。点击<a href=\""+ url +"\">查看任务详情</a>");
		assertTrue(total + 1 == msgService.countMsgList(null));
	}
	
	/**
	 * 测试向某个指定用户组发送消息
	 * @throws Exception
	 */
	@Test
	public void testSendMsgToUserList() throws Exception {
		int appId = app.getAppId();
		
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		List<WXUser> userList = userService.getWXUserList(param);
		
		List<String> userIdList = new ArrayList<>();
		for (WXUser u : userList) {
			userIdList.add(u.getUserid());
		}
		
		int total = msgService.countMsgList(null);
		msgService.sendUserMsg(app.getAppId(), MsgLX.系统通知, userIdList, "赶紧集合啦。点击<a href=\"http://www.baidu.com\">查看详情</a>");
		assertTrue(total + userIdList.size() == msgService.countMsgList(null));
	}
	
	/**
	 * 测试向某个部门下属用户发送消息
	 * @throws Exception
	 */
	@Test
	public void testSendMsgToDepartment() throws Exception {
		int appId = app.getAppId();
		Integer deptNo = 2;
		
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		param.setDepartment("[" + deptNo + "]");
		List<WXUser> userList = userService.getWXUserList(param);
		
		List<String> userIdList = new ArrayList<>();
		for (WXUser u : userList) {
			userIdList.add(u.getUserid());
		}
		
		int total = msgService.countMsgList(null);
		
		msgService.sendDeptMsg(app.getAppId(), MsgLX.系统通知, deptNo, "赶紧集合啦。点击<a href=\"http://www.baidu.com\">查看详情</a>");
		assertTrue(total + 1 == msgService.countMsgList(null));
	}
	
	/**
	 * 测试向某个标签下属用户发送消息
	 * @throws Exception
	 */
	@Test
	public void testSendMsgToTag() throws Exception {
		int appId = app.getAppId();
		Integer tagNo = 2;
		
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
//		param.setTagNo(tagNo);
		List<WXUser> userList = userService.getWXUserList(param);
		
		List<String> userIdList = new ArrayList<>();
		for (WXUser u : userList) {
			userIdList.add(u.getUserid());
		}
		
		int total = msgService.countMsgList(null);
		
		msgService.sendTagMsg(app.getAppId(), MsgLX.系统通知, tagNo, "赶紧集合啦。点击<a href=\"http://www.baidu.com\">查看详情</a>");
		assertTrue(total + 1 == msgService.countMsgList(null));
		
		
	}
	
	/**
	 * 测试向某个标签下属用户发送消息
	 * @throws Exception
	 */
	@Test
	public void testSendMsgToAll() throws Exception {
//		TODO
	}

}
