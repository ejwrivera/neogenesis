package com.badlogic.neogenesis;

/**
 * The Interface Consumable. For performing collision checks to see if something has been eaten
 */
public interface Consumable extends Collidable{

	/**
	 * Be consumed.
	 * Converts self into food, with nutrition values and potential effects
	 * @return the food
	 */
	public Food beConsumed();
	// for checking if edible
	/**
	 * Gets the biomass.
	 * @return the biomass
	 */
	public int getBiomass();
	
}
