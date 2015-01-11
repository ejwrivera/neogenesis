package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public interface AI {
	public Vector2 amble(Circle pos);
	public Vector2 forage(Circle pos);
}
