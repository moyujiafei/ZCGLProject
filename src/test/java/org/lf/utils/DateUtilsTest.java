package org.lf.utils;

import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void test() {
		
		
		Date min = DateUtils.toDate("yyyy-MM-dd", "2017-01-01");
		Date max = DateUtils.toDate("yyyy-MM-dd", "2017-12-31");
		
		for (int i = 0; i < 10; i++) {
			System.out.println(DateUtils.getRandomDate(min, max));
		}
	}

}
