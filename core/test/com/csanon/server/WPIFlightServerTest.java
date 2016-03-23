package com.csanon.server;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;

public class WPIFlightServerTest {

	@Test
	public void testGetAirports() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		List<Airport> result = server.getAirports();
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetDepartingFlights() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		List<Flight> result = server.getFlightsDeparting("BOS", "2016_05_10");
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetArrivingFlights() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		List<Flight> result = server.getFlightsArrivingAt("BOS", "2016_05_10");
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetPlanes() {
		WPIFlightServer server = new WPIFlightServer(new ServerConfig());
		List<Airplane> result = server.getPlanes();
		assertTrue(result.size() > 0);
	}

}
