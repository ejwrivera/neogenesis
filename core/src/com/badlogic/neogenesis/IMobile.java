package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector2;

public interface IMobile {

	void move();
	void addForce(Vector2 force);
}
