package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;

public class Galaxy extends Sprite {

    private Vector2 v;

    public Galaxy(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        v = new Vector2(-0.001f, -0.003f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.4f);
        pos.set(worldBounds.pos);
        pos.set(worldBounds.getTop() - 0.32f, worldBounds.getRight() - 0.1f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(1,1,1,0.4f);
        super.draw(batch);
        batch.setColor(1,1,1,1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v,delta);
    }
}
