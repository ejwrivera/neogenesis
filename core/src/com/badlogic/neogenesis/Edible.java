package com.badlogic.neogenesis;

/**
 * The Interface Edible.
 */
public interface Edible {
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition();
	
	/**
	 * Gets the protein.
	 * @return the protein
	 */
	public int getProtein();
	
	/**
	 * Be swallowed.
	 * @return the food
	 */
	public Food beSwallowed();
}
