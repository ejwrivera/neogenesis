package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * The Class Eve. Player character class - for camera locking and player movement input
 */
public class Eve extends Creature {

	/** The input. */
	private Input input;
	/** The last movement. */
	private Vector2 lastMovement;
	/** The camera. */
	private OrthographicCamera camera;
	
	/**
	 * Instantiates a new eve.
	 * @param startPosAndSize the start pos and size
	 * @param camera the camera
	 */
	public Eve(Rectangle startPosAndSize, OrthographicCamera camera){
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
	public Eve(Rectangle startPosAndSize, OrthographicCamera camera, int biomass){
		super(startPosAndSize, biomass);
		this.camera = camera;
		this.biomass = biomass;
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
	 * @see com.badlogic.neogenesis.Creature#getRectangle()
	 */
	public Rectangle getRectangle() {
		return position;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#consume(com.badlogic.neogenesis.Food)
	 */
	public void consume(Food food) {
		biomass+=food.getNutrition()/5;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#move()
	 */
	public Vector3 move(){
		// process user input
		float oldX = position.x;
		float oldY = position.y;
				
		if (input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			position.x = touchPos.x - 64 / 2;
			position.y = touchPos.y - 64 / 2;
		}
		if (input.isKeyPressed(Keys.LEFT))
			position.x -= 300 * Gdx.graphics.getDeltaTime();
		if (input.isKeyPressed(Keys.RIGHT))
			position.x += 300 * Gdx.graphics.getDeltaTime();
		if (input.isKeyPressed(Keys.UP))
			position.y += 300 * Gdx.graphics.getDeltaTime();
		if (input.isKeyPressed(Keys.DOWN))
			position.y -= 300 * Gdx.graphics.getDeltaTime();
				
		lastMovement = new Vector2(position.x-oldX, position.y-oldY);
		
		return new Vector3(lastMovement, 0);
	}
}
