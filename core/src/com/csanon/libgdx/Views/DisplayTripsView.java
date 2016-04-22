package com.csanon.libgdx.Views;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

public class DisplayTripsView extends BaseView {

	static SeatClass seatClassSelection = SeatClass.ECONOMY; //TODO: MAKE DYNAMIC

	private Button searchButton;

	// private TitleLabel titleLabel;
	private TextLabel departureAirportLabel, arrivalAirportLabel, dateLabel, returnDateLabel, roundTripLabel;
	private DropDown departureAirportDropdown, arrivalAirportDropdown;
	private TintedImage background;
	private TextBox departureDateTextBox, returnTextBox;
	private TripsPanel tripsPanel, returnTripsPanel;
	private Button confirmBtn;
	private Trip selectedTripTo;
	private Trip selectedTripBack;
    private CheckBox checkBox;


	@Override
	public void init() {
        //Background init
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);

        //Labels
		departureAirportLabel = new TextLabel("Depart Airport:", Assets.getInstance().getSmallFont());
		arrivalAirportLabel = new TextLabel("Arrival Airport:", Assets.getInstance().getSmallFont());
		dateLabel = new TextLabel("Departure Date:", Assets.getInstance().getSmallFont());
        roundTripLabel = new TextLabel("Round Trip?", Assets.getInstance().getSmallFont());
        returnDateLabel = new TextLabel("Return Date:", Assets.getInstance().getSmallFont());

        //DropDowns
		departureAirportDropdown = new DropDown();
		arrivalAirportDropdown = new DropDown();

        //Init AirportNames
        List<String> airportNames = initAirports();

        //Setup dropdowns and select current airports
		departureAirportDropdown.setItems(airportNames);
		departureAirportDropdown.pack();
		departureAirportDropdown.setSelected(Constants.departureAirport.getName() + " (" + Constants.departureAirport.getCode() + ")");
		arrivalAirportDropdown.setItems(airportNames);
		arrivalAirportDropdown.setSelected(Constants.arrivalAirport.getName() + " (" + Constants.arrivalAirport.getCode() + ")");
		arrivalAirportDropdown.pack();

        //Init Date TextBoxes
		departureDateTextBox = new TextBox(10, "05/10/2016", TextBox.DATE);
		departureDateTextBox.setText(Constants.dateTime.toDateString());
        returnTextBox = new TextBox(10,"05/15/2016", TextBox.DATE);
        if(Constants.isRoundTrip){
            returnTextBox.setText(Constants.returnDateTime.toDateString());
        } else {
            returnTextBox.setVisible(false);
        }


