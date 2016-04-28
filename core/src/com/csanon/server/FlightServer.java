package com.csanon.server;

import java.util.List;
import java.util.function.Consumer;

import com.csanon.Airplane;
import com.csanon.Airport;
import com.csanon.Flight;
import com.csanon.ITrip;
import com.csanon.time.DateTime;

public interface FlightServer {

	/**
	 * Get the list of airports from the server
	 * 
	 * @return List of Airport
	 */
	public List<Airport> getAirports();

	/**
	 * Get flights that depart the specified airport on the specified date
	 * 
	 * @param airportCode
	 *            Code representing the desired airport
	 * @param date
	 *            Date in GMT to get flights for
	 * @return List of Flight
	 */
	public List<Flight> getFlightsDeparting(Airport airportCode, DateTime date);

	/**
	 * Get flights that arrive at the specified airport on the specified date
	 * 
	 * @param airportCode
	 *            Code representing the desired airport
	 * @param date
	 *            Date in GMT to get flights for
	 * @return List of Flight
	 */
	public List<Flight> getFlightsArrivingAt(Airport airportCode, DateTime date);

	/**
	 * get the list of airplanes from the server
	 * 
	 * @return List of Airplane
	 */
	public List<Airplane> getAirplanes();

	/**
	 * Get the UTC offset at the given lat long
	 * 
	 * @param lat
	 *            Latitude
	 * @param lon
	 *            Longitude
	 * @return UTC offset in seconds
	 * @throws Exception
	 */
	public int getOffsetFromLatLong(double lat, double lon) throws Exception;

	/**
	 * Send a request to lock the server
	 * 
	 * @param callback
	 *            Callback called on lock timeout
	 * @return True if lock was established
	 */
	public boolean lockServer(Consumer<String> callback);

	/**
	 * Unlock the server and cancel lock timeout
	 * 
	 * @return true if server is unlocked
	 */
	public boolean unlockServer();

	/**
	 * Checks to make sure the provided trips are still available
	 * 
	 * @param trips
	 *            List of trips to check
	 * @return True if all flights in all trips for the specified class of seat are still available
	 * @throws Exception
	 *             Server not locked exception
	 */
	public boolean checkTripsAvailable(List<ITrip> trips) throws Exception;

	/**
	 * Resets the server
	 */
	public void resetServer();

	/**
	 * Books the trips
	 * 
	 * @param trips
	 *            Trips to book
	 * @return whether a trip was booked or not
	 * @throws Exception
	 *             Server not locked exception
	 */
	boolean bookTrips(List<ITrip> trips) throws Exception;
}
