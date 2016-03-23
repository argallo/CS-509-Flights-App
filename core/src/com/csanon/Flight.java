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
