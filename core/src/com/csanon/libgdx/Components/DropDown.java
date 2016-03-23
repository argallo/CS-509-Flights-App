package com.csanon.libgdx.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

public class DropDown extends Group{

	private SelectBox<String> selectBox;
	private SelectBoxStyle selectBoxStyle;
	
	public DropDown(){
		selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.fontColor = Tint.BACKGROUND_COLOR;
		selectBoxStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.font = Assets.getInstance().getMidFont();
		//selectBoxStyle.scrollStyle = ScrollPaneStyle;
		//selectBoxStyle.scrollStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle = new List.ListStyle();
		selectBoxStyle.listStyle.font = Assets.getInstance().getMidFont();
		selectBoxStyle.listStyle.fontColorSelected = Color.RED;
		selectBoxStyle.listStyle.fontColorUnselected = Color.ORANGE;
		selectBoxStyle.listStyle.selection = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBox = new SelectBox<String>(selectBoxStyle);
		selectBox.setSize(100, 50);
		addActor(selectBox);
	}
	
	
}
