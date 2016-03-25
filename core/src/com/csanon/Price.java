package com.csanon;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Price {
	private final Float priceVal;

	public Price(float aPriceVal) {
		priceVal = new Float(aPriceVal);
	}

	public Price(String aPriceVal) {
		Float value;
		// strip off the dollar sign
		// TODO: make more extensible
		aPriceVal = aPriceVal.substring(1);
		try {
			value = NumberFormat.getNumberInstance(Locale.US).parse(aPriceVal).floatValue();
		} catch (ParseException e) {
			value = 0.0f;
		}
		priceVal = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((priceVal == null) ? 0 : priceVal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Price other = (Price) obj;
		if (priceVal == null) {
			if (other.priceVal != null) {
				return false;
			}
		} else if (!priceVal.equals(other.priceVal)) {
			return false;
		}
		return true;
	}

	public int compareTo(Price aOther) {
		return priceVal.compareTo(aOther.priceVal);
	}

	public String toString() {
		return String.format("$%.2f", priceVal);
	}
}
