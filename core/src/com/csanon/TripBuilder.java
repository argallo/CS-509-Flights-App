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

public class TripBuilder {
	private static final FlightServer SERVERDEFUALT = ServerFactory.getServer();

	private static final int MAXHOPCOUNTDEFUALT = 3;
	private static final int MINLAYOVERDEFAULT = 1 * 60 * 60;
	private static final int MAXLAYOVERDEFAULT = 5 * 60 * 60;

	private final FlightServer server;
	private final int maxhopcount;
	private final int minlayover;
	private final int maxlayover;

	public TripBuilder() {
		this(MAXHOPCOUNTDEFUALT, MINLAYOVERDEFAULT, MAXLAYOVERDEFAULT);
	}

	public TripBuilder(int aMaxHopCount, int aMinLayover, int aMaxLayover) {
		this(SERVERDEFUALT, aMaxHopCount, aMinLayover, aMaxLayover);
	}

	public TripBuilder(FlightServer aServer, int aMaxHopCount, int aMinLayover, int aMaxLayover) {
		server = aServer;
		maxhopcount = aMaxHopCount;
		minlayover = aMinLayover;
		maxlayover = aMaxLayover;
	}

	public List<Trip> getTrips(Airport aDeparture, Airport aDestination, DateTime aDepartTime) {
		if (aDeparture.getOffset() != 0) {
			aDepartTime = aDepartTime.plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset());
		}
		Map<Airport, Map<String, List<Flight>>> dataset = collectData(maxhopcount, aDeparture, aDestination,
				aDepartTime);
		List<Trip> validtrips = new LinkedList<Trip>();
		// loop over all possible hop counts
		for (int i = 1; i <= maxhopcount; i++) {
			validtrips.addAll(searchForTrips(i, aDeparture, aDestination, aDepartTime, dataset));
		}

