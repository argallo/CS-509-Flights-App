package com.csanon;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.csanon.time.DateTime;

public class TripBuilderTests {
	
	@Before
	public void setUp() {
		Airports.initialize();
		Airplanes.initialize();
	}
	
	@Test
	public void testBOStoLAX() {
		Airport boston = Airports.getAirport("BOS");
		Airport la = Airports.getAirport("LAX");
		
		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> trips = (new TripBuilder()).getTrips(boston, la, depart);

		DateTime departuretime = DateTime.of("2016 May 10 16:20 GMT", 0);
		DateTime arrivaltime = DateTime.of("2016 May 10 22:51 GMT", 0);
		Flight exepctedFlight = new Flight(Airplanes.getAirplane("717"), "391", "2833",
											boston, departuretime,
											la, arrivaltime,
											new Price("$1,233.25"), 17,
											new Price("$360.95"), 50);
		Trip expectedTrip = new Trip(exepctedFlight);

		List<Trip> expectedTrips = new LinkedList<Trip>();
		expectedTrips.add(expectedTrip);
		
		assertEquals(expectedTrips, trips);
	}
	
	@Test
	public void testBOStoPHX() {
		Airport boston = Airports.getAirport("BOS");
		Airport phoenix = Airports.getAirport("PHX");
		
		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> trips = (new TripBuilder()).getTrips(boston, phoenix, depart);

		DateTime departuretime1 = DateTime.of("2016 May 10 11:56 GMT", 0);
		DateTime arrivaltime1 = DateTime.of("2016 May 10 16:44 GMT", 0);
		Flight exepctedFlight1 = new Flight(Airplanes.getAirplane("777"), "288", "2827",
											boston, departuretime1,
											phoenix, arrivaltime1,
											new Price("$276.21"), 35,
											new Price("$54.55"), 15);
		Trip expectedTrip1 = new Trip(exepctedFlight1);
		
		DateTime departuretime2 = DateTime.of("2016 May 10 19:32 GMT", 0);
		DateTime arrivaltime2 = DateTime.of("2016 May 11 01:57 GMT", 0);
		Flight exepctedFlight2 = new Flight(Airplanes.getAirplane("737"), "385", "2839",
											boston, departuretime2,
											phoenix, arrivaltime2,
											new Price("$1,040.53"), 9,
											new Price("$291.35"), 96);
		Trip expectedTrip2 = new Trip(exepctedFlight2);
		
		List<Trip> expectedTrips = new LinkedList<Trip>();
		expectedTrips.add(expectedTrip1);
		expectedTrips.add(expectedTrip2);
		
		assertEquals(expectedTrips, trips);
	}
}
