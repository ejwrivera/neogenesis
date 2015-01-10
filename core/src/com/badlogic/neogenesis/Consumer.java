package com.badlogic.neogenesis;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectMap.Values;

public interface Consumer {

	/**
	 * Consume.
	 * @param consumables the consumables that may be eaten
	 * @return the set of ids that have been consumed
	 */
	public ObjectSet<ID> consume (Values<Consumable> consumables);
}
