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
	
	private String aname;
	private String acode;
	private double alongitude;
	private double alatitude;
	
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
	
	public void name(String name){
		aname=name;
	}
	
	public String name(){
		return aname;
	}
	
	public void code(String code){
		acode=code;
	}
	
	public String code(){
		return acode;
	}
	
	public void latitude(Double la){
		alatitude=la;
	}
	
	public Double latitude(){
		return alatitude;
	}
	
	public void longitude(Double lo){
		alongitude=lo;
	}
	
	public Double longitude(){
		return alongitude;
	}
	
	@Override
	public boolean equals (Object obj) {
		// object equals to itself
		if (obj == this)
			return true;
		
		// null is not equal to the object
		if (obj == null)
			return false;
		
		// if this object is not an airport, not equal
		if (!(obj instanceof Airport)) 
			return false;
		
		// if all the attributes are the same, the teo airports are equal.
		Airport rhs = (Airport) obj;
		if ((rhs.aname.equals(aname)) &&
				(rhs.acode.equals(acode)) &&
				(rhs.alatitude == alatitude) &&
				(rhs.alongitude == alongitude)) {
			return true;
		}
		
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
