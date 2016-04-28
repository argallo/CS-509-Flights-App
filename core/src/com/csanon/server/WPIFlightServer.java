package com.csanon.server;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.xml.ws.http.HTTPException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.ITrip;
import com.csanon.SeatClass;
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
	private final Lock lock;

	/**
	 * Construct the WPIFlightServer with the given configuration
	 * 
	 * @param config
	 *            Server configuration settings
	 */
	public WPIFlightServer(ServerConfig config) {
		this.config = config;
		lock = new Lock(config.getLockTime());
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
			// System.out.println(request.getUrl());
			HttpResponse<String> response = request.asString();

			if (response.getStatus() != 200) {
				throw new HTTPException(response.getStatus());
			} else {
				String result = response.getBody();
				flights = FlightFactory.getInstance().parseFlightsFromXML(result);
				// System.out.println(result);

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
				// System.out.println(result);
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
	public boolean lockServer(Consumer<String> callback) {
		boolean successful = false;
		HttpRequest request = Unirest.post(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "lockDB");
		try {
			HttpResponse<String> response = request.asString();
			// System.out.println(response.getBody() + response.getStatus());
			if (response.getStatus() != 202) {
				throw new HTTPException(response.getStatus());

			} else {
				successful = true;
				lock.lock(callback);
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
			// System.out.println(response.getBody() + response.getStatus());
			if (response.getStatus() != 202) {
				throw new HTTPException(response.getStatus());

			} else {
				successful = true;
				lock.unlock();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return successful;
	}

	@Override
	public boolean checkTripsAvailable(List<ITrip> trips) throws Exception {
		if (!lock.isLocked()) {
			throw new Exception();
		} else {
			boolean available = true;

			for (ITrip trip : trips) {
				SeatClass seatClass = trip.getSeatType();
				// For each of the flights in the trip, confirm that the specified
				// class
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
				if (!available) {
					break;
				}
			}
			return available;
		}
	}

	private Flight getFlightFromServer(Flight flight) {
		Flight result = null;
		List<Flight> flights = getFlightsDeparting(flight.getDepartureAirport(), flight.getDepartureTime());

		for (Flight serverFlight : flights) {
			if (serverFlight.getFlightNum().equals(flight.getFlightNum())) {
				result = serverFlight;
				break;
			}
		}

		return result;
	}

	@Override
	public boolean bookTrips(List<ITrip> trips) throws Exception {

		if (!lock.isLocked()) {
			throw new Exception();
		} else {
			trips.forEach(trip -> {
				trip.getLegs().forEach(flight -> {

					Flight f = getFlightFromServer(flight);
					if (trip.getSeatType() == SeatClass.ECONOMY) {
						System.out.println(f.getFlightNum() + ", E" + f.getEconomySeats());
					} else {
						System.out.println(f.getFlightNum() + ", F" + f.getFirstClassSeats());
					}
				});
			});
			// for each flight in the trip, book the flight with the associated
			// seating
			String flightsXML = trips.stream().map(trip -> {
				return trip.getLegs().stream()
						.map(flight -> "<Flight number=\"" + flight.getFlightNum() + "\" seating=\"" + trip.getSeatType() + "\"/>")
						.collect(Collectors.joining());
			}).collect(Collectors.joining());
			flightsXML = "<Flights>" + flightsXML + "</Flights>";

			HttpRequest request = Unirest.post(config.getURL()).queryString("team", config.getTeamNum())
					.queryString("action", "buyTickets").queryString("flightData", flightsXML);
			// System.out.println(request.getUrl());

			try {
				HttpResponse<String> test = request.asString();
				if (test.getStatus() == 202) {
					System.out.println("Successfully booked trip");
					trips.forEach(trip -> {
						trip.getLegs().forEach(flight -> {
							Flight f = getFlightFromServer(flight);
							if (trip.getSeatType() == SeatClass.ECONOMY) {
								System.out.println(f.getFlightNum() + ", E" + f.getEconomySeats());
							} else {
								System.out.println(f.getFlightNum() + ", F" + f.getFirstClassSeats());
							}
						});
					});
				} else {
					System.out.println("ERROR");
					return false;
				}
			} catch (UnirestException e) {

			}
			return true;
		}
	}

	@Override
	public void resetServer() {
		HttpRequest request = Unirest.get(config.getURL()).queryString("team", config.getTeamNum())
				.queryString("action", "resetDB");

		try {
			request.asString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}