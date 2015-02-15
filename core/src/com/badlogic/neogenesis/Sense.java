package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Sense implements Collidable, Destructible {

	boolean destroyed;
	Vector2 position;
	int size;
	Consumable sensed;
	
	Sense (Vector2 position, int size){
		this.position=position;
		this.size = size;
		destroyed = false;
	}
	
	public Consumable getSensed(){
		return sensed;
	}
	
	public void destroy(){
		destroyed = true;
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	@Override
	public Shape2D getShape() {
		return new Circle(position.x, position.y, size);
	}

	@Override
	public int getMagnitude() {
		return 0;
	}

	@Override
	public boolean stillCollidable() {
		return isDestroyed();
	}
	
	@Override
	public ID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = ((Circle)getShape()).overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps((Circle)getShape(), (Rectangle)other.getShape());
		}
		return overlaps && other.stillCollidable();
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
	public void collidedWith(Collidable collidable) {
	}
	
	@Override
	public void collidedWith(Consumer consumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collidedWith(Consumable consumable) {
		sensed = consumable;
	}

	@Override
	public void collidedWith(Rock rock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	

	
	
}