		return validtrips;
	}

	private Collection<Trip> searchForTrips(int aHopCount, Airport aDeparture, Airport aDestination,
			DateTime aDepartTime, Map<Airport, Map<String, List<Flight>>> aDataSet) {
		List<Trip> validtrips = new LinkedList<Trip>();
		DateTime lowerlimit = aDepartTime.withNewOffset(aDeparture.getOffset()).getUTC().plusSeconds(-aDeparture.getOffset()).withNewOffset(aDeparture.getOffset()).getMidnight();
		DateTime upperlimit = lowerlimit.getNextDay();
		Collection<DateTime> datetimes = findPossibleDates(aDepartTime);
		if (aHopCount == 1) {
			datetimes.forEach(time -> {
				if (aDataSet.containsKey(aDeparture)
						&& aDataSet.get(aDeparture).containsKey(time.toServerDateString())) {
					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						if (flight.getArrivalAirport().equals(aDestination)) {
							if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
									&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
								validtrips.add(new Trip(flight));
							}
						}
					});
				}
			});

		} else if (aHopCount > 1) {
			Map<Airport, Collection<Trip>> tripsbyAirport = new HashMap<Airport, Collection<Trip>>();
			datetimes.forEach(time -> {
				if (aDataSet.containsKey(aDeparture)
						&& aDataSet.get(aDeparture).containsKey(time.toServerDateString())) {
					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						if (!flight.getArrivalAirport().equals(aDestination)) {
							if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
									&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
								if (!tripsbyAirport.containsKey(flight.getArrivalAirport())) {
									tripsbyAirport.put(flight.getArrivalAirport(), new HashSet<Trip>());
								}
								DateTime minlayovertime = flight.getArrivalTime().plusSeconds(minlayover);
								DateTime maxlayovertime = flight.getArrivalTime().plusSeconds(maxlayover);

								Collection<Trip> t = searchForTrips(aHopCount - 1, flight.getArrivalAirport(),
										aDestination, minlayovertime, aDataSet);
								t = searchForTrips(aHopCount - 1, flight.getArrivalAirport(), aDestination,
										maxlayovertime, aDataSet);

								tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
										flight.getArrivalAirport(), aDestination, minlayovertime, aDataSet));
								tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
										flight.getArrivalAirport(), aDestination, maxlayovertime, aDataSet));
							}
						}

					});
				}
			});

			datetimes.forEach(time -> {
				if (aDataSet.containsKey(aDeparture)
						&& aDataSet.get(aDeparture).containsKey(time.toServerDateString())) {
					aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
						if (!flight.getArrivalAirport().equals(aDestination)) {
							if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
									&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
								tripsbyAirport.get(flight.getArrivalAirport()).forEach(trip -> {
									if (flight.getArrivalTime().plusSeconds(minlayover)
											.compareTo(trip.getLegs().get(0).getDepartureTime()) <= 0
											&& flight.getArrivalTime().plusSeconds(maxlayover)
													.compareTo(trip.getLegs().get(0).getDepartureTime()) >= 0) {
										if (!trip.getAirports().contains(flight.getDepartureAirport())) {
											;
											validtrips.add(trip.addLeg(flight, true));
										}
									}
								});
							}
						}
					});
				}
			});

		}
		return validtrips;
	}

	private boolean addFlightsToMap(Airport aDeparture, DateTime aTime,
			Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (!aDataSet.containsKey(aDeparture)) {
			aDataSet.put(aDeparture, new HashMap<String, List<Flight>>());
		}

		if (!aDataSet.get(aDeparture).containsKey(aTime.toServerDateString())) {
			aDataSet.get(aDeparture).put(aTime.toServerDateString(), server.getFlightsDeparting(aDeparture, aTime));
			return true;
		}
		return false;
	}

	private Map<Airport, Map<String, List<Flight>>> collectData(int aMaxHopCount, Airport aDeparture,
			Airport aDestination, DateTime aDepartTime) {
		Map<Airport, Map<String, List<Flight>>> dataset = new HashMap<Airport, Map<String, List<Flight>>>();

		if (aMaxHopCount == 1) {
			collectData(aMaxHopCount, aDeparture, aDestination, aDepartTime, dataset);
		} else if (aMaxHopCount > 1) {
			collectData(aMaxHopCount - 1, aDeparture, aDestination, aDepartTime, dataset);

			findPossibleArrivalDates(dataset).forEach(datetime -> {
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

	private void collectData(int aMaxHopCount, Airport aDeparture, Airport aDestination, DateTime aDepartTime,
			Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (aMaxHopCount > 0) {
			Collection<DateTime> datetimes = findPossibleDates(aDepartTime);

			datetimes.forEach(time -> {
				addFlightsToMap(aDeparture, time, aDataSet);

				aDataSet.get(aDeparture).get(time.toServerDateString()).forEach(flight -> {
					if (!flight.getArrivalAirport().equals(aDestination)) {
						DateTime minlayovertime = flight.getArrivalTime().plusSeconds(minlayover);
						DateTime maxlayovertime = flight.getArrivalTime().plusSeconds(maxlayover);
						collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, minlayovertime,
								aDataSet);
						collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, maxlayovertime,
								aDataSet);
					}
				});
			});

		}
	}

	private static Collection<DateTime> findPossibleDates(DateTime aTime) {
		DateTime basetime = aTime.getMidnight();
		Collection<DateTime> dts = new HashSet<DateTime>();
		dts.add(basetime.getUTC().getMidnight());
		if (!aTime.isUTC()) {
			basetime = basetime.getNextDay();
			dts.add(basetime.getUTC().getMidnight());
		}

		return dts;
	}

	private static Collection<DateTime> findPossibleArrivalDates(Map<Airport, Map<String, List<Flight>>> aDataSet) {
		Collection<DateTime> arrivaldates = new HashSet<DateTime>();

		aDataSet.values().forEach(flightsbyDate -> {
			flightsbyDate.values().forEach(flights -> {
				flights.forEach(flight -> {
					arrivaldates.add(flight.getArrivalTime().getUTC().getMidnight());
					arrivaldates.add(flight.getArrivalTime().getUTC().getMidnight().getNextDay());
					arrivaldates.add(flight.getArrivalTime().getUTC().getMidnight().getNextDay().getNextDay());
				});
			});
		});

		return arrivaldates;
	}
}
