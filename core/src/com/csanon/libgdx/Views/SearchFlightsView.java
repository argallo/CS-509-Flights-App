package com.csanon.libgdx.Views;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.DropDown;
import com.csanon.libgdx.Components.TextBox;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Components.TitleLabel;
import com.csanon.libgdx.Components.TripsPanel;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

public class SearchFlightsView extends BaseView {

	private Button searchButton;

	private TitleLabel titleLabel;
	private TextLabel airportLabel, dateLabel;
	private DropDown airportDropdown;
	private TintedImage background;
	private TextBox textBox;
	private TripsPanel tripsPanel;

	@Override
	public void init() {
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
		titleLabel = new TitleLabel("Flight Finder");
		airportLabel = new TextLabel("Select Airport:", Assets.getInstance().getSmallFont());
		dateLabel = new TextLabel("Departure Date:", Assets.getInstance().getSmallFont());
		airportDropdown = new DropDown();
		List<Airport> airports =  Airports.getAirports();
		List<String> airportNames = new LinkedList<String>();
		for(Airport airport:airports){
			airportNames.add(airport.getName()+" ("+airport.getCode()+")");
		}
		airportDropdown.setItems(airportNames);
		airportDropdown.pack();
		textBox = new TextBox(10,"xx/xx/xxxx", TextBox.DATE);
		
		TintedImage icon = new TintedImage(Pic.Search_Icon);
		icon.setSize(90,90);
		icon.setPosition(600, 385);
		searchButton = new Button(Pic.Pixel, Tint.GRAY, "Search", Assets.getInstance().getMidFont(), icon);
		
		tripsPanel = new TripsPanel();
		//tripsPanel.setVisible(false);
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub
		background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		textBox.setSize(160, 35);
		searchButton.setSize(250, 100);
	}

	@Override
	public void setPositions() {
		titleLabel.setPosition(Constants.VIRTUAL_WIDTH/2 - titleLabel.getWidth()/2, 600);
		airportDropdown.setPosition(235, 500);
		airportLabel.setPosition(10, 500);
		dateLabel.setPosition(805, 500);
		textBox.setPosition(1060, 500);
		searchButton.setPosition(Constants.VIRTUAL_WIDTH/2-searchButton.getWidth()/2, 380);
		tripsPanel.setPosition(Constants.VIRTUAL_WIDTH/2-400, 10);
	}

	@Override
	public void addActors() {
		addActor(background);
		addActor(titleLabel);
		addActor(airportLabel);
		addActor(airportDropdown);
		addActor(dateLabel);
		addActor(textBox);
		addActor(searchButton);
		addActor(tripsPanel);

	}

	@Override
	public void handle(int outcome) {
		// TODO Auto-generated method stub

	}

}
