package com.badlogic.neogenesis;

public interface ILiving {

	/**
	 * Live.
	 */
	public void live();
	
	/**
	 * Die.
	 */
	public void die();
	
	/**
	 * checks for a pulse, or, if it's a plant, a leafy-pulse.
	 * @return true, if is alive
	 */
	public boolean isAlive();
	
	/**
	 * Gets the corpse of the presumably dead thing.
	 * @return the corpse
	 */
	public Corpse getCorpse();

}
