package ru.tarnovskiym.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;

public class Ship extends Sprite {

    private static final float HEIGHT = 0.09f;
    private Vector2 v;
    private Rect worldBounds;

    public Ship(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("1"));
        v = new Vector2(0, 0);

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        float posX = worldBounds.getLeft() + 0.05f;
        float posY = worldBounds.getBottom() + 0.055f;
        this.pos.set(posX, posY);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.D) && (getRight() < worldBounds.getRight())){
            v.set(0.4f, 0);
            pos.mulAdd(v, delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && (getLeft() > worldBounds.getLeft())){
            v.set(-0.4f, 0);
            pos.mulAdd(v, delta);
        }
    }
}
