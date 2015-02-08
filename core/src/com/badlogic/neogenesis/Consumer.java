package com.badlogic.neogenesis;

public interface Consumer extends Collidable {

	/**
	 * Consume.
	 * @param appropriatelySizedConsumables the consumables that may be eaten
	 * @return the set of ids that have been consumed
	 */
	void consume(Consumable consumablesToConsume);
	
}
