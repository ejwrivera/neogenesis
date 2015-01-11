package com.badlogic.neogenesis;

import com.badlogic.gdx.utils.ObjectSet;

public interface Consumer extends Collidable {

	/**
	 * Consume.
	 * @param appropriatelySizedConsumables the consumables that may be eaten
	 * @return the set of ids that have been consumed
	 */
	public ObjectSet<ID> consume (ObjectSet<Consumable> appropriatelySizedConsumables);
	
}
