package com.csanon.factrories;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.csanon.Airplane;
import com.csanon.Airplanes;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.Flight;
import com.csanon.Price;
import com.csanon.time.DateTime;

/**
 * Factory for constructing Flights from xml or passed parameters
 *
 */
public class FlightFactory {

	private static final FlightFactory INSTANCE = new FlightFactory();
	private final static XmlReader reader = new XmlReader();

	private FlightFactory() {

	}

	public static FlightFactory getInstance() {
		return INSTANCE;
	}

	public Flight makeFlight(Airplane airplane, String duration, String flightNumber, Airport departureAirport, DateTime departureTime, Airport arrivalAirport,
			DateTime arrivalTime, Price priceFirstClass, int seatsFirstClass,
			Price priceEconomy, int seatsEconomy) {
		return new Flight(airplane, duration, flightNumber, departureAirport, departureTime, arrivalAirport, arrivalTime, priceFirstClass, seatsFirstClass, priceEconomy,
				seatsEconomy);
	}

	/**
	 * Parse the flights from xml
	 * 
	 * @param xml
	 *            XML representation of a list of flights
	 * @return Parsed list of flights
	 */
	public List<Flight> parseFlightsFromXML(String xml) {
		List<Flight> flights = new LinkedList<Flight>();

		Element flightsNode = reader.parse(xml);

		flightsNode.getChildrenByName("Flight").iterator().forEachRemaining(flightNode -> {

			// get attributes
			String airplaneName = flightNode.get("Airplane");
			Airplane airplane = Airplanes.getAirplane(airplaneName);

			String duration = flightNode.get("FlightTime");
			String flightNumber = flightNode.get("Number");

			// get departure info
			Element departureNode = flightNode.getChildByName("Departure");
			String departureCode = departureNode.get("Code");
			Airport departureAirport = Airports.getAirport(departureCode);
			DateTime departureTime = DateTime.of(departureNode.get("Time"), departureAirport.getOffset());

			// get arrival info
			Element arrivalNode = flightNode.getChildByName("Arrival");
			String arrivalCode = arrivalNode.get("Code");
			Airport arrivalAirport = Airports.getAirport(arrivalCode);
			DateTime arrivalTime = DateTime.of(arrivalNode.get("Time"), arrivalAirport.getOffset());

			// get seating info
			Element seatingNode = flightNode.getChildByName("Seating");

			// get first class data
			Element firstClassNode = seatingNode.getChildByName("FirstClass");
			int seatsFirstClass = Integer.parseInt(firstClassNode.getText());
			Price priceFirstClass = new Price(firstClassNode.get("Price"));

			// get economy data
			Element coachNode = seatingNode.getChildByName("Coach");
			int seatsEconomy = Integer.parseInt(coachNode.getText());
			Price priceEconomy = new Price(coachNode.get("Price"));

			Flight flight = makeFlight(airplane, duration, flightNumber, departureAirport, departureTime, arrivalAirport, arrivalTime, priceFirstClass, seatsFirstClass,
					priceEconomy, seatsEconomy);

			flights.add(flight);
		});

		return flights;
	}
}
