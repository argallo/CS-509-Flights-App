package com.csanon.libgdx.Components;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.csanon.libgdx.Utils.Assets;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

public class DropDown extends Group{

	private SelectBox<String> selectBox;
	private SelectBoxStyle selectBoxStyle;
	
	public DropDown(){
		selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.fontColor = Tint.BACKGROUND_COLOR;
		selectBoxStyle.background = Assets.getInstance().getDrawable(Pic.Dropdown_Icon);
		selectBoxStyle.font = Assets.getInstance().getXSmallFont();
		selectBoxStyle.scrollStyle = new ScrollPaneStyle();
		selectBoxStyle.scrollStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle = new ListStyle();
		selectBoxStyle.listStyle.font = Assets.getInstance().getXSmallFont();
		selectBoxStyle.listStyle.fontColorSelected = Color.RED;
		selectBoxStyle.listStyle.fontColorUnselected = Color.ORANGE;
		selectBoxStyle.listStyle.selection = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBoxStyle.listStyle.background = Assets.getInstance().getDrawable(Pic.Pixel);
		selectBox = new SelectBox<String>(selectBoxStyle);
		addActor(selectBox);
	}
	
	public void setItems(List<String> newItems){
		String[] array = new String[newItems.size()];
		array = newItems.toArray(array);
		selectBox.setItems(array);
	}
	
	public void setPosition(float x, float y){
		selectBox.setPosition(x, y);
	}
	
	public void pack(){
		selectBox.setSize(selectBox.getPrefWidth()+100, selectBox.getPrefHeight());
		System.out.println(selectBox.getWidth()+" "+selectBox.getHeight());
		selectBox.validate();
	}

	public String getCurrentItem(){
		return selectBox.getSelected();
	}
	
	public void setSelected(String item){
		selectBox.setSelected(item);
	}
	
}
