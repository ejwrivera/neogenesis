package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector2;

public interface Devourable {
	
public Food beDigested();
	
	public Food beBitten();

	//public boolean beIngested(Devourer devourer);
	
	public int getBiomass();
	
	public Vector2 getPosition();

	void beIngested(Vector2 bellyDirection, float pullStrength);

	public boolean isDevoured();
	
	
	
}
