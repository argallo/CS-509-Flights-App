package com.csanon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.factrories.FlightFactory;
import com.csanon.time.DateTime;

public class FlightTest {
	@Before
	public void setUp() throws Exception {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;
		
		Airports.initialize();
		Airplanes.initialize();
	}
	
	@Test
	public void test() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Flights><Flight Airplane=\"737\" FlightTime=\"25\" Number=\"2807\"><Departure><Code>BOS</Code><Time>2016 May 10 00:05 GMT</Time></Departure><Arrival><Code>LGA</Code><Time>2016 May 10 00:30 GMT</Time></Arrival><Seating><FirstClass Price=\"$67.11\">9</FirstClass><Coach Price=\"$18.79\">85</Coach></Seating></Flight></Flights>";
		List<Flight> flights=FlightFactory.getInstance().parseFlightsFromXML(xml);
		Flight flight=flights.get(0);
		Airplane airplane = Airplanes.getAirplane("737");
		Flight flightActual=FlightFactory.getInstance().makeFlight(airplane, "25", "2807",Airports.getAirport("BOS"), DateTime.of("2016 May 10 00:05 GMT", Airports.getAirport("BOS").getOffset()), Airports.getAirport("LGA"), DateTime.of("2016 May 10 00:30 GMT", Airports.getAirport("LGA").getOffset()), new Price((float) 67.11), 9,
				new Price((float)18.79), 85);
		assertTrue(flight.equals(flightActual));
		//not sure how to test hashcode();
		assertEquals("BOS -> LGA\n\t05/09/16 20:05 -04:00 -> 05/09/16 20:30 -04:00\n",flight.toString());
		assertTrue(flight.checkEconomyAvailable(2));
		assertFalse(flight.checkEconomyAvailable(100));
		assertTrue(flight.checkFirstClassAvailable(2));
		assertFalse(flight.checkFirstClassAvailable(100));
	}

}
