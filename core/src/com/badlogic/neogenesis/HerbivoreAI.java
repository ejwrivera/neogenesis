package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * The Herbivore implementation of the AI Interface.
 */
public class HerbivoreAI implements AI {

	/** The last movement. */
	private Vector2 lastMovement; 
	
	/**
	 * Instantiates a new herbivore ai.
	 */
	public HerbivoreAI(){
		lastMovement = new Vector2(0,0);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.AI#amble(com.badlogic.gdx.math.Circle)
	 */
	@Override
	public Vector2 amble(Circle position) {
		int rand = MathUtils.random(0, 9);
		switch (rand){
			case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
				Vector2 oldPosition = new Vector2(position.x, position.y);
				Vector2 movement = new Vector2(50 * Gdx.graphics.getDeltaTime(), 0);
				movement = movement.rotate(MathUtils.random(1, 360));
				Vector2 newPosition = new Vector2(oldPosition).add(movement);
				lastMovement = new Vector2(newPosition.x-oldPosition.x, newPosition.y-oldPosition.y);
				break;
			case 9:
				lastMovement = new Vector2(0,0);
				break;
		}
		return lastMovement;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.AI#forage(com.badlogic.gdx.math.Circle)
	 */
	@Override
	public Vector2 forage(Circle position) {
		return lastMovement;
	}

}
