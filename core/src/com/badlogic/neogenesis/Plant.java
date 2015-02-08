package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Plant implements Consumable, Drawable, Mobile {
	/** The ID. */
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	/** The texture. */
	private Texture texture;
	/** The position. */
	private Circle position;
	
	private int biomass;
	
	private boolean alive;
	
	private Vector2 lastMovement;
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Plant(int biomass, Circle position){
		this.biomass=biomass;
		this.position=position;
		id = IDFactory.getNewID();
		consumed=false;
		texture = TextureMap.getTexture("food");
		alive = true;
		lastMovement=new Vector2(0,0);
	}
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return biomass;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beConsumed()
	 */
	@Override
	public Food beConsumed() {
		die();
		return new Food (biomass, 0);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#getBiomass()
	 */
	@Override
	public int getBiomass() {
		return 0;
	}
	
	public boolean beenConsumed() {
		return consumed;
	}

	@Override
	public int getMagnitude() {
		return -1;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	@Override
	public Vector3 move() {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		Vector2 movement = new Vector2(10 * Gdx.graphics.getDeltaTime(), 0);
		movement = movement.rotate(MathUtils.random(1, 45));
		Vector2 newPosition = new Vector2(oldPosition).add(movement);
		position.x=newPosition.x;
		position.y=newPosition.y;
		lastMovement = newPosition.sub(oldPosition);
		return new Vector3(newPosition.x-oldPosition.x, newPosition.y-oldPosition.y, 0);
	}

	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = position.overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps(position, (Rectangle)other.getShape());
		}
		if (overlaps && other.stillCollidable() && id!=other.getID()){	
			other.collidedWith((Consumable)this);
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
	public boolean stillCollidable() {
		return alive;
	}

	@Override
	public void collidedWith(Consumer consumer) {
		consumer.collidedWith((Consumable)this);
	}

	@Override
	public void collidedWith(Consumable consumable) {	
	}
	
	@Override
	public void collidedWith(Rock rock) {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		lastMovement = lastMovement.rotate(180);
		Vector2 newPosition = new Vector2(oldPosition).add(lastMovement);
		position.x=newPosition.x;
		position.y=newPosition.y;
		lastMovement = new Vector2(0,0);
	}
	
	public void die(){
		alive=false;
	}

	
}
