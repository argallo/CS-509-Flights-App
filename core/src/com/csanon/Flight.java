package com.csanon;

import java.time.OffsetDateTime;

public class Flight {
	private Airport DepartureAirport;
	private OffsetDateTime DepatureTime;
	private Airport ArrivalAirport;
	private OffsetDateTime ArrivalTime;
	private Price PriceFirstClass;
	private int SeatsFirstClass;
	private Price PriceEconomy;
	private int SeatsEconomy;
	
	public Flight(Airport aDepatureAirport, OffsetDateTime aDepartureTime, 
						Airport anArrivalAirport, OffsetDateTime anArrivalTime,
						Price aPriceFirstClass, int aSeatsFirstClass,
						Price aPriceEconomy, int aSeatsEconomy) {
		DepartureAirport = aDepatureAirport;
		DepatureTime 	= aDepartureTime;
		ArrivalAirport 	= anArrivalAirport;
		ArrivalTime 	= anArrivalTime;
		PriceFirstClass = aPriceFirstClass;
		SeatsFirstClass = aSeatsFirstClass;
		PriceEconomy 	= aPriceEconomy;
		SeatsEconomy 	= aSeatsEconomy;
	}
	
	public int getFirstClassSeats() {
		return SeatsFirstClass;
	}
	
	public int getCoachSeats() {
		return SeatsEconomy;
	}
	
	public Price getFirstClassPrice() {
		return PriceFirstClass;
	}
	
	public Price getEconomyPrice() {
		return PriceEconomy;
	}
	
	public OffsetDateTime getDepartureTime(){
		return DepatureTime;
	}
	
	public OffsetDateTime getArrivalTime(){
		return ArrivalTime;
	}
	
	public Airport getDepartureAirport(){
		return DepartureAirport;
	}
	
	public Airport getArrivalAirport(){
		return ArrivalAirport;
	}
	
}
