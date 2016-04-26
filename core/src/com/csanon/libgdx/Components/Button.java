package com.csanon.libgdx.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.csanon.libgdx.Utils.ColorHelper;

/**
 * Created by Gallo on 3/19/2016.
 */
public class Button extends TintedImage {

	protected Label buttonLabel;
    private Label.LabelStyle labelStyle;
    private Color backgroundColor;
    private Color pressedColor;
    ButtonAction buttonAction;
    private TintedImage icon;
    boolean staySelected = false;
    private String mainImage;

    public Button(String mainImage, Color backgroundColor){
        this(mainImage, backgroundColor, "", null, null);
    }

    public Button(String mainImage, final Color backgroundColor, String buttonText, BitmapFont fontType){
        this(mainImage, backgroundColor, buttonText, fontType, null);
    }

    public Button(String mainImage, final Color backgroundColor, String buttonText, BitmapFont fontType, TintedImage icon){
        super(mainImage, backgroundColor);
        this.mainImage = mainImage;
        pressedColor = (ColorHelper.darken(new Color(backgroundColor), 0.4f));
        if(fontType != null) {
            labelStyle = new Label.LabelStyle(fontType, Color.WHITE);
            buttonLabel = new Label(buttonText, labelStyle);
            buttonLabel.setAlignment(Align.center);
        }
        if(icon != null){
            this.icon = icon;
        }
        this.backgroundColor = backgroundColor;
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (buttonAction != null)
                    buttonAction.buttonPressed();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                setTint(pressedColor);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if(!staySelected) {
                    setTint(getBackgroundColor());
                }
            }
        });
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if(buttonLabel!= null)
            buttonLabel.setPosition(x, y);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        if(buttonLabel!= null)
            buttonLabel.setX(x);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if(buttonLabel!= null)
            buttonLabel.setSize(width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(icon != null)
            icon.draw(batch, parentAlpha);
        if(buttonLabel!= null && isVisible())
            buttonLabel.draw(batch, parentAlpha);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setButtonAction(ButtonAction action){
        buttonAction = action;
    }

    public String getText(){
        return buttonLabel.getText().toString();
    }

    public void setText(String text){
        buttonLabel.setText(text);
    }

    public Color getPressedColor() {
        return pressedColor;
    }

    public void setStaySelected(boolean staySelected) {
        this.staySelected = staySelected;
    }

    public boolean isStaySelected() {
        return staySelected;
    }

    @Override
    public void setImage(String image) {
        super.setImage(image);
        this.mainImage = image;
    }

    public String getMainImage() {
        return mainImage;
    }
}
