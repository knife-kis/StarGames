package ru.tarnovskiym.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Assets;
import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;

public class MainShip extends Sprite {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;

    private Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private Sound soundBullet;
    private Assets assets;

    private final Vector2 v0;
    private final Vector2 v;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        v0 = new Vector2(0.5f, 0);
        v = new Vector2();
        assets = new Assets();
        soundBullet = assets.getSoundBullet();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        mowing(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        if ((keycode == Input.Keys.A) || (keycode == Input.Keys.LEFT)) {
            pressedLeft = true;
            moveLeft();
        }
        if ((keycode == Input.Keys.D) || (keycode == Input.Keys.RIGHT)) {
            pressedRight = true;
            moveRight();
        }
        if (keycode == Input.Keys.UP) {
            shoot();
            soundBullet.play(0.2f);

        }
        return false;
    }

    public boolean keyUp(int keycode) {
        if ((keycode == Input.Keys.A) || (keycode == Input.Keys.LEFT)) {
            pressedLeft = false;
            if (pressedRight) {
                moveRight();
            } else {
                stop();
            }
        }
        if ((keycode == Input.Keys.D) || (keycode == Input.Keys.RIGHT)) {
            pressedRight = false;
            if (pressedLeft) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public void shoot() {
        Bullet bullet = null;
        try {
            bullet = bulletPool.obtain();
        } catch (GameException e) {
            e.printStackTrace();
        }
        bullet.set(this, bulletRegion, pos, bulletV, 0.01f, worldBounds, 1);

    }

    private void mowing(Rect worldBounds) {
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }
}
