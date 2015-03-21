package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Class Sense.
 */
public class Sense implements Collidable, Destructible {

	/** Whether the sense is destroyed. */
	boolean destroyed;
	
	/** The position. */
	Vector2 position;
	
	/** The size. */
	int size;
	
	/** The sensed prey. */
	Consumable sensed;
	
	/**
	 * Instantiates a new sense.
	 * @param position the position
	 * @param size the size
	 */
	Sense (Vector2 position, int size){
		this.position=position;
		this.size = size;
		destroyed = false;
	}
	
	/**
	 * Gets the sensed prey.
	 * @return the sensed
	 */
	public Consumable getSensed(){
		return sensed;
	}
	
	/**
	 * Destroy itself.
	 */
	public void destroy(){
		destroyed = true;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Destructible#isDestroyed()
	 */
	public boolean isDestroyed(){
		return destroyed;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getShape()
	 */
	@Override
	public Shape2D getShape() {
		return new Circle(position.x, position.y, size);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getMagnitude()
	 */
	@Override
	public int getMagnitude() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#stillCollidable()
	 */
	@Override
	public boolean stillCollidable() {
		return isDestroyed();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = ((Circle)getShape()).overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps((Circle)getShape(), (Rectangle)other.getShape());
		}
		return overlaps && other.stillCollidable();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.utils.Array)
	 */
	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();

		for (Collidable collidable: otherCollidables){
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
			}
		}
		return collidedWith;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public void collidedWith(Collidable collidable) {
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumer)
	 */
	@Override
	public void collidedWith(Consumer consumer) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumable)
	 */
	@Override
	public void collidedWith(Consumable consumable) {
		sensed = consumable;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getPosition()
	 */
	@Override
	public Vector2 getPosition() {
		return position;
	}
}
