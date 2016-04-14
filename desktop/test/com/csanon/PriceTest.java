package com.csanon;

import static org.junit.Assert.*;

import org.junit.Test;

public class PriceTest {

	@Test
	public void test() {
		assertTrue(new Price("$45.1").equals(new Price((float) 45.1)));
		assertTrue(new Price("$45.1").compareTo(new Price("$48"))<0);
	}

}
