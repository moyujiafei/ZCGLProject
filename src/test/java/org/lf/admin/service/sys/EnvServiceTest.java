package org.lf.admin.service.sys;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lf.admin.db.dao.ChuEnvMapper;
import org.lf.admin.db.pojo.ChuEnv;
import org.springframework.beans.factory.annotation.Autowired;

public class EnvServiceTest {
	@Autowired
	private EnvService envService;
	
	@Autowired
	private ChuEnvMapper envDao;

	@Test
	public void testBaseOper() throws Exception {
		int total = envService.countEnvList(null);
		
		String envKey = "新变量";
		String envValue = "新值";
		ChuEnv env = new ChuEnv();
		env.setEnvKey(envKey);
		env.setEnvValue(envValue);
		envService.insertEnv(env);
		assertTrue(envService.countEnvList(null) == total + 1);
		
		ChuEnv newEnv = envService.getEnv(envKey);
		assertTrue(newEnv.getEnvValue().equals(envValue));
		
		// 测试更新操作
		String envValue2 = "新新值";
		env.setEnvValue(envValue2);
		envService.updateEnv(env);
		newEnv = envService.getEnv(envKey);
		assertTrue(newEnv.getEnvValue().equals(envValue2));
		
		// 清理操作
		envDao.deleteByPrimaryKey(newEnv.getId());
	}

}
