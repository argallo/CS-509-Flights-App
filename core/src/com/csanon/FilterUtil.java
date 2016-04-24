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
	
	private static List<ITrip> filterer(List<ITrip> trips, Predicate<ITrip> filter) {
		return trips.stream().filter(filter).collect(Collectors.toList());
	}
}
