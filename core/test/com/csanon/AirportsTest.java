package com.csanon;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class AirportsTest {

	@Test
	public void testInitialization() {
		Airports.initialize();
		List<Airport> result = Airports.getAirports();
		assertTrue(result.size() > 0);
		assertTrue(Airports.getAirport("BOS").getName().equals("Logan International"));
	}

}
