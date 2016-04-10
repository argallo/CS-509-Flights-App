package com.csanon.libgdx.Components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csanon.Flight;
import com.csanon.Trip;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.libgdx.Views.DisplayTripsView;

import java.util.ArrayList;
import java.util.List;

public class TripsPanel extends Group {

	private ScrollPane scrollPane;
    private DisplayTripsView displayTripsView;
    private ArrayList<Button> tripButtons;
    private float scrollSize = 0;
    private Table table;

	public TripsPanel(DisplayTripsView displayTripsView) {
        this.displayTripsView = displayTripsView;
        tripButtons = new ArrayList<>();
		setSize(800, 500);
        table = new Table();
        table.setTouchable(Touchable.childrenOnly);
        scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		addActor(scrollPane);
        setTouchable(Touchable.childrenOnly);
	}


	public void updateTrips(List<Trip> trips) {
        table.clear();

		String info = "";
		for (int i = 0; i < trips.size(); i++) {
            info = "Trip "+(i+1)+": \n";
            //each trip
            for(int j = 0; j < trips.get(i).getLegs().size(); j++) {
                Flight flight = trips.get(i).getLegs().get(j); // TODO: this will be more info from each leg

                info += "Flight number:" + flight.getFlightNum() + " ";
                info += "Duration: " + flight.getDuration() + "\n";
                info += "From: "+flight.getArrivalAirport().getName()+" ";
                info += "Depart: " + flight.getDepartureTime() + "\n";
                info += "To: "+flight.getDepartureAirport().getName()+" ";
                info += "Arrive: " + flight.getArrivalTime() + " ";
                info += "\nEconomy: " + flight.getEconomyPrice() + " ";
                info += "First Class: " + flight.getFirstClassPrice()+"\n";
            }

            TextLabel infoLabel = new TextLabel(info, Assets.getInstance().getXSmallFont(), Align.left);
            infoLabel.setTouchable(Touchable.disabled);
            Button row = new Button(Pic.Pixel, Tint.GRAY);
            tripButtons.add(row);
            final int tripindex = i;
            row.setButtonAction(new ButtonAction() {
                @Override
                public void buttonPressed() {
                    for(Button button:tripButtons){
                        button.setTint(Tint.GRAY);
                        button.setStaySelected(false);
                    }
                    row.setTint(row.getPressedColor());
                    row.setStaySelected(true);
                    //TODO: add param to tripview telling it which one it is To or Back
                    displayTripsView.setSelectedTripTo(trips.get(tripindex));
                }
            });
            Stack stack = new Stack(row,infoLabel);
            stack.setTouchable(Touchable.childrenOnly);

            table.add(stack).fill().padBottom(10);
            table.row();
            scrollSize+=infoLabel.getHeight();
		}

		scrollPane.invalidate();

	}


}
