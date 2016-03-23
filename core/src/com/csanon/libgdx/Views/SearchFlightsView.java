package com.csanon.libgdx.Views;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