        //Init Search Button
		TintedImage icon = new TintedImage(Pic.Search_Icon);
		icon.setSize(40, 40);
		icon.setPosition(920, 610);
		searchButton = new Button(Pic.Pixel, Tint.GRAY, "Search", Assets.getInstance().getSmallFont(), icon);
		searchButton.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				handle(0);
			}
		});


        //Create Trips Panel
		tripsPanel = new TripsPanel(this);
		returnTripsPanel = new TripsPanel(this);
        if(Constants.isRoundTrip){
            returnTripsPanel.setVisible(true);
        } else {
            returnTripsPanel.setVisible(false);
        }


        //Checkbox setup
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = Assets.getInstance().getXSmallFont();
        checkBoxStyle.checkboxOff = Assets.getInstance().getDrawable(Pic.Check_Empty);
        checkBoxStyle.checkboxOn = Assets.getInstance().getDrawable(Pic.Check_Mark);;
        checkBox = new CheckBox("", checkBoxStyle);
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if(checkBox.isChecked()){
                    returnDateLabel.setVisible(true);
                    returnTextBox.setVisible(true);
                } else {
                    returnDateLabel.setVisible(false);
                    returnTextBox.setVisible(false);
                }
            }
        });
        if(Constants.isRoundTrip){
            checkBox.setChecked(true);
            returnDateLabel.setVisible(true);
            returnTextBox.setVisible(true);
        } else {
            checkBox.setChecked(false);
            returnDateLabel.setVisible(false);
            returnTextBox.setVisible(false);
        }

        //Booking Button
		confirmBtn = new Button(Pic.Pixel, Tint.GRAY, "Book", Assets.getInstance().getSmallFont());
		confirmBtn.setVisible(false);
		confirmBtn.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
                Constants.TripTO = selectedTripTo;
                Constants.TripBACK = selectedTripBack;
				ViewManager.getInstance().transitionViewTo(ViewID.BOOKING, TransitionType.SLIDE_R_TRANSITION);
			}
		});

		handle(0);
	}

	@Override
	public void setSizes() {
		background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		departureDateTextBox.setSize(160, 35);
        returnTextBox.setSize(160, 35);
		searchButton.setSize(170, 50);
		confirmBtn.setSize(150, 50);
        checkBox.setSize(40, 40);
	}

	@Override
	public void setPositions() {
		departureAirportDropdown.setPosition(235, 670);
		arrivalAirportDropdown.setPosition(235, 610);
		departureAirportLabel.setPosition(10, 670);
		arrivalAirportLabel.setPosition(10, 610);
		dateLabel.setPosition(805, 670);
		departureDateTextBox.setPosition(1060, 670);
		searchButton.setPosition(860, 605);
		tripsPanel.setPosition(10, 10);
        returnTripsPanel.setPosition(Constants.VIRTUAL_WIDTH/2, 10);
		confirmBtn.setPosition(1050, 605);

        roundTripLabel.setPosition(10, 560);
        returnDateLabel.setPosition(290, 560);
        checkBox.setPosition(245, 560);
        returnTextBox.setPosition(510, 560);
	}

	@Override
	public void addActors() {
		addActor(background);
		addActor(departureAirportLabel);
		addActor(arrivalAirportLabel);
		addActor(departureAirportDropdown);
		addActor(arrivalAirportDropdown);
		addActor(dateLabel);
		addActor(departureDateTextBox);
		addActor(searchButton);
		addActor(tripsPanel);
		addActor(returnTripsPanel);
		addActor(confirmBtn);
        addActor(roundTripLabel);
        addActor(returnDateLabel);
        addActor(checkBox);
        addActor(returnTextBox);
	}

	@Override
	public void handle(int outcome) {
		switch (outcome) {
		case 0:
            tripsPanel.loading();
            if(Constants.isRoundTrip){
                String date = departureDateTextBox.getText();
                String returnDate = returnTextBox.getText();

                int year, month, day;
                String[] dateArray = date.split("/");
                year = Integer.parseInt(dateArray[2]);
                day = Integer.parseInt(dateArray[1]);
                month = Integer.parseInt(dateArray[0]);
                DateTime depart = DateTime.of(year, month, day, 0);

                dateArray = returnDate.split("/");
                year = Integer.parseInt(dateArray[2]);
                day = Integer.parseInt(dateArray[1]);
                month = Integer.parseInt(dateArray[0]);
                DateTime returnDT = DateTime.of(year, month, day, 0);

                Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
                Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());

                Constants.setGlobals(departAirport, arrivalAirport, depart, returnDT, true);
                // Run the search delayed so we can see the loading message
                addAction(Actions.sequence(Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        List<Trip> trips = (new TripBuilder()).getTrips(departAirport, arrivalAirport, depart);
                        List<Trip> returnTrips = (new TripBuilder()).getTrips(arrivalAirport, departAirport, returnDT);
                        tripsPanel.updateTrips(trips, seatClassSelection);
                        returnTripsPanel.updateTrips(returnTrips, seatClassSelection);
                        return true;
                    }
                }));

            } else {
                String date = departureDateTextBox.getText();
                System.out.println(date);
                int year, month, day;
                String[] dateArray = date.split("/");
                year = Integer.parseInt(dateArray[2]);
                day = Integer.parseInt(dateArray[1]);
                month = Integer.parseInt(dateArray[0]);
                DateTime depart = DateTime.of(year, month, day, 0);
                //TODO: write return date info
                Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
                Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());

                Constants.setGlobals(departAirport, arrivalAirport, depart, null, false);
                // Run the search delayed so we can see the loading message
                addAction(Actions.sequence(Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        List<Trip> trips = (new TripBuilder()).getTrips(departAirport, arrivalAirport, depart);
                        tripsPanel.updateTrips(trips, seatClassSelection);
                        return true;
                    }
                }));
            }

		}

	}

    public List<String> initAirports(){
        List<Airport> airports = Airports.getAirports();
        List<String> airportNames = new LinkedList<String>();
        for (Airport airport : airports) {
            airportNames.add(airport.getName() + " (" + airport.getCode() + ")");
        }
        return airportNames;
    }

	public Airport getAirport(String airportString) {
		airportString = airportString.substring(airportString.length() - 4, airportString.length() - 1);
		System.out.print(airportString);
		return Airports.getAirport(airportString);
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
