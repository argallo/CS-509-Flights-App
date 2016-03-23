package com.csanon;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Price {
	private Float priceVal;

	public Price(float aPriceVal) {
		priceVal = new Float(aPriceVal);
	}

	public Price(String aPriceVal) {
		// strip off the dollar sign
		// TODO: make more extensible
		aPriceVal = aPriceVal.substring(1);
		try {
			priceVal = NumberFormat.getNumberInstance(Locale.US).parse(aPriceVal).floatValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int CompareTo(Price aOther) {
		return priceVal.compareTo(aOther.priceVal);
	}

	public String toString() {
		return String.format("$%.2f", priceVal);
	}
}
