package com.csanon.libgdx.ScreenManaging;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csanon.libgdx.AppStage;
import com.csanon.libgdx.Utils.ViewID;
import com.csanon.libgdx.Views.BaseView;

/**
 * Created by Gallo on 3/19/2016.
 */
public class TransitionManager {

    public void createTransition(final BaseView currentView, final BaseView newView, final ViewID newViewID, final AppStage stage, TransitionType transitionType) {
        Gdx.input.setInputProcessor(null);
        stage.addView(newView);
        Action[] actions = executeTransition(currentView, newView, transitionType);
        currentView.addAction(actions[0]);
        newView.addAction(Actions.sequence(actions[1], new Action() {
            public boolean act(float delta) {
                stage.removeView();
                stage.setCurrentView(newView);
                Gdx.input.setInputProcessor(stage);
                return true;
            }
        }));
    }

    public Action[] executeTransition (final BaseView currentView, final BaseView newView, TransitionType transitionType) {
        Action[] actions = new Action[2];
        switch(transitionType){
            case SLIDE_R_TRANSITION:
                actions[0] = Actions.sequence(Actions.moveTo(0, 0), Actions.moveTo(-currentView.getWidth(), 0, 0.4f));
                actions[1] = Actions.sequence(Actions.moveTo(newView.getWidth(), 0), Actions.moveTo(0, 0, 0.4f));
                return actions;
            case SLIDE_L_TRANSITION:
                actions[0] = Actions.sequence(Actions.moveTo(0, 0), Actions.moveTo(currentView.getWidth(), 0, 0.4f));
                actions[1] = Actions.sequence(Actions.moveTo(-newView.getWidth(), 0), Actions.moveTo(0, 0, 0.4f));
                return actions;
            case DEFAULT_TRANSITION:
                actions[0] = Actions.sequence();
                actions[1] = Actions.sequence();
                return actions;
            default:
                return null;
        }

    }
}
