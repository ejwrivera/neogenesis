package com.badlogic.neogenesis;

/**
 * For performing cleanup activities.
 */
public interface Destructible extends Identifiable{
	
	/**
	 * Checks if is destroyed.
	 * @return true, if is destroyed
	 */
	boolean isDestroyed();
}
