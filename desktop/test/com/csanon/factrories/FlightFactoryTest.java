package com.csanon.factrories;

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
import com.csanon.Airplane;
import com.csanon.Airplanes;
import com.csanon.Airports;
import com.csanon.Flight;

public class FlightFactoryTest {
	
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
		assertEquals(9,flight.getFirstClassSeats());
		assertEquals(85,flight.getEconomySeats());
		assertEquals("$67.11",flight.getFirstClassPrice());
		assertEquals("$18.79",flight.getEconomyPrice());
		
	}

}
