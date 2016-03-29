package com.csanon;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class AirplanesTest {

	@Test
	public void testInitialization() {
		Airplanes.initialize();
		List<Airplane> result = Airplanes.getAirplanes();
		assertTrue(result.size() > 0);
		assertTrue(Airplanes.getAirplane("747").getManufacturer().equals("Boeing"));
	}

}
