package com.csanon.libgdx.Views;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csanon.Airport;
import com.csanon.Airports;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.DropDown;
import com.csanon.libgdx.Components.TextBox;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Components.TitleLabel;
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

/**
 * Created by Gallo on 3/29/2016.
 */
public class SearchFlightsHomeView  extends BaseView{

    private Button searchButton;
    private TitleLabel titleLabel;
    private TextLabel departureAirportLabel, arrivalAirportLabel, dateLabel, returnDateLabel, roundTripLabel;
    private DropDown departureAirportDropdown, arrivalAirportDropdown;
    private TintedImage background;
    private TextBox textBox, returnTextBox;
    private CheckBox checkBox;


    @Override
    public void init() {
        background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
        titleLabel = new TitleLabel("Flight Finder");
        departureAirportLabel = new TextLabel("Depart Airport:", Assets.getInstance().getSmallFont());
        arrivalAirportLabel = new TextLabel("Arrival Airport:", Assets.getInstance().getSmallFont());
        dateLabel = new TextLabel("Depart Date:", Assets.getInstance().getSmallFont());
        roundTripLabel = new TextLabel("Round Trip?", Assets.getInstance().getSmallFont());
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
        icon.setPosition(600, 105);
        searchButton = new Button(Pic.Pixel, Tint.GRAY, "Search", Assets.getInstance().getMidFont(), icon);
        searchButton.setButtonAction(new ButtonAction() {
            @Override
            public void buttonPressed() {
                handle(0);
            }
        });

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


        returnDateLabel = new TextLabel("Return Date:", Assets.getInstance().getSmallFont());
        returnTextBox = new TextBox(10,"05/15/2016", TextBox.DATE);
        returnDateLabel.setVisible(false);
        returnTextBox.setVisible(false);

    }

    @Override
    public void setSizes() {
        background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        textBox.setSize(160, 35);
        returnTextBox.setSize(160, 35);
        searchButton.setSize(250, 100);
        checkBox.setSize(40, 40);
    }

    @Override
    public void setPositions() {
        titleLabel.setPosition(Constants.VIRTUAL_WIDTH / 2 - titleLabel.getWidth() / 2, 600);
        departureAirportDropdown.setPosition(455, 500);
        arrivalAirportDropdown.setPosition(455, 420);
        departureAirportLabel.setPosition(220, 500);
        arrivalAirportLabel.setPosition(220, 420);
        dateLabel.setPosition(220, 340);
        roundTripLabel.setPosition(220, 260);
        returnDateLabel.setPosition(640, 340);
        textBox.setPosition(455, 340);
        searchButton.setPosition(Constants.VIRTUAL_WIDTH / 2 - searchButton.getWidth() / 2, 100);
        checkBox.setPosition(455, 260);
        returnTextBox.setPosition(860, 340);
    }

    @Override
    public void addActors() {
        addActor(background);
        addActor(titleLabel);
        addActor(departureAirportLabel);
        addActor(arrivalAirportLabel);
        addActor(roundTripLabel);
        addActor(departureAirportDropdown);
        addActor(arrivalAirportDropdown);
        addActor(dateLabel);
        addActor(textBox);
        addActor(returnDateLabel);
        addActor(checkBox);
        addActor(returnTextBox);
        addActor(searchButton);
    }

    @Override
    public void handle(int outcome) {
        switch (outcome){
            case 0:
                ViewManager.getInstance().unfocusAll();
                if(checkBox.isChecked()){
                    if (textBox.isValid() && returnTextBox.isValid()) {
                        String date = textBox.getText();
                        String returnDate = returnTextBox.getText();

                        //Set Start Date
                        int year, month, day;
                        String[] dateArray = date.split("/");
                        year = Integer.parseInt(dateArray[2]);
                        day = Integer.parseInt(dateArray[1]);
                        month = Integer.parseInt(dateArray[0]);
                        DateTime depart = DateTime.of(year, month, day, 0);

                        //Set End Date
                        dateArray = returnDate.split("/");
                        year = Integer.parseInt(dateArray[2]);
                        day = Integer.parseInt(dateArray[1]);
                        month = Integer.parseInt(dateArray[0]);
                        DateTime returnDT = DateTime.of(year, month, day, 0);

                        if(depart.compareTo(returnDT)>0){
                            returnTextBox.setTextColor(com.badlogic.gdx.graphics.Color.RED);
                        } else {

                            //get airports selected
                            Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
                            Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());

                            //Update Globals
                            Constants.setGlobals(departAirport, arrivalAirport, depart, returnDT, true);

                            //Transition to next screen
                            ViewManager.getInstance().transitionViewTo(ViewID.DISPLAY_SEARCH, TransitionType.SLIDE_R_TRANSITION);
                        }
                    }
                } else {
                    if (textBox.isValid()) {
                        String date = textBox.getText();

                        //Set start date
                        int year, month, day;
                        String[] dateArray = date.split("/");
                        year = Integer.parseInt(dateArray[2]);
                        day = Integer.parseInt(dateArray[1]);
                        month = Integer.parseInt(dateArray[0]);
                        DateTime depart = DateTime.of(year, month, day, 0);

                        //get airports selected
                        Airport departAirport = getAirport(departureAirportDropdown.getCurrentItem());
                        Airport arrivalAirport = getAirport(arrivalAirportDropdown.getCurrentItem());

                        //Update Globals
                        Constants.setGlobals(departAirport, arrivalAirport, depart);

                        //Transition to next screen
                        ViewManager.getInstance().transitionViewTo(ViewID.DISPLAY_SEARCH, TransitionType.SLIDE_R_TRANSITION);
                    }
                }
                break;
        }
    }



    public Airport getAirport(String airportString){
        airportString = airportString.substring(airportString.length()-4,airportString.length()-1);
        return Airports.getAirport(airportString);
    }


}
