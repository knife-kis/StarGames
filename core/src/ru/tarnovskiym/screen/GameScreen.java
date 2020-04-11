package ru.tarnovskiym.screen;

import com.badlogic.gdx.Game;
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
import ru.tarnovskiym.pool.ExplosionPool;
import ru.tarnovskiym.sprites.Background;
import ru.tarnovskiym.sprites.Bullet;
import ru.tarnovskiym.sprites.Enemy;
import ru.tarnovskiym.sprites.Galaxy;
import ru.tarnovskiym.sprites.botton.NewGame;
import ru.tarnovskiym.sprites.text.GameOver;
import ru.tarnovskiym.sprites.MainShip;
import ru.tarnovskiym.sprites.Star;
import ru.tarnovskiym.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private enum State {PLAYING, PAUSE, GAME_OVER;}
    private static final int STAR_COUNT = 128;

    private Texture bg;
    private Texture galaxy;

    private Background background;
    private Galaxy backgroundGalaxy;

    private TextureAtlas atlas;
    private Star[] stars;

    private MainShip mainShip;

    private GameOver gameOver;
    private NewGame newGame;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private ExplosionPool explosionPool;
    private EnemyEmitter enemyEmitter;
    private Music music;
    private Sound laserSound;

    private Sound bulletSound;
    private Sound explosion;
    private State state;
    @Override
    public void show() {
        super.show();
        galaxy = new Texture("textures/galaxy.png");
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosion);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
        state = State.PLAYING;
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
        backgroundGalaxy.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        galaxy.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosion.dispose();
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
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer, button);
        }
        if (state == State.GAME_OVER){
            newGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer, button);
        }
        if (state == State.GAME_OVER){
            newGame.touchUp(touch, pointer, button);
        }
        return false;
    }
    private void initSprites() {
        try {
            background = new Background(bg);
            backgroundGalaxy = new Galaxy(galaxy);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
            mainShip = new MainShip(atlas, bulletPool, explosionPool, laserSound);
            gameOver = new GameOver(atlas);
            newGame = new NewGame(atlas, this);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta){
        for (Star star : stars) {
            star.update(delta);
        }
        backgroundGalaxy.update(delta);
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
        }

    }

    private void checkCollisions()  {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (mainShip.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
                mainShip.damage(enemy.getDamage());
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        backgroundGalaxy.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state) {
            case PLAYING:
                mainShip.draw(batch);
                enemyPool.drawActiveSprites(batch);
                bulletPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                newGame.draw(batch);
                break;
        }
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }

    public void chengeStatePlay() {
        enemyPool.dispose();
        mainShip.setHp(10);
        mainShip.life();
        bulletPool.dispose();
        state = State.PLAYING;
    }
}
