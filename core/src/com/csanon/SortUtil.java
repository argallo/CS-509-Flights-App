package com.csanon;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortUtil {
	public static List<ITrip> sortByPrice(List<ITrip> trips) {
		return sortByPrice(trips, false);
	}
	
	public static List<ITrip> sortByPrice(List<ITrip> trips, boolean reverse) {
		Comparator<ITrip> comp = (ITrip a, ITrip b) -> {
			if (a.getTotalPrice().compareTo(b.getTotalPrice())!=0) {
				return a.getTotalPrice().compareTo(b.getTotalPrice());
			} else if (!reverse && a.getTotalTravelTime().compareTo(b.getTotalTravelTime())!=0) {
				return a.getTotalTravelTime().compareTo(b.getTotalTravelTime());
			}  else if (reverse && a.getTotalTravelTime().compareTo(b.getTotalTravelTime())!=0) {
				return -a.getTotalTravelTime().compareTo(b.getTotalTravelTime());
			} else if (a.getSeatType().compareTo(b.getSeatType())!=0) {
				return a.getSeatType().compareTo(b.getSeatType());
			} else {
				return 0;
			}
		};
		
		return sorter(trips, reverse, comp);
	}
	
	public static List<ITrip> sortByTravelTime(List<ITrip> trips) {
		return sortByTravelTime(trips, false);
	}
	
	public static List<ITrip> sortByTravelTime(List<ITrip> trips, boolean reverse) {
		Comparator<ITrip> comp = (ITrip a, ITrip b) -> {
			if (a.getTotalTravelTime().compareTo(b.getTotalTravelTime())!=0) {
				return a.getTotalTravelTime().compareTo(b.getTotalTravelTime());
			} else if (!reverse && a.getTotalPrice().compareTo(b.getTotalPrice())!=0) {
				return a.getTotalPrice().compareTo(b.getTotalPrice());
			} else if (reverse && a.getTotalPrice().compareTo(b.getTotalPrice())!=0) {
				return -a.getTotalPrice().compareTo(b.getTotalPrice());
			} else if (a.getSeatType().compareTo(b.getSeatType())!=0) {
				return a.getSeatType().compareTo(b.getSeatType());
			} else {
				return 0;
			}
		};
		
		return sorter(trips, reverse, comp);
	}
	
	private static List<ITrip> sorter(List<ITrip> trips, boolean reverse, Comparator<ITrip> comp) {
		List<ITrip> newTrips = new LinkedList<ITrip>(trips);	
		newTrips.sort(comp);
		if (reverse) {
			Collections.reverse(newTrips);
		}
		return newTrips;
	}
}
