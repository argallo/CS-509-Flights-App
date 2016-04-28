package com.csanon;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;
import com.csanon.time.DateTime;

/**
 * @author Sean
 *
 */
public class TripBuilder {
	private static final FlightServer SERVERDEFUALT = ServerFactory.getServer();

	private static final int MAXHOPCOUNTDEFUALT = 3;
	private static final int MINLAYOVERDEFAULT = 1 * 60 * 60;
	private static final int MAXLAYOVERDEFAULT = 5 * 60 * 60;

	private final FlightServer server;
	private final int maxHopCount;
	private final int minLayover;
	private final int maxLayover;

	/**
	 * Init for the object using static defaults
	 */
	public TripBuilder() {
		this(MAXHOPCOUNTDEFUALT, MINLAYOVERDEFAULT, MAXLAYOVERDEFAULT);
	}

	/**
	 * Init for the object using the given server and everything else default
	 * 
	 * @param aServer
	 *            a FlightServer that will be used by this instance
	 */
	public TripBuilder(FlightServer aServer) {
		this(aServer, MAXHOPCOUNTDEFUALT, MINLAYOVERDEFAULT, MAXLAYOVERDEFAULT);
	}

	/**
	 * Init for the object using custom layover bounds and hopcount
	 * 
	 * @param aMaxHopCounts
	 *            the max number allowed for each hopcount
	 * @param aMinLayover
	 *            the minimum time for layover in seconds
	 * @param aMaxLayover
	 *            the maximum time for a layover in seconds
	 */
	public TripBuilder(int aMaxHopCount, int aMinLayover, int aMaxLayover) {
		this(SERVERDEFUALT, aMaxHopCount, aMinLayover, aMaxLayover);
	}

	/**
	 * Init for the object using the given data
	 * 
	 * @param aServer
	 *            the server to be used for making calls
	 * @param aMaxHopCounts
	 *            the max number allowed for each hopcount
	 * @param aMinLayover
	 *            the minimum time for layover in seconds
	 * @param aMaxLayover
	 *            the maximum time for a layover in seconds
	 */
	public TripBuilder(FlightServer aServer, int aMaxHopCount, int aMinLayover, int aMaxLayover) {
		server = aServer;
		maxHopCount = aMaxHopCount;
		minLayover = aMinLayover;
		maxLayover = aMaxLayover;
	}

	/**
	 * Fetches all trips from the departure to destination on the given date
	 * 
	 * @param aDeparture
	 *            the departure airport where the trips must originate
	 * @param aDestination
	 *            the destination airport where the trips must arrive
	 * @param aDepartTime
	 *            the local time date in which the plane must leave
	 * @return a list of Trip bound by the instance settings
	 */
	public List<ITrip> getTrips(Airport aDeparture, Airport aDestination, DateTime aDepartTime) {
		// Convert the departtime to the same local time with the departure
		// offset
		System.out.println("Dep: " + aDeparture.getCode() + " Dest: " + aDestination.getCode() + " Date: "
				+ aDepartTime.toString());
		aDepartTime = aDepartTime.plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset());
		Map<Airport, Map<String, List<Flight>>> dataset = collectData(maxHopCount, aDeparture, aDestination,
				aDepartTime);

		System.out.println("Dataset size: " + dataset.size());

		Collection<ITrip> validtrips = searchForTrips(maxHopCount, aDeparture, aDestination, aDepartTime, dataset);

