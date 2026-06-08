package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen
{
    final Drop game;

    public MainMenuScreen(Drop game)
    {
        this.game = game;
    }

    @Override
    public void show() {

    }

    private void input()
    {
        if(Gdx.input.isTouched())
        {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void logic()
    {

    }

    private void draw()
    {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.font.draw(game.batch, "welcome to drop", 1, 1.5f);
        game.font.draw(game.batch, "tap anywhere to begin", 1, 1);

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

        game.viewport.update(width, height, true);
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

    }

}
