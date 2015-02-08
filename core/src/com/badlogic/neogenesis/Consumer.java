package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector2;

public interface Consumer extends Collidable {

	/**
	 * Consume.
	 * @param appropriatelySizedConsumables the consumables that may be eaten
	 * @return the set of ids that have been consumed
	 */
	void ingest(Consumable consumablesToConsume);
	Vector2 getCenter();
	
}
