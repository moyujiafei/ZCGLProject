package org.lf.admin.service.logs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lf.admin.db.dao.LMsgMapper;
import org.lf.admin.db.pojo.LMsg;
import org.lf.admin.service.OperException;
import org.lf.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 杨靖 Email:763559353@qq.com
 * @version 创建时间：2017年7月21日 下午12:59:53
 * 类说明
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class LMsgServiceTest {

	@Autowired
	private LMsgMapper lMsgDao;
	
	@Autowired
	private LMsgService lMsgService;
	
	@Autowired
	private MsgService msgService;
	
	@Test
	public void testGetMsgListByJsr() {
		LMsg param=new LMsg();
		param.setAppId(17);
		param.setJsr("{\"toUser\":[\"pengxiao\"]}");
		List<LMsg> list=lMsgDao.getMsgListByJsr(param);
		
		for (LMsg lMsg : list) {
			System.out.println(lMsg.getNr());
		}
	}
	
	@Test
	public void testGetMsgList() throws OperException{
		SortedSet<LMsg> set=lMsgService.getMsgList(17, 0, "XuQingWei");
		for (LMsg msg : set) {
			System.out.println(msg.getNr());
		}
		System.out.println(set.size());
	}
	
	@Test
	public void updateFSSJ() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date minDate=sdf.parse("2017-7-20 15:48:00");
		Date maxDate=sdf.parse("2017-12-31 15:48:00");
		int total=msgService.countMsgList(null);
		LMsg record=new LMsg();
		for(int i=0;i<total;i++){
			record.setId(i+1);
			record.setFssj(DateUtils.getRandomDate(minDate, maxDate));
			lMsgDao.updateByPrimaryKeySelective(record);
		}
		
	}

}
