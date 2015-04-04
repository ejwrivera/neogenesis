package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Movable implements IMobile {
		
	Vector2 position;
	Vector2 velocity;
	Array<Vector2> forces;
	
	public Movable (Vector2 startPos){
		position = startPos;
		velocity = new Vector2(0,0);
		forces = new Array<Vector2>();
	}
	
	public void move(){

		Vector2 acceleration = new Vector2(0, 0);
		for (Vector2 force: forces){
			acceleration.add(force);
		}
		velocity = new Vector2(delta(acceleration.x, velocity.x), delta(acceleration.y, velocity.y));
		position.add(velocity);
		forces.clear();
		
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Vector2 getVelocity(){
		return velocity;
	}
	
	
	/* Below are temporary physics simulations - to be replaced with a universal physics system */
	
	/**
	 * Delta.
	 *
	 * @param acceleration the acceleration
	 * @param momentum the momentum
	 * @param input the input
	 * @return the float
	 */
	private float delta(float acceleration, float momentum){
		float delta = momentum;
		delta += acceleration * Gdx.graphics.getDeltaTime();
		delta += applyFriction(delta);
		return maxVelocityLimited(delta);
	}
	
	/**
	 * Apply friction.
	 *
	 * @param magnitude the magnitude
	 * @return the float
	 */
	private float applyFriction (float magnitude){
		return magnitude > 0 ? -2 * Gdx.graphics.getDeltaTime() : 2 * Gdx.graphics.getDeltaTime();
	}
	
	/**
	 * Max velocity limited.
	 *
	 * @param magnitude the magnitude
	 * @return the float
	 */
	private float maxVelocityLimited(float magnitude){
		return maxVelocityLimited(magnitude, 7);
	}
	
	/**
	 * Max velocity limited.
	 *
	 * @param magnitude the magnitude
	 * @param maxSpeed the max speed
	 * @return the float
	 */
	private float maxVelocityLimited(float magnitude, float maxSpeed){
		return magnitude > maxSpeed ? maxSpeed : magnitude < -maxSpeed ? -maxSpeed : magnitude;
	}

	@Override
	public void addForce(Vector2 force) {
		forces.add(force);
	}
	
	// these properties don't belong here, and should probably be refactored into a class that handles abilities
	
	/** The abilities. */
	protected ObjectMap<String, Boolean> abilities;
		
		
	
}
