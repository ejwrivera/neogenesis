package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * The Class Sense.
 */
public class Sense extends GameObject {

	/** Whether the sense is destroyed. */
	boolean destroyed;
	
	/** The position. */
	Vector2 position;
	
	/** The size. */
	int size;
	
	/** The sensed prey. */
	Prey sensed;
	
	/**
	 * Instantiates a new sense.
	 * @param position the position
	 * @param size the size
	 */
	Sense (Vector2 position, int size){
		super(new NonVisible(), new Audible(), new NonMovable(new Circle(position.x, position.y, size)), new Collidable(new Circle(position.x, position.y, size)), new Living());
		this.position=position;
		this.size = size;
		destroyed = false;
	}
	
	/**
	 * Gets the sensed prey.
	 * @return the sensed
	 */
	public Prey getSensed(){
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
	
	public void collidedWith(Prey prey) {
		//super.collidedWith(consumable);
		sensed = prey;
	}
}
