package com.csanon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.factrories.FlightFactory;
import com.csanon.time.DateTime;

public class TripTest {
	Trip trip1=null;
	Trip trip2=null;
	Trip trip3=null;
	Flight flight1=null;
	Flight flight2=null;
	Flight BOS2LAX_9_1=null;
	Flight BOS2LAX_9_2=null;
	Flight BOS2LAX_9_3=null;
	
	@Before
	public void setUp() {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;

		Airports.initialize();
		Airplanes.initialize();
		Airplane sss = Airplanes.getAirplane("777");
		Airport boston = Airports.getAirport("BOS");
		Airport losangeles = Airports.getAirport("LAX");
		Airport phoenix = Airports.getAirport("PHX");
		Airport kansascity = Airports.getAirport("MCI");
		//Make up flight.
		BOS2LAX_9_1 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 09 23:59 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 10 05:32 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_9_2=new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 10 07:59 GMT", losangeles.getOffset()),
				phoenix, DateTime.of("2016 May 10 15:32 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_9_3=new Flight(sss, "100", "TEST",
				phoenix, DateTime.of("2016 May 10 18:59 GMT", phoenix.getOffset()),
				kansascity, DateTime.of("2016 May 11 05:32 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		trip2=new Trip(BOS2LAX_9_1,BOS2LAX_9_2);
		trip3=new Trip(BOS2LAX_9_1,BOS2LAX_9_2,BOS2LAX_9_3);
		//Make up flight to test Seat.
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Flights><Flight Airplane=\"737\" FlightTime=\"25\" Number=\"2807\"><Departure><Code>BOS</Code><Time>2016 May 10 00:05 GMT</Time></Departure><Arrival><Code>LGA</Code><Time>2016 May 10 00:30 GMT</Time></Arrival><Seating><FirstClass Price=\"$67.11\">9</FirstClass><Coach Price=\"$18.79\">85</Coach></Seating></Flight>"
				+ "<Flight Airplane=\"737\" FlightTime=\"25\" Number=\"2807\"><Departure><Code>BOS</Code><Time>2016 May 10 01:30 GMT</Time></Departure><Arrival><Code>LGA</Code><Time>2016 May 10 06:05 GMT</Time></Arrival><Seating><FirstClass Price=\"$67.11\">1</FirstClass><Coach Price=\"$18.79\">85</Coach></Seating></Flight></Flights>";
		List<Flight> flights=FlightFactory.getInstance().parseFlightsFromXML(xml);
		flight1=flights.get(0);
		flight2=flights.get(1);
		trip1=new Trip(flight1,flight2);
		
	}

	@Test
	public void test() {
		assertEquals(354,trip3.getLayoverTime());
		assertEquals(1773,trip3.getTotalTime());
		assertTrue(trip3.getAirports().get(0).getCode().equals("BOS"));
		assertTrue(trip3.getLegs().get(2).equals(BOS2LAX_9_3));
		assertTrue(trip3.equals(trip2.addLeg(BOS2LAX_9_3, false)));
		assertTrue(trip1.hasFirstClassSeatsAvailable(10));
		assertTrue(trip1.hasEconomySeatsAvailable(10));
		

	}

}
