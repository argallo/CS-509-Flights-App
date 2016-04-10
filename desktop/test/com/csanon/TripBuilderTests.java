package com.csanon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

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
import com.csanon.server.FlightServer;
import com.csanon.time.DateTime;

public class TripBuilderTests {

	private static FlightServer MOCKSERVER= mock(FlightServer.class);
	Flight BOS2LAX_9_1 = null;
	Flight BOS2LAX_10_1 = null;
	Flight BOS2LAX_10_2 = null;
	Flight BOS2LAX_10_3 = null;
	Flight BOS2LAX_11_1 = null;
	Flight BOS2MDW_10_1 = null;
	Flight BOS2MDW_10_2 = null;
	Flight BOS2PHX_10_1 = null;
	Flight BOS2PHX_10_2 = null;
	Flight BOS2MCI_10_1 = null;
	Flight BOS2MCI_10_2 = null;
	Flight MDW2LAX_10_1 = null;
	Flight MDW2LAX_10_2 = null;
	Flight MDW2PHX_10_1 = null;
	Flight MDW2LAX_11_1 = null;
	Flight MCI2BOS_10_1 = null;
	Flight MCI2BOS_11_1 = null;
	Flight MCI2MDW_10_1 = null;
	Flight MCI2MDW_11_1 = null;
	Flight MCI2MDW_11_2 = null;
	Flight PHX2LAX_10_1 = null;
	Flight PHX2LAX_11_1 = null;
	Flight PHX2MCI_10_1 = null;
	Flight PHX2MCI_11_1 = null;
	Flight PHX2BOS_10_1 = null;
	Flight LAX2MCI_10_1 = null;
	Flight LAX2BOS_10_1 = null;
	Flight LAX2MCI_11_1 = null;
	Flight LAX2MDW_10_1 = null;
	Flight LAX2PHX_10_1 = null;
	Flight LAX2PHX_11_1 = null;


