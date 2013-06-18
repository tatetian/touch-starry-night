package me.tatetian;

import junit.framework.Assert;
import me.tatetian.stars.Star;

import org.junit.Test;

public class TestStar {
	@Test
	public void test() {
		Star o = new Star(0, 0, 0);
		Star smaller = new Star(-1, -1, 0);
		Star bigger  = new Star(1, 1, 0);
		Star equal1  = new Star(1, -1, 0);
		Star equal2  = new Star(-1, 1, 0);
		
		Assert.assertEquals(o.compareTo(smaller), 1);
		Assert.assertEquals(o.compareTo(bigger), -1);
		Assert.assertEquals(o.compareTo(equal1), 0);
		Assert.assertEquals(o.compareTo(equal2), 0);
	}
}
