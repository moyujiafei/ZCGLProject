package org.lf.admin.service.sys;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.ChuRole;
import org.lf.admin.service.sys.RoleService;
import org.lf.utils.EasyuiTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class RoleServiceTest {
	@Autowired
	private RoleService dao;
	
	/**
	 * 测试构建角色菜单树 
	 */
	@Test
	public void testTree() throws Exception {
		List<EasyuiTree> tree = dao.getTreeRoleList(27, 0);
		System.out.println(JSON.toJSONString(tree));
	}
	
	@Test
	public void testBaseOper() throws Exception {
		String name = "测试";
		
		int total = dao.countRoleList(null);
		ChuRole newRole = new ChuRole();
		newRole.setName(name);
		newRole.setPrivList("10;101");
		dao.insertRole(newRole);
		assert(dao.countRoleList(null) == total + 1);
		
		ChuRole role2 = dao.getRole(name);
		
		List<String> menuIdList = new ArrayList<>();
		menuIdList.add("1000");
		menuIdList.add("1001");
		menuIdList.add("1002");
		dao.resetMenuList(role2.getId(), menuIdList);
		
		List<Integer> menuIdList2 = dao.getMenuList(role2.getId());
		assertTrue(menuIdList.size() == menuIdList2.size());
		
		dao.delRole(role2.getId());
	}

}
