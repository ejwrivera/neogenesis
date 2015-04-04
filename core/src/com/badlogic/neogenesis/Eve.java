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

	/** The utilized biomass. */
	private int usedBiomass;
	/** The protein. */
	private int protein;
	/** The protein store, used instead of making protein a float. */
	private int proteinStore;
	
	/** The available abilities. */
	public ObjectMap<String, Boolean> availableAbilities;
	/** The available abilities cost. */
	public ObjectMap<String, Integer> availableAbilitiesCost;
	
	public Input input;
	public int impetusAmount;
	
	
	/**
	 * Instantiates a new eve.  It begins.
	 *
	 * @param startPos the starting position
	 * @param biomass the starting biomass
	 */
	public Eve(Vector2 startPos, int biomass){
		super(startPos, biomass);
		moveLogic = new Movable(startPos);
		((Movable)moveLogic).abilities = abilities;
		drawLogic = new Visible(TextureMap.getTexture("eve"));
		
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
	}
	
	public void setInput(Input input){
		this.input = input;
	}
	
	/**
	 * Gets the last movement so that camera can be oriented properly.
	 * @return the last movement
	 */
	public Vector2 getvelocity(){
		return ((Movable)moveLogic).velocity;
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
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
		
		Vector2 velocity = new Vector2 (moveLogic.getVelocity());
		Vector2 crash;
		if (velocity.x < 1 && velocity.y < 1){
			crash = new Vector2(rock.getCollidable().getPosition());
			crash.sub(moveLogic.getPosition());
			crash.rotate(180);
		}
		else {
			crash = new Vector2(velocity.x*2, velocity.y*2);
			crash.rotate(180);
		}
		moveLogic.addForce(crash);
	}
	
	public ICollidable getCollide(){
		return collideLogic;
	}
	
	public void move(){
		Vector2 position = moveLogic.getPosition();
		
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
		
		int impetus = input.isKeyPressed(Keys.SHIFT_LEFT)&&abilities.get("boost") ? 10 : 5;
		
		moveLogic.addForce(new Vector2 ((right ? impetus : left ? -impetus : 0), (up ? impetus : down ? -impetus : 0)));
		
		super.move();
	}
	
	
}