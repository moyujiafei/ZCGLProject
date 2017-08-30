package org.lf.admin.service.zcgl;

import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.pojo.JRW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class RWServiceTest {
	@Autowired
	private RWService rwService;
	@Test
	public void test() throws Exception{
		JRW jrw = new JRW();
		
		 jrw.setAppId(1);
		 jrw.setCzr("daidadao");
		 jrw.setLx(2);
		 Date date = new Date();
		 jrw.setKssj(date);
		 Date date1 = new Date();
		 jrw.setJssj(date1);
		 
		 //reService.insertRW(jrw);
		
		int  result = rwService.countRWList(jrw);
		System.out.println("sum   "+result);
		//assertTrue(result.getLx().equals(jrw.getLx()));
//		reService.updateRW(jrw);
		//reService.delRW(result);
	}

}
