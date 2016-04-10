package com.csanon.factrories;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.csanon.Airplane;

public class AirplaneFactoryTest {

	@Test
	public void test() {
		List<Airplane> planes = AirplaneFactory.getInstance().parseAirplanesFromXML("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Airplanes><Airplane Manufacturer=\"Airbus\" Model=\"A310\"><FirstClassSeats>24</FirstClassSeats><CoachSeats>200</CoachSeats></Airplane></Airplanes>");
		Airplane plane = planes.get(0);
		assertEquals("Airbus", plane.getManufacturer());
		assertEquals(200, plane.getEconomySeatCount());
		assertEquals("A310",plane.getModel());
		assertEquals(24,plane.getFirstClassSeatCount());
	}

}
