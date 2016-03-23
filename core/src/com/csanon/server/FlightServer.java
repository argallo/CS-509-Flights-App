package com.csanon.server;

import java.util.List;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;

public interface FlightServer {

	public List<Airport> getAirports();

	public List<Flight> getFlightsDeparting(String airportCode, String date);

	public List<Flight> getFlightsArrivingAt(String airportCode, String date);

	public List<Airplane> getPlanes();
}
