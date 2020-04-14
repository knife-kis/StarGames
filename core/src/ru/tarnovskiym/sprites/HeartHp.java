package ru.tarnovskiym.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.screen.GameScreen;

public class HeartHp extends Sprite {

    private GameScreen gs;
    private Texture texture;

    public HeartHp(GameScreen gs) {
//        super("textures/main/heart.png", 1, 1, 1);
        this.gs = gs;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.02f);
        setRight(worldBounds.getRight() - 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
