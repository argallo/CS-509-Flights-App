package com.csanon;

public interface FlightServer {

	public String getAirports();

	public String getFlights(String airportCode, String date);
}
