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
	
	@Override
	public boolean equals(Object aOther) {
		if (aOther == null) {
			return false;
		} else if (!(aOther instanceof Airplane)) {
			return false;
		} else if (aOther == this) {
			return true;
		} else {
			Airplane other = (Airplane)aOther;
			return (Manufacturer.equals(other.Manufacturer)) && 
					(Model.equals(other.Model)) && 
					(FirstClassSeats == other.FirstClassSeats) && 
					(EconomySeats == other.EconomySeats);
		}
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
