package com.csanon;

;

public class Airport {
	/*
	 * Constant number for the range of latitude and longitude.
	 */
	static final double MAX_LA=90.00;
	static final double MIN_LA=-90.00;
	static final double MAX_LO=180.00;
	static final double MIN_LO=-180.00;
	
	/*
	 * Attributes for the airport.
	 */
	
	private final String aname;
	private final String acode;
	private final double alongitude;
	private final double alatitude;
	
	/*
	 * Construct the default constructor without params.
	 */
	public Airport(){
		aname="";
		acode="";
		alongitude=MAX_LO;
		alatitude=MAX_LA;	
	}
	
	/*
	 * Initialize the constructor. 
	 * 
	 * All attributes were put with the demand format.
	 * 
	 * @param name The airport name that human can read.
	 * @param code The 3 letter airport code that identify the airport.
	 * @param latitude The north/south coordinate of the airport.
	 * @param longitude The east/west coordinate of the airport.
	 */
	public Airport(String name, String code, double la, double lo){
		this.aname=name;
		this.acode=code;
		alongitude=la;
		alatitude=lo;
	}
	/*
	 * Initialize the constructor. All input are in string format.
	 * 
	 * @param name The airport name that human can read.
	 * @param code The 3 letter airport code that identify the airport.
	 * @param latitude The north/south coordinate of the airport.
	 * @param longitude The east/west coordinate of the airport.
	 */
	
	public Airport(String name, String code, String la, String lo){
		aname=name;
		acode=code;
		alatitude=Double.parseDouble(la);
		alongitude=Double.parseDouble(lo);
	}
	
	
	public String getName(){
		return aname;
	}
	
	public String getCode(){
		return acode;
	}
	
	
	
	public Double getLatitude(){
		return alatitude;
	}
	
	
	
	public Double getLongitude(){
		return alongitude;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		if(obj==this) return true;
		if(!(obj instanceof Airport)) return false;
		Airport airport=(Airport)obj;
		if(airport.aname.equals(this.aname)
				&& airport.acode.equals(this.acode)
				&& airport.alatitude==this.alatitude
				&& airport.alongitude==this.alongitude) return true;
		return false;
			
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
