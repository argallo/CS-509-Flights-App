package com.csanon.libgdx.Components;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Constants;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;
import com.csanon.libgdx.Views.BaseView;

/**
 * Created by Gallo on 1/13/2016.
 */
public class BookingPopup extends AbsPopup {

    private TextLabel infoText;
    private Button goBack;

    public BookingPopup(BaseView baseView){
        super(baseView);

        infoText = new TextLabel("Done", Assets.getInstance().getSmallFont());
        infoText.setPosition(Constants.VIRTUAL_WIDTH / 2 - 150, 500);
        infoText.setVisible(false);


        goBack = new Button(Pic.Pixel, Tint.GRAY, "Go Back", Assets.getInstance().getSmallFont());
        goBack.setSize(200, 100);
        goBack.setPosition(Constants.VIRTUAL_WIDTH / 2 - goBack.getWidth() / 2, 200);
        goBack.setVisible(false);
        setupButtons();

        addActor(infoText);
        addActor(goBack);
    }


    public void activatePopup(String infoString){
        infoText.setText(infoString);
        infoText.setPosition(Constants.VIRTUAL_WIDTH / 2, 500);
        background.addAction(Actions.sequence(Actions.sizeTo(0, 0), Actions.moveTo(Constants.VIRTUAL_WIDTH / 2, Constants.VIRTUAL_HEIGHT / 2),
                Actions.visible(true),
                Actions.parallel(Actions.sizeTo(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, 0.5f, Interpolation.exp10), Actions.moveTo(0, 0, 0.5f, Interpolation.exp10)), Actions.delay(0.1f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        infoText.setVisible(true);
                        goBack.setVisible(true);
                        return true;
                    }
                }));


    }

    @Override
    public void deactivatePopup() {
        background.addAction(Actions.sequence(new Action() {
                                                  @Override
                                                  public boolean act(float delta) {
                                                      infoText.setVisible(false);
                                                      goBack.setVisible(false);
                                                      return true;
                                                  }
                                              }, Actions.delay(0.1f),
                Actions.parallel(Actions.sizeTo(0, 0, 0.5f, Interpolation.exp10), Actions.moveTo(Constants.VIRTUAL_WIDTH / 2, Constants.VIRTUAL_HEIGHT / 2, 0.5f, Interpolation.exp10)), Actions.visible(false)));

    }

    public void setupButtons(){
        goBack.setButtonAction(new ButtonAction() {
            @Override
            public void buttonPressed() {
                deactivatePopup();
                baseView.handle(AbsPopup.CLOSE);
            }
        });
    }

}
