package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Collidable implements ICollidable {

	public Shape2D position;
	
	public Collidable (Shape2D startPos){
		this.position = startPos;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#stillCollidable()
	 */
	@Override
	public boolean stillCollidable() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getPosition()
	 */
	@Override
	public Vector2 getPosition() {
		try {
			return new Vector2(((Circle)position).x, ((Circle)position).y);
		}
		catch (Exception ex){
			return new Vector2(((Rectangle)position).x, ((Rectangle)position).y);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.utils.Array)
	 */
	@Override
	public Array<GameObject> collidesWith(Array<GameObject> otherCollidables) {
		// this should be called internally to grab all the GameObject that have been collided with and perform some kind of triage for them
		Array<GameObject> collidedWith = new Array<GameObject>();
		for (GameObject collidable: otherCollidables){
			if (collidable.stillCollidable() && collidesWith(collidable)){
				collidedWith.add(collidable);
				collidedWith(collidable);
				
			}
		}
		return collidedWith;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public Boolean collidesWith(GameObject other) {
		boolean overlaps;
		if (position instanceof Circle){
			if (other.getShape() instanceof Circle){
				overlaps = ((Circle)position).overlaps((Circle)other.getShape());
			}
			else {
				overlaps = Intersector.overlaps(((Circle)position), (Rectangle)other.getShape());
			}
		}
		else {
			if (other.getShape() instanceof Rectangle){
				overlaps = ((Rectangle)position).overlaps((Rectangle)other.getShape());
			}
			else {
				overlaps = Intersector.overlaps((Circle)other.getShape(), ((Rectangle)position));
			}
		}
		
		return overlaps && other.stillCollidable();
	}
	
	@Override
	public boolean collidesWith(ICollidable other) {
		boolean overlaps;
		if (position instanceof Circle){
			if (other.getShape() instanceof Circle){
				overlaps = ((Circle)position).overlaps((Circle)other.getShape());
			}
			else {
				overlaps = Intersector.overlaps(((Circle)position), (Rectangle)other.getShape());
			}
		}
		else {
			if (other.getShape() instanceof Rectangle){
				overlaps = ((Rectangle)position).overlaps((Rectangle)other.getShape());
			}
			else {
				overlaps = Intersector.overlaps((Circle)other.getShape(), ((Rectangle)position));
			}
		}
		
		return overlaps && other.stillCollidable();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
	}
	@Override
	public Shape2D getShape() {
		return position;
	}
	@Override
	public void collidedWith(ICollidable collidable) {
	}
	
	@Override
	public void collidedWith(GameObject object) {	
	}
	
}
