package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector2;

/**
 * The Consumer Interface.  For interactions with the Consumable interface.
 */
public interface Consumer extends Collidable {

	/**
	 * Ingest.
	 * @param the consumable to ingest
	 */
	void ingest(Consumable consumablesToIngest);
	
	/**
	 * Gets the center of the consumer so that the consumed can be forced towards it.
	 * @return the center
	 */
	Vector2 getCenter();
	
}
