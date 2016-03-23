package com.csanon.libgdx.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.Airplanes;
import com.csanon.Airports;
import com.csanon.libgdx.ScreenManaging.TransitionType;
import com.csanon.libgdx.Utils.ViewID;

import com.csanon.libgdx.ScreenManaging.ViewManager;

/**
 * Created by Gallo on 3/19/2016.
 */
public class SplashView extends BaseView {
    public static final int SPLASH_TIME = 2; //approx seconds

    private Image splashLogo;
    private boolean counting = true;
    private float counter = 0;

    
	@Override
	public void init() {
    	splashLogo = new Image(Assets.getInstance().getSplash());
        //for now just load texture atlas but as it becomes bigger we may need to modify the act method to load in increments
        Assets.getInstance().loadCommonAssets();

        new Thread(new Runnable() {
        	   @Override
        	   public void run() {
        	      // do something important here, asynchronously to the rendering thread
        		   Airports.initialize();
        		   Airplanes.initialize();
        	      // post a Runnable to the rendering thread that processes the result
        	      Gdx.app.postRunnable(new Runnable() {
        	         @Override
        	         public void run() {
        	            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
        	        	 completeSplashView();
        	         }
        	      });
        	   }
        	}).start();
        //Initialize the list of planes and airports
       
        //Airplanes.initialize()
    }


    @Override
    public void setSizes() {

    }
    @Override
    public void setPositions() {

    }
    @Override
    public void handle(int outcome) {

    }

    public void addActors(){
    	addActor(splashLogo);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //updateSplash(delta);
    }

    /**
     * Waits a certain amount of time before transitioning to the main application
     * @param delta the detla time passed
     */
    public void updateSplash(float delta){
        if(counting) {
            counter += delta;
            //If other preloading needs to be done we can wait until its finished before transitioning
            if(counter > SPLASH_TIME){
                completeSplashView();
            }
        }
    }

    /**
     * Make the transition to the next view
     */
    public void completeSplashView(){
        counting = false;
        ViewManager.getInstance().transitionViewTo(ViewID.SEARCH, TransitionType.DEFAULT_TRANSITION);
    }

}
