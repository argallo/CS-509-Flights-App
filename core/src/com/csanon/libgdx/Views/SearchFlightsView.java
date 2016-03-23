package com.csanon.libgdx.Views;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.libgdx.Components.DropDown;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;

public class SearchFlightsView extends BaseView {

	private Button searchButton;

	private TextLabel airportLabel, dateLabel;
	private DropDown airportDropdown;

	@Override
	public void init() {
		airportDropdown = new DropDown();
		List<Airport> airports =  Airports.getAirports();
		List<String> airportNames = new LinkedList<String>();
		for(Airport airport:airports){
			airportNames.add(airport.getName()+" ("+airport.getCode()+")");
		}
		airportDropdown.setItems(airportNames);
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPositions() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addActors() {
		addActor(airportDropdown);

	}

	@Override
	public void handle(int outcome) {
		// TODO Auto-generated method stub

	}

}
