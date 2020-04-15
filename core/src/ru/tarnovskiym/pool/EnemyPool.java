package ru.tarnovskiym.pool;

import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.sprites.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {

    private ParticlePool particlePool;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Rect worldBounds;
    private HealPool healPool;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, ParticlePool particlePool, HealPool healPool) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.particlePool = particlePool;
        this.healPool = healPool;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, explosionPool, worldBounds, particlePool, healPool);
    }
}
