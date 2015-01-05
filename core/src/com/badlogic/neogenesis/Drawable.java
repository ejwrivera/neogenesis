package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Interface Drawable.  For rendering only.
 */
public interface Drawable extends Identifiable{
	
	/**
	 * Gets the bounding rectangle.
	 * @return the rectangle
	 */
	public Rectangle getRectangle();
	
	/**
	 * Gets the texture.
	 * @return the texture
	 */
	public Texture getTexture();
}
