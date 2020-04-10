package ru.tarnovskiym.sprites.text;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;

public class GameOver extends Sprite {

    public GameOver(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setTop(0.1f);
    }
}
