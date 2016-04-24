package com.csanon.libgdx.Utils;

import com.badlogic.gdx.Gdx;
import com.csanon.Airport;
import com.csanon.ITrip;
import com.csanon.time.DateTime;

/**
 * Created by Gallo on 3/19/2016.
 */
public class Constants {

    public static final int APP_WIDTH = Gdx.graphics.getWidth();
    public static final int APP_HEIGHT = Gdx.graphics.getHeight();

    //default Sizes
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;

    public static Airport departureAirport, arrivalAirport;
    public static DateTime dateTime, returnDateTime;
    public static ITrip TripTO, TripBACK;
    public static boolean isRoundTrip = false;


    public static void setGlobals(Airport depart, Airport arrive, DateTime date, DateTime returnDate, boolean isRoundTrip){
        Constants.departureAirport = depart;
        Constants.arrivalAirport = arrive;
        Constants.dateTime = date;
        Constants.returnDateTime = returnDate;
        Constants.isRoundTrip = isRoundTrip;
    }

    public static void setGlobals(Airport depart, Airport arrive, DateTime date){
        setGlobals(depart, arrive, date, null, false);
    }

}
