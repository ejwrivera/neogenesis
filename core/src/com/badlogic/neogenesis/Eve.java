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
	/** The available abilities. */
	private ObjectMap<String, Boolean> availableAbilities;
	/** The available abilities cost. */
	private ObjectMap<String, Integer> availableAbilitiesCost;
	/** The utilized biomass. */
	private int usedBiomass;
	/** The protein. */
	private int protein;
	/** The protein store, used instead of making protein a float. */
	private int proteinStore;
	/** Whether Eve is currently emitting sound. */
	private boolean sound;
	
	/**
	 * Instantiates a new eve.  It begins.
	 *
	 * @param startPos the starting position
	 * @param biomass the starting biomass
	 */
	public Eve(Vector2 startPos, int biomass){
		super(startPos, biomass);
		texture = TextureMap.getTexture("eve");
		availableAbilities = new ObjectMap<String, Boolean>();
		availableAbilitiesCost = new ObjectMap<String, Integer>();
		availableAbilities.put("sense", false);
		availableAbilities.put("boost", false);
		availableAbilities.put("impetus", false);
		availableAbilities.put("photosynthesis", false);
		availableAbilitiesCost.put("sense", 10);
		availableAbilitiesCost.put("boost", 20);
		availableAbilitiesCost.put("impetus", 10);
		availableAbilitiesCost.put("photosynthesis", 50);
		usedBiomass = 0;
		protein = 0;
		proteinStore = 0;
		sound = false;
	}
	
	/**
	 * Sets the input.
	 * @param input the new input
	 */
	public void setInput(Input input){
		this.input = input;
	}
	
	/**
	 * Gets the last movement so that camera can be oriented properly.
	 * @return the last movement
	 */
	public Vector2 getLastMovement(){
		return lastMovement;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#consume(com.badlogic.neogenesis.Food)
	 */
	public void digest(Food food){
		super.digest(food);
		proteinStore+=food.getProtein();
		if (proteinStore>10){
			protein++;
			proteinStore-=10;
		}
		
		if (protein >= 3){
			for(String ability: availableAbilities.keys()){
				if ( !(abilities.get(ability)||availableAbilities.get(ability)) && biomass > availableAbilitiesCost.get(ability)){
					availableAbilities.put(ability,  true);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#ingest(com.badlogic.neogenesis.Consumable)
	 */
	public void ingest (Consumable consumableToIngest){
		if (!belly.contains(consumableToIngest)){
			belly.add(consumableToIngest );
			sound = true;
		}
	}
	
	/**
	 * Checks for sense.
	 * @return true, if the sense ability has been procured
	 */
	public boolean hasSense() {
		return abilities.get("sense");
	}

	/**
	 * Gets the available upgrades.  For displaying the upgrades on screen.
	 * @return the available upgrades
	 */
	public Array<String> getAvailableUpgrades() {
		Array<String> upgrades = new Array<String>();
		for (String potential: availableAbilities.keys()){
			if (availableAbilities.get(potential)){
				upgrades.add(potential);
			}
		}
		return upgrades;
	}

	/**
	 * Adds an upgrade.
	 * @param upgrade the upgrade
	 * @return true, if the upgrade can be currently purchased and the resources are available
	 */
	public boolean addUpgrade(String upgrade) {
		int cost = availableAbilitiesCost.get(upgrade);
		if (cost > biomass-usedBiomass || protein < 3){
			return false;
		}
		abilities.put(upgrade, true);
		availableAbilities.put(upgrade, false);
		usedBiomass+=cost;
		protein-=3;
		return true;
	}	
	
	/**
	 * Gets the utilized biomass for display.
	 * @return the used biomass
	 */
	public int getUsedBiomass(){
		return usedBiomass;
	}
	
	/**
	 * Gets the protein amount for display.
	 * @return the protein
	 */
	public int getProtein(){
		return protein;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#emittingSound()
	 */
	public boolean emittingSound (){
		return sound;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#emitSound()
	 */
	public void emitSound(){
		sound = false;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Creature#move()
	 */
	public Vector3 move(){
		// needs to be refactored to remove this branching.  Also it might be possible to have a super() call handle most of the logic if the impetus force is added, but this would
		// require that AI creatures determine their movement and similarly make a super() call
		if (inBellyOf==null){
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
			
			return new Vector3(lastMovement, 0);
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
			return new Vector3(lastMovement, 0);
		}
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