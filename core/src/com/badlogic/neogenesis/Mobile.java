package com.badlogic.neogenesis;

/**
 * The Mobile Interface. For moving critters about.
 */
public interface Mobile extends Identifiable {

	/**
	 * Move.
	 * @return the vector3
	 */
	public void move();
}
