package com.csanon.server;

public interface FlightServer {

	public String getAirports();

	public String getFlightsDeparting(String airportCode, String date);
	
	public String getFlightsArrivingAt(String airportCode, String date);
	
	public String getPlanes();
}
