package org.lf.utils;

import static org.junit.Assert.assertTrue;

<<<<<<< HEAD
import org.junit.Test;

=======
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysql.fabric.xmlrpc.base.Array;

>>>>>>> upstream/master
public class StringUtilsTest {

	@Test
	public void testRandomString() {
<<<<<<< HEAD
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
=======
		List<Integer> a =new ArrayList<>();
		a.add(1);
		System.out.println(a.get(1));
>>>>>>> upstream/master
	}


}
