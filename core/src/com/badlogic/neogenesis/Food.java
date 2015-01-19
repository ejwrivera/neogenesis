package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food implements Consumable, Drawable, Mobile {

	/** The nutrition. */
	private int nutrition;
	/** The ID. */
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	/** The texture. */
	private Texture texture;
	/** The position. */
	private Circle position;
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition, Circle position){
		this.nutrition=nutrition;
		this.position=position;
		id = IDFactory.getNewID();
		consumed=false;
		texture = TextureMap.getTexture("food");
	}
	
	public Food(int nutrition){
		this(nutrition, new Circle());
	}
	
	public Food(Food food){
		this(food.getNutrition(), new Circle());
	}
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return nutrition;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.math.Rectangle)
	 */
	@Override
	public Boolean collidesWith(Circle other) {
		return position.overlaps(other);
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
		consumed = true;
		return this;
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
	public Circle getCircle() {
		return position;
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
		return new Vector3(newPosition.x-oldPosition.x, newPosition.y-oldPosition.y, 0);
	}
}
