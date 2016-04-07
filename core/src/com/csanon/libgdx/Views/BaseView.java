package com.csanon.libgdx.Views;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Tint;

/**
 * Created by Gallo on 3/19/2016.
 */
public abstract class BaseView extends Group {

    private Rectangle rectangle;

    public BaseView() {
        Tint.resetTints();
        rectangle = new Rectangle(0,0, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        init();
        setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        setSizes();
        setPositions();
        addActors();
    }

    public abstract void init();
    public abstract void setSizes();
    public abstract void setPositions();
    public abstract void addActors();
    public abstract void handle(int outcome);


}
