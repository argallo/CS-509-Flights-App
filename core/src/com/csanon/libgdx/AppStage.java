package com.csanon.libgdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csanon.libgdx.Views.BaseView;

/**
 * Created by Gallo on 3/19/2016.
 */
public class AppStage extends Stage {

    BaseView currentView;

    public AppStage(Viewport viewport, Batch batch){
        super(viewport, batch);
    }

    public void addInitialView(BaseView baseView) {
        setCurrentView(baseView);
        addView(baseView);
    }

    public void addView(BaseView baseView) {
        this.addActor(baseView);
    }

    public void removeView(){
        currentView.remove();
    }

    public void setCurrentView(BaseView baseView) {
        currentView = baseView;
    }

    public BaseView getCurrentView() {
        return currentView;
    }
}
