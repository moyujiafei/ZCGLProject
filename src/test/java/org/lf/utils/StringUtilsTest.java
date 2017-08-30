package org.lf.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testRandomString() {
		char[] base = "0123456789".toCharArray();
		
		String s = StringUtils.randomString(base, 9);
		assertTrue(s.length() == 9);
		System.out.println(s);
		
		s = StringUtils.randomString(base, 11);
		assertTrue(s.length() == 11);
		System.out.println(s);
		
		s = StringUtils.randomString(base, 19);
		assertTrue(s.length() == 19);
		System.out.println(s);
	}


}
