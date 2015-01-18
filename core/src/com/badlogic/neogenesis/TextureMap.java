package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The Class TextureMap.
 */
public class TextureMap {
	
	private static ObjectMap <String, Texture> textureMap = new ObjectMap<String, Texture>(); 
	
	/**
	 * Gets the texture.
	 *
	 * @param textureName the texture name
	 * @return the texture
	 */
	public static Texture getTexture(String textureName){
		if (!textureMap.containsKey(textureName)){
			textureMap.put(textureName, new Texture(Gdx.files.internal(textureName+".png")));
		}
		return textureMap.get(textureName);	
	}
	
	/**
	 * Dispose.
	 */
	public static void dispose(){
		for (Texture texture: textureMap.values()){
			texture.dispose();
		}
	}
}
