package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Collidable interface.  Used for collision.
 */
public interface ICollidable {
	
	/**
	 * Determines collision
	 * @param other The other collidable
	 * @return returns true if there is an overlap between this collidable's bounding box and the passed in box
	 */
	public Boolean collidesWith(GameObject other);
	
	/**
	 * Determines collision in bulk
	 * @param otherCollidables the other collidables
	 * @return a list of all the collidables this collidable is colliding with, expected to utilize the boolean collidesWith method
	 */
	public Array<GameObject> collidesWith (Array<GameObject> otherCollidables);
	
	public Vector2 getForce();
	
	/**
	 * Gets the bounding shape to determine collision.
	 * @return the shape
	 */
	public Shape2D getShape();
	
	/**
	 * Returns an arbitrary point somewhere within the bounding shape, used for tracking
	 * @return the position
	 */
	public Vector2 getPosition();
	
	/**
	 * Determines whether collision is possible
	 * @return true, if this thing should still be colliding with other things
	 */
	public boolean stillCollidable();
	
	/**
	 * Invoked from the colliding collidable to allow what it collided with an opportunity to respond.  For implementing Visitor pattern.
	 * @param collidable this 
	 */
	public void collidedWith(ICollidable collidable);
	
	/**
	 * Invoked from the colliding collidable if it is an obstruction to allow what it collided with an opportunity to respond.  For implementing Visitor pattern.
	 *
	 * @param rock this
	 */
	
	public void collidedWith(GameObject object);

	public void collidedWith(Rock rock);
	
	public void collidedWith(Devourer devourer);

	public void collidedWith(Devourable devourable);
	
	public boolean collidesWith(ICollidable collidable);
	
}
