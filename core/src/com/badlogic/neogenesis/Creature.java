package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * The Class Creature. Base class of all critters, currently concrete, eventually abstract
 */
public class Creature implements Consumable, Consumer, Mobile, Drawable, Living {

	/** The creature's position. */
	protected Circle position;
	/** The last movement. */
	protected Vector2 lastMovement;
	/** The creature's id. */
	protected ID id;
	/** The creature's biomass. */
	protected int biomass;
	/** The creature's texture. */
	public Texture texture;
	/** The biomass in the creature's belly. */
	protected ObjectSet<Consumable> belly;
	
	protected Consumer inBellyOf;
		
	protected int impetusAmount;
	
	protected int clocktick;
	
	protected ObjectMap<String, Boolean> abilities;
	
	protected AI AI;
	
	protected boolean alive;
	
	/**
	 * Instantiates a new creature.
	 * @param startPosAndSize the start pos and size
	 */
	public Creature(Vector2 startPos){
		this(startPos, 5);
	}
	
	/**
	 * Instantiates a new creature.
	 * @param startPosAndSize the start pos and size
	 * @param biomass the biomass
	 */
	public Creature(Vector2 startPos, int biomass){
		position = new Circle(startPos, biomass/2);
		id = IDFactory.getNewID();
		this.biomass = biomass;
		texture = TextureMap.getTexture("creature");
		lastMovement = new Vector2(0, 0);
		belly = new ObjectSet<Consumable>();
		AI = new HerbivoreAI();
		abilities = new ObjectMap<String, Boolean>();
		abilities.put("sense", false);
		abilities.put("boost", false);
		abilities.put("photosynthesis", false);
		abilities.put("impetus", false);
		
		clocktick = 0;
		alive=true;
	}
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID(){
		return id;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getMagnitude()
	 */
	@Override
	public int getMagnitude() {
		return biomass/500;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beDigested()
	 */
	@Override
	public Food beDigested() {
		die();
		int digestedBiomass = biomass;
		biomass=0;
		return new Food(digestedBiomass, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beDigested()
	 */
	@Override
	public Food beBitten() {
		if (biomass < 1){
			return beDigested();
		}
		biomass-=1;
		return new Food(10, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#getBiomass()
	 */
	public int getBiomass() {
		return biomass;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumer#consume(com.badlogic.gdx.utils.ObjectSet)
	 */
	@Override
	public void ingest (Consumable consumableToIngest){
		belly.add(consumableToIngest);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Mobile#move()
	 */
	@Override
	public Vector3 move() {
		if (inBellyOf==null){
			if (MathUtils.random(1,50)==50){
				lastMovement = AI.amble(position);
				position.x+=lastMovement.x;
				position.y+=lastMovement.y;
			} else {
				lastMovement = AI.forage(position);
				position.x+=lastMovement.x;
				position.y+=lastMovement.y;
			}
			return new Vector3(lastMovement, 0);
		}
		else {
			Vector2 oldPosition = new Vector2(position.x, position.y);
			Vector2 movement = new Vector2(320 * Gdx.graphics.getDeltaTime(), 0);
			// point towards center of inBellyOf
			Vector2 bellyCenter = inBellyOf.getCenter();
			movement = movement.rotate(oldPosition.sub(bellyCenter).angle());
			Vector2 newPosition = new Vector2(oldPosition).add(movement);
			lastMovement = new Vector2(newPosition.x-oldPosition.x, newPosition.y-oldPosition.y);
			position.x = lastMovement.x;
			position.y = lastMovement.y;
			lastMovement = new Vector2(position.x-oldPosition.x, position.y-oldPosition.y);
			return new Vector3(lastMovement, 0);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Drawable#getTexture()
	 */
	@Override
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Consume. Should be a renamed and possibly for consumer interface, along with consume above
	 * @param food the food
	 */
	public void digest(Food food) {
		biomass += food.getNutrition()/10;
	}

	public void digest(Consumable consumableToDigest){
		digest(consumableToDigest.beBitten());
	}
	
	
	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = position.overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps(position, (Rectangle)other.getShape());
		}
		if (overlaps && other.stillCollidable() && id!=other.getID()){	
			other.collidedWith((Consumer)this);
			other.collidedWith((Consumable)this);
			return true;
		}
		return false;
	}

	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();
		
		for (Collidable collidable: otherCollidables){
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
			}
		}
		return collidedWith;
	}

	@Override
	public Shape2D getShape() {
		return position;
	}

	@Override
	public boolean stillCollidable() {
		return alive;
	}

	@Override
	public void collidedWith(Consumer consumer) {
	}

	@Override
	public void collidedWith(Consumable consumable) {
		if (consumable.getBiomass()<biomass && consumable.beIngested(this)){
			ingest(consumable);
		}
	}
	
	@Override
	public void collidedWith(Rock rock) {
		Vector2 oldPosition = new Vector2(position.x, position.y);
		if ( (lastMovement.x > 1 || lastMovement .x < -1) || (lastMovement.y > 1 || lastMovement.y < -1)){
			lastMovement = lastMovement.rotate(180);
			Vector2 newPosition = new Vector2(oldPosition).add(lastMovement);
			position.x=newPosition.x;
			position.y=newPosition.y;
		}
		
		// squeezes the stuck thing out the right side of the rock, needs to properly squeeze out depending on direction of rock collision
		if (collidesWith(rock)){
			position.x++;
			lastMovement = new Vector2(1,0);
		}
		
	}

	public void die(){
		alive=false;
	}

	@Override
	public void live() {
		clocktick++;
		if (abilities.get("photosynthesis") && clocktick%25==0){
			digest(new Food(5));
		}
		if (clocktick%20==0){
			if (belly.size>0){
				ObjectSet<Consumable> toRemove = new ObjectSet<Consumable>();
				for (Consumable consumableToDigest: belly){
					digest(consumableToDigest);
					if (consumableToDigest.getBiomass()<=0){
						toRemove.add(consumableToDigest); 
					}
				}
				for (Consumable needsRemoving: toRemove){
					belly.remove(needsRemoving);
				}
			}
		}
		
		position.radius=biomass/2;
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
	public Vector2 getCenter() {
		return new Vector2 (position.x, position.y);
	}
	
}
