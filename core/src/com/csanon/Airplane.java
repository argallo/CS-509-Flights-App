package com.csanon;

public class Airplane {
	private String Manufacturer;
	private String Model;
	private int FirstClassSeats;
	private int EconomySeats;
	
	public Airplane(String aManufacturer, String aModel, int aFirstClassSeats, int aEconomySeats) {
		Manufacturer = aManufacturer;
		Model = aModel;
		FirstClassSeats = aFirstClassSeats;
		EconomySeats = aEconomySeats;
	}
}
