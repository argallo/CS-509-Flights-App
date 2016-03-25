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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + EconomySeats;
		result = prime * result + FirstClassSeats;
		result = prime * result + ((Manufacturer == null) ? 0 : Manufacturer.hashCode());
		result = prime * result + ((Model == null) ? 0 : Model.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Airplane other = (Airplane) obj;
		if (EconomySeats != other.EconomySeats) {
			return false;
		}
		if (FirstClassSeats != other.FirstClassSeats) {
			return false;
		}
		if (Manufacturer == null) {
			if (other.Manufacturer != null) {
				return false;
			}
		} else if (!Manufacturer.equals(other.Manufacturer)) {
			return false;
		}
		if (Model == null) {
			if (other.Model != null) {
				return false;
			}
		} else if (!Model.equals(other.Model)) {
			return false;
		}
		return true;
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
