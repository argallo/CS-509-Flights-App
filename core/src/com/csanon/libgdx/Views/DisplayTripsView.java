package com.csanon.libgdx.Views;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.Trip;
import com.csanon.TripBuilder;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.DropDown;
import com.csanon.libgdx.Components.TextBox;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Components.TripsPanel;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class DisplayTripsView extends BaseView {

	private Button searchButton;

	//private TitleLabel titleLabel;
	private TextLabel departureAirportLabel, arrivalAirportLabel, dateLabel;
	private DropDown departureAirportDropdown, arrivalAirportDropdown;
	private TintedImage background;
	private TextBox textBox;
	private TripsPanel tripsPanel;

	@Override
	public void init() {
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
		//titleLabel = new TitleLabel("Flight Finder");
		departureAirportLabel = new TextLabel("Depart Airport:", Assets.getInstance().getSmallFont());
		arrivalAirportLabel = new TextLabel("Arrival Airport:", Assets.getInstance().getSmallFont());
		dateLabel = new TextLabel("Departure Date:", Assets.getInstance().getSmallFont());
		departureAirportDropdown = new DropDown();
		arrivalAirportDropdown = new DropDown();
		List<Airport> airports =  Airports.getAirports();
		List<String> airportNames = new LinkedList<String>();
		for(Airport airport:airports){
			airportNames.add(airport.getName()+" ("+airport.getCode()+")");
		}
		departureAirportDropdown.setItems(airportNames);
		departureAirportDropdown.pack();
		arrivalAirportDropdown.setItems(airportNames);
		arrivalAirportDropdown.pack();
		textBox = new TextBox(10,"05/10/2016", TextBox.DATE);
		
		TintedImage icon = new TintedImage(Pic.Search_Icon);
		icon.setSize(90, 90);
		icon.setPosition(980, 535);
		searchButton = new Button(Pic.Pixel, Tint.GRAY, "Search", Assets.getInstance().getMidFont(), icon);
		searchButton.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				handle(0);
			}
		});
		tripsPanel = new TripsPanel();
        addAction(Actions.sequence(Actions.delay(1f), new Action() {
            @Override
            public boolean act(float delta) {
                List<Trip> trips = (new TripBuilder()).getTrips(SearchFlightsHomeView.departureAirport, SearchFlightsHomeView.arriavalAirport, SearchFlightsHomeView.dateTime);
                tripsPanel.updateTrips(trips);
                return true;
            }
        }));

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
		//titleLabel.setPosition(Constants.VIRTUAL_WIDTH/2 - titleLabel.getWidth()/2, 600);
		departureAirportDropdown.setPosition(235, 650);
		arrivalAirportDropdown.setPosition(235, 570);
		departureAirportLabel.setPosition(10, 650);
		arrivalAirportLabel.setPosition(10, 570);
		dateLabel.setPosition(805, 650);
		textBox.setPosition(1060, 650);
		searchButton.setPosition(900, 530);
		tripsPanel.setPosition(Constants.VIRTUAL_WIDTH / 2 - 400, 10);
	}

	@Override
	public void addActors() {
		addActor(background);
		//addActor(titleLabel);
		addActor(departureAirportLabel);
		addActor(arrivalAirportLabel);
		addActor(departureAirportDropdown);
		addActor(arrivalAirportDropdown);
		addActor(dateLabel);
		addActor(textBox);
		addActor(searchButton);
		addActor(tripsPanel);

	}

	@Override
	public void handle(int outcome) {
		switch (outcome){
			case 0:
				int year = Integer.parseInt(textBox.getText().substring(6));
				int month = Integer.parseInt(textBox.getText().substring(0,2));
				int day = Integer.parseInt(textBox.getText().substring(3, 5));
				Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
				Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());
				System.out.print(year+" "+ month+" "+day);
				DateTime depart = DateTime.of(year, month, day, 0);
				List<Trip> trips = (new TripBuilder()).getTrips(departAirport, arrivalAirport, depart);
				tripsPanel.updateTrips(trips);
				break;
		}

	}

	public Airport getAirport(String airportString){
		airportString = airportString.substring(airportString.length()-4,airportString.length()-1);
		System.out.print(airportString);
		return Airports.getAirport(airportString);
	}


    @Override
    public void act(float delta) {
        super.act(delta);

    }
}
