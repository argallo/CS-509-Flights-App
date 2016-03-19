package com.csanon.server;

import static org.junit.Assert.*;

import org.junit.Test;

import com.csanon.server.ServerConfig;
import com.csanon.server.WPIFlightServer;

public class WPIFlightServerTest {

	@Test
	public void test() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		String result = server.getAirports();
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void testGetDepartingFlights() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		String result = server.getFlightsDeparting("BOS", "2016_05_10");
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void testGetArrivingFlights() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		String result = server.getFlightsArrivingAt("BOS", "2016_05_10");
		System.out.println(result);
		assertNotNull(result);
	}

	@Test
	public void testGetPlanes() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		String result = server.getPlanes();
		System.out.println(result);
		assertNotNull(result);
	}

}
