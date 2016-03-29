package com.csanon;

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

public class AirportsTest {

	@Before
	public void setUp() throws Exception {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;
	}

	@Test
	public void testInitialization() {
		Airports.initialize();
		List<Airport> result = Airports.getAirports();
		assertTrue(result.size() > 0);
		assertTrue(Airports.getAirport("BOS").getName().equals("Logan International"));
	}

}
