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
import com.csanon.Airport;

public class AirportFactoryTest {
	@Before
	public void setUp() throws Exception {
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + "/OffsetLatLong.pref"));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;
	}

	@Test
	public void test() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Airports><Airport Code=\"ATL\" Name=\"Hartsfield-Jackson Atlanta International\"><Latitude>33.641045</Latitude><Longitude>-84.427764</Longitude></Airport></Airports>";
		List<Airport> airports = AirportFactory.getInstance().parseAirportsFromXML(xml);
		Airport airport = airports.get(0);
		assertEquals("ATL", airport.getCode());
		assertEquals("Hartsfield-Jackson Atlanta International", airport.getName());
		assertEquals((Double)33.641045, airport.getLatitude());
		assertEquals((Double)(-84.427764),airport.getLongitude());

	}

}
