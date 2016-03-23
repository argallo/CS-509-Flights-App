package com.csanon;

public class Airplane {
	private final String Manufacturer;
	private final String Model;
	private final int FirstClassSeats;
	private final int EconomySeats;
	
	public Airplane(String aManufacturer, String aModel, int aFirstClassSeats, int aEconomySeats) {
		Manufacturer = aManufacturer;
		Model = aModel;
		FirstClassSeats = aFirstClassSeats;
		EconomySeats = aEconomySeats;
	}
	
	public String getManufacturer() {
		return Manufacturer;
	}
	
	public String getModel() {
		return Model;
	}
	
	public int getFirstClassSeatCount() {
		return FirstClassSeats;
	}
	
	public int getEconomySeatCount() {
		return EconomySeats;
	}
}