		List<ITrip> seperatedTrips = new LinkedList<ITrip>();
		validtrips.forEach(aTrip -> {
			try {
				seperatedTrips.add(new FirstClassTrip(aTrip));
			} catch (Exception e) {

			}

			try {
				seperatedTrips.add(new EconomyTrip(aTrip));
			} catch (Exception e) {

			}
		});
		System.out.println("Total returned " + seperatedTrips.size());
		return seperatedTrips;
	}

	/**
	 * From a dataset produce a collection of Trip
	 * 
	 * @param aHopCount
	 *            the current hops left before the destination must be reached
	 * @param aDeparture
	 *            the departure airport where trips must originate from
	 * @param aDestination
	 *            the destination airport where trips must terminate
	 * @param aDepartTime
	 *            the departure date of the flight
	 * @param aDataSet
	 *            a list of all flights by date and by airport
	 * @return a collection of valid trips
	 */
	private Collection<ITrip> searchForTrips(int aHopCount, Airport aDeparture, Airport aDestination,
			DateTime aDepartTime, Map<Airport, Map<String, List<Flight>>> aDataSet) {

		List<ITrip> validTrips = new LinkedList<ITrip>();

		// find the lower and upper limits times for the given datetime
		DateTime lowerLimit = aDepartTime.withNewOffset(aDeparture.getOffset()).getUTC()
				.plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset()).getMidnight().getUTC();
		DateTime upperLimit = lowerLimit.getNextDay().getUTC();

		// get the possible days where the given depart time may actually lie
		Collection<DateTime> dateTimes = findPossibleDates(aDepartTime);

		// the map that will be used to store possible trips by a trip
		Map<Airport, Collection<ITrip>> tripsbyAirport = new HashMap<Airport, Collection<ITrip>>();
		// loop through each possible time when the trip may start
		if (aHopCount > 0) {
			dateTimes.forEach(time -> {
				// ensure that the dataset contains the departure airport and
				// the current date
				if (aDataSet.containsKey(aDeparture)
						&& aDataSet.get(aDeparture).containsKey(time.toServerDateString())) {
					// loop through each flight for the departure airport and
					// the current date
					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						// verify that the departure time is within the upper
						// and lower bound
						if (lowerLimit.compareTo(flight.getDepartureTime().getUTC()) <= 0
								&& upperLimit.compareTo(flight.getDepartureTime().getUTC()) > 0) {

							// Check whether the flight is going to the
							// destination
							if (flight.getArrivalAirport().equals(aDestination)) {
								// if so then we can add it to valid trips
								try {
									validTrips.add(new GeneralTrip(flight));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else { // try to find trips with one less hop that
										// will terminate at the destination
								// add an empty set with the arrival airport as
								// key if it doesn't exist
								if (!tripsbyAirport.containsKey(flight.getArrivalAirport())) {
									tripsbyAirport.put(flight.getArrivalAirport(), new HashSet<ITrip>());
								}
								// get possible dates when the flight may depart
								// from based on layover bounds
								DateTime minLayoverTime = flight.getArrivalTime().plusSeconds(minLayover);
								DateTime maxLayovertime = flight.getArrivalTime().plusSeconds(maxLayover);

								// using possible dates add possible trips with
								// one less hop for the current flights arrival
								// airport
								tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
										flight.getArrivalAirport(), aDestination, minLayoverTime, aDataSet));
								tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
										flight.getArrivalAirport(), aDestination, maxLayovertime, aDataSet));
							}
						}

					});
				}
			});

			// Loop through the flights originating from the departure and find
			// valid trips
			// Once again loop through possible times that a trip may start
			dateTimes.forEach(time -> {
				// Make sure that the dataset contains the departure airport and
				// date
				if (aDataSet.containsKey(aDeparture)
						&& aDataSet.get(aDeparture).containsKey(time.toServerDateString())) {
					// Loop through the flights
					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						// make sure the departure is within the instance bounds
						if (lowerLimit.compareTo(flight.getDepartureTime().getUTC()) <= 0
								&& upperLimit.compareTo(flight.getDepartureTime().getUTC()) > 0) {
							// make sure the arrival airport is not the
							// destination since
							if (!flight.getArrivalAirport().equals(aDestination)) {
								// loop through possible trips trying to find
								// complete trips
								tripsbyAirport.get(flight.getArrivalAirport()).forEach(trip -> {
									// make sure the departure time of the trip
									// is within the bounds of the arrival for
									// the current flight
									if (flight.getArrivalTime().plusSeconds(minLayover).getUTC()
											.compareTo(trip.getLegs().get(0).getDepartureTime().getUTC()) <= 0
											&& flight.getArrivalTime().plusSeconds(maxLayover).getUTC().compareTo(
													trip.getLegs().get(0).getDepartureTime().getUTC()) >= 0) {
										// Make sure that we would not be adding
										// a duplicate airport
										if (!trip.getAirports().contains(flight.getDepartureAirport())) {
											validTrips.add(trip.addLeg(flight, true));
										}
									}
								});
							}
						}
					});
				}
			});
		}

		return validTrips;
	}

	/**
	 * Using this instance's server find the list of flights used to make sure
	 * that server calls are minimized
	 * 
	 * @param aDeparture
	 *            a departure airport that is used to find
	 * @param aTime
	 *            the date time that will be used
	 * @param aDataSet
	 *            the complete dataset of possible flights
	 * @return a boolean indicating whether a server call was made
	 */
	private boolean addFlightsToMap(Airport aDeparture, DateTime aTime,
			Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (!aDataSet.containsKey(aDeparture)) {
			aDataSet.put(aDeparture, new HashMap<String, List<Flight>>());
		}

		if (!aDataSet.get(aDeparture).containsKey(aTime.toServerDateString())) {
			List<Flight> flights = server.getFlightsDeparting(aDeparture, aTime);
			List<Flight> filteredflights = new LinkedList<Flight>();
			flights.forEach(flight -> {
				if (flight.checkEconomyAvailable(1) && flight.checkFirstClassAvailable(1)) {
					filteredflights.add(flight);
				}
			});
			aDataSet.get(aDeparture).put(aTime.toServerDateString(), filteredflights);
			return true;
		}
		return false;
	}

	/**
	 * Collects the data, creating the dataset, and if the hopcount is greater
	 * than 1 it does a more efficient data builder
	 * 
	 * @param aMaxHopCount
	 *            the maximum number of hops allowed
	 * @param aDeparture
	 *            the departure airport that the graph will start at
	 * @param aDestination
	 *            the destination airport that the graph will terminate at
	 * @param aDepartTime
	 *            the date that the data will start at
	 * @return a list of Flight by date string by airport
	 */
	private Map<Airport, Map<String, List<Flight>>> collectData(int aMaxHopCount, Airport aDeparture,
			Airport aDestination, DateTime aDepartTime) {
		Map<Airport, Map<String, List<Flight>>> dataset = new HashMap<Airport, Map<String, List<Flight>>>();

		if (aMaxHopCount == 1) {
			// special case where it's somewhat trivial to find data
			collectData(aMaxHopCount, aDeparture, aDestination, aDepartTime, dataset);
		} else if (aMaxHopCount > 1) {
			collectData(aMaxHopCount - 1, aDeparture, aDestination, aDepartTime, dataset);

			// find the last leg of trips efficiently
			findPossibleArrivalDates(dataset).forEach(datetime -> {
				// put each flight from the server response into the dataset
				server.getFlightsArrivingAt(aDestination, datetime).forEach(flight -> {
					if (!dataset.containsKey(flight.getDepartureAirport())) {
						dataset.put(flight.getDepartureAirport(), new HashMap<String, List<Flight>>());
					}

					if (!dataset.get(flight.getDepartureAirport()).containsKey(datetime.toServerDateString())) {
						dataset.get(flight.getDepartureAirport()).put(datetime.toServerDateString(),
								new LinkedList<Flight>());
					}
					if (!dataset.get(flight.getDepartureAirport()).get(datetime.toServerDateString())
							.contains(flight)) {
						dataset.get(flight.getDepartureAirport()).get(datetime.toServerDateString()).add(flight);
					}
				});
			});

		}

		return dataset;
	}

	/**
	 * Updates the given dataset with a branching of possible dates
	 * 
	 * @param aMaxHopCount
	 *            the maximum number of hops allowed
	 * @param aDeparture
	 *            the departure airport that the graph will start at
	 * @param aDestination
	 *            the destination airport that the graph will terminate at
	 * @param aDepartTime
	 *            the date that the data will start at
	 * @param aDataSet
	 *            a list of Flight by date string by Airport
	 */
	private void collectData(int aMaxHopCount, Airport aDeparture, Airport aDestination, DateTime aDepartTime,
			Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (aMaxHopCount > 0) {
			Collection<DateTime> dateTimes = findPossibleDates(aDepartTime);

			// loop through possible datetimes and
			dateTimes.forEach(time -> {
				if (addFlightsToMap(aDeparture, time, aDataSet)) {

					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						if (!flight.getArrivalAirport().equals(aDestination)) {
							// Collect data for each of the layover bounds
							DateTime minLayoverTime = flight.getArrivalTime().plusSeconds(minLayover);
							DateTime maxLayoverTime = flight.getArrivalTime().plusSeconds(maxLayover);
							collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, minLayoverTime,
									aDataSet);
							collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, maxLayoverTime,
									aDataSet);
						}
					});
				}
			});

		}
	}

	/**
	 * Find possible dates a flight might start by the given date with an
	 * airport's offset
	 * 
	 * @param aTime
	 *            the date that will be used
	 * @return a collection of possible DateTime
	 */
	private static Collection<DateTime> findPossibleDates(DateTime aTime) {
		DateTime baseTime = aTime.getMidnight();
		Collection<DateTime> dateTimes = new HashSet<DateTime>();
		dateTimes.add(baseTime.getUTC().getMidnight());
		if (!aTime.isUTC()) {
			baseTime = baseTime.getNextDay();
			dateTimes.add(baseTime.getUTC().getMidnight());
		}

		return dateTimes;
	}

	/**
	 * Find possible dates for the last leg of trips
	 * 
	 * @param aDataSet
	 *            the dataset that will be use to generate the dates
	 * @return a collection of DateTime
	 */
	private Collection<DateTime> findPossibleArrivalDates(Map<Airport, Map<String, List<Flight>>> aDataSet) {
		Collection<DateTime> arrivalDates = new HashSet<DateTime>();

		aDataSet.values().forEach(flightsByDate -> {
			flightsByDate.values().forEach(flights -> {
				flights.forEach(flight -> {
					arrivalDates.add(flight.getArrivalTime().getUTC().getMidnight());
					arrivalDates.add(flight.getArrivalTime().getNextDay().getUTC().getMidnight());
					arrivalDates.add(flight.getArrivalTime().getNextDay().getNextDay().getUTC().getMidnight());
				});
			});
		});

		return arrivalDates;
	}
}
