package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;

public class Rock implements Drawable, Collidable{

	private ID id;
	private Rectangle position;
	private Texture texture;
	
	public Rock(Rectangle position){
		this.position=position;
		id = IDFactory.getNewID();
		texture=TextureMap.getTexture("rock");
	}
	@Override
	public ID getID() {
		return id;
	}

	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Rectangle){
			overlaps = position.overlaps((Rectangle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps((Circle)other.getShape(),position);
		}
		
		
		if (overlaps && other.stillCollidable() && id!=other.getID()){	
			other.collidedWith(this);
			return true;
		}
		return false;
	}

	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();
		for (Collidable collidable: otherCollidables){
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
			}
		}
		return collidedWith;
	}

	@Override
	public Shape2D getShape() {
		return position;
	}

	@Override
	public int getMagnitude() {
		return (int) position.getWidth()/100;
	}

	@Override
	public boolean stillCollidable() {
		return true;
	}

	@Override
	public void collidedWith(Consumer consumer) {
	}

	@Override
	public void collidedWith(Consumable consumable) {
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	@Override
	public void collidedWith(Rock rock) {
		// TODO Auto-generated method stub
		
	}

}
