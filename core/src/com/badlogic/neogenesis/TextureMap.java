package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * The Class TextureMap.
 */
public class TextureMap {
	
	/** The creature image. */
	private static Texture creatureImage;
	/** The food image. */
	private static Texture foodImage;
	/** The eve image. */
	private static Texture eveImage;
	
	/**
	 * Lazy load textures.
	 */
	private static void lazyLoadTextures(){
		if (creatureImage!=null){
			return;
		}
		creatureImage = new Texture(Gdx.files.internal("creature.png"));
		eveImage = new Texture(Gdx.files.internal("eve.png"));
		foodImage = new Texture(Gdx.files.internal("food.png"));
	}
	
	/**
	 * Gets the texture.
	 *
	 * @param textureName the texture name
	 * @return the texture
	 */
	public static Texture getTexture(String textureName){
		lazyLoadTextures();
		if (textureName.equals("creature")){
			return creatureImage;
		}
		else if (textureName.equals("food")){
			return foodImage;
		}
		else {
			return eveImage;
		}
		
	}
	
	/**
	 * Dispose.
	 */
	public static void dispose(){
		creatureImage.dispose();
		eveImage.dispose();
	}
	
}
