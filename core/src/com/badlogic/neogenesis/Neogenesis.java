package com.badlogic.neogenesis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * The Neogenesis game class.
 */
public class Neogenesis extends Game {

	/** The batch. */
	SpriteBatch batch;
	/** The font. */
	BitmapFont font;
	
	/**  The shader. */
	public ShaderProgram shader;
	
	/**  The shader attributes. */
	private ShaderAttributes shaderAttributes;
	
	/** The save manager. */
	public SaveManager saveManager;
	
	/** The shader on. */
	private boolean shaderOn;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	public void create() {
		// shader init
		shaderAttributes = new ShaderAttributes();
		shader = shaderAttributes.shader;
			
		shaderOn = false;
		batch = new SpriteBatch();
		
		// Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		saveManager = new SaveManager(false);
	}

	/**
	 * Toggle shader.
	 */
	public void toggleShader(){
		if (shaderOn){
			shaderOn = false;
			batch = new SpriteBatch();
		}
		else{
			shaderOn = true;
			batch = new SpriteBatch(1000, shader);
			batch.setShader(shader);
		}
			
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

	/**
	 * Shader resize.
	 * @param width the width
	 * @param height the height
	 */
	public void shaderResize(int width, int height) {
		shader.begin();
		shader.setUniformf("Resolution", width, height);
		shader.end();
	}

}