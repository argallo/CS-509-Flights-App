package com.csanon;

/**
 * Enum representing the different seat classes available
 *
 */
public enum SeatClass {
	FIRSTCLASS("FirstClass"), ECONOMY("Coach");
	
	private final String name;
	SeatClass(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
