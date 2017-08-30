package org.lf.wx.user;

import java.util.List;

import org.junit.Test;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

import com.alibaba.fastjson.JSON;

public class UserManagerTest {

	@Test
	public void testGetUser() throws WXException {
		User u = UserManager.getUser(WXUtils.getLocalToken(), "XuQingWei");
		
		System.out.println(u.getJSON().toJSONString());
	}
	
	@Test
	public void testGetUserList() throws WXException {
		List<User> uList = UserManager.getUserList(WXUtils.getLocalToken(), 12, false, UserStatus.全部成员);
		System.out.println(JSON.toJSONString(uList));
	}

}
