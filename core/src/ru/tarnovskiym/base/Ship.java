package ru.tarnovskiym.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.pool.ExplosionPool;
import ru.tarnovskiym.sprites.Bullet;
import ru.tarnovskiym.sprites.Explosion;

public abstract class Ship extends Sprite {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private static final float DELTA_COEFF = 1.2f;
    private float savedDelta = 0f;

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion objectRegion;
    protected Vector2 objectV;
    protected Vector2 objectPos;
    protected float objectHeight;
    protected int damage;
    protected Sound shootSound;
    protected float hp;

    protected Vector2 v0;

    protected Vector2 v;
    protected float reloadInterval;

    protected float reloadTimer;
    protected float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;
    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) throws GameException {
        super(region, rows, cols, frames);
    }

    public Vector2 getV() {
        return v;
    }

    @Override
    public void update(float delta) {
        if (savedDelta == 0f) {
            savedDelta = delta;
        }
        if (delta > savedDelta*DELTA_COEFF) {
            delta = savedDelta;
        }
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
        boom();
    }

    public float getHp() {
        return hp;
    }



    public void damage(int damage) {
        damageAnimateTimer = 0f;
        frame = 1;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    protected void autoShoot(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
    }

    private void shoot() {
        Bullet bullet = bulletPool.getActiveElement();
        bullet.set(this, objectRegion, objectPos, objectV, objectHeight, worldBounds, damage);
        shootSound.play();
    }

    private void boom() {
        Explosion explosion = explosionPool.getActiveElement();
        explosion.set(pos, getHeight());
    }



}
