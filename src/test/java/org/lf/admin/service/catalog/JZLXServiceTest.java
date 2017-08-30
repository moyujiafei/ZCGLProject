package org.lf.admin.service.catalog;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class JZLXServiceTest {
	
	@Autowired
	private JZLXService jzlxService;
	
	/**
	 * 测试统计建筑类型列表
	 */
	@Test
	public void testBaseOper() {
		
		assertTrue(jzlxService.countJZLXList(null) == jzlxService.getJZLXList(null).size());
		
		assertTrue(jzlxService.getJZLXList(null).get(0).getMc().equals("行政办公建筑"));
	}
}
