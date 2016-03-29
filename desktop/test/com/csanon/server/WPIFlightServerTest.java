package com.csanon.server;

import static org.junit.Assert.assertTrue;
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
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.Flight;
import com.csanon.time.DateTime;

public class WPIFlightServerTest {

	private static final String SECONDFILENAME = "/OffsetLatLong.pref";
	Airport airport;
	DateTime date;

	@Before
	public void setUp() throws Exception {

		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + SECONDFILENAME));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;

		date = DateTime.of(2016, 5, 10, 0);
		Airports.initialize();
		Airplanes.initialize();
		airport = Airports.getAirport("BOS");

	}

	@Test
	public void testGetAirports() {
		FlightServer server = ServerFactory.getServer();
		List<Airport> result = server.getAirports();
		System.out.println(result);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetDepartingFlights() {
		FlightServer server = ServerFactory.getServer();
		List<Flight> result = server.getFlightsDeparting(airport, date);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetArrivingFlights() {
		FlightServer server = ServerFactory.getServer();
		List<Flight> result = server.getFlightsArrivingAt(airport, date);
		assertTrue(result.size() > 0);
	}

	@Test
	public void testGetPlanes() {
		FlightServer server = ServerFactory.getServer();
		List<Airplane> result = server.getAirplanes();
		assertTrue(result.size() > 0);
	}

	@Test
	public void testLockServer() {
		FlightServer server = ServerFactory.getServer();
		boolean result = server.lockServer();
		assertTrue(result);
	}

	@Test
	public void testUnlockServer() {
		FlightServer server = ServerFactory.getServer();
		boolean result = server.unlockServer();
		assertTrue(result);
	}

}
