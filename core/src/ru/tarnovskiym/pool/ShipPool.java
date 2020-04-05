package ru.tarnovskiym.pool;

import ru.tarnovskiym.base.SpritesPool;
import ru.tarnovskiym.exception.GameException;
import ru.tarnovskiym.sprites.CompShip;

public class ShipPool extends SpritesPool<CompShip> {
    @Override
    protected CompShip newObject() throws GameException {
        return new CompShip();
    }
}
