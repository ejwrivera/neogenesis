package com.badlogic.neogenesis;

/**
 * The Consumable Interface. For performing collision checks to see if something has been eaten and being-eaten related activities.
 */
public interface Consumable extends Collidable{

	/**
	 * Be consumed.
	 * Converts self into food, with nutrition values and potential effects
	 * @return the food
	 */
	public Food beDigested();
	
	/**
	 * Be bitten.
	 * @return the food
	 */
	public Food beBitten();
	
	/**
	 * Be ingested.
	 * @param consumer the consumer
	 * @return true, if successful
	 */
	public boolean beIngested(Consumer consumer);
	
	/**
	 * Gets the biomass.
	 * @return the biomass
	 */
	public int getBiomass();
}
