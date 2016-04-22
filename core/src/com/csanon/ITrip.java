package com.csanon;

import java.time.Duration;
import java.util.List;

public interface ITrip {
	public Price getTotalPrice();
	public Duration getTotalLayoverTime();
	public Duration getTotalTravelTime();
	public boolean hasSeatsAvailable(int anAmount);
	public List<Flight> getLegs();
	public SeatClass getSeatType();
	public List<Airport> getAirports();
	public ITrip addLeg(Flight leg, boolean addbefore);
}
