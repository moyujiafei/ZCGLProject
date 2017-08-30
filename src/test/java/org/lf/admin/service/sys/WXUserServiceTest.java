package org.lf.admin.service.sys;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.dao.ChuTagMapper;
import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.dao.ChuWXUserMapper;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.ChuWXUser;
import org.lf.admin.db.pojo.WXUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class WXUserServiceTest {
	@Autowired
	ChuWXUserMapper userDao;
	
	@Autowired
	ChuWXDeptMapper deptDao;
	
	@Autowired
	ChuTagMapper tagDao;
	
	@Autowired 
	private WXUserService service;
	
	private int appId = 99;
	
	@Before
	public void init() {
		// user add
		ChuWXUser cUser = new ChuWXUser();
		cUser.setAppId(appId);
		cUser.setUserid("testUser");
		cUser.setDepartment("[]");
		cUser.setGender(1);
		userDao.insert(cUser);
				
		// 创建了两个部门
		ChuWXDept cDept = new ChuWXDept();
		cDept.setAppId(appId);
		cDept.setDeptNo(1);
		cDept.setDeptName("A");
		deptDao.insert(cDept);
		
		cDept = new ChuWXDept();
		cDept.setAppId(appId);
		cDept.setDeptNo(2);
		cDept.setDeptName("B");
		deptDao.insert(cDept);
		
	}

	@Test
	public void testBaseOper() throws Exception {
		ChuWXUser param = new ChuWXUser();
		param.setAppId(appId);
		assertTrue(service.countWXUserList(param) == 1);
		
		// 测试没有department的情况. 由于只插入了一个用户
		WXUser wxUser = service.getWXUserList(param).get(0);
		assertTrue(wxUser.getSex().equals("男"));
		assertTrue(wxUser.getDeptList().size() == 0);
		
		// 测试一个department的情况
		param = new ChuWXUser();
		param.setAppId(appId);
		param.setUserid("testUser");
		ChuWXUser newUser = userDao.select(param);
		assertTrue(newUser != null);
		
		newUser.setDepartment("[1]");
		userDao.updateByPrimaryKey(newUser);
		wxUser = service.getWXUserList(param).get(0);
		assertTrue(wxUser.getSex().equals("男"));
		assertTrue(wxUser.getDeptList().size() == 1);
		
		// 测试两个department的情况
		newUser.setDepartment("[1, 2]");
		userDao.updateByPrimaryKey(newUser);
		wxUser = service.getWXUserList(param).get(0);
		assertTrue(wxUser.getSex().equals("男"));
		assertTrue(wxUser.getDeptList().size() == 2);
	}
	
	@Test
	public void testMenuList() throws Exception {
		
	}

	@After
	public void finish() {
		userDao.deleteByAppId(appId);
		deptDao.deleteByAppId(appId);
	}
}
