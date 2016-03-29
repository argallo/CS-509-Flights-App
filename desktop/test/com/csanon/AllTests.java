package com.csanon;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.csanon.server.WPIFlightServerTest;

@RunWith(Suite.class)
@SuiteClasses({ AirplanesTest.class, AirportsTest.class, TripBuilderTests.class, WPIFlightServerTest.class })
public class AllTests {
	private static final String SECONDFILENAME = "/OffsetLatLong.pref";
	@BeforeClass
	public static void setUpClass() {
		System.out.println("Master setup");
		// mock the application and preferences
		Application app = mock(Application.class);
		Preferences pref = new LwjglPreferences(new FileHandle(System.getProperty("user.dir") + SECONDFILENAME));
		when(app.getPreferences("comcsanonlatlongoffsets")).thenReturn(pref);
		Gdx.app = app;
	}
}
