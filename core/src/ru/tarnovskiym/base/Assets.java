package ru.tarnovskiym.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Assets {
    private Sound soundBullet;
    private Sound soundExplosion;
    private Sound soundLaser;
    private Sound soundMusic;

    public Assets() {
        soundBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        soundLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        soundMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/music.mp3"));
    }

    public Sound getSoundBullet() {
        return soundBullet;
    }

    public Sound getSoundExplosion() {
        return soundExplosion;
    }

    public Sound getSoundLaser() {
        return soundLaser;
    }

    public Sound getSoundMusic() {
        return soundMusic;
    }
}
