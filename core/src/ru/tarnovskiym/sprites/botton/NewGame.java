package ru.tarnovskiym.sprites.botton;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tarnovskiym.base.ScaledButton;
import ru.tarnovskiym.base.Sprite;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;

public class NewGame extends ScaledButton {

    public NewGame(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("button_new_game"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(0);
    }

    @Override
    public void action() {

    }
}
