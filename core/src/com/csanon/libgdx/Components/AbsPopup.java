package com.csanon.libgdx.Components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Views.BaseView;
/**
 * Created by Gallo on 1/17/2016.
 */
public abstract class AbsPopup extends Group {

    public static final int CLOSE = 88;

    protected TintedImage background;
    protected BaseView baseView;

    public AbsPopup(BaseView baseView){
        this.baseView = baseView;
        background = new TintedImage(Pic.Blank_Popup);
        background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        background.setVisible(false);
        addActor(background);
    }

    public abstract void activatePopup(String infoString);

    public abstract void deactivatePopup();



}
