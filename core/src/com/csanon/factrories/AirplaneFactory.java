package com.csanon.factrories;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.csanon.Airplane;

/**
 * 
 * Factory for constructing Airplanes from xml or passed parameters
 *
 */
public class AirplaneFactory {
	private static final AirplaneFactory INSTANCE = new AirplaneFactory();
	private final static XmlReader reader = new XmlReader();

	private AirplaneFactory() {

	}

	/**
	 * Get the singleton instance of the factory
	 * 
	 * @return INSTANCE
	 */
	public static AirplaneFactory getInstance() {
		return INSTANCE;
	}

	public Airplane makeAirplane(String manufacturer, String model, int numFirst, int numEcon) {
		return new Airplane(manufacturer, model, numFirst, numEcon);
	}

	/**
	 * Parse the airplanes from xml
	 * 
	 * @param xml
	 *            XML representation of a list of airplanes
	 * @return Parsed list of airplanes
	 */
	public List<Airplane> parseAirplanesFromXML(String xml) {
		List<Airplane> airplanes = new LinkedList<Airplane>();

		Element airplanesNode = reader.parse(xml);

		airplanesNode.getChildrenByName("Airplane").iterator().forEachRemaining(airportNode -> {
			String manufacturer = airportNode.get("Manufacturer");
			String model = airportNode.get("Model");
			int numFirst = airportNode.getInt("FirstClassSeats");
			int numEcon = airportNode.getInt("CoachSeats");
			Airplane airplane = makeAirplane(manufacturer, model, numFirst, numEcon);

			airplanes.add(airplane);
		});

		return airplanes;
	}
}
