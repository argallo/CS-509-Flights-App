package com.csanon;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

public class TripBuilder {
	private static final TripBuilder instance = new TripBuilder();
	private static final FlightServer server = ServerFactory.getServer();
	private static final DateTimeFormatter adateformat = DateTimeFormatter.ofPattern("yyy_MM_dd");
	private static final int maxhopcount = 1;
	private static final int minlayover = 30 * 60;
	private static final int maxlayover = 2 * 60 * 60;

	private TripBuilder() {
	}

	public static TripBuilder getInstance() {
		return instance;
	}

	public List<Trip> getTrips(Airport aDeparture, Airport aDestination, OffsetDateTime aDepartTime) {
		Map<Airport, Map<String, List<Flight>>> dataset = collectAData(maxhopcount, aDeparture, aDestination,
				aDepartTime);
		List<Trip> validtrips = new LinkedList<Trip>();

		for (int i = 1; i <= maxhopcount; i++) {
			validtrips.addAll(searchForTrips(i, aDeparture, aDestination, aDepartTime, dataset));
		}

		return validtrips;
	}

	private static List<Trip> searchForTrips(int aHopCount, Airport aDeparture, Airport aDestination,
			OffsetDateTime aDepartTime, Map<Airport, Map<String, List<Flight>>> aDataSet) {
		List<Trip> validtrips = new LinkedList<Trip>();
		if (aHopCount == 1) {
			ZoneOffset offset = aDepartTime.getOffset();
			OffsetDateTime lowerlimit = OffsetDateTime.of(aDepartTime.toLocalDate(), LocalTime.MIDNIGHT, offset);
			OffsetDateTime upperlimit = lowerlimit.plusDays(1);
			Collection<OffsetDateTime> datetimes = findPossibleDates(aDepartTime);

			datetimes.forEach(time -> {
				aDataSet.get(aDeparture).get(adateformat.format(time)).forEach(flight -> {
					if (flight.getArrivalAirport().equals(aDestination)) {
						if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
								&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
							validtrips.add(new Trip(flight));
						}
					}
				});
			});
		} else if (aHopCount > 1) {
			Map<Airport, Collection<Trip>> tripsbyAirport = new HashMap<Airport, Collection<Trip>>();

			ZoneOffset offset = aDepartTime.getOffset();
			OffsetDateTime lowerlimit = OffsetDateTime.of(aDepartTime.toLocalDate(), LocalTime.MIDNIGHT, offset);
			OffsetDateTime upperlimit = lowerlimit.plusDays(1);
			Collection<OffsetDateTime> datetimes = findPossibleDates(aDepartTime);

			datetimes.forEach(time -> {
				aDataSet.get(aDeparture).get(adateformat.format(time)).forEach(flight -> {
					if (!flight.getArrivalAirport().equals(aDestination)) {
						if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
								&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
							if (!tripsbyAirport.containsKey(flight.getArrivalAirport())) {
								tripsbyAirport.put(flight.getArrivalAirport(), new HashSet<Trip>());
							}
							OffsetDateTime minlayovertime = flight.getArrivalTime().plusSeconds(minlayover);
							OffsetDateTime maxlayovertime = flight.getArrivalTime().plusSeconds(maxlayover);
							tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
									flight.getArrivalAirport(), aDestination, minlayovertime, aDataSet));
							tripsbyAirport.get(flight.getArrivalAirport()).addAll(searchForTrips(aHopCount - 1,
									flight.getArrivalAirport(), aDestination, maxlayovertime, aDataSet));
						}
					}
				});
			});

			datetimes.forEach(time -> {
				aDataSet.get(aDeparture).get(adateformat.format(time)).forEach(flight -> {
					if (!flight.getArrivalAirport().equals(aDestination)) {
						if (lowerlimit.compareTo(flight.getDepartureTime()) <= 0
								&& upperlimit.compareTo(flight.getDepartureTime()) > 0) {
							tripsbyAirport.get(flight.getArrivalAirport()).forEach(trip -> {
								if (flight.getArrivalTime().plusSeconds(minlayover)
										.compareTo(trip.getLegs().get(0).getDepartureTime()) <= 0
										&& flight.getArrivalTime().plusSeconds(maxlayover)
												.compareTo(trip.getLegs().get(0).getDepartureTime()) >= 0) {
									if (!trip.getAirports().contains(flight.getDepartureAirport())) {
										validtrips.add(trip.addLeg(flight, true));
									}
								}
							});
						}
					}
				});
			});

		}
		return validtrips;
	}

	private static void addFlightsToMap(Airport aDeparture, OffsetDateTime aTime,
			Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (!aDataSet.containsKey(aDeparture)) {
			aDataSet.put(aDeparture, new HashMap<String, List<Flight>>());
		}

		if (!aDataSet.get(aDeparture).containsKey(adateformat.format(aTime))) {
			aDataSet.get(aDeparture).put(adateformat.format(aTime), server.getFlightsDeparting(aDeparture, aTime));
		}
	}

	private static Map<Airport, Map<String, List<Flight>>> collectAData(int aMaxHopCount, Airport aDeparture,
			Airport aDestination, OffsetDateTime aDepartTime) {
		Map<Airport, Map<String, List<Flight>>> dataset = new HashMap<Airport, Map<String, List<Flight>>>();
		if (aMaxHopCount > 0) {
			collectData(aMaxHopCount, aDeparture, aDestination, aDepartTime, dataset);
		}
		return dataset;
	}

	private static void collectData(int aMaxHopCount, Airport aDeparture, Airport aDestination,
			OffsetDateTime aDepartTime, Map<Airport, Map<String, List<Flight>>> aDataSet) {
		if (aMaxHopCount > 0) {
			Collection<OffsetDateTime> datetimes = findPossibleDates(aDepartTime);

			datetimes.forEach(time -> {
				addFlightsToMap(aDeparture, time, aDataSet);

				aDataSet.get(aDeparture).get(adateformat.format(time)).forEach(flight -> {
					OffsetDateTime minlayovertime = flight.getArrivalTime().plusSeconds(minlayover);
					OffsetDateTime maxlayovertime = flight.getArrivalTime().plusSeconds(maxlayover);
					collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, minlayovertime, aDataSet);
					collectData(aMaxHopCount - 1, flight.getArrivalAirport(), aDestination, maxlayovertime, aDataSet);
				});
			});

		}
	}

	private static Collection<OffsetDateTime> findPossibleDates(OffsetDateTime aTime) {
		ZoneOffset offset = aTime.getOffset();
		OffsetDateTime basetime = OffsetDateTime.of(aTime.toLocalDate(), LocalTime.MIDNIGHT, offset);
		Collection<OffsetDateTime> dts = new HashSet<OffsetDateTime>();
		dts.add(basetime.withOffsetSameInstant(ZoneOffset.ofHours(0)));
		if (offset.getTotalSeconds() != 0) {
			basetime = basetime.plusDays(1);
			dts.add(basetime.withOffsetSameInstant(ZoneOffset.ofHours(0)));
		}

		return dts;
	}

}
