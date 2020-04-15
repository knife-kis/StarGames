package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.math.Rect;

public class PowerUp extends Sprite {
    public enum Type {
        MEDKIT(0), MONEY(1);

        int index;

        Type(int index) {
            this.index = index;
        }
    }

    private Rect worldBounds;
    private final Vector2 v = new Vector2();
    private float heal;
    private Type type;

    public PowerUp()  {
        regions = new TextureRegion[1];
    }
    public void set(
            Type type,
            TextureRegion region,
            Vector2 pos0,
            Vector2 v0,
            float height,
            Rect worldBounds,
            float heal
    ) {
        this.type = type;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.heal = heal;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        angle += delta * 80;
        if (isOutside(worldBounds)) {
            destroy();
        }
    }


    public float getHeal() {
        return heal;
    }

}
