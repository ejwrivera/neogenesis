package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;

/**
 * The Interface Collidable.  Used for collision; separate from consumable, which extends it
 */
public interface Collidable extends Identifiable {
	
	/**
	 * Collides with.
	 * @param other The other collidable's bounding box
	 * @return returns true if there is an overlap between this collidable's bounding box and the passed in box
	 */
	public Boolean collidesWith(Circle other);
}
