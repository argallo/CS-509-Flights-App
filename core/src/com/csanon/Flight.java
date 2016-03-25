package com.csanon;

import java.time.OffsetDateTime;

public class Flight {

	private final Airplane airplane;
	private final String duration;
	private final String flightNum;

	private final Airport departureAirport;
	private final OffsetDateTime depatureTime;

	private final Airport arrivalAirport;
	private final OffsetDateTime arrivalTime;

	private final Price priceFirstClass;
	private final int seatsFirstClass;

	private final Price priceEconomy;
	private final int seatsEconomy;

	public Flight(Airplane aAirplane, String aDuration, String aFlightNum, Airport aDepatureAirport,
			OffsetDateTime aDepartureTime, Airport anArrivalAirport, OffsetDateTime anArrivalTime,
			Price aPriceFirstClass, int aSeatsFirstClassRemaining, Price aPriceEconomy, int aSeatsEconomyRemaining) {
		airplane = aAirplane;
		duration = aDuration;
		flightNum = aFlightNum;

		departureAirport = aDepatureAirport;
		depatureTime = aDepartureTime;

		arrivalAirport = anArrivalAirport;
		arrivalTime = anArrivalTime;

		priceFirstClass = aPriceFirstClass;
		seatsFirstClass = airplane.getFirstClassSeatCount() - aSeatsFirstClassRemaining;

		priceEconomy = aPriceEconomy;
		seatsEconomy = airplane.getEconomySeatCount() - aSeatsFirstClassRemaining;
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
		result = prime * result + ((depatureTime == null) ? 0 : depatureTime.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((flightNum == null) ? 0 : flightNum.hashCode());
		result = prime * result + ((priceEconomy == null) ? 0 : priceEconomy.hashCode());
		result = prime * result + ((priceFirstClass == null) ? 0 : priceFirstClass.hashCode());
		result = prime * result + seatsEconomy;
		result = prime * result + seatsFirstClass;
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
		if (depatureTime == null) {
			if (other.depatureTime != null) {
				return false;
			}
		} else if (!depatureTime.equals(other.depatureTime)) {
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
		if (seatsEconomy != other.seatsEconomy) {
			return false;
		}
		if (seatsFirstClass != other.seatsFirstClass) {
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
					&& (depatureTime.equals(aOther.depatureTime)) && (arrivalAirport.equals(aOther.arrivalAirport))
					&& (arrivalTime.equals(aOther.arrivalTime)) && (priceFirstClass.equals(aOther.priceFirstClass))
					&& (seatsFirstClass == aOther.seatsFirstClass) && (priceEconomy.equals(aOther.priceEconomy))
					&& (seatsEconomy == aOther.seatsEconomy);
		}
	}

	public int getFirstClassSeats() {
		return seatsFirstClass;
	}

	public int getEconomySeats() {
		return seatsEconomy;
	}

	public Price getFirstClassPrice() {
		return priceFirstClass;
	}

	public Price getEconomyPrice() {
		return priceEconomy;
	}

	public OffsetDateTime getDepartureTime() {
		return depatureTime;
	}

	public OffsetDateTime getArrivalTime() {
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

}
