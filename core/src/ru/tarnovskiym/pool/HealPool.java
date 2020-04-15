package ru.tarnovskiym.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;
import ru.tarnovskiym.sprites.PowerUp;

public class HealPool extends SpritesPool<PowerUp> {

    private TextureRegion region;


    @Override
    protected PowerUp newObject() {
        return new PowerUp();
    }

    public HealPool(TextureAtlas atlas) {
        this.region = atlas.findRegion("heal");
    }

    public void setup(Vector2 pos, Vector2 v, float height, Rect worldBounds, float probability){
        if (Rnd.nextFloat(0, 100) <= probability){
            getActiveElement().set(PowerUp.Type.MEDKIT, region, pos, v, height, worldBounds, 10);
        }
    }

}
