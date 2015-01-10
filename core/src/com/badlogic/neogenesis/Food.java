package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Rectangle;

// TODO: Auto-generated Javadoc
/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food implements Consumable {

	/** The nutrition. */
	private int nutrition;
	/** The ID. */
	private ID id;
	
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition){
		this.nutrition=nutrition;
		id = IDFactory.getNewID();
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
	public Boolean collidesWith(Rectangle other) {
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#getBiomass()
	 */
	@Override
	public int getBiomass() {
		return 0;
	}
}
