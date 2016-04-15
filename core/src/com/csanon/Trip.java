package com.csanon;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import com.csanon.time.DateTime;

public class Trip {
	private final List<Flight> legs = new LinkedList<Flight>();

	public Trip(Flight aFlight) {
		legs.add(aFlight);
	}

	public Trip(Flight aFlight1, Flight aFlight2) {
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
		DateTime arrivalTime = null;

		for (Flight flight : legs) {
			if (arrivalTime != null) {
				Duration layover = arrivalTime.getDifference(flight.getDepartureTime());
				minutes += layover.toMinutes();
			}
			arrivalTime = flight.getArrivalTime();
		}

		return minutes;
	}

	public int getTotalTime() {
		DateTime arrivalTime = legs.get(legs.size() - 1).getArrivalTime();
		DateTime departureTime = legs.get(0).getDepartureTime();
		Duration duration = departureTime.getDifference(arrivalTime);
		return (int) duration.toMinutes();

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legs == null) ? 0 : legs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Trip other = (Trip) obj;
		if (legs == null) {
			if (other.legs != null) {
				return false;
			}
		} else if (!legs.equals(other.legs)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		String repr = "=================================\n";

		for (Flight flight: legs) {
			repr += flight.toString() + "\n";
		}
		repr += "=================================\n\n";
		return repr;
	}
	
	public boolean hasEconomySeatsAvailable(int seats){
		boolean result = true;
		for(Flight flight: legs){
			if(!flight.checkEconomyAvailable(seats)){
				result = false;
				break;
			}
		}
		return result;
	}
	public boolean hasFirstClassSeatsAvailable(int seats){
		boolean result = true;
		for(Flight flight: legs){
			if(!flight.checkFirstClassAvailable(seats)){
				result = false;
				break;
			}
		}
		return result;
	}
}
