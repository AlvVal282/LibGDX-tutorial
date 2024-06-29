package com.mazeproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import screen.MainMenuScreen;
import tools.ScrollingBackground;

public class MazeProject extends Game {
	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;

	private OrthographicCamera cam;
	private StretchViewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.scrollingBackground.resize(width, height);
	}
}
