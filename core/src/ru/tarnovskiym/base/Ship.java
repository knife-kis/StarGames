package ru.tarnovskiym.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.sprites.Bullet;

public abstract class Ship extends Sprite {

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected float bulletHeight;
    protected int damage;
    protected Sound shootSound;
    protected int hp;

    protected Vector2 v0;
    protected Vector2 v;

    protected float reloadInterval;
    protected float reloadTimer;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) throws GameException {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            try {
                shoot();
            } catch (GameException e) {
                e.printStackTrace();
            }
        }
    }

    private void shoot() throws GameException {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play();
    }



}
