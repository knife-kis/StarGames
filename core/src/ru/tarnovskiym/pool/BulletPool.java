package ru.tarnovskiym.pool;

import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    private int type;

    public BulletPool(int type) {
        this.type = type;
    }

    @Override
    protected Bullet newObject() {
        return new Bullet(type);
    }
}
