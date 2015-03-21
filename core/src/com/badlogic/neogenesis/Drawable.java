package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;

/**
 * The Drawable Interface.  For rendering only.
 */
public interface Drawable extends Identifiable{
	
	/**
	 * gets its position and size
	 * @return the circle
	 */
	public Shape2D getShape();
	
	/**
	 * Gets the texture.
	 * @return the texture
	 */
	public Texture getTexture();
}
