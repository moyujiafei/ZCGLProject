package org.lf.wx.campus;

import java.net.URLEncoder;

import org.junit.Test;
import org.lf.utils.StringUtils;

public class CampusUtilsTest {

	@Test
	public void test() {
		String stringA="data[id]=1&data[name]=张三&objType=2&objectid=1&openAppID=597882146416&timestamp=1442401156&userid=1661";
		String stringSignTemp=stringA + "&key=testtoken123456";
		
		String sign = StringUtils.toMD5(stringSignTemp).toUpperCase();
		String stringB = URLEncoder.encode(stringA);
		
		System.out.println(stringB);
		
	}

}
