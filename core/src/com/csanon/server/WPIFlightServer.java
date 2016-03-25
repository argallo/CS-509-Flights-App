package com.csanon.server;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.factrories.AirplaneFactory;
import com.csanon.factrories.AirportFactory;
import com.csanon.factrories.FlightFactory;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class WPIFlightServer implements FlightServer {
	private final ServerConfig config;
	private final DateTimeFormatter serverDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");

	public WPIFlightServer(ServerConfig config) {
		this.config = config;
	}

	@Override
	public List<Airport> getAirports() {
		List<Airport> airports = null;
		try {
			HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
					.queryString("action", "list").queryString("list_type", "airports");
			System.out.println(request.getUrl());
			HttpResponse<String> response = request.asString();
			String result = response.getBody();

			airports = AirportFactory.getInstance().parseAirportsFromXML(result);

			System.out.println(airports.get(0).getName());

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return airports;
	}

	@Override
	public List<Flight> getFlightsDeparting(Airport airport, OffsetDateTime date) {
		return getFlights(airport, date, "departing");
	}

	@Override
	public List<Flight> getFlightsArrivingAt(Airport airport, OffsetDateTime date) {
		return getFlights(airport, date, "arriving");
	}

	private List<Flight> getFlights(Airport airport, OffsetDateTime date, String direction) {
		List<Flight> flights = null;
		String dateString = date.format(serverDateFormat);
		String airportCode = airport.getCode();
		
		try {
			HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
					.queryString("action", "list").queryString("list_type", direction)
					.queryString("airport", airportCode).queryString("day", dateString);
			System.out.println(request.getUrl());
			HttpResponse<String> response = request.asString();
			String result = response.getBody();
			System.out.println(result);
			flights = FlightFactory.getInstance().parseFlightsFromXML(result);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flights;
	}

	@Override
	public List<Airplane> getAirplanes() {
		List<Airplane> airplanes = null;
		try {
			HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
					.queryString("action", "list").queryString("list_type", "airplanes");
			System.out.println(request.getUrl());
			HttpResponse<String> response = request.asString();
			String result = response.getBody();
			airplanes = AirplaneFactory.getInstance().parseAirplanesFromXML(result);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return airplanes;
	}

}