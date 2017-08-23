package org.lf.wx.message.security;

import org.junit.Test;

public class WXEncryptUtilsTest {

	@Test
	public void test() throws AESException {
		String msg = "Z+vlaHXI8qQe+joPulc/9mEIudJnydsMRfLwlJ3jga6MN55j012YvBpQ1QzF2r7fva2045EdnWPT+wUGtBxp9A==";
		System.out.println(WXEncryptUtils.decrypt(msg));
	}

}
