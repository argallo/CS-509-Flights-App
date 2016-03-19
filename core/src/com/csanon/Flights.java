package com.csanon;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

public class Flights {
	private Collection<Flight> FlightList;
	
	public Flights(Collection<Flight> aFlightList) {
		FlightList = aFlightList;
	}
	
	public Collection<OffsetDateTime> getDepartureDates() {
		Collection<OffsetDateTime> departuredates = new HashSet<OffsetDateTime>();
		Iterator<Flight> iterator = FlightList.iterator();
		
		while (iterator.hasNext()) {
			departuredates.add(iterator.next().getDepartureTime());
		}
		
		return departuredates;
	}
	
	public Collection<Airport> getDepartureAirports() {
		Collection<Airport> departureairports = new HashSet<Airport>();
		Iterator<Flight> iterator = FlightList.iterator();
		
		while (iterator.hasNext()) {
			departureairports.add(iterator.next().getDepartureAirport());
		}
		
		return departureairports;
	}
	
	public Collection<OffsetDateTime> getArrivalDates() {
		Collection<OffsetDateTime> arrivaldates = new HashSet<OffsetDateTime>();
		Iterator<Flight> iterator = FlightList.iterator();
		
		while (iterator.hasNext()) {
			arrivaldates.add(iterator.next().getArrivalTime());
		}
		
		return arrivaldates;
	}
	
	public Collection<Airport> getArrivalAirports() {
		Collection<Airport> arrivalairports = new HashSet<Airport>();
		Iterator<Flight> iterator = FlightList.iterator();
		
		while (iterator.hasNext()) {
			arrivalairports.add(iterator.next().getArrivalAirport());
		}
		
		return arrivalairports;
	}
	
	public Collection<Flight> getFlights() {
		return FlightList;
	}
}
