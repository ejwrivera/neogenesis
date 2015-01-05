package com.badlogic.neogenesis;

/**
 * A factory for creating ID objects. Generates IDs, currently empty, using java's reference magic for mapkeys
 */
public class IDFactory {
	
	/**
	 * Gets the new id.
	 *
	 * @return the new id
	 */
	public static ID getNewID(){
		return new ID();
	}
}
