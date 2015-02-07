package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The Class Eve. Player character class - for camera locking and player movement input
 */
public class Eve extends Creature {

	/** The input. */
	private Input input;
	private ObjectMap<String, Boolean> availableAbilities;
	/**
	 * Instantiates a new eve.
	 * @param startPosAndSize the start pos and size
	 * @param camera the camera
	 */
	public Eve(Vector2 startPos){
		this(startPos, 10);
	}
	
	/**
	 * Instantiates a new eve.
	 * @param startPosAndSize the start pos
	 * @param biomass the starting biomass
	 */
	public Eve(Vector2 startPos, int biomass){
		super(startPos, biomass);
		texture = TextureMap.getTexture("eve");
		availableAbilities = new ObjectMap<String, Boolean>();
		availableAbilities.put("sense", false);
		availableAbilities.put("boost", false);
		availableAbilities.put("photosynthesis", false);
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
		if (biomass+undigestedBiomass >=31 && !(abilities.get("sense")||availableAbilities.get("sense"))){
			availableAbilities.put("sense", true);
		}
		if (biomass+undigestedBiomass >=36 && !(abilities.get("boost")||availableAbilities.get("boost"))){
			availableAbilities.put("boost", true);
		}
		if (biomass+undigestedBiomass >=40 && !(abilities.get("photosynthesis")||availableAbilities.get("photosynthesis"))){
			availableAbilities.put("photosynthesis", true);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#move()
	 */
	public Vector3 move(){
		// process user input
		float oldX = position.x;
		float oldY = position.y;
		
		Vector3 mousePosition = CameraHandler.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		boolean left = input.isKeyPressed(Keys.LEFT) || (mousePosition.x < position.x && Gdx.input.isTouched());
		boolean right = input.isKeyPressed(Keys.RIGHT) || (mousePosition.x > position.x && Gdx.input.isTouched());
		boolean up = input.isKeyPressed(Keys.UP) || (mousePosition.y > position.y && Gdx.input.isTouched());
		boolean down = 	input.isKeyPressed(Keys.DOWN) || (mousePosition.y < position.y && Gdx.input.isTouched());
		
		// calculate the change in X
		position.x += delta(input.isKeyPressed(Keys.SHIFT_LEFT)&&abilities.get("boost") ? 10 : 5, lastMovement.x, left ? "DECREASE" : right ? "INCREASE" : "NONE" );
		// calculate the change in Y
		position.y += delta(input.isKeyPressed(Keys.SHIFT_LEFT)&&abilities.get("boost") ? 10 : 5, lastMovement.y, down ? "DECREASE" : up ? "INCREASE" : "NONE");

		lastMovement = new Vector2(position.x-oldX, position.y-oldY);
		
		return new Vector3(lastMovement, 0);
	}
	
	private float delta(int acceleration, float momentum, String input){
		float delta = momentum;
		delta += input.equals("DECREASE") ? -acceleration * Gdx.graphics.getDeltaTime() 
				: input.equals("INCREASE") ? acceleration * Gdx.graphics.getDeltaTime()
				: 0;
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

	public boolean hasSense() {
		return abilities.get("sense");
	}

	public Array<String> getAvailableUpgrades() {
		Array<String> upgrades = new Array<String>();
		for (String potential: availableAbilities.keys()){
			if (availableAbilities.get(potential)){
				upgrades.add(potential);
			}
		}
		return upgrades;
	}

	public void addUpgrade(String upgrade) {
		abilities.put(upgrade, true);
		availableAbilities.put(upgrade, false);
	}
	
}