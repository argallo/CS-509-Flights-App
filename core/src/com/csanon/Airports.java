package com.csanon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

public class Airports {

	private final static HashMap<String, Airport> airports = new HashMap<String, Airport>();
	private final static FlightServer server = ServerFactory.getServer();
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) {
			return;
		}
		List<Airport> airportList = server.getAirports();
		Map<String, Airport> airportMap = airportList.stream()
				.collect(Collectors.toMap(Airport::getCode, Function.identity()));
		airports.putAll(airportMap);
		initialized = true;
	}

	public static List<Airport> getAirports() {
		return new LinkedList<Airport>(airports.values());
	}

	public static Airport getAirport(String code) {
		return airports.get(code);
	}

}
