package com.csanon.libgdx.ScreenManaging;

import com.badlogic.gdx.graphics.Camera;
import com.csanon.libgdx.AppStage;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.ViewID;

/**
 * Created by Gallo on 3/19/2016.
 */
public class ViewManager {

    private Camera camera;
    private AppStage stage;
    private ViewBuilderFactory viewBuilder = new ViewBuilderFactory();
    private TransitionManager transitionManager = new TransitionManager();

    private static final ViewManager INSTANCE = new ViewManager();

    public static ViewManager getInstance() {
        return INSTANCE;
    }

    public void setStage(AppStage stage) {
        this.stage = stage;
    }

    /**
     * Called when view wants to transition to a new view
     * @param newViewID the ID of the new view
     * @param transitionType the type of transition to make when moving to new view
     */
    public void transitionViewTo(ViewID newViewID, TransitionType transitionType) {
        if(stage.getCurrentView().equals(ViewID.SPLASH)){
            Assets.getInstance().disposeSplash();
        }
        transitionManager.createTransition(stage.getCurrentView(), viewBuilder.build(newViewID), newViewID, stage, transitionType);
    }

    /**
     * Sets the initial view when the app starts up.
     * @param viewID
     */
    public void setInitialView(ViewID viewID) {
        stage.addInitialView(viewBuilder.build(viewID));
    }

    /**
     * Used for login/create account screen, quick way to unfocus all objects in a stage
     */
    public void unfocusAll(){
        stage.unfocusAll();
    }

}
