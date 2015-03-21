package com.badlogic.neogenesis;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Food implements Edible {

	/** The nutrition. */
	private int nutrition;
	/** The ID. */
	private int protein;
	
	/** The id. */
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	
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
	
	/**
	 * Instantiates a new food with no protein.
	 * @param nutrition the nutrition
	 */
	public Food(int nutrition){
		this(nutrition, 0);
	}
	/**
	 * Instantiates a new food, copy constructor.
	 *
	 * @param food the food
	 */
	public Food(Food food){
		this(food.getNutrition(), food.getProtein());
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID() {
		return id;
	}

	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return nutrition;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Edible#getProtein()
	 */
	@Override
	public int getProtein() {
		return protein;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Edible#beSwallowed()
	 */
	@Override
	public Food beSwallowed() {
		if(!consumed){
			consumed=true;
			return this;
		}
		return null;
	}
}
