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
			offset = latLongMap.get(latLongString);
		} else {
			offset = getOffsetFromServer(lat, lon, latLongString, 0);
		}

		int result = offset;
		// System.out.println("RESULT: " + result);
		return result;
	}

	private int getOffsetFromServer(double lat, double lon, String latLongString, int count) {
		int offset;
		try {
			offset = server.getOffsetFromLatLong(lat, lon);
			latLongMap.put(latLongString, offset);
			if (prefs != null) {
				prefs.putInteger(latLongString, offset);
				prefs.flush();
			}
		} catch (Exception e) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (count > 5) {
				return 0;
			} else {
				return getOffsetFromServer(lat, lon, latLongString, count++);
			}
		}
		return offset;
	}
}
