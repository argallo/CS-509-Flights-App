package com.csanon.server;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.ws.http.HTTPException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.SeatClass;
import com.csanon.Trip;
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

		HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "list").queryString("list_type", "airports");

		try {
			// get the response
			HttpResponse<String> response = request.asString();

			if (response.getStatus() != 200) {
				throw new HTTPException(response.getStatus());

			} else {
				String result = response.getBody();

				airports = AirportFactory.getInstance().parseAirportsFromXML(result);
			}
		} catch (Exception e) {
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
		String dateString = date.getUTC().toServerDateString();
		String airportCode = airport.getCode();

		HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "list").queryString("list_type", direction).queryString("airport", airportCode)
				.queryString("day", dateString);
		try {
			HttpResponse<String> response = request.asString();

			if (response.getStatus() != 200) {
				throw new HTTPException(response.getStatus());
			} else {
				String result = response.getBody();
				flights = FlightFactory.getInstance().parseFlightsFromXML(result);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flights;
	}

	@Override
	public List<Airplane> getAirplanes() {
		List<Airplane> airplanes = null;

		HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "list").queryString("list_type", "airplanes");
		try {
			HttpResponse<String> response = request.asString();

			if (response.getStatus() != 200) {
				throw new HTTPException(response.getStatus());
			} else {
				String result = response.getBody();
				airplanes = AirplaneFactory.getInstance().parseAirplanesFromXML(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return airplanes;
	}

	@Override
	public int getOffsetFromLatLong(double lat, double lon) throws Exception {
		int offset = 0;

		HttpRequest request = Unirest.get(config.getLatLongURL()).queryString("lat", new Double(lat))
				.queryString("lng", new Double(lon)).queryString("key", config.getLatLongKey());
		HttpResponse<String> response = request.asString();

		if (response.getStatus() != 200) {
			throw new HTTPException(response.getStatus());
		} else {
			String result = response.getBody();

			XmlReader reader = new XmlReader();
			Element resultNode = reader.parse(result);

			offset = Integer.parseInt(resultNode.get("gmtOffset"));
		}

		return offset;
	}

	@Override
	public boolean lockServer() {
		boolean successful = false;
		HttpRequest request = Unirest.post(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "lockDB");
		try {
			HttpResponse<String> response = request.asString();
			System.out.println(response.getBody() + response.getStatus());
			if (response.getStatus() != 202) {
				throw new HTTPException(response.getStatus());

			} else {
				successful = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return successful;
	}

	@Override
	public boolean unlockServer() {
		boolean successful = false;
		HttpRequest request = Unirest.post(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "unlockDB");
		try {
			HttpResponse<String> response = request.asString();
			System.out.println(response.getBody() + response.getStatus());
			if (response.getStatus() != 202) {
				throw new HTTPException(response.getStatus());

			} else {
				successful = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return successful;
	}

	@Override
	public boolean checkTripAvailable(Trip trip, SeatClass seatClass) {
		boolean available = true;
		// TODO: confirm lock

		// For each of the flights in the trip, confirm that the specified class
		// of seat is still available
		for (Flight flight : trip.getLegs()) {

			Flight serverFlight = getFlightFromServer(flight);

			boolean result = false;
			if (seatClass == SeatClass.ECONOMY) {
				result = serverFlight.checkEconomyAvailable(1);
			} else {
				result = serverFlight.checkFirstClassAvailable(1);
			}

			// if the flight is unavailable set available to false and break
			if (!result) {
				available = false;
				break;
			}
		}

		return available;
	}

	private Flight getFlightFromServer(Flight flight) {
		Flight result = null;
		List<Flight> flights = getFlightsDeparting(flight.getDepartureAirport(), flight.getDepartureTime());

		for (Flight serverFlight : flights) {
			if (serverFlight.getFlightNum().equals(serverFlight.getFlightNum())) {
				result = serverFlight;
				break;
			}
		}

		return result;
	}

	@Override
	public void bookTrip(Trip trip, SeatClass seatClass) {

		// TODO: confirm lock

		// for each flight in the trip, book the flight with the associated
		// seating
		String flightsXML = trip.getLegs().stream()
				.map(flight -> "<Flight number=\"" + flight.getFlightNum() + "\" seating=\"" + seatClass + "\"/>")
				.collect(Collectors.joining());
		flightsXML = "<Flights>" + flightsXML + "</Flights>";

		HttpRequest request = Unirest.post(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "buyTickets").queryString("flightData", flightsXML);

		try {
			HttpResponse<String> response = request.asString();
		} catch (UnirestException e) {

		}
	}

}