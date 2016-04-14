package com.csanon.libgdx.Utils;

import com.badlogic.gdx.Gdx;
import com.csanon.Airport;
import com.csanon.Trip;
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
    public static DateTime dateTime;
    public static Trip TripTO, TripBACK;


}
