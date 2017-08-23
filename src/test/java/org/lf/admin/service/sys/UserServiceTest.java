package org.lf.admin.service.sys;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class UserServiceTest {
	@Autowired
	private UserService dao;

	@Test
	public void testBaseOper() throws OperException {
		ChuUser user = new ChuUser();
		user.setName("测试");
		user.setUname("test");
		user.setUpw("123");
		user.setAppId(1);
		dao.insertUser(user,true,null);//超级管理员没有appid
		
		ChuUser u2 = dao.getUser(user.getUname());
		assertTrue(u2.getName().equals(user.getName()));
		
		dao.changePasswd(user, "123", UserService.DEFAULT_PASSWD);
		
		dao.delUser(u2.getUid());
	}
}
