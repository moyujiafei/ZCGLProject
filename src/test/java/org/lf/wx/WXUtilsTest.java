package org.lf.wx;

import java.util.Arrays;

import org.junit.Test;

public class WXUtilsTest {

	@Test
	public void test() {
		String[] paramArr = new String[] { "jsapi_ticket=1", "timestamp=2", "noncestr=3", "url=4"};
		Arrays.sort(paramArr);
		// 将排序后的结果拼接成一个字符串
		String content = paramArr[0].concat("&" + paramArr[1]).concat("&" + paramArr[2]).concat("&" + paramArr[3]);
		System.out.println("拼接之后的content为:" + content);
	}

}
