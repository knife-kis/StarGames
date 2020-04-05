package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.pool.BulletPool;

public class CompShip extends Sprite {
    private static final float SHIP_HEIGHT = 0.15f;
    private static final float TOP_MARGIN = 0.16f;

    private Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private Vector2 v;


    public CompShip(TextureAtlas atlas, BulletPool bulletPool) throws GameException {
        super(atlas.findRegion("enemy0"), 1,2,2);
        this.bulletPool = bulletPool;
        bulletRegion = atlas.findRegion("bulletEnemy");
        bulletV = new Vector2(0, 0.5f);
        v = new Vector2(0.1f, 0);
    }


    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if(isOutside(worldBounds)){
            destroy();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getTop() - TOP_MARGIN);
        setLeft(worldBounds.getLeft() - 0.18f);
    }
    public void shoot() throws GameException {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, 0.01f, worldBounds, 1);
    }

}
