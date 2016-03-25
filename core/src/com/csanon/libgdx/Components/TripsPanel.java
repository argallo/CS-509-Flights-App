package com.csanon.libgdx.Components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.csanon.Flight;
import com.csanon.Trip;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

import java.util.List;

public class TripsPanel extends Group {

	private ScrollPane scrollPane;
	private Group rows;

	public TripsPanel() {
		setSize(800, 300);
		rows = new Group();
		rows.setSize(800, 20 * 100);
		loadRows();
		scrollPane = new ScrollPane(rows);
		scrollPane.setFillParent(true);
		addActor(scrollPane);
	}

	public void loadRows() {
		for (int i = 0; i < 20; i++) {
			TintedImage row = new TintedImage(Pic.Pixel, Tint.GRAY);
			row.setSize(800, 98);
			row.setPosition(0, i * 100);
			rows.addActor(row);
		}

	}

	public void updateTrips(List<Trip> trips) {

		removeRows();

		// scrollPane.setSize(800, trips.size()*100);
		String info = "";
		for (int i = 0; i < trips.size() - 1; i++) {
			Flight flight = trips.get(i).getLegs().get(0); // TODO: this will be more info from each leg

			info += "Flight number:" + flight.getFlightNum() + " ";
			info += "Duration: " + flight.getDuration() + " ";
			info += "\nDepart: " + flight.getDepartureTime() + " ";
			info += "Arrive: " + flight.getArrivalTime() + " ";
			info += "\nEconomy: " + flight.getEconomyPrice() + " ";
			info += "First Class: " + flight.getFirstClassPrice() + "\n\n";
		}
		Flight flight = trips.get(trips.size() - 1).getLegs().get(0);
		info += "Flight number:" + flight.getFlightNum() + " ";
		info += "Duration: " + flight.getDuration() + " ";
		info += "\nDepart: " + flight.getDepartureTime() + " ";
		info += "Arrive: " + flight.getArrivalTime() + " ";
		info += "\nEconomy: " + flight.getEconomyPrice() + " ";
		info += "First Class: " + flight.getFirstClassPrice() + "\n";

		TextLabel infoLabel = new TextLabel(info, Assets.getInstance().getXSmallFont());
		infoLabel.setPosition(170, 0);

		for (int i = 0; i < trips.size(); i++) {
			TintedImage row = new TintedImage(Pic.Pixel, Tint.GRAY);
			row.setSize(800, 90);
			// infoLabel.setPosition(170, i * 100);
			row.setPosition(0, i * 92);
			rows.addActor(row);

		}

		rows.addActor(infoLabel);
		rows.setSize(800, infoLabel.getHeight());
		scrollPane.invalidate();

	}

	public void removeRows() {
		rows.clearChildren();
	}
	/*
	 * public void appendNames(){ String names = ""; String crystalCount = ""; for(int i = 0; i < 19; i++){ names =
	 * (names+(i+1)+". Jimmy3430\n"); crystalCount = (crystalCount+(20-i)+"\n"); } names = (names+(20)+". Jimmy3430");
	 * crystalCount = (crystalCount+(1));
	 * 
	 * TextLabel textLabel = new TextLabel(names, Assets.getInstance().getMidFont(), Align.left);
	 * textLabel.setPosition(10, 0); LogUtils.Log(textLabel.getHeight());
	 * 
	 * TextLabel crystalLabel = new TextLabel(crystalCount, Assets.getInstance().getMidFont(), Align.right);
	 * crystalLabel.setPosition(420, 0);
	 * 
	 * rows.addActor(textLabel); rows.addActor(crystalLabel);
	 * 
	 * for(int i = 0; i < 20; i++){ TintedImage crystal = new TintedImage(Pic.Mega_Crystal); crystal.setSize(60,50);
	 * crystal.setPosition(490, i*58); rows.addActor(crystal); } }
	 */

}
