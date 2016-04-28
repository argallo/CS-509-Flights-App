package com.csanon.factrories;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.csanon.Airport;

/**
 * Factory for constructing Airports from xml or passed parameters
 */
public class AirportFactory {

	private static final AirportFactory INSTANCE = new AirportFactory();
	private final static XmlReader reader = new XmlReader();

	private AirportFactory() {

	}

	public static AirportFactory getInstance() {
		return INSTANCE;
	}

	public Airport makeAirport(String airportName, String airportCode, String lat, String lon) {
		return new Airport(airportName, airportCode, lat, lon);
	}
	
	/**
	 * Parse the airports from xml
	 * 
	 * @param xml
	 *            XML representation of a list of airports
	 * @return Parsed list of airports
	 */
	public List<Airport> parseAirportsFromXML(String xml) {
		List<Airport> airports = new LinkedList<Airport>();

		Element airportsNode = reader.parse(xml);

		airportsNode.getChildrenByName("Airport").iterator().forEachRemaining(airportNode -> {
			String airportCode = airportNode.get("Code");
			String airportName = airportNode.get("Name");
			String lat = airportNode.get("Latitude");
			String lon = airportNode.get("Longitude");
			Airport airport = makeAirport(airportName, airportCode, lat, lon);

			airports.add(airport);
		});

		return airports;
	}
}
