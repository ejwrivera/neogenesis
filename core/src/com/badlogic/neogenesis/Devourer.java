package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Vector2;

public interface Devourer {

	void ingest(Devourable devourableToIngest);
	
	Vector2 getCenter();
}
