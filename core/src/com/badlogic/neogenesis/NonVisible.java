package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;

public class NonVisible implements IDrawable {

	@Override
	public Shape2D getShape() {
		return null;
	}

	@Override
	public Texture getTexture() {
		return null;
	}

}
