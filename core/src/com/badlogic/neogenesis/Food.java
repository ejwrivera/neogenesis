package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food implements Consumable {

	/** The nutrition. */
	private int nutrition;
	/** The ID. */
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition){
		this.nutrition=nutrition;
		id = IDFactory.getNewID();
		consumed=false;
	}
	
	public Food(Food food){
		this(food.getNutrition());
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
		// TODO Auto-generated method stub
		return null;
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
}
