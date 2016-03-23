package com.csanon.server;

import static org.junit.Assert.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.csanon.Airplane;
import com.csanon.Airplanes;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.Flight;

public class WPIFlightServerTest {
	
	Airport airport;
	OffsetDateTime date;
	
	@Before
	public void setUp() throws Exception {
		ZoneOffset offset = ZoneOffset.of("-00:00");
		date = OffsetDateTime.of(2016, 5, 10, 0, 0, 0, 0, offset);
		Airports.initialize();
		Airplanes.initialize();
		airport = Airports.getAirport("BOS");
	}

	@Test
	public void testGetAirports() {
		FlightServer server = ServerFactory.getServer();
		List<Airport> result = server.getAirports();
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetDepartingFlights() {
		FlightServer server = ServerFactory.getServer();
		List<Flight> result = server.getFlightsDeparting(airport, date);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetArrivingFlights() {
		FlightServer server = ServerFactory.getServer();
		List<Flight> result = server.getFlightsArrivingAt(airport, date);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetPlanes() {
		FlightServer server = ServerFactory.getServer();
		List<Airplane> result = server.getAirplanes();
		assertTrue(result.size() > 0);
	}

}
