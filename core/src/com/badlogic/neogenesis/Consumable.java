package com.badlogic.neogenesis;

import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.ObjectSet;

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
	/**
	 * Consume.
	 * @param consumables the consumables that may be eaten, should be in the consumer interface
	 * @return the set of ids that have been consumed
	 */
	public ObjectSet<ID> consume (Values<Consumable> consumables);
}
