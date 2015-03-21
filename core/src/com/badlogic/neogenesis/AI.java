package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * The AI Interface.  Used to determine where something with an AI is attempting to moving towards.
 */
public interface AI {
	
	/**
	 * Amble.
	 * @param pos the pos
	 * @return the vector2
	 */
	public Vector2 amble(Circle pos);
	
	/**
	 * Forage.
	 * @param pos the pos
	 * @return the vector2
	 */
	public Vector2 forage(Circle pos);
}
