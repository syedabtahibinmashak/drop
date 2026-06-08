package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final Drop game;

    Texture backgroundTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Music music;

    Sprite bucketSprite;

    Vector2 touchPosition;

    Array<Sprite> dropSprites;

    float dropTimer;

    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    int dropsGathered;

    public GameScreen(Drop game) {

        this.game = game;

        // load the game assets
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.25f);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        touchPosition = new Vector2();

        dropSprites = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
    }


    @Override
    public void show() {
        // play the bgm when the screen is shown
        music.play();
    }

    private void input()
    {
        float delta = Gdx.graphics.getDeltaTime();
        float speed = delta * 5f;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            bucketSprite.translateX(speed);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            bucketSprite.translateX(-speed);
        }

        if(Gdx.input.isTouched())
        {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPosition);
            bucketSprite.setCenterX(touchPosition.x);
        }
    }

    private void logic()
    {
        float delta = Gdx.graphics.getDeltaTime();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        dropTimer += delta;
        if(dropTimer >= 1f)
        {
            dropTimer = 0;
            createDroplet();
        }

        for(int i = dropSprites.size - 1; i >= 0; i--)
        {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-3f * delta);
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if(dropSprite.getY() < - dropHeight) dropSprites.removeIndex(i);

            if(bucketRectangle.overlaps(dropRectangle))
            {
                dropsGathered++;
                dropSprites.removeIndex(i);
                dropSound.play();
            }
        }
    }

    private void createDroplet()
    {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float dropWidth = 1;
        float dropHeight = 1;

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);

        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth));
        dropSprite.setY(worldHeight);

        dropSprites.add(dropSprite);
    }

    private void draw()
    {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();

        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        bucketSprite.draw(game.batch);

        game.font.draw(game.batch, "drops collected: " + dropsGathered, 0, worldHeight);

        for(Sprite dropSprite : dropSprites)
        {
            dropSprite.draw(game.batch);
        }

        game.batch.end();
    }

    @Override
    public void render(float delta) {

        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {

        game.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        backgroundTexture.dispose();
        bucketTexture.dispose();
        dropTexture.dispose();
        dropSound.dispose();
        music.dispose();
    }
}
