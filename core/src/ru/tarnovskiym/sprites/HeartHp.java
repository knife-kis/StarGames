package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;
import ru.tarnovskiym.pool.HeartPool;

public class HeartHp extends Sprite {

    private MainShip mainShip;
    private HeartPool heartPool;


    public HeartHp(TextureAtlas atlas, MainShip mainShip, HeartPool heartPool) throws GameException {
        super(atlas.findRegion("14heart"), 1, 1, 1);
        this.mainShip = mainShip;
        this.heartPool = heartPool;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < 1; i++) {
            heartPool.setup(
                    Rnd.nextFloat(getLeft() + 0.02f , getRight() - 0.02f), getTop() - 0.025f,
                    Rnd.nextFloat(-0.0005f, 0.0005f),  Rnd.nextFloat(0.02f, 0.06f),
                    0.8f,
                    0.00005f, 0.0002f,
                    0.9f, 0.5f, 0.4f, 1.0f,
                    0.3f, 0.9f, 1.0f, 0.1f
            );
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setRight(worldBounds.getLeft() + 0.07f);
        setBottom(worldBounds.getBottom() + 0.01f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        scale = mainShip.getHp() / 100;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
