package com.csanon.libgdx.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Gallo on 3/19/2016.
 */
public class Assets extends AssetManager {

   // private TextureAtlas textureAtlas;

    //Font vars
    private BitmapFont xsmallFont;
    private BitmapFont smallFont;
    private BitmapFont midFont;
    private BitmapFont largeFont;
    private FreeTypeFontGenerator.FreeTypeFontParameter font;
    private FreeTypeFontGenerator generator;

    private TextureLoader.TextureParameter textureParam;


    private static final Assets INSTANCE = new Assets();

    public static Assets getInstance() {
        return INSTANCE;
    }

    public Assets() {
        //maybe someday ill find a use for anything but linear filters...
        textureParam = new TextureLoader.TextureParameter();
        textureParam.minFilter = Texture.TextureFilter.Linear;
        textureParam.magFilter = Texture.TextureFilter.Linear;
        createFonts();

    }

    /**
     * Creates the different fonts that will be used in the application
     */
    public void createFonts(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/arial.ttf"));
        font = new FreeTypeFontGenerator.FreeTypeFontParameter();
        font.magFilter = textureParam.magFilter;
        font.minFilter = textureParam.minFilter;
        font.size = 20;
        xsmallFont = generator.generateFont(font);
        xsmallFont.getRegion().getTexture().setFilter(textureParam.minFilter, textureParam.magFilter);
        font.size = 35;
        smallFont = generator.generateFont(font);
        smallFont.getRegion().getTexture().setFilter(textureParam.minFilter, textureParam.magFilter);
        font.size = 50;
        midFont = generator.generateFont(font);
        midFont.getRegion().getTexture().setFilter(textureParam.minFilter, textureParam.magFilter);
        font.size = 100;
        largeFont = generator.generateFont(font);
        largeFont.getRegion().getTexture().setFilter(textureParam.minFilter, textureParam.magFilter);
    }

    /**
     * Splash background image will not be in the texture atlas that way we can show this image
     * while we load the texture atlas images.
     * @return the texture region containing the splash texture
     */
    public TextureRegion getSplash(){
        load(Pic.Splash, Texture.class, textureParam);
        finishLoading();
        return new TextureRegion(get(Pic.Splash,Texture.class));
    }

    /**
     * Splash image is only needed during the splash loading screen and will not be used again
     * so we can dispose of it to free up space.
     */
    public void disposeSplash(){
        unload(Pic.Splash);
    }

    /**
     * loads assets that will stay loaded for all screens
     */
    public void loadCommonAssets() {
        //load("Appimages/textureatlas.atlas", TextureAtlas.class);
    	load(Pic.Pixel, Texture.class, textureParam);
    	load(Pic.Dropdown_Icon, Texture.class, textureParam);
    	load(Pic.Curve_rectangle, Texture.class, textureParam);
    	load(Pic.Black, Texture.class, textureParam);
    	load(Pic.Search_Icon, Texture.class, textureParam);
    	load(Pic.Blank_Popup, Texture.class, textureParam);
    	load(Pic.Check_Empty, Texture.class, textureParam);
    	load(Pic.Check_Mark, Texture.class, textureParam);
    	load(Pic.Radio_BTN, Texture.class, textureParam);
    	load(Pic.Radio_BTN_Selected, Texture.class, textureParam);
        finishLoading();
    }

    /**
     * getTextureRegion: uses the textures file path as the id so that it can
     * create a texture region of the image to return
     *
     * @param textureID The id of the texture
     * @return TextureRegion the texture found in assets
     */
    public TextureRegion getTextureRegion(String textureID) {
        if(textureID.endsWith(".png")){
            return new TextureRegion(get(textureID, Texture.class));
        } else {
            return get("Appimages/textureatlas.atlas", TextureAtlas.class).findRegion(textureID);
        }
    }
    
    public Drawable getDrawable(String textureID){
    	return new TextureRegionDrawable(getTextureRegion(textureID));
    }

    public BitmapFont getXSmallFont() {
        return xsmallFont;
    }
    public BitmapFont getSmallFont() {
        return smallFont;
    }
    public BitmapFont getMidFont() {
        return midFont;
    }
    public BitmapFont getLargeFont() {
        return largeFont;
    }


}
