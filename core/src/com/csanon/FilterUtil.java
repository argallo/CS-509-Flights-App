package com.csanon;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterUtil {

	public static List<ITrip> filterByEconomy(List<ITrip> trips) {
		return filterBySeat(trips, SeatClass.ECONOMY);
	}

	public static List<ITrip> filterByFirstClass(List<ITrip> trips) {
		return filterBySeat(trips, SeatClass.FIRSTCLASS);
	}

	public static List<ITrip> filterBySeat(List<ITrip> trips, SeatClass seatType) {
		Predicate<ITrip> filter = trip -> trip.getSeatType() == seatType;
		return filterer(trips, filter);
	}

	public static List<ITrip> filterByTrip(ITrip filtertrip, List<ITrip> returnTrips) {
		Predicate<ITrip> filter = trip -> filtertrip.getLegs().get(filtertrip.getLegs().size() - 1).getArrivalTime()
				.getDifference(trip.getLegs().get(0).getDepartureTime()).toMinutes() >= 60;
		returnTrips.forEach(trip -> {
			System.out.println(filtertrip.getLegs().get(filtertrip.getLegs().size() - 1).getArrivalTime()
					.getDifference(trip.getLegs().get(0).getDepartureTime()).toMinutes());
		});
		return filterer(returnTrips, filter);
	}

	private static List<ITrip> filterer(List<ITrip> trips, Predicate<ITrip> filter) {
		return trips.stream().filter(filter).collect(Collectors.toList());
	}
}
