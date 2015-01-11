package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class HerbivoreAI implements AI {

	private Vector2 lastMovement; 
	
	public HerbivoreAI(){
		lastMovement = new Vector2(0,0);
	}
	@Override
	public Vector2 amble(Circle position) {
		int rand = MathUtils.random(0, 9);
		switch (rand){
			case 0: case 1: case 2: case 3: case 4: case 5: case 6:
				break;
			case 7:
			case 8:
				float oldX = position.x;
				float oldY = position.y;
				position.x += MathUtils.random(-50, 50) * Gdx.graphics.getDeltaTime();
				position.y += MathUtils.random(-50, 50) * Gdx.graphics.getDeltaTime();
				lastMovement = new Vector2(position.x-oldX, position.y-oldY);
				break;
			case 9:
				lastMovement = new Vector2(0,0);
				break;
		}
		
		return lastMovement;
	}

	@Override
	public Vector2 forage(Circle position) {
		int rand = MathUtils.random(0, 9);
		switch (rand){
			case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
				break;
			case 9:
				lastMovement = new Vector2(0,0);
				break;
		}
		return lastMovement;
	}

}
