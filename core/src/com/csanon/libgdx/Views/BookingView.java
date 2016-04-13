package com.csanon.libgdx.Views;

import java.util.function.Consumer;

import com.csanon.SeatClass;
import com.csanon.Trip;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.server.FlightServer;
import com.csanon.server.ServerFactory;

/**
 * Created by Gallo on 4/10/2016.
 */
public class BookingView extends BaseView {
	private final static int CONFIRM = 0;
	private TintedImage background;
	private Button confirmBtn;
	private FlightServer server;
	private Trip tripTo;
	private Trip tripBack;
	private SeatClass seatClass;

	@Override
	public void init() {
		server = ServerFactory.getServer();
		tripTo = DisplayTripsView.TripTO;
		seatClass = SeatClass.ECONOMY;// TODO changes
		tripBack = DisplayTripsView.TripBACK;
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
		confirmBtn = new Button(Pic.Pixel, Tint.GRAY, "Confirm", Assets.getInstance().getXSmallFont());
		confirmBtn.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				handle(CONFIRM);
			}
		});

		// TODO make real callback
		Consumer<String> callback = null;
		// Get a lock on the server
		boolean locked = server.lockServer(callback);
		if (!locked) {
			// TODO: display something
		} else {
			try {
				boolean available = server.checkTripAvailable(tripTo, seatClass);
				if (!available) {
					// TODO display something
					System.out.println("TRIP UNAVAILABLE");
				}
			} catch (Exception e) {
				// TODO Display something
				System.out.println("ERROR");
			}
		}
	}

	@Override
	public void setSizes() {
		background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		confirmBtn.setSize(200, 100);
	}

	@Override
	public void setPositions() {
		confirmBtn.setPosition(640, 600);
	}

	@Override
	public void addActors() {
		addActor(background);
		addActor(confirmBtn);
	}

	@Override
	public void handle(int outcome) {
		switch (outcome) {
		case CONFIRM:
			try {
				server.bookTrip(tripTo, seatClass);
			} catch (Exception e) {
				// TODO display something
			}
			break;
		}
	}

}
