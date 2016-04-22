package com.csanon;

import com.csanon.time.DateTime;

public class Flight {

	private final Airplane airplane;
	private final String duration;
	private final String flightNum;

	private final Airport departureAirport;
	private final DateTime departureTime;

	private final Airport arrivalAirport;
	private final DateTime arrivalTime;

	private final Price priceFirstClass;
	private final int seatsBookedFirstClass;

	private final Price priceEconomy;
	private final int seatsBookedEconomy;

	public Flight(Airplane aAirplane, String aDuration, String aFlightNum, Airport aDepatureAirport,
			DateTime aDepartureTime, Airport anArrivalAirport, DateTime anArrivalTime, Price aPriceFirstClass,
			int seatsBookedFirstClass, Price aPriceEconomy, int seatsBookedEconomy) {
		airplane = aAirplane;
		duration = aDuration;
		flightNum = aFlightNum;

		departureAirport = aDepatureAirport;
		departureTime = aDepartureTime;

		arrivalAirport = anArrivalAirport;
		arrivalTime = anArrivalTime;

		priceFirstClass = aPriceFirstClass;
		this.seatsBookedFirstClass = seatsBookedFirstClass;

		priceEconomy = aPriceEconomy;
		this.seatsBookedEconomy = seatsBookedEconomy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airplane == null) ? 0 : airplane.hashCode());
		result = prime * result + ((arrivalAirport == null) ? 0 : arrivalAirport.hashCode());
		result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
		result = prime * result + ((departureAirport == null) ? 0 : departureAirport.hashCode());
		result = prime * result + ((departureTime == null) ? 0 : departureTime.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((flightNum == null) ? 0 : flightNum.hashCode());
		result = prime * result + ((priceEconomy == null) ? 0 : priceEconomy.hashCode());
		result = prime * result + ((priceFirstClass == null) ? 0 : priceFirstClass.hashCode());
		result = prime * result + seatsBookedEconomy;
		result = prime * result + seatsBookedFirstClass;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		Flight other = (Flight) obj;
		if (airplane == null) {
			if (other.airplane != null) {
				return false;
			}
		} else if (!airplane.equals(other.airplane)) {
			return false;
		}
		if (arrivalAirport == null) {
			if (other.arrivalAirport != null) {
				return false;
			}
		} else if (!arrivalAirport.equals(other.arrivalAirport)) {
			return false;
		}
		if (arrivalTime == null) {
			if (other.arrivalTime != null) {
				return false;
			}
		} else if (!arrivalTime.equals(other.arrivalTime)) {
			return false;
		}
		if (departureAirport == null) {
			if (other.departureAirport != null) {
				return false;
			}
		} else if (!departureAirport.equals(other.departureAirport)) {
			return false;
		}
		if (departureTime == null) {
			if (other.departureTime != null) {
				return false;
			}
		} else if (!departureTime.equals(other.departureTime)) {
			return false;
		}
		if (duration == null) {
			if (other.duration != null) {
				return false;
			}
		} else if (!duration.equals(other.duration)) {
			return false;
		}
		if (flightNum == null) {
			if (other.flightNum != null) {
				return false;
			}
		} else if (!flightNum.equals(other.flightNum)) {
			return false;
		}
		if (priceEconomy == null) {
			if (other.priceEconomy != null) {
				return false;
			}
		} else if (!priceEconomy.equals(other.priceEconomy)) {
			return false;
		}
		if (priceFirstClass == null) {
			if (other.priceFirstClass != null) {
				return false;
			}
		} else if (!priceFirstClass.equals(other.priceFirstClass)) {
			return false;
		}
		if (seatsBookedEconomy != other.seatsBookedEconomy) {
			return false;
		}
		if (seatsBookedFirstClass != other.seatsBookedFirstClass) {
			return false;
		}
		return true;
	}

	public boolean equals(Flight aOther) {
		if (aOther == null) {
			return false;
		} else if (aOther == this) {
			return true;
		} else {
			return (airplane.equals(aOther.airplane)) && (duration.equals(aOther.duration))
					&& (flightNum.equals(aOther.flightNum)) && (departureAirport.equals(aOther.departureAirport))
					&& (departureTime.equals(aOther.departureTime)) && (arrivalAirport.equals(aOther.arrivalAirport))
					&& (arrivalTime.equals(aOther.arrivalTime)) && (priceFirstClass.equals(aOther.priceFirstClass))
					&& (seatsBookedFirstClass == aOther.seatsBookedFirstClass)
					&& (priceEconomy.equals(aOther.priceEconomy)) && (seatsBookedEconomy == aOther.seatsBookedEconomy);
		}
	}

	public int getFirstClassSeats() {
		return seatsBookedFirstClass;
	}

	public int getEconomySeats() {
		return seatsBookedEconomy;
	}

	public Price getFirstClassPrice() {
		return priceFirstClass;
	}

	public Price getEconomyPrice() {
		return priceEconomy;
	}

	public DateTime getDepartureTime() {
		return departureTime;
	}

	public DateTime getArrivalTime() {
		return arrivalTime;
	}

	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public Airport getArrivalAirport() {
		return arrivalAirport;
	}

	public String getDuration() {
		return duration;
	}

	public String getFlightNum() {
		return flightNum;
	}

	public String toString() {
		String repr = "";
		repr += departureAirport.getCode() + " -> " + arrivalAirport.getCode() + " - model " + airplane.getModel() + "\n";
		repr += "\t" + duration + "min - " + flightNum + "\n";
		repr += "\t" + departureTime.toString() + " -> " + arrivalTime.toString() + "\n";
		repr += "\tFIRS : " + seatsBookedFirstClass + " at " + priceFirstClass + "\n";
		repr += "\tECON : " + seatsBookedEconomy + " at " + priceEconomy + "\n";
		return repr;
	}

	public boolean checkEconomyAvailable(int numSeats) {
		return airplane.getEconomySeatCount() >= seatsBookedEconomy + numSeats;
	}

	public boolean checkFirstClassAvailable(int numSeats) {
		return airplane.getFirstClassSeatCount() >= seatsBookedFirstClass + numSeats;
	}

}
