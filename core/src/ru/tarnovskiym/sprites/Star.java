package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.math.Rnd;

public class Star extends Sprite {

    private static final float HEIGHT = 0.01f;

    private Vector2 v;
    private Rect worldBounds;
    private float scale;

    private float animateInterval = 0.5f;
    private float animateTimer;

    public Star(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("star"));
        float vx = Rnd.nextFloat(-0.005f, 0.005f);
        float vy = Rnd.nextFloat(-0.008f, -0.1f);
        v = new Vector2(vx, vy);
        animateTimer = Rnd.nextFloat(0, 0.5f);
        this.scale =  vy / 9.5f;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(scale);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        this.pos.set(posX, posY);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        scale += 0.01f;
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0;
            scale = 1;
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
            pos.x = Rnd.nextFloat(-0.35f, 0.35f);
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
    }
}
