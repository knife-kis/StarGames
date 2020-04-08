package ru.tarnovskiym.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private final Vector2 v = new Vector2();
    private int damage;
    private Object owner;
    private int type;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public Bullet(int type) {
        regions = new TextureRegion[1];
        this.type = type;
    }

    public void set(
            Object owner,
            TextureRegion region,
            Vector2 pos0,
            Vector2 v0,
            float height,
            Rect worldBounds,
            int damage,
            int type
    ) {
       this.owner = owner;
       this.regions[0] = region;
       this.pos.set(pos0);
       this.v.set(v0);
       setHeightProportion(height);
       this.worldBounds = worldBounds;
       this.damage = damage;
       this.type = type;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Object getOwner() {
        return owner;
    }
}
