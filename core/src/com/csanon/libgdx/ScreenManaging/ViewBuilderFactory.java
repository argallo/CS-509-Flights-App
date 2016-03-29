package com.csanon.libgdx.ScreenManaging;

import com.csanon.libgdx.Utils.ViewID;
import com.csanon.libgdx.Views.BaseView;
import com.csanon.libgdx.Views.DisplayTripsView;
import com.csanon.libgdx.Views.EmptyView;
import com.csanon.libgdx.Views.SearchFlightsHomeView;
import com.csanon.libgdx.Views.SplashView;

/**
 * Created by Gallo on 3/19/2016.
 */
public class ViewBuilderFactory {

    public ViewBuilderFactory() {

    }

    public BaseView build(ViewID viewID) {
        switch(viewID) {
            case SPLASH:
                return new SplashView();
            case SEARCH_MAIN:
                return new SearchFlightsHomeView();
            case DISPLAY_SEARCH:
                return new DisplayTripsView();
            default:
                return new EmptyView();
        }
    }
}
