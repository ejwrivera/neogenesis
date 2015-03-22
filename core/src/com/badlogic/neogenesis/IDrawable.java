package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;

public interface IDrawable {
	public Shape2D getShape();
	public Texture getTexture();
	
}
