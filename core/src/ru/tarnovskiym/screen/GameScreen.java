package ru.tarnovskiym.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.tarnovskiym.base.BaseScreen;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.pool.EnemyPool;
import ru.tarnovskiym.sprites.Background;
import ru.tarnovskiym.sprites.Bullet;
import ru.tarnovskiym.sprites.Enemy;
import ru.tarnovskiym.sprites.MainShip;
import ru.tarnovskiym.sprites.Star;
import ru.tarnovskiym.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private BulletPool bulletPoolEnemy;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;
    private MainShip mainShip;
    private Music music;
    private Sound bulletSound;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPoolEnemy = new BulletPool(1);
        bulletPool = new BulletPool(0);
        enemyPool = new EnemyPool(bulletPoolEnemy, worldBounds);
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
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
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPoolEnemy.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        mainShip.disposeMusik();
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
        bulletPoolEnemy.updateActiveSprites(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        try {
            enemyEmitter.generate(delta);
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    private void checkCollisions() {
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            for (Bullet bullet : bulletList) {
                if (enemy.isDestroyed()) {
                    continue;
                }
                float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
                if (mainShip.pos.dst(enemy.pos) < minDist) {
                    enemy.destroy();
                }
                minDist = enemy.getHalfWidth();
                if (enemy.pos.dst(bullet.pos) < minDist) {
                    enemy.dmg();
                    bullet.destroy();
                }
            }
        }
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
            mainShip = new MainShip(atlas, bulletPool);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        bulletPoolEnemy.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
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
        bulletPoolEnemy.drawActiveSprites(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }
}
