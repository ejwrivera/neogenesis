package com.badlogic.neogenesis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The Class Neogenesis.
 */
public class Neogenesis extends Game {

	/** The batch. */
	SpriteBatch batch;
	/** The font. */
	BitmapFont font;

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
	public void render() {
		super.render();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}