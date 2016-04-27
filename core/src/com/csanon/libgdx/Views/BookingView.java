package com.csanon.libgdx.Views;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

import com.csanon.Flight;
import com.csanon.ITrip;
import com.csanon.SeatClass;
import com.csanon.libgdx.Components.AbsPopup;
import com.csanon.libgdx.Components.BookingPopup;
import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.ScreenManaging.TransitionType;
import com.csanon.libgdx.ScreenManaging.ViewManager;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.libgdx.Utils.ViewID;
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
	private ITrip tripTo;
	private ITrip tripBack;
	private SeatClass seatClass;
	private BookingPopup popup;
	private Button backBtn;
	private TextLabel toTripInfo, fromTripInfo;

	@Override
	public void init() {
		server = ServerFactory.getServer();
		tripTo = Constants.TripTO;
		seatClass = SeatClass.ECONOMY;// TODO changes
		tripBack = Constants.TripBACK;
		popup = new BookingPopup(this);
		background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
		confirmBtn = new Button(Pic.Pixel, Tint.GRAY, "Confirm", Assets.getInstance().getXSmallFont());
		backBtn = new Button(Pic.Pixel, Tint.GRAY, "Cancel", Assets.getInstance().getXSmallFont());
		confirmBtn.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				handle(CONFIRM);
			}
		});
		backBtn.setButtonAction(new ButtonAction() {
			@Override
			public void buttonPressed() {
				ViewManager.getInstance().transitionViewTo(ViewID.DISPLAY_SEARCH, TransitionType.SLIDE_L_TRANSITION);
			}
		});
		// TODO make real callback
		Consumer<String> callback = message -> {
			popup.activatePopup(message);
		};
		// Get a lock on the server
		boolean locked = server.lockServer(callback);
		if (!locked) {
			popup.activatePopup("Error: No Server Lock :)");
		} else {
			try {
				boolean available = server.checkTripAvailable(tripTo);
				if (!available) {
					popup.activatePopup("Sorry This Trip is UNAVAILABLE");
					// System.out.println("TRIP UNAVAILABLE");
				}
			} catch (Exception e) {
				popup.activatePopup("Error: Something went wrong!");
				// System.out.println("ERROR");
			}
		}

		toTripInfo = new TextLabel("", Assets.getInstance().getXSmallFont());
		fromTripInfo = new TextLabel("", Assets.getInstance().getXSmallFont());

		populateInfo();

	}

	public void populateInfo() {
		String info = "";
		List<Flight> flights = Constants.TripTO.getLegs();
		for (Flight flight : flights) {
			info += "Flight number:" + flight.getFlightNum() + " ";
			info += "Duration: " + flight.getDuration() + "\n";
			info += "From: " + flight.getDepartureAirport().getName() + "\n";
			info += "(" + flight.getDepartureAirport().getCode() + ") ";
			info += "Depart: " + flight.getDepartureTime() + "\n";
			info += "To: " + flight.getArrivalAirport().getName() + "\n";
			info += "(" + flight.getArrivalAirport().getCode() + ") ";
			info += "Arrive: " + flight.getArrivalTime() + "\n\n";
		}
		Duration layover = Constants.TripTO.getTotalLayoverTime();
		Duration totalTime = Constants.TripTO.getTotalTravelTime();
		info += "Total Layover Time: " + String.format("%d:%02d%n", layover.toHours(), layover.minusHours(layover.toHours()).toMinutes());
		info += "Total Travel Time: " + String.format("%d:%02d%n", totalTime.toHours(), totalTime.minusHours(totalTime.toHours()).toMinutes());
		info += "Total Price: " + Constants.TripTO.getTotalPrice();
		toTripInfo.setText(info);
		toTripInfo.setPosition(Constants.VIRTUAL_WIDTH / 2 - toTripInfo.getWidth() / 2, 400);
		if (Constants.isRoundTrip) {
			String info2 = "";
			List<Flight> flights2 = Constants.TripBACK.getLegs();
			for (Flight flight : flights2) {
				info2 += "Flight number:" + flight.getFlightNum() + " ";
				info2 += "Duration: " + flight.getDuration() + "\n";
				info2 += "From: " + flight.getDepartureAirport().getName() + "\n";
				info2 += "(" + flight.getDepartureAirport().getCode() + ") ";
				info2 += "Depart: " + flight.getDepartureTime() + "\n";
				info2 += "To: " + flight.getArrivalAirport().getName() + "\n";
				info2 += "(" + flight.getArrivalAirport().getCode() + ") ";
				info2 += "Arrive: " + flight.getArrivalTime() + "\n\n";
			}
			Duration layover2 = Constants.TripBACK.getTotalLayoverTime();
			Duration totalTime2 = Constants.TripBACK.getTotalTravelTime();
			info2 += "Total Layover Time: " + String.format("%d:%02d%n", layover2.toHours(), layover2.minusHours(layover2.toHours()).toMinutes());
			info2 += "Total Travel Time: " + String.format("%d:%02d%n", totalTime2.toHours(), totalTime2.minusHours(totalTime2.toHours()).toMinutes());
			info2 += "Total Price: " + Constants.TripBACK.getTotalPrice();
			fromTripInfo.setText(info2);
			fromTripInfo.setPosition(Constants.VIRTUAL_WIDTH / 2 + 300, 400);
			toTripInfo.setPosition(Constants.VIRTUAL_WIDTH / 2 - 300, 400);
		}

	}

	@Override
	public void setSizes() {
		background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		confirmBtn.setSize(200, 80);
		backBtn.setSize(200, 80);
	}

	@Override
	public void setPositions() {
		confirmBtn.setPosition(590, 30);
		backBtn.setPosition(50, 30);
	}

	@Override
	public void addActors() {
		addActor(background);
		addActor(toTripInfo);
		addActor(fromTripInfo);
		addActor(confirmBtn);
		addActor(backBtn);
		addActor(popup);
	}

	@Override
	public void handle(int outcome) {
		switch (outcome) {
		case CONFIRM:
			try {
				server.bookTrip(tripTo);
				server.unlockServer();
				popup.activatePopup("Success! Trip has been Booked");

			} catch (Exception e) {
				popup.activatePopup("Error: No Server Lock :)");
			}
			break;
		case AbsPopup.CLOSE:
			ViewManager.getInstance().transitionViewTo(ViewID.DISPLAY_SEARCH, TransitionType.SLIDE_L_TRANSITION);
		}
	}

}
