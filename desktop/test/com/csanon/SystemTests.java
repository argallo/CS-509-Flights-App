package com.csanon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;
import com.csanon.time.DateTime;

public class SystemTests {

	private final static long TIMEOUT = 2 * 60 * 5;
	private final static List<ITrip> tripsBOS2LAX_5_14_16 = new LinkedList<ITrip>();
	private final static List<ITrip> tripsLAX2BOS_5_20_16 = new LinkedList<ITrip>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;

		Airports.initialize();
		Airplanes.initialize();
	}

	@Before
	public void setUp() throws Exception {
		// TODO ServerFactory.getServer().resetServer()
		ITrip aTrip = null;
		Airplane currentPlane = null;
		Airport departureAirport = null;
		Airport arrivalAirport = null;
		tripsBOS2LAX_5_14_16.clear();

		
		currentPlane = Airplanes.getAirplane("A330");
		departureAirport = Airports.getAirport("BOS");
		arrivalAirport = Airports.getAirport("LAX");
		Flight BOS2LAX_5_14_1 = new Flight(currentPlane, "367", "2988", 
				departureAirport, DateTime.of("2016 May 14 06:36 GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 14 12:43 GMT", arrivalAirport.getOffset()), 
				new Price(661.70), 37,
				new Price(74.11), 346);
		aTrip = new GeneralTrip(BOS2LAX_5_14_1);
		tripsBOS2LAX_5_14_16.add(new EconomyTrip(aTrip));
		tripsBOS2LAX_5_14_16.add(new FirstClassTrip(aTrip));

		
		currentPlane = Airplanes.getAirplane("747");
		departureAirport = Airports.getAirport("BOS");
		arrivalAirport = Airports.getAirport("LAX");
		Flight BOS2LAX_5_14_2 = new Flight(currentPlane, "313", "3001", 
				departureAirport, DateTime.of("2016 May 14 11:26 GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 14 16:39 GMT", arrivalAirport.getOffset()), 
				new Price(190.95), 103,
				new Price(59.20), 144);
		aTrip = new GeneralTrip(BOS2LAX_5_14_2);
		tripsBOS2LAX_5_14_16.add(new EconomyTrip(aTrip));
		tripsBOS2LAX_5_14_16.add(new FirstClassTrip(aTrip));
		

		currentPlane = Airplanes.getAirplane("A330");
		departureAirport = Airports.getAirport("BOS");
		arrivalAirport = Airports.getAirport("LAS");
		Flight BOS2LAS_5_15_1 = new Flight(currentPlane, "304", "14440", 
				departureAirport, DateTime.of("2016 May 15 00:30 GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 15 05:34 GMT", arrivalAirport.getOffset()), 
				new Price(549.13), 13,
				new Price(61.5), 160);
		currentPlane = Airplanes.getAirplane("A380");
		departureAirport = Airports.getAirport("LAS");
		arrivalAirport = Airports.getAirport("SMF");
		Flight LAS2SMF_5_15_1 = new Flight(currentPlane, "63", "25891", 
				departureAirport, DateTime.of("2016 May 15 08:52 GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 14 09:55 GMT", arrivalAirport.getOffset()), 
				new Price(37.09), 41,
				new Price(11.12), 69);
		currentPlane = Airplanes.getAirplane("A320");
		departureAirport = Airports.getAirport("SMF");
		arrivalAirport = Airports.getAirport("LAX");
		Flight SMF2LAX_5_15_1 = new Flight(currentPlane, "71", "25891", 
				departureAirport, DateTime.of("2016 May 14 14:47 GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 14 15:58 GMT", arrivalAirport.getOffset()), 
				new Price(446.05), 3,
				new Price(43.26), 118);
		aTrip = new GeneralTrip(BOS2LAS_5_15_1, LAS2SMF_5_15_1, SMF2LAX_5_15_1);
		tripsBOS2LAX_5_14_16.add(new EconomyTrip(aTrip));
		tripsBOS2LAX_5_14_16.add(new FirstClassTrip(aTrip));
		
		
		/*
		currentPlane = Airplanes.getAirplane("A330");
		departureAirport = Airports.getAirport("BOS");
		arrivalAirport = Airports.getAirport("LAX");
		Flight BOS2LAS_5_14_1 = new Flight(currentPlane, "", "", 
				departureAirport, DateTime.of("2016 May 14  GMT", departureAirport.getOffset()), 
				arrivalAirport, DateTime.of("2016 May 14  GMT", arrivalAirport.getOffset()), 
				new Price(), ,
				new Price(), );
		*/
		
		tripsBOS2LAX_5_14_16.add(aTrip);

		tripsLAX2BOS_5_20_16.clear();

	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Select the cheapest option Lock the server Book the
	 * trip unlock the server Search trips again confirm that the components
	 * flights have changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void RegularOneWay() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>();

		ITrip bookedTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);
		flightServer.bookTrip(bookedTrip);

		flightServer.unlockServer();

		ITrip newBookedTrip = null;

		List<ITrip> changedTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		assertEquals(true, changedTrips.contains(newBookedTrip));
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX Select the most expensive option on the way
	 * Select the cheapest option on the way back Lock the server Book the two
	 * trips unlock the server Search trips again confirm that the components
	 * flights have changed
	 * 
	 * @throws Exception
	 */
	@Test
	public void RegularRoundTrip() throws Exception {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		DateTime returnTime = DateTime.of(2016, 5, 20, 0);
		List<ITrip> actualDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> actualreturnTrips = tripBuilder.getTrips(arrivalAirport, departureAirport, departureTime);
		List<ITrip> expectedDepartureTrips = new LinkedList<ITrip>();
		List<ITrip> expectedReturnTrips = new LinkedList<ITrip>();

		ITrip bookedDepartureTrip = null;
		ITrip bookedreturnTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedDepartureTrips), new HashSet<ITrip>(actualDepartureTrips));
		assertEquals(new HashSet<ITrip>(expectedReturnTrips), new HashSet<ITrip>(actualreturnTrips));

		Consumer<String> callback = message -> {
			fail("Server Timed out when it shouldn't have");
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);

		flightServer.bookTrip(bookedDepartureTrip);
		flightServer.bookTrip(bookedreturnTrip);

		flightServer.unlockServer();

		ITrip changedDepartureBookTrip = null;
		ITrip changedReturnBookTrip = null;

		List<ITrip> changedDepartureTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> changedReturnTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		assertEquals(true, changedDepartureTrips.contains(changedDepartureBookTrip));
		assertEquals(true, changedReturnTrips.contains(changedReturnBookTrip));
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Select the cheapest option Lock the server Wait for
	 * 2.5 minutes attempt to book the trip
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void ServerTimeout() throws InterruptedException {
		List<Boolean> success = new LinkedList<Boolean>();
		success.add(false);
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>();

		ITrip bookedTrip = null;

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(actualTrips));

		Consumer<String> callback = message -> {
			success.set(0, true);
		};

		boolean booked = flightServer.lockServer(callback);
		assertEquals(true, booked);

		flightServer.wait(TIMEOUT + 1);
		
		assertTrue(success.get(0));

		try {
			flightServer.bookTrip(bookedTrip);
			fail("Able to book a trip which should not happen");
		} catch (Exception e) {

		}
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Sort trips by the price ascending Sort trips by the
	 * price descending
	 */
	@Test
	public void SortbyPrice() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>();
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(expectedSortedASCTrips);
		Collections.reverse(expectedSortedDESCTrips);
		List<ITrip> actualASCTrips = null; // sort(actualTrips, False)
		List<ITrip> actualDESCTrips = null; // sort(actualTrips, TRUE)

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX Sort trips by the travel time descending Sort trips
	 * by the travel time ascending
	 */
	@Test
	public void SortbyTravelTime() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		List<ITrip> expectedSortedASCTrips = new LinkedList<ITrip>();
		List<ITrip> expectedSortedDESCTrips = new LinkedList<ITrip>(expectedSortedASCTrips);
		Collections.reverse(expectedSortedDESCTrips);
		List<ITrip> actualASCTrips = null; // sort(actualTrips, False)
		List<ITrip> actualDESCTrips = null; // sort(actualTrips, TRUE)

		// verify that we get the expected trips
		assertEquals(expectedSortedASCTrips, actualASCTrips);
		assertEquals(expectedSortedDESCTrips, actualDESCTrips);
	}

	/**
	 * Goes through the normal process of booking a one way flight Search trips
	 * going from BOS to LAX For each component flight confirm the duration
	 * matches the actual time difference, Confirm the timezone of each airport
	 * as well
	 */
	@Test
	public void LocalTime() {
		// TODO
	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX Filter and confirm trips for only economy
	 * seats Filter and confirm trips for only first class seats Confirm that
	 * those two filters make up the entire list of trips
	 */
	@Test
	public void FilterBySeat() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);

		List<ITrip> expectedFilteredECONTrips = new LinkedList<ITrip>();
		List<ITrip> expectedFilteredFIRSTrips = new LinkedList<ITrip>();
		List<ITrip> actualFilteredECONTrips = null; // filter(actualTrips,
													// SeatClass.ECONOMY)
		List<ITrip> actualFilteredFIRSTrips = null; // filter(actualTrips,
													// SeatClass.FIRST)

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedFilteredECONTrips), new HashSet<ITrip>(actualFilteredECONTrips));
		assertEquals(new HashSet<ITrip>(expectedFilteredFIRSTrips), new HashSet<ITrip>(actualFilteredFIRSTrips));

		List<ITrip> combined = new LinkedList<ITrip>(actualFilteredECONTrips);
		combined.addAll(actualFilteredFIRSTrips);
		assertEquals(new HashSet<ITrip>(combined), new HashSet<ITrip>(actualFilteredFIRSTrips));

	}

	/**
	 * Goes through the normal process of booking a round trip flight Search
	 * trips going from BOS to LAX For each trip book the last leg of the trip
	 * Research Confirming that the specific trip both seat options no longer
	 * contains that trip Confirm after all possible trips there are no trips
	 * left
	 */
	@Test
	public void NoFlightsIfAllBooked() {
		TripBuilder tripBuilder = new TripBuilder();
		FlightServer flightServer = ServerFactory.getServer();

		Airport departureAirport = Airports.getAirport("BOS");
		Airport arrivalAirport = Airports.getAirport("LAX");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> fullActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>();

		// verify that we get the expected trips
		assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(fullActualTrips));

		fullActualTrips.forEach(aTrip -> {
			Consumer<String> callback = message -> {
				fail("Server Timed out when it shouldn't have");
			};
			Flight lastFlight = aTrip.getLegs().get(aTrip.getLegs().size() - 1);

			flightServer.lockServer(callback);
			int i = 1;
			while (lastFlight.checkEconomyAvailable(i)) {
				i++;
				try {
					flightServer.bookTrip(new EconomyTrip(lastFlight));
				} catch (Exception e) {
					fail("Unable to book a trip which should not happen");
				}
			}
			flightServer.unlockServer();

			flightServer.lockServer(callback);
			i = 1;
			while (lastFlight.checkFirstClassAvailable(i)) {
				i++;
				try {
					flightServer.bookTrip(new FirstClassTrip(lastFlight));
				} catch (Exception e) {
					fail("Unable to book a trip which should not happen");
				}
			}
			flightServer.unlockServer();

			List<ITrip> toRemove = new LinkedList<ITrip>();
			expectedTrips.forEach(eTrip -> {
				if (eTrip.getLegs().contains(lastFlight)) {
					toRemove.add(eTrip);
				}
			});
			expectedTrips.removeAll(toRemove);

			List<ITrip> partialActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
			assertEquals(new HashSet<ITrip>(expectedTrips), new HashSet<ITrip>(partialActualTrips));
		});

		List<ITrip> emptyActualTrips = tripBuilder.getTrips(departureAirport, arrivalAirport, departureTime);
		List<ITrip> emptyExpectedTrips = new LinkedList<ITrip>();

		assertEquals(emptyExpectedTrips, emptyActualTrips);
	}

	/**
	 * Attempt to search for trips going from BOS to BOS Confirm that there are
	 * no trips available
	 */
	@Test
	public void SameAirport() {
		TripBuilder tripBuilder = new TripBuilder();

		Airport departureAirport = Airports.getAirport("BOS");
		DateTime departureTime = DateTime.of(2016, 5, 14, 0);
		List<ITrip> actualTrips = tripBuilder.getTrips(departureAirport, departureAirport, departureTime);
		List<ITrip> expectedTrips = new LinkedList<ITrip>();

		// verify that we get the expected trips
		assertEquals(expectedTrips, actualTrips);
	}

}
