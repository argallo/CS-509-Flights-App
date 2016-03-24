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
	
	public Trip(List<Flight> aLegs) {
		legs.addAll(aLegs);
	}
	
	public int getLayoverTime() {
		int minutes = 0;
		if (legs.size() == 1) {
		} else if (legs.size() == 2) {
			int difference = (int)(legs.get(1).getDepartureTime().toEpochSecond() -
								legs.get(0).getArrivalTime().toEpochSecond());
			minutes = difference / 60;
		} else {
			int difference = (int)(legs.get(1).getDepartureTime().toEpochSecond() -
					legs.get(0).getArrivalTime().toEpochSecond());
			difference += (int)(legs.get(2).getDepartureTime().toEpochSecond() -
					legs.get(1).getArrivalTime().toEpochSecond());
			minutes = difference / 60;
		}
		return minutes;
	}
	
	public int getTotalTime() {
		int time = (int)(legs.get(legs.size()-1).getDepartureTime().toEpochSecond() -
				legs.get(1).getArrivalTime().toEpochSecond());
		return time;
		
	}
	
	public List<Flight> getLegs() {
		return legs;
	}
	
	public Trip addLeg(Flight aFlight, boolean isBefore) {
		List<Flight> nlegs = new LinkedList<Flight>();
		if (isBefore) {
			nlegs.add(aFlight);
			nlegs.addAll(legs);
		} else {
			nlegs.addAll(legs);
			nlegs.add(aFlight);
		}
		return new Trip(nlegs);
	}
	
	public List<Airport> getAirports() {
		List<Airport> airports = new LinkedList<Airport>();
		airports.add(legs.get(0).getDepartureAirport());
		legs.forEach(flight -> {
			airports.add(flight.getArrivalAirport());
		});
		return airports;
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
			return legs.equals(other.legs);
		}
	}
	
}
