package com.csanon;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EconomyTrip extends GeneralTrip {

	public EconomyTrip(List<Flight> flights) throws Exception {
		super(flights);
		for (Flight flight : flights) {
			if (!flight.checkEconomyAvailable(1)) {
				throw new Exception ("There are no economy seats in one leg");
			}
		}
		seatType = SeatClass.ECONOMY;
	}

	public EconomyTrip(Flight ... flights) throws Exception {
		this(Arrays.asList(flights));
	}

	public EconomyTrip(ITrip aTrip) throws Exception {
		this(aTrip.getLegs());
	}
	
	@Override
	public Price getTotalPrice() {
		Price totalPrice = new Price(0);
		for (Flight flight : legs) {
			totalPrice = totalPrice.add(flight.getEconomyPrice());
		}
		return totalPrice;
	}
	
	@Override
	public boolean hasSeatsAvailable(int anAmount) {
		boolean returnValue = true;
		for (Flight flight : legs) {
			returnValue = returnValue && flight.checkEconomyAvailable(anAmount);
		}
		return returnValue;
	}
	
	@Override
	public ITrip addLeg(Flight leg, boolean addbefore) {
		List<Flight> nlegs = new LinkedList<Flight>();
		if (addbefore) {
			nlegs.add(leg);
			nlegs.addAll(legs);
		} else {
			nlegs.addAll(legs);
			nlegs.add(leg);
		}
		try {
			return new EconomyTrip(nlegs);
		} catch (Exception e) {
			return this;
		}
	}
}
