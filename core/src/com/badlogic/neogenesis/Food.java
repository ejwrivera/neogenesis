package com.badlogic.neogenesis;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food {

	/** The nutrition. */
	private int nutrition;
	
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition){
		this.nutrition=nutrition;
	}
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return nutrition;
	}
}
