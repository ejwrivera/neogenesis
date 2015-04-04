package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NonMovable implements IMobile {

	public Rectangle position;
	
	public NonMovable (Rectangle position){
		this.position = position;
	}
	
	public NonMovable (Circle position){
	}
	
	@Override
	public void move() {
	}

	@Override
	public void addForce(Vector2 force) {
		
	}

	@Override
	public Vector2 getPosition() {
		return new Vector2 (position.x, position.y);
	}

	@Override
	public Vector2 getVelocity() {
		return new Vector2 (0, 0);
	}

}
