package com.badlogic.neogenesis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * The Class Neogenesis.
 */
public class Neogenesis extends Game {

	/** The batch. */
	SpriteBatch batch;
	/** The font. */
	BitmapFont font;
	/** The shader */
	public ShaderProgram shader;
	/** The shader attributes */
	private ShaderAttributes shaderAttributes;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	public void create() {
		// shader init
		shaderAttributes = new ShaderAttributes();
		shader = shaderAttributes.shader;
			
		//batch = new SpriteBatch();
		batch = new SpriteBatch(1000, shader);
		batch.setShader(shader);
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

	public void shaderResize(int width, int height) {
		shader.begin();
		shader.setUniformf("Resolution", width, height);
		shader.end();
	}

}