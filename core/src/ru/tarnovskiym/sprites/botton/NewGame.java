package ru.tarnovskiym.sprites.botton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.tarnovskiym.base.ScaledButton;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.math.Rect;
import ru.tarnovskiym.screen.GameScreen;

public class NewGame extends ScaledButton {

    GameScreen gs;

    public NewGame(TextureAtlas atlas, GameScreen gs) throws GameException {
        super(atlas.findRegion("button_new_game"));
        this.gs = gs;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(0);
    }

    @Override
    public void action() {
        gs.chengeStatePlay();
    }
}
