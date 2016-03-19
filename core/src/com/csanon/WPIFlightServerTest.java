package com.csanon;

import static org.junit.Assert.*;

import org.junit.Test;

public class WPIFlightServerTest {

	@Test
	public void test() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		String result = server.getAirports();
		System.out.println(result);
	}

}
