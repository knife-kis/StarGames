package ru.tarnovskiym.pool;

import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.sprites.Bullet;

public class BulletPoolEnemy extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
