package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;

public class Background extends Sprite {

    float worldSize;

    public Background(Texture texture, float worldSize) throws GameException {
        super(new TextureRegion(texture));
        this.worldSize = worldSize;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldSize);
        pos.set(worldBounds.pos);
    }
}
