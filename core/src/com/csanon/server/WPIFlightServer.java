package com.csanon.server;

import java.util.List;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.factrories.AirplaneFactory;
import com.csanon.factrories.AirportFactory;
import com.csanon.factrories.FlightFactory;
import com.csanon.time.DateTime;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

public class WPIFlightServer implements FlightServer {
	private final ServerConfig config;

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

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return airports;
	}

	@Override
	public List<Flight> getFlightsDeparting(Airport airport, DateTime date) {
		return getFlights(airport, date, "departing");
	}

	@Override
	public List<Flight> getFlightsArrivingAt(Airport airport, DateTime date) {
		return getFlights(airport, date, "arriving");
	}

	private List<Flight> getFlights(Airport airport, DateTime date, String direction) {
		List<Flight> flights = null;
		String dateString = date.toServerDateString();
		String airportCode = airport.getCode();

		try {
			HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
					.queryString("action", "list").queryString("list_type", direction)
					.queryString("airport", airportCode).queryString("day", dateString);
			HttpResponse<String> response = request.asString();
			String result = response.getBody();
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