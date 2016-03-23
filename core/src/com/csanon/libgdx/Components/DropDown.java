package com.csanon.libgdx.Components;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
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
		selectBoxStyle.font = Assets.getInstance().getSmallFont();
		selectBoxStyle.scrollStyle = new ScrollPaneStyle();
		selectBoxStyle.scrollStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle = new ListStyle();
		selectBoxStyle.listStyle.font = Assets.getInstance().getSmallFont();
		selectBoxStyle.listStyle.fontColorSelected = Color.RED;
		selectBoxStyle.listStyle.fontColorUnselected = Color.ORANGE;
		selectBoxStyle.listStyle.selection = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBox = new SelectBox<String>(selectBoxStyle);
		selectBox.setSize(150, 50);
		selectBox.setPosition(300, 300);
		addActor(selectBox);
	}
	
	public void setItems(List<String> newItems){
		String[] array = new String[newItems.size()];
		array = newItems.toArray(array);
		selectBox.setItems(array);
	}
	
	
}
