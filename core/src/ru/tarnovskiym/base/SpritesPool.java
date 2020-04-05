package ru.tarnovskiym.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import ru.tarnovskiym.exception.GameException;

public abstract class SpritesPool<T extends Sprite> {

    protected final List<T> activeObjects = new ArrayList<>();
    protected final List<T> freeObjects = new ArrayList<>();

    protected abstract T newObject() throws GameException;

    public T obtain() throws GameException {
        if (freeObjects.isEmpty()) {
            freeObjects.add(newObject());
        }
        T object = freeObjects.remove(freeObjects.size() - 1);
        activeObjects.add(object);
        System.out.println(this.getClass().getName() + " active/free: " + activeObjects.size() + "/" + freeObjects.size());
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for (Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = activeObjects.size() - 1; i >= 0; i--) {
            T sprite = activeObjects.get(i);
            if (activeObjects.get(i).isDestroyed()) {
                free(i);
                sprite.flushDestroy();
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    private void free(int index) {
        freeObjects.add(activeObjects.remove(index));
        System.out.println(this.getClass().getName() + " active/free: " + activeObjects.size() + "/" + freeObjects.size());
    }
}
