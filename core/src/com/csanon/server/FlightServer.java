package com.csanon.server;

import java.time.OffsetDateTime;
import java.util.List;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;

public interface FlightServer {

	public List<Airport> getAirports();

	public List<Flight> getFlightsDeparting(Airport airportCode, OffsetDateTime date);

	public List<Flight> getFlightsArrivingAt(Airport airportCode, OffsetDateTime date);

	public List<Airplane> getAirplanes();
}
