package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;

public class Visible implements IDrawable {

	private Texture texture;
	private Shape2D shape;
	
	public Visible (Texture texture){
		this.texture = texture;
	}
	
	@Override
	public Shape2D getShape() {
		return shape;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	public void setShape(Shape2D shape){
		this.shape = shape;
	}


}
