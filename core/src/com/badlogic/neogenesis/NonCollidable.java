package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class NonCollidable implements ICollidable {
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#stillCollidable()
	 */
	@Override
	public boolean stillCollidable() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getPosition()
	 */
	@Override
	public Vector2 getPosition() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
	}

	@Override
	public Shape2D getShape() {
		return null;
	}

	@Override
	public void collidedWith(ICollidable collidable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collidedWith(GameObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean collidesWith(GameObject other) {
		return false;
	}

	@Override
	public Array<GameObject> collidesWith(Array<GameObject> otherCollidables) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean collidesWith(ICollidable collidable) {
		return false;
	}

	@Override
	public void collidedWith(Devourer devourer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collidedWith(Devourable devourable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vector2 getForce() {
		return new Vector2 (0, 0);
	}
}
