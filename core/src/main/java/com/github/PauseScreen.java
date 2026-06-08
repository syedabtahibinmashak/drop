package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PauseScreen implements Screen {

    final Drop game;
    final GameScreen gameScreen;

    public PauseScreen(Drop game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {

    }

    private void input()
    {
        if(Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            game.setScreen(gameScreen);
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

        game.font.draw(game.batch, "game paused", 1, 1.5f);
        game.font.draw(game.batch, "press escape or tap anywhere to resume", 1, 1);

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
