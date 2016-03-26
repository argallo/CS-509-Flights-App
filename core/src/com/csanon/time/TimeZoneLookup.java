package com.csanon.time;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

public class TimeZoneLookup {

	private static final FlightServer server = ServerFactory.getServer();
	private static final TimeZoneLookup INSTANCE = new TimeZoneLookup();
	private final Preferences prefs;
	private final Map<String, Integer> latLongMap;

	@SuppressWarnings("unchecked")
	private TimeZoneLookup() {
		latLongMap = new HashMap<String, Integer>();

		// load from file
		Application app = Gdx.app;
		if (app != null) {
			prefs = app.getPreferences("comcsanonlatlongoffsets");
			((Map<String, String>) prefs.get()).forEach((key, value) -> {
				latLongMap.put(key, Integer.parseInt(value));
			});

			System.out.println("Loaded " + latLongMap.size() + "items");
		} else {
			prefs = null;
		}
	}

	public static TimeZoneLookup getInstance() {
		return INSTANCE;
	}

	public int getOffsetFromLatLong(double lat, double lon) {
		int offset;
		String latLongString = lat + "," + lon;
		if (latLongMap.containsKey(latLongString)) {
			System.out.println("GETTING FROM CACHE");
			offset = latLongMap.get(latLongString);
		} else {
			System.out.println("RETRIEVING FROM SERVER");
			try {
				offset = server.getOffsetFromLatLong(lat, lon);
				latLongMap.put(latLongString, offset);
				if (prefs != null) {
					prefs.putInteger(latLongString, offset);
					prefs.flush();
				}
			} catch (Exception e) {
				System.out.println("ERROR calling again");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return getOffsetFromLatLong(lat, lon);
			}
		}

		int result = offset / 60 / 60;
		// System.out.println("RESULT: " + result);
		return result;
	}
}
