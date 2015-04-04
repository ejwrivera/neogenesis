package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public class MovableEVETEMP extends Movable implements IMobile {
	
	public MovableEVETEMP (Vector2 startPos){
		super(startPos, new HerbivoreAI());
	}
	
	public void move(){
		// needs to be refactored to remove this branching.  Also it might be possible to have a super() call handle most of the logic if the impetus force is added, but this would
		// require that AI creatures determine their movement and similarly make a super() call
		if (inBellyOf == null){
			// process user input
			float oldX = position.x;
			float oldY = position.y;
			
			Vector3 mousePosition = CameraHandler.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			boolean left = input.isKeyPressed(Keys.LEFT) || (mousePosition.x < position.x && Gdx.input.isTouched());
			boolean right = input.isKeyPressed(Keys.RIGHT) || (mousePosition.x > position.x && Gdx.input.isTouched());
			boolean up = input.isKeyPressed(Keys.UP) || (mousePosition.y > position.y && Gdx.input.isTouched());
			boolean down = 	input.isKeyPressed(Keys.DOWN) || (mousePosition.y < position.y && Gdx.input.isTouched());
			
			if (impetusAmount > 0){
				impetusAmount++;
			}
			if (impetusAmount==32){
				impetusAmount=0;
			}
			
			if (input.isKeyPressed(Keys.CONTROL_LEFT) && impetusAmount==0 && abilities.get("impetus")){
				impetusAmount = 1;
			}
			
			int movement = impetusAmount+(input.isKeyPressed(Keys.SHIFT_LEFT)&&abilities.get("boost") ? 10 : 5);
			// calculate the change in X
			position.x += delta(movement, lastMovement.x, left ? "DECREASE" : right ? "INCREASE" : "NONE" );
			// calculate the change in Y
			position.y += delta(movement, lastMovement.y, down ? "DECREASE" : up ? "INCREASE" : "NONE");

			lastMovement = new Vector2(position.x-oldX, position.y-oldY);
		}
		else {
			Vector2 newPosition;
			Vector2 bellyCenter = inBellyOf.getCenter();
			Vector2 oldPosition = new Vector2(position.x, position.y);
			if (Math.abs(position.x-bellyCenter.x)+Math.abs(position.y-bellyCenter.y)<6){
				newPosition = bellyCenter;
			}
			else{
				
				Vector2 movement = new Vector2(320 * Gdx.graphics.getDeltaTime(), 0);
				// point towards center of inBellyOf
				
				movement = movement.rotate(bellyCenter.sub(oldPosition).angle());
				newPosition = new Vector2(oldPosition).add(movement);
			}
			position.x = newPosition.x;
			position.y = newPosition.y;
			lastMovement = new Vector2(position.x-oldPosition.x, position.y-oldPosition.y);
		}
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Input input;
	public int impetusAmount;
	
	// these properties don't belong here, and should probably be refactored into a class that handles abilities
	
	/** The abilities. */
	protected ObjectMap<String, Boolean> abilities;
	
	
	/* Below are temporary physics simulations - to be replaced with a universal physics system */
	
	/**
	 * Delta.
	 *
	 * @param acceleration the acceleration
	 * @param momentum the momentum
	 * @param input the input
	 * @return the float
	 */
	private float delta(int acceleration, float momentum, String input){
		float delta = momentum;
		delta += input.equals("DECREASE") ? -acceleration * Gdx.graphics.getDeltaTime() 
				: input.equals("INCREASE") ? acceleration * Gdx.graphics.getDeltaTime()
				: 0;
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
	
}
