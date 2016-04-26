package com.csanon.libgdx.Components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.csanon.FilterUtil;
import com.csanon.Flight;
import com.csanon.ITrip;
import com.csanon.SeatClass;
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
	// private float scrollSize = 0;
	private Table table;
	private List<ITrip> originalList;
	private SeatClass seatClassSelection;
	private boolean isRoundTrip = false;
    private boolean selectable = true;

	public TripsPanel(DisplayTripsView displayTripsView, boolean isRoundTrip, boolean selectable) {
		this.displayTripsView = displayTripsView;
		this.isRoundTrip = isRoundTrip;
		tripButtons = new ArrayList<>();
		setSize(600, 500);
		table = new Table();
        if(selectable) {
            table.setTouchable(Touchable.childrenOnly);
        } else {
            table.setTouchable(Touchable.disabled);
        }
		scrollPane = new ScrollPane(table);
		scrollPane.setFillParent(true);
		addActor(scrollPane);
		setTouchable(Touchable.childrenOnly);
	}

	public void updateTrips(List<ITrip> trips, SeatClass seatClass) {
		originalList = trips;
		seatClassSelection = seatClass;
		update();

	}

	private void update() {
		table.clear();
		System.out.println("Orgi len: " + originalList.size());
		List<ITrip> trips = FilterUtil.filterBySeat(originalList, seatClassSelection);
		System.out.println("Filtered list: " + trips.size());

		// If there are no results
		if (trips.size() == 0) {
			// display no results message
			TextLabel label = new TextLabel("No results found.\n Try changing the search parameters ",
					Assets.getInstance().getSmallFont(), Align.center);
			table.add(label);
		} else {

			String info = "";
			for (int i = 0; i < trips.size(); i++) {
				info = "Trip " + (i + 1) + ": \n";
				// each trip
				for (int j = 0; j < trips.get(i).getLegs().size(); j++) {
					Flight flight = trips.get(i).getLegs().get(j);

					info += "Flight number:" + flight.getFlightNum() + " ";
					info += "Duration: " + flight.getDuration() + "\n";
					info += "From: " + flight.getDepartureAirport().getName() + " ";
					info += "(" + flight.getDepartureAirport().getCode() + ")\n";
					info += "Depart: " + flight.getDepartureTime() + "\n";
					info += "To: " + flight.getArrivalAirport().getName() + " ";
					info += "(" + flight.getArrivalAirport().getCode() + ")\n";
					info += "Arrive: " + flight.getArrivalTime() + " ";
					info += "\nEconomy: " + flight.getEconomyPrice() + " ";
					info += "First Class: " + flight.getFirstClassPrice() + "\n\n";
				}

				TextLabel infoLabel = new TextLabel(info, Assets.getInstance().getXSmallFont(), Align.left);
				infoLabel.setTouchable(Touchable.disabled);
				Button row = new Button(Pic.Pixel, Tint.GRAY);
				tripButtons.add(row);
				final int tripindex = i;
				row.setButtonAction(new ButtonAction() {
					@Override
					public void buttonPressed() {
						for (Button button : tripButtons) {
							button.setTint(Tint.GRAY);
							button.setStaySelected(false);
						}
						row.setTint(row.getPressedColor());
						row.setStaySelected(true);
						// TODO: add param to tripview telling it which one it
						// is To or Back
						if (isRoundTrip) {
							displayTripsView.setSelectedTripBack(trips.get(tripindex));
						} else {
							displayTripsView.setSelectedTripTo(trips.get(tripindex));
						}
					}
				});
				Stack stack = new Stack(row, infoLabel);
				stack.setTouchable(Touchable.childrenOnly);

				table.add(stack).fill().padBottom(10);
				table.row();
				// scrollSize += infoLabel.getHeight();
			}
		}
		setWidth(table.getWidth());
		scrollPane.setWidth(table.getWidth());
		scrollPane.invalidate();
	}

	public void loading() {
		table.clear();
		TextLabel label = new TextLabel("LOADING...", Assets.getInstance().getLargeFont(), Align.center);
		table.add(label);
		scrollPane.invalidate();
	}

	public void setSeatClass(SeatClass seatClass) {
		this.seatClassSelection = seatClass;
		update();
	}

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        if(selectable) {
            table.setTouchable(Touchable.childrenOnly);
        } else {
            table.setTouchable(Touchable.disabled);
        }
    }

    public void unselect(){
        for (Button button : tripButtons) {
            button.setTint(Tint.GRAY);
            button.setStaySelected(false);
        }
    }
}
