package com.csanon.libgdx.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

/**
 * Created by Gallo on 3/23/2016.
 */
public class TextBox extends Group {

	public static final int ALL = 0;
	public static final int DATE = 3;
	public static final int EMAIL = 1;
	public static final int CHARDIG = 2;
	private static final String DATE_PATTERN = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";

	private TextField textField;
	private TextField.TextFieldStyle style;
	private TintedImage backgroundFocused;
	private String backgroundText;
	private boolean password;
	private int filterType;

	public TextBox(int maxLength, final String backgroundText, int filterType) {
		this(maxLength, backgroundText, filterType, false);
	}

	public TextBox(int maxLength, final String backgroundText, final int filterType, boolean password) {
		this.backgroundText = backgroundText;
		this.password = password;
		this.filterType = filterType;
		style = new TextField.TextFieldStyle();
		style.background = new TextureRegionDrawable(Assets.getInstance().getTextureRegion(Pic.Curve_rectangle));
		style.background.setLeftWidth(style.background.getLeftWidth() + 25);
		style.background.setRightWidth(style.background.getRightWidth() + 25);
		style.cursor = new TextureRegionDrawable(Assets.getInstance().getTextureRegion(Pic.Black));
		style.font = com.csanon.libgdx.Utils.Assets.getInstance().getXSmallFont();
		style.fontColor = Color.LIGHT_GRAY;
		style.focusedFontColor = Color.BLACK;
		textField = new TextField(backgroundText, style);
		backgroundFocused = new TintedImage(Pic.Curve_rectangle, Tint.ORANGE);
		backgroundFocused.setPosition(-2, -2);
		backgroundFocused.setVisible(false);
		textField.addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				backgroundFocused.setVisible(focused);
				style.fontColor = Color.LIGHT_GRAY;
				if (textField.getText().equals(backgroundText)) {
					removeBackgroundText();
				}
				if (!focused && textField.getText().isEmpty()) {
					setBackgroundText();
				}
			}
		});
		textField.setTextFieldFilter(new TextField.TextFieldFilter() {
			@Override
			public boolean acceptChar(TextField textField, char c) {
				switch (filterType) {
				case ALL:
					return true;
				case DATE:
					if (Character.isDigit(c) || c == '/') {
						return true;
					}
					return false;
				case CHARDIG:
					return (Character.isDigit(c) || Character.isLetter(c)) ? true : false;
				}
				return true;
			}
		});
		textField.setMaxLength(maxLength);
		addActor(backgroundFocused);
		addActor(textField);
	}

	public TextBox() {
		this(0, "", TextBox.ALL);
	}

	public void setSize(float width, float height) {
		super.setSize(width, height);
		textField.setSize(width, height);
		backgroundFocused.setSize(width + 4, height + 4);
	}

	public String getText() {
		return textField.getText();
	}

	private void setBackgroundText() {
		textField.getStyle().fontColor = Color.LIGHT_GRAY;
		textField.setText(backgroundText);
		textField.setPasswordMode(false);

	}

	private void removeBackgroundText() {
		textField.setText("");
		if (password) {
			textField.setPasswordMode(true);
			textField.setPasswordCharacter('*');
		}
	}

	public boolean isValid() {
		backgroundFocused.setVisible(false);
		/**
		 * Check if date is valid
		 */
		if (filterType == DATE && !textField.getText().matches(DATE_PATTERN)) {
			textField.getStyle().fontColor = Color.RED;
			return false;
		}

		return true;
	}

	public void setText(String str) {
		textField.setText(str);
	}

    public void setTextColor(Color tint){
        style.fontColor = tint;
        textField.setStyle(style);
    }
}
