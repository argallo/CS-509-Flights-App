package com.csanon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csanon.libgdx.AppStage;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.ViewID;
import com.csanon.libgdx.ScreenManaging.ViewManager;

public class App extends ApplicationAdapter {
    private static final ViewID INITIAL_SCREEN = ViewID.SPLASH;
    private AppStage stage;
    private SpriteBatch batch;
    private Viewport viewport;
    private FPSLogger fpsLogger;
    long startTime;

    @Override
    public void create () {
        initCamera();
        //Sets the initial spritebatch that will be used to draw the stage
        batch = new SpriteBatch();
        //Initializes the app stage passing the viewport and batch to render the stage
        stage = new AppStage(viewport, batch);
        //Will be used in switching between views
        ViewManager.getInstance().setStage(stage);
        //Sets the initial screen to be rendered
        ViewManager.getInstance().setInitialView(INITIAL_SCREEN);
        //sets the input to actors that are found in the stage
        Gdx.input.setInputProcessor(stage);

        fpsLogger = new FPSLogger();
    }

    /**
     * initCamera: creates the camera and viewport to be used in the application.
     */
    public void initCamera() {
        viewport = new StretchViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        viewport.apply();
    }


    @Override
    public void render() {
        this.batch.totalRenderCalls = 0;
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        fpsLogger.log();
        if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */{
            Gdx.app.log("Draws", "amt: " + this.batch.totalRenderCalls);
            startTime = TimeUtils.nanoTime();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // stage.pause();
    }

    @Override
    public void resume() {
        // stage.resume();
    }

    @Override
    public void dispose() {
        //Always dispose Audio/Assets here
        //AudioUtils.dispose();
        stage.dispose();
        Assets.getInstance().dispose();
    }
}
