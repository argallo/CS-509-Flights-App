package com.csanon.factrories;

import java.time.OffsetDateTime;
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
import com.csanon.TimeUtil;

public class FlightFactory {

	private static final FlightFactory INSTANCE = new FlightFactory();
	private final static XmlReader reader = new XmlReader();

	private FlightFactory() {

	}

	public static FlightFactory getInstance() {
		return INSTANCE;
	}

	public Flight makeFlight(Airplane airplane, String duration, String flightNumber, Airport departureAirport, OffsetDateTime departureTime, Airport arrivalAirport, OffsetDateTime arrivalTime, Price priceFirstClass, int seatsFirstClass,
			Price priceEconomy, int seatsEconomy) {
		return new Flight(airplane, duration, flightNumber, departureAirport, departureTime, arrivalAirport, arrivalTime, priceFirstClass, seatsFirstClass, priceEconomy, seatsEconomy);
	}

	public List<Flight> parseFlightsFromXML(String xml) {
		List<Flight> flights = new LinkedList<Flight>();

		Element flightsNode = reader.parse(xml);

		flightsNode.getChildrenByName("Flight").iterator().forEachRemaining(flightNode -> {

			// TODO: use all fo the flight data
			// get attributes
			String airplaneName = flightNode.get("Airplane");
			String duration = flightNode.get("FlightTime");
			String flightNumber = flightNode.get("Number");

			// get departure info
			Element departureNode = flightNode.getChildByName("Departure");
			String departureCode = departureNode.get("Code");
			// TODO: insert real offset
			OffsetDateTime departureTime = TimeUtil.string2OffsetDateTime(departureNode.get("Time"), 0);

			// get arrival info
			Element arrivalNode = flightNode.getChildByName("Arrival");
			String arrivalCode = arrivalNode.get("Code");
			// TODO: insert real offset
			OffsetDateTime arrivalTime = TimeUtil.string2OffsetDateTime(arrivalNode.get("Time"), 0);

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

			Airport departureAirport = Airports.getAirport(departureCode);
			Airport arrivalAirport = Airports.getAirport(arrivalCode);
			Airplane airplane = Airplanes.getAirplane(airplaneName);

			Flight flight = makeFlight(airplane, duration, flightNumber, departureAirport, departureTime, arrivalAirport, arrivalTime, priceFirstClass, seatsFirstClass, priceEconomy, seatsEconomy);

			flights.add(flight);
		});

		return flights;
	}
}
