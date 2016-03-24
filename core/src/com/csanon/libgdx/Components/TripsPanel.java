package com.csanon.libgdx.Components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.csanon.libgdx.Utils.Pic;
import com.csanon.libgdx.Utils.Tint;

public class TripsPanel extends Group{
	
	 private ScrollPane scrollPane;
	    private Group rows;

	    public TripsPanel(){
	        setSize(800, 300);
	        rows = new Group();
	        rows.setSize(800, 20*100);
	        loadRows();
	        scrollPane = new ScrollPane(rows);
	        scrollPane.setFillParent(true);
	        addActor(scrollPane);
	    }


	    public void loadRows(){
	        for(int i = 0; i < 20; i++){
	            TintedImage row = new TintedImage(Pic.Pixel, Tint.GRAY);
	            row.setSize(800, 98);
	            row.setPosition(0, i*100);
	            rows.addActor(row);
	        }
	        //appendNames();

	    }
/*
	    public void appendNames(){
	        String names = "";
	        String crystalCount = "";
	        for(int i = 0; i < 19; i++){
	            names = (names+(i+1)+". Jimmy3430\n");
	            crystalCount = (crystalCount+(20-i)+"\n");
	        }
	        names = (names+(20)+". Jimmy3430");
	        crystalCount = (crystalCount+(1));

	        TextLabel textLabel = new TextLabel(names, Assets.getInstance().getMidFont(), Align.left);
	        textLabel.setPosition(10, 0);
	        LogUtils.Log(textLabel.getHeight());

	        TextLabel crystalLabel = new TextLabel(crystalCount, Assets.getInstance().getMidFont(), Align.right);
	        crystalLabel.setPosition(420, 0);

	        rows.addActor(textLabel);
	        rows.addActor(crystalLabel);

	        for(int i = 0; i < 20; i++){
	            TintedImage crystal = new TintedImage(Pic.Mega_Crystal);
	            crystal.setSize(60,50);
	            crystal.setPosition(490, i*58);
	            rows.addActor(crystal);
	        }
	    }
*/

}
