package com.csanon;

import java.util.LinkedList;
import java.util.List;

public class Trip {
	private final Flight flight;
	
	public Trip(Flight aFlight) {
		flight = aFlight;
	}
	
	public String getLayoverTime() {
		return "00:00";
	}
	
	public String getTotalTime() {
		return flight.getDuration();
	}
	
	public List<Flight> getLegs() {
		List<Flight> legs = new LinkedList<Flight>();
		legs.add(flight);
		return legs;
	}
	
	@Override
	public boolean equals(Object aOther) {
		if (aOther == null) {
			return false;
		} else if (!(aOther instanceof Trip)) {
			return false;
		} else if (aOther == this) {
			return true;
		} else {
			Trip other = (Trip)aOther;
			return flight.equals(other.flight);
		}
	}
	
}
