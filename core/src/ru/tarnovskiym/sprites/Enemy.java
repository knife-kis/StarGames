package ru.tarnovskiym.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Ship;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;
import ru.tarnovskiym.pool.BulletPool;
import ru.tarnovskiym.pool.ExplosionPool;
import ru.tarnovskiym.pool.HealPool;
import ru.tarnovskiym.pool.ParticlePool;

public class Enemy extends Ship {

    private ParticlePool particlePool;
    private HealPool healPool;

    private final Vector2 descentV;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, ParticlePool particlePool, HealPool healPool) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.particlePool = particlePool;
        this.healPool = healPool;
        v = new Vector2();
        v0 = new Vector2();
        objectV = new Vector2();
        objectPos = new Vector2();
        descentV = new Vector2(0, -0.3f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        objectPos.set(pos.x, pos.y - getHalfHeight());
        if (getTop() <= worldBounds.getTop()) {
            v.set(v0);
            autoShoot(delta);
            fireShow();

        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    private void fireShow() {
        for (int i = 0; i < 5; i++) {
            particlePool.setup(
                    pos.x, getTop(),
                    0, v.y * 2.0f + Rnd.nextFloat(0.2f, 4.6f),
                    0.04f,
                    0.0001f, 0.002f,
                    0.7f, 0.5f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 0.1f
            );
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            Sound shootSound,
            int hp,
            float height) {
        this.regions = regions;
        this.v0.set(v0);
        this.objectRegion = bulletRegion;
        this.objectHeight = bulletHeight;
        this.objectV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(descentV);
        setHeightProportion(height);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y);
    }
}
