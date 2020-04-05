package ru.tarnovskiym.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.BaseScreen;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.sprites.Background;
import ru.tarnovskiym.sprites.CompShip;
import ru.tarnovskiym.sprites.MainShip;
import ru.tarnovskiym.sprites.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private BulletPool bulletPoolHero;
    private BulletPool bulletPoolComp;
//    private ShipPool shipPool;
    private MainShip mainShip;
    private CompShip compShip;
//    private Sound soundMusic;
//    private Assets assets = new Assets();



    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPoolHero = new BulletPool();
        bulletPoolComp = new BulletPool();
//        shipPool = new ShipPool(atlas, bulletPoolComp);
        initSprites();
//        playMusic();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        compShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPoolHero.dispose();
        bulletPoolComp.dispose();
//        soundMusic.dispose();
        super.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }
    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        mainShip.update(delta);
        compShip.update(delta);
        bulletPoolHero.updateActiveSprites(delta);
        bulletPoolComp.updateActiveSprites(delta);
    }

//    private void playMusic() {
//        soundMusic = assets.getSoundMusic();
//        long id = soundMusic.play();
//        soundMusic.setLooping(id, true);
//    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
            mainShip = new MainShip(atlas, bulletPoolHero);
            compShip = new CompShip(atlas, bulletPoolComp);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    public void freeAllDestroyed() {
        bulletPoolHero.freeAllDestroyedActiveObjects();
        bulletPoolComp.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        mainShip.draw(batch);
        compShip.draw(batch);
        bulletPoolHero.drawActiveSprites(batch);
        bulletPoolComp.drawActiveSprites(batch);
        batch.end();
    }
}
