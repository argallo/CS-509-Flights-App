package com.csanon;

import java.time.OffsetDateTime;

public class Flight {

	private final Airplane Airplane;
	private final String Duration;
	private final String FlightNum;

	private final Airport DepartureAirport;
	private final OffsetDateTime DepatureTime;

	private final Airport ArrivalAirport;
	private final OffsetDateTime ArrivalTime;

	private final Price PriceFirstClass;
	private final int SeatsFirstClass;

	private final Price PriceEconomy;
	private final int SeatsEconomy;

	public Flight(Airplane aAirplane, String aDuration, String aFlightNum, Airport aDepatureAirport,
			OffsetDateTime aDepartureTime, Airport anArrivalAirport, OffsetDateTime anArrivalTime,
			Price aPriceFirstClass, int aSeatsFirstClassRemaining, Price aPriceEconomy, int aSeatsEconomyRemaining) {
		Airplane = aAirplane;
		Duration = aDuration;
		FlightNum = aFlightNum;

		DepartureAirport = aDepatureAirport;
		DepatureTime = aDepartureTime;

		ArrivalAirport = anArrivalAirport;
		ArrivalTime = anArrivalTime;

		PriceFirstClass = aPriceFirstClass;
		SeatsFirstClass = Airplane.getFirstClassSeatCount() - aSeatsFirstClassRemaining;

		PriceEconomy = aPriceEconomy;
		SeatsEconomy = Airplane.getEconomySeatCount() - aSeatsFirstClassRemaining;
	}
	
	@Override
	public boolean equals(Object aOther) {
		if (aOther == null) {
			return false;
		} else if (!(aOther instanceof Flight)) {
			return false;
		} else if (aOther == this) {
			return true;
		} else {
			Flight other = (Flight)aOther;
			return (Airplane.equals(other.Airplane)) && 
					(Duration.equals(other.Duration)) && 
					(FlightNum.equals(other.FlightNum)) && 
					(DepartureAirport.equals(other.DepartureAirport)) && 
					(DepatureTime.equals(other.DepatureTime)) && 
					(ArrivalAirport.equals(other.ArrivalAirport)) && 
					(ArrivalTime.equals(other.ArrivalTime)) && 
					(PriceFirstClass.equals(other.PriceFirstClass)) && 
					(SeatsFirstClass == other.SeatsFirstClass) && 
					(PriceEconomy.equals(other.PriceEconomy)) &&
					(SeatsEconomy == other.SeatsEconomy);
		}
	}
	
	public boolean equals(Flight aOther) {
		if (aOther == null) {
			return false;
		} else if (aOther == this) {
			return true;
		} else {
			return (Airplane.equals(aOther.Airplane)) && 
					(Duration.equals(aOther.Duration)) && 
					(FlightNum.equals(aOther.FlightNum)) && 
					(DepartureAirport.equals(aOther.DepartureAirport)) && 
					(DepatureTime.equals(aOther.DepatureTime)) && 
					(ArrivalAirport.equals(aOther.ArrivalAirport)) && 
					(ArrivalTime.equals(aOther.ArrivalTime)) && 
					(PriceFirstClass.equals(aOther.PriceFirstClass)) && 
					(SeatsFirstClass == aOther.SeatsFirstClass) && 
					(PriceEconomy.equals(aOther.PriceEconomy)) &&
					(SeatsEconomy == aOther.SeatsEconomy);
		}
	}
	
	public int getFirstClassSeats() {
		return SeatsFirstClass;
	}

	public int getEconomySeats() {
		return SeatsEconomy;
	}

	public Price getFirstClassPrice() {
		return PriceFirstClass;
	}

	public Price getEconomyPrice() {
		return PriceEconomy;
	}

	public OffsetDateTime getDepartureTime() {
		return DepatureTime;
	}

	public OffsetDateTime getArrivalTime() {
		return ArrivalTime;
	}

	public Airport getDepartureAirport() {
		return DepartureAirport;
	}

	public Airport getArrivalAirport() {
		return ArrivalAirport;
	}

	public String getDuration() {
		return Duration;
	}

	public String getFlightNum() {
		return FlightNum;
	}

}
