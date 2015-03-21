package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The Class Rock.
 */
public class Rock implements Drawable, Collidable{

	/** The id. */
	private ID id;
	
	/** The position. */
	private Rectangle position;
	
	/** The texture. */
	private Texture texture;
	
	/**
	 * Instantiates a new rock.
	 * @param position the position
	 */
	public Rock(Rectangle position){
		this.position=position;
		id = IDFactory.getNewID();
		texture=TextureMap.getTexture("rock");
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID() {
		return id;
	}

	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Drawable#getShape()
	 */
	@Override
	public Shape2D getShape() {
		return position;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getMagnitude()
	 */
	@Override
	public int getMagnitude() {
		return (int) position.getWidth()/100;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#stillCollidable()
	 */
	@Override
	public boolean stillCollidable() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.neogenesis.Collidable)
	 */
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Rectangle){
			overlaps = position.overlaps((Rectangle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps((Circle)other.getShape(),position);
		}
		return overlaps && other.stillCollidable();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.utils.Array)
	 */
	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();
		for (Collidable collidable: otherCollidables){
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
				collidedWith(collidable);
				collidable.collidedWith((Collidable)this);
			}
		}
		return collidedWith;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public void collidedWith(Collidable other) {
		other.collidedWith((Rock)this);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumer)
	 */
	@Override
	public void collidedWith(Consumer consumer) {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumable)
	 */
	@Override
	public void collidedWith(Consumable consumable) {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Drawable#getTexture()
	 */
	@Override
	public Texture getTexture() {
		return texture;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getPosition()
	 */
	@Override
	public Vector2 getPosition() {
		// doesn't return the center
		return new Vector2(position.x, position.y);
	}

}
