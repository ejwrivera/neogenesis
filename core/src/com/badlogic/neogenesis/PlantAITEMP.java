package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * The Herbivore implementation of the AI Interface.
 */
public class PlantAITEMP implements AI {

	/** The last movement. */
	private Vector2 lastMovement; 
	
	/**
	 * Instantiates a new herbivore ai.
	 */
	public PlantAITEMP(){
		lastMovement = new Vector2(0,0);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.AI#amble(com.badlogic.gdx.math.Circle)
	 */
	@Override
	public Vector2 amble(Circle position) {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		Vector2 movement = new Vector2(10 * Gdx.graphics.getDeltaTime(), 0);
		movement = movement.rotate(MathUtils.random(1, 45));
		Vector2 newPosition = new Vector2(oldPosition).add(movement);
		position.x=newPosition.x;
		position.y=newPosition.y;
		lastMovement = newPosition.sub(oldPosition);
		return lastMovement;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.AI#forage(com.badlogic.gdx.math.Circle)
	 */
	@Override
	public Vector2 forage(Circle position) {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		Vector2 movement = new Vector2(10 * Gdx.graphics.getDeltaTime(), 0);
		movement = movement.rotate(MathUtils.random(1, 45));
		Vector2 newPosition = new Vector2(oldPosition).add(movement);
		position.x=newPosition.x;
		position.y=newPosition.y;
		lastMovement = newPosition.sub(oldPosition);
		return lastMovement;
	}

}
