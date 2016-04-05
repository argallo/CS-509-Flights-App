package com.csanon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.time.DateTime;

public class TripBuilderTests {

	@Before
	public void setUp() {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;
		
		Airports.initialize();
		Airplanes.initialize();
	}

	private void assertValidTrips(List<Trip> aTrips, Airport aDeparture, Airport aDestination, DateTime aDepartTime,
			int aMaxHopCount, int aMinLayover, int aMaxLayover) {

		aTrips.forEach(trip -> {
			List<Flight> legs = trip.getLegs();
			// check to make sure the size is within bounds
			assertTrue(legs.size() >= 1);
			assertTrue(legs.size() <= aMaxHopCount);

			// check to make sure the departtime is on the same day as the first leg
			DateTime lowerlimit = aDepartTime.withNewOffset(aDeparture.getOffset()).getUTC().plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset())
					.getMidnight();
			DateTime upperlimit = lowerlimit.getNextDay();
			assertTrue(lowerlimit.compareTo(legs.get(0).getDepartureTime()) <= 0);
			assertTrue(upperlimit.compareTo(legs.get(0).getDepartureTime()) > 0);

			// check to make sure that there are no duplicates of airports
			assertEquals(legs.size() + 1, (new HashSet<Airport>(trip.getAirports()).size()));

			// check that the start and the end are correct
			assertEquals(aDeparture, legs.get(0).getDepartureAirport());
			assertEquals(aDestination, legs.get(legs.size() - 1).getArrivalAirport());

			// check that the layover time is between the given times
			Iterator<Flight> legiter = legs.iterator();
			Flight lastflight = legiter.next();
			while (legiter.hasNext()) {
				Flight next = legiter.next();
				long difference = lastflight.getArrivalTime().getDifference(next.getDepartureTime()).getSeconds();
				assertTrue(aMinLayover <= difference);
				assertTrue(aMaxLayover >= difference);
				lastflight = next;
			}

		});
	}

	@Test
	public void testBOStoLAX() {
		Airport boston = Airports.getAirport("BOS");
		Airport la = Airports.getAirport("LAX");

		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> actualtrips = (new TripBuilder()).getTrips(boston, la, depart);

		DateTime departuretime = DateTime.of("2016 May 10 16:20 GMT", boston.getOffset());
		DateTime arrivaltime = DateTime.of("2016 May 10 22:51 GMT", la.getOffset());
		Flight exepctedFlight = new Flight(Airplanes.getAirplane("717"), "391", "2833", boston, departuretime, la,
				arrivaltime, new Price("$1,233.25"), 17, new Price("$360.95"), 50);
		Trip expectedTrip = new Trip(exepctedFlight);

		List<Trip> expectedTrips = new LinkedList<Trip>();
		expectedTrips.add(expectedTrip);

		assertValidTrips(actualtrips, boston, la, depart, 3, 1 * 60 * 60, 5 * 60 * 60);
		assertEquals(expectedTrips, actualtrips);
	}

	@Test
	public void testBOStoPHX() {
		Airport boston = Airports.getAirport("BOS");
		Airport phoenix = Airports.getAirport("PHX");

		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> actualtrips = (new TripBuilder()).getTrips(boston, phoenix, depart);

		DateTime departuretime1 = DateTime.of("2016 May 10 11:56 GMT", boston.getOffset());
		DateTime arrivaltime1 = DateTime.of("2016 May 10 16:44 GMT", phoenix.getOffset());
		Flight exepctedFlight1 = new Flight(Airplanes.getAirplane("777"), "288", "2827", boston, departuretime1,
				phoenix, arrivaltime1, new Price("$276.21"), 35, new Price("$54.55"), 15);
		Trip expectedTrip1 = new Trip(exepctedFlight1);

		DateTime departuretime2 = DateTime.of("2016 May 10 19:32 GMT", boston.getOffset());
		DateTime arrivaltime2 = DateTime.of("2016 May 11 01:57 GMT", phoenix.getOffset());
		Flight exepctedFlight2 = new Flight(Airplanes.getAirplane("737"), "385", "2839", boston, departuretime2,
				phoenix, arrivaltime2, new Price("$1,040.53"), 9, new Price("$291.35"), 96);
		Trip expectedTrip2 = new Trip(exepctedFlight2);

		List<Trip> expectedTrips = new LinkedList<Trip>();
		expectedTrips.add(expectedTrip1);
		expectedTrips.add(expectedTrip2);

		assertValidTrips(actualtrips, boston, phoenix, depart, 3, 1 * 60 * 60, 5 * 60 * 60);
		assertEquals(expectedTrips, actualtrips);
	}
}
