package com.csanon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

public class Airplanes {

	private final static HashMap<String, Airplane> airplanes = new HashMap<String, Airplane>();
	private final static FlightServer server = ServerFactory.getServer();
	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) {
			// TODO: throw exception
		}
		List<Airplane> airplaneList = server.getAirplanes();
		Map<String, Airplane> airplaneMap = airplaneList.stream()
				.collect(Collectors.toMap(Airplane::getModel, Function.identity()));
		airplanes.putAll(airplaneMap);
		initialized = true;
	}

	public static List<Airplane> getAirplanes() {
		return new LinkedList<Airplane>(airplanes.values());
	}

	public static Airplane getAirplane(String code) {
		return airplanes.get(code);
	}

}
