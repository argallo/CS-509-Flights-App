package com.csanon;

import java.time.OffsetDateTime;
import java.util.List;

public class TripBuilder {
	private static TripBuilder instance = null;

	private TripBuilder() {

	}

	public TripBuilder getInstance() {
		if (instance == null) {
			instance = new TripBuilder();
		}
		return instance;
	}

	public List<Trip> getTrips(Airport aDeparture, Airport aDestination, OffsetDateTime aDepartTime) {
		// TODO: make a list of valid trips from aDeparture airport to a
		// aDestination airport at the given time
		return null;
	}
}
