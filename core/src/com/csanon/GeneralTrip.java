package com.csanon;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.csanon.time.DateTime;

public class GeneralTrip implements ITrip {

	protected final List<Flight> legs = new LinkedList<Flight>();
	protected SeatClass seatType;

	public GeneralTrip(List<Flight> flights) throws Exception {
		if (flights.size() <= 0) {
			throw new Exception("Cannot be given an empty trip");
		}
		legs.addAll(flights);
		seatType = null;
	}
	
	public GeneralTrip(Flight... flights) throws Exception {
		this(Arrays.asList(flights));
	}
	
	public GeneralTrip(ITrip aTrip) throws Exception {
		this(aTrip.getLegs());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legs == null) ? 0 : legs.hashCode());
		result = prime * result + ((seatType == null) ? 0 : seatType.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralTrip other = (GeneralTrip) obj;
		if (legs == null) {
			if (other.legs != null)
				return false;
		} else if (!legs.equals(other.legs))
			return false;
		if (seatType != other.seatType)
			return false;
		return true;
	}

	@Override
	public Price getTotalPrice() {
		return null;
	}

	@Override
	public Duration getTotalLayoverTime() {
		Duration layoverTime = Duration.ofSeconds(0);
		DateTime arrivalTime = null;

		for (Flight flight : legs) {
			if (arrivalTime != null) {
				layoverTime = Duration.ofSeconds(
						arrivalTime.getDifference(flight.getDepartureTime()).getSeconds() + layoverTime.getSeconds());
			}
			arrivalTime = flight.getArrivalTime();
		}
		return layoverTime;
	}

	@Override
	public Duration getTotalTravelTime() {
		DateTime arrivalTime = legs.get(legs.size() - 1).getArrivalTime();
		DateTime departureTime = legs.get(0).getDepartureTime();
		return departureTime.getDifference(arrivalTime);
	}

	@Override
	public boolean hasSeatsAvailable(int anAmount) {
		return false;
	}

	@Override
	public List<Flight> getLegs() {
		return legs;
	}

	@Override
	public SeatClass getSeatType() {
		return seatType;
	}

	@Override
	public List<Airport> getAirports() {
		List<Airport> airports = new LinkedList<Airport>();
		for (Flight flight : legs) {
			if (airports.size() == 0) {
				airports.add(flight.getDepartureAirport());
			}
			airports.add(flight.getArrivalAirport());
		}
		return airports;
	}

	@Override
	public ITrip addLeg(Flight leg, boolean addbefore) {
		List<Flight> nlegs = new LinkedList<Flight>();
		if (addbefore) {
			nlegs.add(leg);
			nlegs.addAll(legs);
		} else {
			nlegs.addAll(legs);
			nlegs.add(leg);
		}
		try {
			return new GeneralTrip(nlegs);
		} catch (Exception e) {
			return this;
		}
	}

}
