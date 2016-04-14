package com.csanon.libgdx.Views;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.SeatClass;
import com.csanon.Trip;
import com.csanon.TripBuilder;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.DropDown;
import com.csanon.libgdx.Components.TextBox;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Components.TripsPanel;
import com.csanon.libgdx.ScreenManaging.TransitionType;
import com.csanon.libgdx.ScreenManaging.ViewManager;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.libgdx.Utils.ViewID;
import com.csanon.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayTripsView extends BaseView {

	static Trip TripTO, TripBACK;
	static SeatClass seatClassSelection = SeatClass.ECONOMY; //TODO: MAKE DYNAMIC

	private Button searchButton;

	// private TitleLabel titleLabel;
	private TextLabel departureAirportLabel, arrivalAirportLabel, dateLabel;
	private DropDown departureAirportDropdown, arrivalAirportDropdown;
	private TintedImage background;
	private TextBox departureDateTextBox;
	private TripsPanel tripsPanel;
	private Button confirmBtn;
	private Trip selectedTripTo;
	private Trip selectedTripBack;

	@Override
	public void init() {
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
		// titleLabel = new TitleLabel("Flight Finder");
		departureAirportLabel = new TextLabel("Depart Airport:", Assets.getInstance().getSmallFont());
		arrivalAirportLabel = new TextLabel("Arrival Airport:", Assets.getInstance().getSmallFont());
		dateLabel = new TextLabel("Departure Date:", Assets.getInstance().getSmallFont());
		departureAirportDropdown = new DropDown();
		arrivalAirportDropdown = new DropDown();
		List<Airport> airports = Airports.getAirports();
		List<String> airportNames = new LinkedList<String>();
		for (Airport airport : airports) {
			airportNames.add(airport.getName() + " (" + airport.getCode() + ")");
		}
		departureAirportDropdown.setItems(airportNames);
		departureAirportDropdown.pack();
		departureAirportDropdown.setSelected(SearchFlightsHomeView.departureAirport.getName() + " ("
				+ SearchFlightsHomeView.departureAirport.getCode() + ")");
		arrivalAirportDropdown.setItems(airportNames);

		arrivalAirportDropdown.setSelected(SearchFlightsHomeView.arrivalAirport.getName() + " ("
				+ SearchFlightsHomeView.arrivalAirport.getCode() + ")");
		arrivalAirportDropdown.pack();
		departureDateTextBox = new TextBox(10, "05/10/2016", TextBox.DATE);
		departureDateTextBox.setText(SearchFlightsHomeView.dateTime.toDateString());

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
		tripsPanel = new TripsPanel(this);

		confirmBtn = new Button(Pic.Pixel, Tint.GRAY, "Book", Assets.getInstance().getSmallFont());
		confirmBtn.setVisible(false);
		confirmBtn.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				TripTO = selectedTripTo;
				TripBACK = selectedTripBack;
				ViewManager.getInstance().transitionViewTo(ViewID.BOOKING, TransitionType.SLIDE_R_TRANSITION);
			}
		});

		handle(0);
	}

	@Override
	public void setSizes() {
		background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		departureDateTextBox.setSize(160, 35);
		searchButton.setSize(250, 100);
		confirmBtn.setSize(150, 70);
	}

	@Override
	public void setPositions() {
		// titleLabel.setPosition(Constants.VIRTUAL_WIDTH/2 -
		// titleLabel.getWidth()/2, 600);
		departureAirportDropdown.setPosition(235, 650);
		arrivalAirportDropdown.setPosition(235, 570);
		departureAirportLabel.setPosition(10, 650);
		arrivalAirportLabel.setPosition(10, 570);
		dateLabel.setPosition(805, 650);
		departureDateTextBox.setPosition(1060, 650);
		searchButton.setPosition(900, 530);
		tripsPanel.setPosition(Constants.VIRTUAL_WIDTH / 2 - 400, 10);
		confirmBtn.setPosition(1100, 10);
	}

	@Override
	public void addActors() {
		addActor(background);
		// addActor(titleLabel);
		addActor(departureAirportLabel);
		addActor(arrivalAirportLabel);
		addActor(departureAirportDropdown);
		addActor(arrivalAirportDropdown);
		addActor(dateLabel);
		addActor(departureDateTextBox);
		addActor(searchButton);
		addActor(tripsPanel);
		addActor(confirmBtn);
	}

	@Override
	public void handle(int outcome) {
		switch (outcome) {
		case 0:
			tripsPanel.loading();
			String date = departureDateTextBox.getText();
			System.out.println(date);
			int year, month, day;
			String[] dateArray = date.split("/");
			year = Integer.parseInt(dateArray[2]);
			day = Integer.parseInt(dateArray[1]);
			month = Integer.parseInt(dateArray[0]);
			Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
			Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());
			DateTime depart = DateTime.of(year, month, day, 0);

			// Run the search delayed so we can see the loading message
			addAction(Actions.sequence(Actions.delay(2f), new Action() {
				@Override
				public boolean act(float delta) {
					List<Trip> trips = (new TripBuilder()).getTrips(departAirport, arrivalAirport, depart);
					trips = trips.stream().filter(trip -> {
						if (seatClassSelection == SeatClass.ECONOMY) {
							return trip.hasEconomySeatsAvailable(1);
						} else {
							return trip.hasFirstClassSeatsAvailable(1);
						}
					}).collect(Collectors.toList());
					tripsPanel.updateTrips(trips);
					return true;
				}
			}));

		}

	}

	public Airport getAirport(String airportString) {
		airportString = airportString.substring(airportString.length() - 4, airportString.length() - 1);
		System.out.print(airportString);
		return Airports.getAirport(airportString);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

	}

	public void setSelectedTripTo(Trip selectedTrip) {
		this.selectedTripTo = selectedTrip;
		confirmBtn.setVisible(true);
	}

	// TODO: check to see if round trips selected before setting book button to
	// true
	public void setSelectedTripBack(Trip selectedTrip) {
		this.selectedTripBack = selectedTrip;
		confirmBtn.setVisible(true);
	}
}
