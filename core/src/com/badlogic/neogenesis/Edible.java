package com.badlogic.neogenesis;

public interface Edible extends Identifiable {
	public int getNutrition();
	public int getProtein();
	public Food beBitten();
	public Food beSwallowed();
}
