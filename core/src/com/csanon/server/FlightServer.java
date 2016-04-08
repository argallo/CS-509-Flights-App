package com.csanon.server;

import java.util.List;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.SeatClass;
import com.csanon.Trip;
import com.csanon.time.DateTime;

public interface FlightServer {

	public List<Airport> getAirports();

	public List<Flight> getFlightsDeparting(Airport airportCode, DateTime date);

	public List<Flight> getFlightsArrivingAt(Airport airportCode, DateTime date);

	public List<Airplane> getAirplanes();

	public int getOffsetFromLatLong(double lat, double lon) throws Exception;

	public boolean lockServer();

	public boolean unlockServer();
	
	public boolean checkTripAvailable(Trip trip, SeatClass seatClass);

	public void bookTrip(Trip trip, SeatClass seatClass);
}
