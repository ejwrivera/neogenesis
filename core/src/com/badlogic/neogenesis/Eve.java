package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * The Class Eve. Player character class - for camera locking and player movement input
 */
public class Eve extends Creature {

	/** The input. */
	private Input input;
	/** The camera. */
	private OrthographicCamera camera;
	
	/**
	 * Instantiates a new eve.
	 * @param startPosAndSize the start pos and size
	 * @param camera the camera
	 */
	public Eve(Circle startPosAndSize, OrthographicCamera camera){
		super(startPosAndSize);
		this.camera = camera;
		biomass = 10;
		texture = TextureMap.getTexture("eve");
	}
	
	/**
	 * Instantiates a new eve.
	 * @param startPosAndSize the start pos and size
	 * @param camera the camera
	 * @param biomass the biomass
	 */
	public Eve(Circle startPosAndSize, OrthographicCamera camera, int biomass){
		super(startPosAndSize, biomass);
		this.camera = camera;
		this.biomass = biomass;
		position.radius=biomass/2;
		texture = TextureMap.getTexture("eve");
	}
	
	/**
	 * Sets the input.
	 * @param input the new input
	 */
	public void setInput(Input input){
		this.input = input;
	}
	
	/**
	 * Gets the last movement.
	 * @return the last movement
	 */
	public Vector2 getLastMovement(){
		return lastMovement;
	}
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#consume(com.badlogic.neogenesis.Food)
	 */
	public void consume(Food food) {
		super.consume(new Food(food.getNutrition()*2));
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#move()
	 */
	public Vector3 move(){
		// process user input
		float oldX = position.x;
		float oldY = position.y;
		/*
		if (input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			position.x = touchPos.x - 64 / 2;
			position.y = touchPos.y - 64 / 2;
		}*/
		
		// calculate the change in X
		position.x += deltaX();
		// calculate the change in Y
		position.y += deltaY();

		lastMovement = new Vector2(position.x-oldX, position.y-oldY);
		
		return new Vector3(lastMovement, 0);
	}
	
	private float deltaX(){
		float delta=lastMovement.x;
		if (input.isKeyPressed(Keys.LEFT)||input.isKeyPressed(Keys.RIGHT)){   
			delta += input.isKeyPressed(Keys.LEFT) ? -10 * Gdx.graphics.getDeltaTime() : 10 * Gdx.graphics.getDeltaTime();
		}
		delta += applyFriction(delta);
		return maxVelocityLimited(delta);
	}
	private float deltaY(){
		float delta=lastMovement.y;
		if (input.isKeyPressed(Keys.DOWN)||input.isKeyPressed(Keys.UP)){   
			delta += input.isKeyPressed(Keys.DOWN) ? -10 * Gdx.graphics.getDeltaTime() : 10 * Gdx.graphics.getDeltaTime();
		}
		delta += applyFriction(delta);
		return maxVelocityLimited(delta);
	}
	
	private float applyFriction (float magnitude){
		return magnitude > 0 ? -2 * Gdx.graphics.getDeltaTime() : 2 * Gdx.graphics.getDeltaTime();
	}
	
	private float maxVelocityLimited(float magnitude){
		return maxVelocityLimited(magnitude, 7);
	}
	private float maxVelocityLimited(float magnitude, float maxSpeed){
		return magnitude > maxSpeed ? maxSpeed : magnitude < -maxSpeed ? -maxSpeed : magnitude;
	}
	
}