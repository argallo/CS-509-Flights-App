package com.csanon.libgdx.Views;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csanon.Airplanes;
import com.csanon.Airports;
import com.csanon.libgdx.Components.TextLabel;
import com.csanon.libgdx.ScreenManaging.TransitionType;
import com.csanon.libgdx.ScreenManaging.ViewManager;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.ViewID;

/**
 * Created by Gallo on 3/19/2016.
 */
public class SplashView extends BaseView {
	public static final int SPLASH_TIME = 2; // approx seconds

	private Image splashLogo;
	private static Timer timer = new Timer();
	private static TextLabel loadingMessage = new TextLabel("Loading...");
	private int counter = 0;
	private boolean messagesFinished = false;
	private boolean loadingFinished = false;
	private static String[] messages = { "Booting Windows Vista", "Assembling Airplanes", "Hiring Geoff, The Runway Engineer",
			"Clearing Runways", "Constructing Airports", "   NA","NA   ","   NA","NA   ","!! BATMAN !!", "Communicating Objectives", "Building The Internet", "est time remaining: 1 min",
			"Redirecting Cats", "Contacting Server", "Vista requires an update", "est time remaining: 5 min", "est time remaining: 10 min", "est time remaining: 10 hours" };

	@Override
	public void init() {
		splashLogo = new Image(Assets.getInstance().getSplash());
		// for now just load texture atlas but as it becomes bigger we may need
		// to modify the act method to load in increments
		Assets.getInstance().loadCommonAssets();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering
				// thread
				Airports.initialize();
				Airplanes.initialize();
				// post a Runnable to the rendering thread that processes the
				// result
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						// process the result, e.g. add it to an Array<Result>
						// field of the ApplicationListener.
						loadingFinished = true;
						completeSplashView();
					}
				});
			}
		}).start();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (counter == messages.length) {
					messagesFinished = true;
					completeSplashView();
				}
				loadingMessage.setText(messages[counter % messages.length]);
				counter++;
			}
		}, 1500, 1500);
	}

	@Override
	public void setSizes() {

	}

	@Override
	public void setPositions() {
		loadingMessage.setPosition(Constants.VIRTUAL_WIDTH / 2 - loadingMessage.getWidth() / 2,
				Constants.VIRTUAL_HEIGHT / 4);
	}

	@Override
	public void handle(int outcome) {

	}

	public void addActors() {
		addActor(splashLogo);
		addActor(loadingMessage);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// updateSplash(delta);
	}

	/**
	 * Make the transition to the next view
	 */
	public void completeSplashView() {
		if (messagesFinished && loadingFinished) {
			timer.cancel();
			ViewManager.getInstance().transitionViewTo(ViewID.SEARCH_MAIN, TransitionType.DEFAULT_TRANSITION);
		}
	}

}
