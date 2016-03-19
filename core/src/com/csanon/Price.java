package com.csanon;

public class Price {
	private Float PriceVal;
	
	public Price(float aPriceVal) {
		PriceVal = new Float(aPriceVal);
	}
	
	public Price(String aPriceVal) {
		PriceVal = Float.valueOf(aPriceVal);
	}
	
	public int CompareTo(Price aOther) {
		return PriceVal.compareTo(aOther.PriceVal);
	}
	
	public String toString() {
		return String.format("$%.2f", PriceVal);
	}
}
