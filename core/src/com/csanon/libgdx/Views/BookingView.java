package com.csanon.libgdx.Views;

import com.csanon.libgdx.Components.Button;
import com.csanon.libgdx.Components.ButtonAction;
import com.csanon.libgdx.Components.TintedImage;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

/**
 * Created by Gallo on 4/10/2016.
 */
public class BookingView extends BaseView {
    private final static int CONFIRM = 0;
    private TintedImage background;
    private Button confirmBtn;

    @Override
    public void init() {
        background = new TintedImage(Pic.Pixel, Tint.BACKGROUND_COLOR);
        confirmBtn = new Button(Pic.Pixel, Tint.GRAY, "Confirm", Assets.getInstance().getXSmallFont());
        confirmBtn.setButtonAction(new ButtonAction() {
            @Override
            public void buttonPressed() {
                handle(CONFIRM);
            }
        });
    }

    @Override
    public void setSizes() {
        background.setSize(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        confirmBtn.setSize(200,100);
    }

    @Override
    public void setPositions() {
        confirmBtn.setPosition(640,600);
    }

    @Override
    public void addActors() {
        addActor(background);
        addActor(confirmBtn);
    }

    @Override
    public void handle(int outcome) {
        switch(outcome){
            case CONFIRM:

                break;
        }
    }


}
