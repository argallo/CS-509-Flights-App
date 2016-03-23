package com.csanon;

import java.util.LinkedList;
import java.util.List;

public class Trip {
	private final List<Flight> legs = new LinkedList<Flight>();
	
	public Trip(Flight aFlight) {
		legs.add(aFlight);
	}
	
	public Trip(Flight aFlight1, Flight aFlight2 ) {
		legs.add(aFlight1);
		legs.add(aFlight2);
	}
	
	public Trip(Flight aFlight1, Flight aFlight2, Flight aFlight3) {
		legs.add(aFlight1);
		legs.add(aFlight2);
		legs.add(aFlight3);
	}
	
	public String getLayoverTime() {
		if (legs.size() == 1) {
			return "00:00";
		} else if (legs.size() == 2) {
			long difference = legs.get(1).getDepartureTime().toEpochSecond() -
								legs.get(0).getArrivalTime().toEpochSecond();
			int hours
			return 
		} else {
			
		}
	}
	
	public String getTotalTime() {
		if (legs.size() == 1) {
			return legs.get(0).getDuration();
		} else if (legs.size() == 2) {
			
		} else {
			
		}
		
	}
	
	public List<Flight> getLegs() {
		return legs;
	}
}
