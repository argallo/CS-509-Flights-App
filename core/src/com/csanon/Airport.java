package com.csanon;

import com.csanon.time.TimeZoneLookup;

public class Airport {

	/*
	 * Constant number for the range of latitude and longitude.
	 */
	public static final double MAX_LA=90.00;
	public static final double MIN_LA=-90.00;
	public static final double MAX_LO=180.00;
	public static final double MIN_LO=-180.00;

	/*
	 * Attributes for the airport.
	 */

	private final String aname;
	private final String acode;
	private final double alongitude;
	private final double alatitude;
	private final int offset;
	
	
	/*
	 * Construct the default constructor without params.
	 */
	public Airport(){
		this("", "", MAX_LA, MAX_LO);
	}

	/*
	 * Initialize the constructor.
	 * 
	 * All attributes were put with the demand format.
	 * 
	 * @param name The airport name that human can read.
	 * 
	 * @param code The 3 letter airport code that identify the airport.
	 * 
	 * @param latitude The north/south coordinate of the airport.
	 * 
	 * @param longitude The east/west coordinate of the airport.
	 */
	public Airport(String name, String code, double la, double lo){
		this.aname=name;
		this.acode=code;
		alongitude=la;
		alatitude=lo;
		offset = TimeZoneLookup.getInstance().getOffsetFromLatLong(alongitude, alatitude);
	}
	
	/*
	 * Initialize the constructor. All input are in string format.
	 * 
	 * @param name The airport name that human can read.
	 * 
	 * @param code The 3 letter airport code that identify the airport.
	 * 
	 * @param latitude The north/south coordinate of the airport.
	 * 
	 * @param longitude The east/west coordinate of the airport.
	 */
	public Airport(String name, String code, String la, String lo){
		this(name, code, Double.parseDouble(la), Double.parseDouble(lo));
	}

	public String getName() {
		return aname;
	}

	public String getCode() {
		return acode;
	}

	public Double getLatitude() {
		return alatitude;
	}

	public Double getLongitude() {
		return alongitude;
	}
	
	public int getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acode == null) ? 0 : acode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(alatitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(alongitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((aname == null) ? 0 : aname.hashCode());
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
		Airport other = (Airport) obj;
		if (acode == null) {
			if (other.acode != null) {
				return false;
			}
		} else if (!acode.equals(other.acode)) {
			return false;
		}
		if (Double.doubleToLongBits(alatitude) != Double.doubleToLongBits(other.alatitude)) {
			return false;
		}
		if (Double.doubleToLongBits(alongitude) != Double.doubleToLongBits(other.alongitude)) {
			return false;
		}
		if (aname == null) {
			if (other.aname != null) {
				return false;
			}
		} else if (!aname.equals(other.aname)) {
			return false;
		}
		return true;
	}

	public boolean isValid() {

		// If the name is null, airport is not valid.
		if ((aname == null) || (aname == ""))
			return false;

		// If we don't have a 3 character code, object isn't valid
		if ((acode == null) || (acode.length() != 3))
			return false;

		// Verify latitude and longitude are within range
		if ((alatitude > MAX_LA) || (alatitude < MIN_LA) ||
				(alongitude > MAX_LO) || (alongitude < MIN_LO)) {
			return false;
		}

		return true;
	}

}
