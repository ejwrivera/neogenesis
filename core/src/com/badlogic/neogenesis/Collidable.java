package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Interface Collidable.  Used for collision; separate from consumable, which extends it
 */
public interface Collidable extends Identifiable {
	
	/**
	 * Collides with.
	 * @param other The other collidable's bounding box
	 * @return returns true if there is an overlap between this collidable's bounding box and the passed in box
	 */
	public Boolean collidesWith(Collidable other);
	public Array<Collidable> collidesWith (Array<Collidable> otherCollidables);
	public Shape2D getShape();
	public Vector2 getPosition();
	public int getMagnitude();
	public boolean stillCollidable();
	
	public void collidedWith(Collidable collidable);
	public void collidedWith(Consumer consumer);
	public void collidedWith(Consumable consumable);
	public void collidedWith(Rock rock);
	
}
