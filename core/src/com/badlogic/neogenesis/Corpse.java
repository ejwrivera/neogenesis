package com.badlogic.neogenesis;

/**
 * The Class Corpse.  For determining what is left behind when something dies.
 */
public class Corpse {

	/** The plant, if a corpse is to be treated as a plant(immotile consumable) - temporary. */
	public Plant plant;
	
	/**
	 * Instantiates a new corpse.
	 */
	public Corpse(){}
	
	/**
	 * Instantiates a new corpse.
	 * @param corpse the corpse
	 */
	public Corpse(Plant corpse){
		plant = corpse;
	}
}
