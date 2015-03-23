package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Rectangle;

/**
 * The Class Rock.
 */
public class Rock extends GameObject {
		
	/**
	 * Instantiates a new rock.
	 * @param position the position
	 */
	public Rock(Rectangle position){
		super(new Visible(TextureMap.getTexture("rock")), new Audible(), new NonMovable(position), new Collidable(position), new Living());
		((Visible)drawLogic).setShape(position);
		
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public void collidedWith(ICollidable other) {
		other.collidedWith((Rock)this);
	}
	
	public ICollidable getCollidable(){
		return collideLogic;
	}

}
