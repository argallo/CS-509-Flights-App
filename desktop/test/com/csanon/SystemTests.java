package com.csanon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;

public class SystemTests {

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
		//TODO reset server
	}


	/**
	 * Goes through the normal process of booking a one way flight
	 * Search trips going from BOS to LAX
	 * Select the cheapest option
	 * Lock the server
	 * Book the trip
	 * unlock the server
	 * Search trips again confirm that the components flights have changed
	 */
	@Test
	public void RegularOneWay() {
		
	}
	
	/**
	 * Goes through the normal process of booking a round trip flight
	 * Search trips going from BOS to LAX
	 * Select the most expensive option on the way
	 * Select the cheapest option on the way back
	 * Lock the server
	 * Book the two trips
	 * unlock the server
	 * Search trips again confirm that the components flights have changed
	 */
	@Test
	public void RegularRoundTrip() {
		
	}
	
	/**
	 * Goes through the normal process of booking a one way flight
	 * Search trips going from BOS to LAX
	 * Select the cheapest option
	 * Lock the server
	 * Wait for 2.5 minutes
	 * attempt to book the trip
	 */
	@Test
	public void ServerTimeout() {
		
	}
	
	/**
	 * Goes through the normal process of booking a one way flight
	 * Search trips going from BOS to LAX
	 * Sort trips by the price ascending
	 * Sort trips by the price descending
	 */
	@Test
	public void SortbyPrice() {
		
	}
	
	/**
	 * Goes through the normal process of booking a one way flight
	 * Search trips going from BOS to LAX
	 * Sort trips by the travel time descending
	 * Sort trips by the travel time ascending
	 */
	@Test
	public void SortbyTravelTime() {
		
	}
	
	/**
	 * Goes through the normal process of booking a one way flight
	 * Search trips going from BOS to LAX
	 * For each component flight confirm the duration matches the actual time difference,
	 * Confirm the timezone of each airport as well
	 */
	@Test
	public void LocalTime() {
		
	}
	
	/**
	 * Goes through the normal process of booking a round trip flight
	 * Search trips going from BOS to LAX
	 * Filter and confirm trips for only economy seats
	 * Filter and confirm trips for only first class seats
	 * Confirm that those two filters make up the entire list of trips
	 */
	@Test
	public void FilterBySeat() {
		
	}
	
	/**
	 * Goes through the normal process of booking a round trip flight
	 * Search trips going from BOS to LAX
	 * For each trip book the last leg of the trip
	 * Research Confirming that the specific trip both seat options no longer contains that trip
	 * Confirm after all possible trips there are no trips left
	 */
	@Test
	public void NoFlightsIfAllBooked() {
		
	}
	
	/**
	 * Attempt to search for trips going from BOS to BOS
	 * Confirm that there are no trips available
	 */
	@Test
	public void SameAirport() {
		
	}

}
