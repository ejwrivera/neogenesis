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

}
