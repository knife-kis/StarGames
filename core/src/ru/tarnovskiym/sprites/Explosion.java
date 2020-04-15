package ru.tarnovskiym.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;

public class Explosion extends Sprite {

    private static final float ANIMATE_INTERVAL = 0.002f;

    private float animateTimer;

    private Sound explosionSound;

    public Explosion(TextureAtlas atlas, Sound explosionSound) throws GameException {
        super(atlas.findRegion("explosion"), 9, 9, 74);
        this.explosionSound = explosionSound;
    }

    public void set(Vector2 pos, float height) {
        this.pos.set(pos);
        setHeightProportion(height);
        explosionSound.play(0.3f);
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer >= ANIMATE_INTERVAL) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
