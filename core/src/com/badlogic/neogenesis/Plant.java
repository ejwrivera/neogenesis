package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * The Class Food. For packaging together nutrition information of a given edible delight
 */
public class Plant implements Consumable, Drawable, Mobile, Living, Destructible {
	/** The ID. */
	private ID id;
	/** Whether or not this food has been consumed. */
	private boolean consumed;
	/** The texture. */
	private Texture texture;
	/** The position. */
	private Vector2 position;
	
	private float size;
	
	private int biomass;
	
	protected Consumer inBellyOf;
	
	private boolean alive;
	
	private Vector2 lastMovement;
	/**
	 * Instantiates a new food.
	 * @param nutrition the nutrition
	 */
	public Plant(int biomass, Circle position){
		this.biomass=biomass;
		this.position=new Vector2(position.x, position.y);
		this.size = position.radius;
		id = IDFactory.getNewID();
		consumed=false;
		texture = TextureMap.getTexture("food");
		alive = true;
		lastMovement=new Vector2(0,0);
	}
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return biomass;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beDigested()
	 */
	@Override
	public Food beDigested() {
		die();
		int digestedBiomass = biomass;
		biomass=0;
		return new Food(digestedBiomass);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beDigested()
	 */
	@Override
	public Food beBitten() {
		if (biomass <= 1){
			return beDigested();
		}
		biomass-=2;
		return new Food(1, 1);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#getBiomass()
	 */
	@Override
	public int getBiomass() {
		return biomass;
	}
	
	public boolean beenConsumed() {
		return consumed;
	}

	@Override
	public int getMagnitude() {
		return -1;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	@Override
	public Vector3 move() {
		if (inBellyOf==null){
			Vector2 oldPosition = new Vector2(position.x, position.y);
			Vector2 movement = new Vector2(10 * Gdx.graphics.getDeltaTime(), 0);
			movement = movement.rotate(MathUtils.random(1, 45));
			Vector2 newPosition = new Vector2(oldPosition).add(movement);
			position.x=newPosition.x;
			position.y=newPosition.y;
			lastMovement = newPosition.sub(oldPosition);
			return new Vector3(newPosition.x-oldPosition.x, newPosition.y-oldPosition.y, 0);
		}
		else {
			Vector2 newPosition;
			Vector2 bellyCenter = inBellyOf.getCenter();
			Vector2 oldPosition = new Vector2(position.x, position.y);
			if (Math.abs(position.x-bellyCenter.x)+Math.abs(position.y-bellyCenter.y)<6){
				newPosition = bellyCenter;
			}
			else{
				Vector2 movement = new Vector2(320 * Gdx.graphics.getDeltaTime(), 0);
				// point towards center of inBellyOf
				
				movement = movement.rotate(bellyCenter.sub(oldPosition).angle());
				newPosition = new Vector2(oldPosition).add(movement);
			}
			position.x = newPosition.x;
			position.y = newPosition.y;
			lastMovement = new Vector2(position.x-oldPosition.x, position.y-oldPosition.y);
			return new Vector3(lastMovement, 0);
		}
	}

	@Override
	public boolean stillCollidable() {
		return alive;
	}
	
	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = (getCircle()).overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps(getCircle(), (Rectangle)other.getShape());
		}
		return overlaps && other.stillCollidable();	
	}

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

	@Override
	public void collidedWith(Collidable other){
		other.collidedWith((Consumable)this);
	}
	
	@Override
	public void collidedWith(Consumer consumer) {
	}

	@Override
	public void collidedWith(Consumable consumable) {	
	}
	
	@Override
	public void collidedWith(Rock rock) {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		lastMovement = lastMovement.rotate(180);
		Vector2 newPosition = new Vector2(oldPosition).add(lastMovement);
		position.x=newPosition.x;
		position.y=newPosition.y;
		lastMovement = new Vector2(0,0);
	}
	
	@Override
	public Shape2D getShape() {
		return getCircle();
	}
	
	public void die(){
		alive=false;
	}

	public Corpse getCorpse(){
		if (!alive){
			return new Corpse();
		}
		return null;
	}
	
	@Override
	public void live() {
	}

	@Override
	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public boolean beIngested(Consumer consumer) {
		inBellyOf = consumer;
		return true;
	}

	@Override
	public boolean isDestroyed() {
		return isAlive();
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	private Circle getCircle() {
		return new Circle(position.x, position.y, size);
	}
	
}
