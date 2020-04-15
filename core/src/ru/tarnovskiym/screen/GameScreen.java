package ru.tarnovskiym.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.tarnovskiym.base.BaseScreen;
import ru.tarnovskiym.base.Font;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.pool.EnemyPool;
import ru.tarnovskiym.pool.ExplosionPool;
import ru.tarnovskiym.pool.HealPool;
import ru.tarnovskiym.pool.HeartPool;
import ru.tarnovskiym.pool.ParticlePool;
import ru.tarnovskiym.sprites.Background;
import ru.tarnovskiym.sprites.Bullet;
import ru.tarnovskiym.sprites.Enemy;
import ru.tarnovskiym.sprites.Galaxy;
import ru.tarnovskiym.sprites.PowerUp;
import ru.tarnovskiym.sprites.HeartHp;
import ru.tarnovskiym.sprites.botton.NewGame;
import ru.tarnovskiym.sprites.text.GameOver;
import ru.tarnovskiym.sprites.MainShip;
import ru.tarnovskiym.sprites.Star;
import ru.tarnovskiym.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    public enum State {PLAYING, PAUSE, GAME_OVER}

    private static final int STAR_COUNT = 64;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private Texture bg;
    private Texture galaxy;

    private Background background;
    private Galaxy backgroundGalaxy;

    private TextureAtlas atlas;
    private TextureAtlas atlas2;

    private Star[] stars;
    private MainShip mainShip;
    private GameOver gameOver;
    private NewGame newGame;
    private HeartHp heartHp;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ParticlePool particlePool;
    private HeartPool heartPool;
    private ExplosionPool explosionPool;
    private EnemyEmitter enemyEmitter;
    private HealPool healPool;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosion;
    private Sound apteka;
    private State state;
    private State prevState;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private int frags;

    @Override
    public void show() {
        super.show();
        galaxy = new Texture("textures/galaxy.png");
        bg = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlas2 = new TextureAtlas(Gdx.files.internal("textures/Hp/games.pack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        apteka = Gdx.audio.newSound(Gdx.files.internal("sounds/apteka.wav"));
        healPool = new HealPool(atlas2);
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosion);
        particlePool = new ParticlePool(atlas);
        heartPool = new HeartPool(atlas2);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, particlePool, healPool);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, bulletSound);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
        state = State.PLAYING;
        prevState = State.PLAYING;
        frags = 0;
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
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
        music.play();
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
        heartHp.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        galaxy.dispose();
        atlas.dispose();
        atlas2.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        healPool.dispose();
        heartPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosion.dispose();
        apteka.dispose();
        super.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
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
            heartHp = new HeartHp(atlas2, mainShip, heartPool);
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
            heartHp.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            healPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
            particlePool.update(delta);
            heartPool.update(delta);
        }

    }

    private void checkCollisions()  {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        List<PowerUp> powerUpsList = healPool.getActiveObjects();
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
                    if (enemy.isDestroyed()) {
                        frags++;
                        float velosityHeal = Rnd.nextFloat(-0.1f, -0.32f);
                        healPool.setup(enemy.pos, enemy.getV().set(0, velosityHeal), 0.04f, worldBounds, 100);
                    }
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
        for (PowerUp powerUp : powerUpsList) {
            if(!powerUp.isOutside(mainShip)){
                mainShip.heal(powerUp.getHeal());
                apteka.play();
                powerUp.destroy();
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
        particlePool.freeAllDestroyedActiveObjects();
        healPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        heartHp.draw(batch);
        backgroundGalaxy.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state) {
            case PLAYING:
                mainShip.draw(batch);
                enemyPool.drawActiveSprites(batch);
                bulletPool.drawActiveSprites(batch);
                particlePool.render(batch);
                heartPool.render(batch);
                healPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                newGame.draw(batch);
                break;
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    public void chengeStatePlay() {
        state = State.PLAYING;
        mainShip.startNewGame(worldBounds);
        frags = 0;
        bulletPool.freeAllActiveObjects();
        healPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
    }
}
