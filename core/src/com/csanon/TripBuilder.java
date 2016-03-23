package com.csanon;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

public class TripBuilder {
	private static final TripBuilder instance = new TripBuilder();
	private static final DateTimeFormatter dprintformat = DateTimeFormatter.ofPattern("yyyy_MMM_dd");
	private static final FlightServer aserver = ServerFactory.getServer();
	
	private TripBuilder() {}

	public TripBuilder getInstance() {
		return instance;
	}

	public List<Trip> getTrips(Airport aDeparture, Airport aDestination, OffsetDateTime aDepartTime) {
		List<Flight> flights = aserver.getFlightsDeparting(aDeparture, aDepartTime);
		List<Trip> validtrips = new LinkedList<Trip>();
		
		flights.forEach(flight -> {
			if (flight.getArrivalAirport().equals(aDestination)) {
				validtrips.add(new Trip(flight));
			}
		});
		
		return validtrips;
	}
}
