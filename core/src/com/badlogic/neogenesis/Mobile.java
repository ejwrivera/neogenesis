package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector3;

/**
 * The Interface Mobile. For moving critters about
 */
public interface Mobile extends Identifiable {

	/**
	 * Move.
	 *
	 * @return the vector3
	 */
	public Vector3 move();
}
