package com.csanon.libgdx.Views;

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

	public void populateInfo(){
		String info = "";
		List<Flight> flights = Constants.TripTO.getLegs();
		for(Flight flight: flights){
			info += "Flight number:" + flight.getFlightNum() + " ";
			info += "Duration: " + flight.getDuration() + "\n";
			info += "From: " + flight.getDepartureAirport().getName() + "\n";
			info += "(" + flight.getDepartureAirport().getCode() + ") ";
			info += "Depart: " + flight.getDepartureTime() + "\n";
			info += "To: " + flight.getArrivalAirport().getName() + "\n";
			info += "(" + flight.getArrivalAirport().getCode() + ") ";
			info += "Arrive: " + flight.getArrivalTime() + " ";
			info += "\nEconomy: " + flight.getEconomyPrice() + " ";
			info += "First Class: " + flight.getFirstClassPrice() + "\n";
		}
		info+= "Total Layover Time: "+Constants.TripTO.getTotalLayoverTime()+"\n";
		info+= "Total Travel Time: "+Constants.TripTO.getTotalTravelTime()+"\n";
		info+= "Total Price: "+Constants.TripTO.getTotalPrice();
		toTripInfo.setText(info);
		toTripInfo.setPosition(Constants.VIRTUAL_WIDTH/2-toTripInfo.getWidth()/2, 400);
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
