package com.badlogic.neogenesis;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
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
	
	/**
	 * Instantiates a new eve.  It begins.
	 *
	 * @param startPos the starting position
	 * @param biomass the starting biomass
	 */
	public Eve(Vector2 startPos, int biomass){
		super(startPos, biomass);
		moveLogic = new MovableEVETEMP(startPos);
		((MovableEVETEMP)moveLogic).abilities = abilities;
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
	
	/**
	 * Sets the input.
	 * @param input the new input
	 */
	public void setInput(Input input){
		((MovableEVETEMP)moveLogic).input = input;
	}
	
	/**
	 * Gets the last movement so that camera can be oriented properly.
	 * @return the last movement
	 */
	public Vector2 getLastMovement(){
		return ((MovableEVETEMP)moveLogic).lastMovement;
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
		// this needs to be changed so that force is applied
		
		Vector2 oldPosition = new Vector2(((MovableEVETEMP)moveLogic).getPosition().x, ((MovableEVETEMP)moveLogic).getPosition().y);
		if ( (((MovableEVETEMP)moveLogic).lastMovement.x > 1 || ((MovableEVETEMP)moveLogic).lastMovement .x < -1) || (((MovableEVETEMP)moveLogic).lastMovement.y > 1 || ((MovableEVETEMP)moveLogic).lastMovement.y < -1)){
			((MovableEVETEMP)moveLogic).lastMovement = ((MovableEVETEMP)moveLogic).lastMovement.rotate(180);
			Vector2 newPosition = new Vector2(oldPosition).add(((MovableEVETEMP)moveLogic).lastMovement);
			((MovableEVETEMP)moveLogic).getPosition().x=newPosition.x;
			((MovableEVETEMP)moveLogic).getPosition().y=newPosition.y;
		}
		// squeezes the stuck thing out the right side of the rock, needs to properly squeeze out depending on direction of rock collision
		if (collidesWith(rock.getCollidable())){
			((MovableEVETEMP)moveLogic).getPosition().x++;
			((MovableEVETEMP)moveLogic).lastMovement = new Vector2(1,0);
		}	
	}
	
	public ICollidable getCollide(){
		return collideLogic;
	}
	
}