	@Before
	public void setUp() {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;

		Airports.initialize();
		Airplanes.initialize();

		Airport boston = Airports.getAirport("BOS");
		Airport phoenix = Airports.getAirport("PHX");
		Airport losangeles = Airports.getAirport("LAX");
		Airport chicago = Airports.getAirport("MDW");
		Airport kansascity = Airports.getAirport("MCI");

		Airplane sss = Airplanes.getAirplane("777");
		Airplane attz = Airplanes.getAirplane("A320");

		//direct flights from BOS to LAX
		BOS2LAX_9_1 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 09 23:59 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 10 05:32 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_10_1 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 10 04:00 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 10 07:05 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_10_2 = new Flight(attz, "100", "TEST",
				boston, DateTime.of("2016 May 11 00:00 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 11 04:05 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_10_3 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 11 03:59 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 11 08:35 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2LAX_11_1 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 11 04:00 GMT", boston.getOffset()),
				losangeles, DateTime.of("2016 May 11 08:36 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		
		//All flights out of BOS on the May 10th
		BOS2MDW_10_1 = new Flight(attz, "100", "TEST",
				boston, DateTime.of("2016 May 10 12:09 GMT", boston.getOffset()),
				chicago, DateTime.of("2016 May 10 15:45 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2MDW_10_2 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 10 14:05 GMT", boston.getOffset()),
				chicago, DateTime.of("2016 May 10 17:01 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2PHX_10_1 = new Flight(attz, "100", "TEST",
				boston, DateTime.of("2016 May 10 15:19 GMT", boston.getOffset()),
				phoenix, DateTime.of("2016 May 10 18:05 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2PHX_10_2 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 11 00:49 GMT", boston.getOffset()),
				phoenix, DateTime.of("2016 May 11 04:05 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2MCI_10_1 = new Flight(attz, "100", "TEST",
				boston, DateTime.of("2016 May 10 17:42 GMT", boston.getOffset()),
				kansascity, DateTime.of("2016 May 10 21:39 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		BOS2MCI_10_2 = new Flight(sss, "100", "TEST",
				boston, DateTime.of("2016 May 11 03:30 GMT", boston.getOffset()),
				kansascity, DateTime.of("2016 May 11 07:21 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		
		//All Flights coming out of MDW
		MDW2LAX_10_1 = new Flight(sss, "100", "TEST",
				chicago, DateTime.of("2016 May 10 18:00 GMT", chicago.getOffset()),
				losangeles, DateTime.of("2016 May 10 23:05 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MDW2LAX_10_2 = new Flight(sss, "100", "TEST",
				chicago, DateTime.of("2016 May 10 20:44 GMT", chicago.getOffset()),
				losangeles, DateTime.of("2016 May 11 00:05 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MDW2PHX_10_1 = new Flight(attz, "100", "TEST",
				chicago, DateTime.of("2016 May 10 21:20 GMT", chicago.getOffset()),
				phoenix, DateTime.of("2016 May 11 04:55 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MDW2LAX_11_1 = new Flight(sss, "100", "TEST",
				chicago, DateTime.of("2016 May 11 18:00 GMT", chicago.getOffset()),
				losangeles, DateTime.of("2016 May 11 23:05 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		
		//All Flights coming out of MCI
		MCI2BOS_10_1 = new Flight(sss, "100", "TEST",
				kansascity, DateTime.of("2016 May 10 23:00 GMT", kansascity.getOffset()),
				boston, DateTime.of("2016 May 11 05:05 GMT", boston.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MCI2BOS_11_1 = new Flight(sss, "100", "TEST",
				kansascity, DateTime.of("2016 May 11 08:49 GMT", kansascity.getOffset()),
				boston, DateTime.of("2016 May 11 11:05 GMT", boston.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MCI2MDW_10_1 = new Flight(attz, "100", "TEST",
				kansascity, DateTime.of("2016 May 10 23:00 GMT", kansascity.getOffset()),
				chicago, DateTime.of("2016 May 11 00:38 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MCI2MDW_11_1 = new Flight(sss, "100", "TEST",
				kansascity, DateTime.of("2016 May 11 05:49 GMT", kansascity.getOffset()),
				chicago, DateTime.of("2016 May 11 12:05 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		MCI2MDW_11_2 = new Flight(attz, "100", "TEST",
				kansascity, DateTime.of("2016 May 11 12:20 GMT", kansascity.getOffset()),
				chicago, DateTime.of("2016 May 11 14:45 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		
		//All flights coming out of PHX
		PHX2LAX_10_1 = new Flight(sss, "100", "TEST",
				phoenix, DateTime.of("2016 May 10 23:06 GMT", phoenix.getOffset()),
				losangeles, DateTime.of("2016 May 11 01:45 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		PHX2LAX_11_1 = new Flight(sss, "100", "TEST",
				phoenix, DateTime.of("2016 May 11 08:21 GMT", phoenix.getOffset()),
				losangeles, DateTime.of("2016 May 11 09:45 GMT", losangeles.getOffset()),
				new Price(200), 0, new Price(100), 0);
		PHX2MCI_10_1 = new Flight(sss, "100", "TEST",
				phoenix, DateTime.of("2016 May 10 22:27 GMT", phoenix.getOffset()),
				kansascity, DateTime.of("2016 May 11 00:45 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		PHX2MCI_11_1 = new Flight(attz, "100", "TEST",
				phoenix, DateTime.of("2016 May 11 05:06 GMT", phoenix.getOffset()),
				kansascity, DateTime.of("2016 May 11 06:20 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		PHX2BOS_10_1 = new Flight(sss, "100", "TEST",
				phoenix, DateTime.of("2016 May 10 22:27 GMT", phoenix.getOffset()),
				boston, DateTime.of("2016 May 11 02:45 GMT", boston.getOffset()),
				new Price(200), 0, new Price(100), 0);
		
		
		//All flights coming out of LAX
		LAX2MCI_10_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 10 14:27 GMT", losangeles.getOffset()),
				kansascity, DateTime.of("2016 May 10 18:45 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		LAX2BOS_10_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 11 00:27 GMT", losangeles.getOffset()),
				boston, DateTime.of("2016 May 11 05:45 GMT", boston.getOffset()),
				new Price(200), 0, new Price(100), 0);
		LAX2MCI_11_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 12 00:27 GMT", losangeles.getOffset()),
				kansascity, DateTime.of("2016 May 12 04:45 GMT", kansascity.getOffset()),
				new Price(200), 0, new Price(100), 0);
		LAX2MDW_10_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 10 21:14 GMT", losangeles.getOffset()),
				chicago, DateTime.of("2016 May 11 01:45 GMT", chicago.getOffset()),
				new Price(200), 0, new Price(100), 0);
		LAX2PHX_10_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 10 19:07 GMT", losangeles.getOffset()),
				phoenix, DateTime.of("2016 May 11 22:05 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);
		LAX2PHX_11_1 = new Flight(sss, "100", "TEST",
				losangeles, DateTime.of("2016 May 11 07:14 GMT", losangeles.getOffset()),
				phoenix, DateTime.of("2016 May 11 14:45 GMT", phoenix.getOffset()),
				new Price(200), 0, new Price(100), 0);

		
		
		//start with the base case for all possiblites
		when(MOCKSERVER.getFlightsDeparting(any(Airport.class), any(DateTime.class))).thenReturn(new LinkedList<Flight>());
		when(MOCKSERVER.getFlightsArrivingAt(any(Airport.class), any(DateTime.class))).thenReturn(new LinkedList<Flight>());
		
		List<Flight> temp = new LinkedList<Flight>();
		
		//Server mocking for boston
		temp.add(BOS2LAX_9_1);
		when(MOCKSERVER.getFlightsDeparting(boston, DateTime.of(2016, 5, 9, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

		temp.add(BOS2LAX_10_1);
		temp.add(BOS2MDW_10_1);
		temp.add(BOS2MDW_10_2);
		temp.add(BOS2PHX_10_1);
		temp.add(BOS2MCI_10_1);
		when(MOCKSERVER.getFlightsDeparting(boston, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		temp.add(BOS2LAX_10_2);
		temp.add(BOS2LAX_10_3);
		temp.add(BOS2PHX_10_2);
		temp.add(BOS2MCI_10_2);
		temp.add(BOS2LAX_11_1);
		when(MOCKSERVER.getFlightsDeparting(boston, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		//Server mocking for Chicago
		temp.add(MDW2LAX_10_1);
		temp.add(MDW2LAX_10_2);
		temp.add(MDW2PHX_10_1);
		when(MOCKSERVER.getFlightsDeparting(chicago, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

		temp.add(MDW2LAX_11_1);
		when(MOCKSERVER.getFlightsDeparting(chicago, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		//Server mocking for Kansas City
		temp.add(MCI2BOS_10_1);
		temp.add(MCI2MDW_10_1);
		when(MOCKSERVER.getFlightsDeparting(kansascity, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

		temp.add(MCI2BOS_11_1);
		temp.add(MCI2MDW_11_1);
		temp.add(MCI2MDW_11_2);
		when(MOCKSERVER.getFlightsDeparting(kansascity, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		//Server mocking for Phoenix
		temp.add(PHX2LAX_10_1);
		temp.add(PHX2MCI_10_1);
		temp.add(PHX2BOS_10_1);
		when(MOCKSERVER.getFlightsDeparting(phoenix, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

		temp.add(PHX2LAX_11_1);
		temp.add(PHX2MCI_11_1);
		when(MOCKSERVER.getFlightsDeparting(phoenix, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		//Server mocking for Los Angeles
		temp.add(LAX2MCI_10_1);
		temp.add(LAX2MDW_10_1);
		when(MOCKSERVER.getFlightsDeparting(losangeles, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

		temp.add(LAX2BOS_10_1);
		temp.add(LAX2PHX_10_1);
		temp.add(LAX2PHX_11_1);
		when(MOCKSERVER.getFlightsDeparting(losangeles, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		temp.add(LAX2MCI_11_1);
		when(MOCKSERVER.getFlightsDeparting(losangeles, DateTime.of(2016, 5, 12, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		temp.add(BOS2LAX_9_1);
		temp.add(BOS2LAX_10_1);
		temp.add(MDW2LAX_10_1);
		when(MOCKSERVER.getFlightsArrivingAt(losangeles, DateTime.of(2016, 5, 10, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();
		
		temp.add(BOS2LAX_10_2);
		temp.add(BOS2LAX_10_3);
		temp.add(BOS2LAX_11_1);
		temp.add(MDW2LAX_10_2);
		temp.add(MDW2LAX_11_1);
		temp.add(PHX2LAX_10_1);
		temp.add(PHX2LAX_11_1);
		when(MOCKSERVER.getFlightsArrivingAt(losangeles, DateTime.of(2016, 5, 11, 0))).thenReturn(new LinkedList<Flight>(temp));
		temp.clear();

	}

	private void assertValidTrips(List<Trip> aTrips, Airport aDeparture, Airport aDestination, DateTime aDepartTime,
			int aMaxHopCount, int aMinLayover, int aMaxLayover) {

		aTrips.forEach(trip -> {
			List<Flight> legs = trip.getLegs();
			// check to make sure the size is within bounds
			assertTrue(legs.size() >= 1);
			assertTrue(legs.size() <= aMaxHopCount);

			// check to make sure the departtime is on the same day as the first
			// leg
			DateTime lowerlimit = aDepartTime.withNewOffset(aDeparture.getOffset()).getUTC()
					.plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset()).getMidnight();
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
				assertTrue(String.format("%d <= %d is not true", aMinLayover,  difference), aMinLayover <= difference);
				assertTrue(String.format("%d >= %d is not true", aMaxLayover,  difference), aMaxLayover >= difference);
				lastflight = next;
			}

		});
	}

	@Test
	public void testBOStoLAXdirect() {
		Airport departureairport = Airports.getAirport("BOS");
		Airport arriveairport = Airports.getAirport("LAX");

		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> actualtrips = (new TripBuilder(MOCKSERVER, 1, 1*60*60, 5*60*60)).getTrips(departureairport, arriveairport, depart);

		List<Trip> expectedtrips = new LinkedList<Trip>();
		expectedtrips.add(new Trip(BOS2LAX_10_1));
		expectedtrips.add(new Trip(BOS2LAX_10_2));
		expectedtrips.add(new Trip(BOS2LAX_10_3));

		assertValidTrips(actualtrips, departureairport, arriveairport, depart, 1, 1 * 60 * 60, 5 * 60 * 60);
		assertEquals(new HashSet<Trip>(expectedtrips), new HashSet<Trip>(actualtrips));
	}
	
	@Test
	public void testBOStoLAXsinglelayover() {
		Airport departureairport = Airports.getAirport("BOS");
		Airport arriveairport = Airports.getAirport("LAX");

		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> actualtrips = (new TripBuilder(MOCKSERVER, 2, 1*60*60, 5*60*60)).getTrips(departureairport, arriveairport, depart);

		List<Trip> expectedtrips = new LinkedList<Trip>();
		expectedtrips.add(new Trip(BOS2LAX_10_1));
		expectedtrips.add(new Trip(BOS2LAX_10_2));
		expectedtrips.add(new Trip(BOS2LAX_10_3));

		expectedtrips.add(new Trip(BOS2MDW_10_1, MDW2LAX_10_1));
		expectedtrips.add(new Trip(BOS2MDW_10_1, MDW2LAX_10_2));
		expectedtrips.add(new Trip(BOS2MDW_10_2, MDW2LAX_10_2));
		expectedtrips.add(new Trip(BOS2PHX_10_2, PHX2LAX_11_1));	

		assertValidTrips(actualtrips, departureairport, arriveairport, depart, 2, 1 * 60 * 60, 5 * 60 * 60);
		assertEquals(new HashSet<Trip>(expectedtrips), new HashSet<Trip>(actualtrips));
	}
	
	@Test
	public void testBOStoLAXdefault() {
		Airport departureairport = Airports.getAirport("BOS");
		Airport arriveairport = Airports.getAirport("LAX");

		DateTime depart = DateTime.of(2016, 5, 10, 0);
		List<Trip> actualtrips = (new TripBuilder(MOCKSERVER)).getTrips(departureairport, arriveairport, depart);

		List<Trip> expectedtrips = new LinkedList<Trip>();
		expectedtrips.add(new Trip(BOS2LAX_10_1));
		expectedtrips.add(new Trip(BOS2LAX_10_2));
		expectedtrips.add(new Trip(BOS2LAX_10_3));

		expectedtrips.add(new Trip(BOS2MDW_10_1, MDW2LAX_10_1));
		expectedtrips.add(new Trip(BOS2MDW_10_1, MDW2LAX_10_2));
		expectedtrips.add(new Trip(BOS2MDW_10_2, MDW2LAX_10_2));
		expectedtrips.add(new Trip(BOS2PHX_10_2, PHX2LAX_11_1));
		

		expectedtrips.add(new Trip(BOS2MDW_10_2, MDW2PHX_10_1, PHX2LAX_11_1));
		expectedtrips.add(new Trip(BOS2MCI_10_2, MCI2MDW_11_2, MDW2LAX_11_1));		

		assertValidTrips(actualtrips, departureairport, arriveairport, depart, 3, 1 * 60 * 60, 5 * 60 * 60);
		assertEquals(new HashSet<Trip>(expectedtrips), new HashSet<Trip>(actualtrips));
	}
	
}
