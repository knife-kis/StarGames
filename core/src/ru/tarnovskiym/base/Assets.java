package ru.tarnovskiym.base;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tarnovskiym.screen.GameScreen;

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {return ourInstance;   }
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() { assetManager = new AssetManager(); }


    public void loadAssets(GameScreen.State type) {
        switch (type) {
            case PLAYING:
                assetManager.load("textures/mainAtlas.tpack", TextureAtlas.class);

//                createStandardFont(32);
                assetManager.finishLoading();
                textureAtlas = assetManager.get("textures/mainAtlas.tpack", TextureAtlas.class);
                break;
        }
    }

//    private void createStandardFont(int size) {
//        FileHandleResolver resolver = new InternalFileHandleResolver();
//        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
//        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
//        fontParameter.fontFileName = "fonts/Roboto-Medium.ttf";
//        fontParameter.fontParameters.size = size;
//        fontParameter.fontParameters.color = Color.WHITE;
//        fontParameter.fontParameters.shadowOffsetX = 1;
//        fontParameter.fontParameters.shadowOffsetY = 1;
//        fontParameter.fontParameters.shadowColor = Color.DARK_GRAY;
//        assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
//    }

    public void clear() {
        assetManager.clear();
    }


//    private Sound soundBullet;
//    private Sound soundExplosion;
//    private Sound soundLaser;

//    private Sound soundMusic;
//    public Assets() {
//        soundBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
//        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
//        soundLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
//        soundMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/music.mp3"));

//    }

}
