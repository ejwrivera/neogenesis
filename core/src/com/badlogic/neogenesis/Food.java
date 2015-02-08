package com.badlogic.neogenesis;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food implements Edible {

	/** The nutrition. */
	private int nutrition;
	/** The ID. */
	private int protein;
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	/** The texture. */
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition, int protein){
		this.nutrition = nutrition;
		this.protein = protein;
		id = IDFactory.getNewID();
		consumed=false;
	}
	
	public Food(int nutrition){
		this(nutrition, 0);
	}
	// copy constructor
	public Food(Food food){
		this(food.getNutrition(), food.getProtein());
	}
	

	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return nutrition;
	}

	@Override
	public int getProtein() {
		return protein;
	}

	@Override
	public Food beBitten() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Food beSwallowed() {
		if(!consumed){
			consumed=true;
			return this;
		}
		return null;
	}

	@Override
	public ID getID() {
		return id;
	}


}
