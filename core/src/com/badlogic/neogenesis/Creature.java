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
public class Creature implements Consumable, Consumer, Mobile, Drawable, Living, Destructible {
	
	/** The creature's position. */
	protected Vector2 position;
	/** The creature's ideal position. */
	protected Vector2 movingTowards;
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
	
	protected Sense sense;
	
	private boolean hunting;
	
	private int undigestedBiomass;
	
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
		position = startPos;
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
		alive = true;
		hunting = false;
		
		sense = new Sense(position,200);
		undigestedBiomass = 0;
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
		if (biomass <= 1){
			return beDigested();
		}
		biomass-=2;
		return new Food(1, 1);
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
			if (hunting&&movingTowards!=null){
				Vector2 oldPosition = new Vector2(position.x, position.y);
				Vector2 movement = new Vector2(50 * Gdx.graphics.getDeltaTime(), 0);
				Vector2 temp = new Vector2(movingTowards);
				movement = movement.rotate(temp.sub(oldPosition).angle());
				Vector2 newPosition = new Vector2(oldPosition).add(movement);
				position.x = newPosition.x;
				position.y = newPosition.y;
				lastMovement = new Vector2(position.x-oldPosition.x, position.y-oldPosition.y);
				return new Vector3(lastMovement, 0);		
			}
			else{
				if (MathUtils.random(1,50)==50){
					lastMovement = AI.amble(getCircle());
					position.x+=lastMovement.x;
					position.y+=lastMovement.y;
				} else {
					lastMovement = AI.forage(getCircle());
					position.x+=lastMovement.x;
					position.y+=lastMovement.y;
				}
				return new Vector3(lastMovement, 0);
			}
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
		biomass += food.getNutrition();
	}

	public void digest(Consumable consumableToDigest){
		digest(consumableToDigest.beBitten());
	}
	
	
	@Override
	public boolean stillCollidable() {
		return alive;
	}
	
	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();
		
		Consumable potentialPrey=null;
		Vector2 potentialDirection=null;
		for (Collidable collidable: otherCollidables){
			
			collidable.collidedWith(sense);
			if (potentialPrey!=sense.getSensed()){
				potentialPrey=sense.getSensed();
				if (potentialDirection==null){
					potentialDirection = potentialPrey.getPosition(); 
					hunting = true;
				}
				else {
					if (position.dst(potentialDirection)>position.dst(potentialPrey.getPosition())){
						potentialDirection=potentialPrey.getPosition();
						hunting = true;
					}
				}
			}
			
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
				collidedWith(collidable);
				collidable.collidedWith((Collidable)this);
			}
		}
		movingTowards = potentialDirection;
		return collidedWith;
	}
	
	@Override
	public Boolean collidesWith(Collidable other) {
		boolean overlaps;
		if (other.getShape() instanceof Circle){
			overlaps = getCircle().overlaps((Circle)other.getShape());
		}
		else {
			overlaps = Intersector.overlaps(getCircle(), (Rectangle)other.getShape());
		}
		return overlaps && other.stillCollidable();
	}

	public void collidedWith(Collidable other){
		other.collidedWith((Consumer)this);
		other.collidedWith((Consumable)this);
	}
	
	@Override
	public void collidedWith(Consumer consumer) {
	}

	@Override
	public void collidedWith(Consumable consumable) {
		
		if (inBellyOf==null && consumable.getBiomass()<biomass && consumable.beIngested(this)){
			ingest(consumable);
		}
		hunting = false;
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
	
	@Override
	public Shape2D getShape() {
		return getCircle();
	}

	public void die(){
		alive=false;
	}

	@Override
	public void live() {
		clocktick++;
		if (abilities.get("photosynthesis") && clocktick%25==0){
			digest(new Food(1));
		}
		if (clocktick%4==0){
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
		return new Vector2(position);
	}

	public Circle getCircle() {
		return new Circle(position, biomass/2);
	}
	
	@Override
	public boolean isDestroyed() {
		return isAlive();
	}

	@Override
	public Vector2 getPosition() {
		return new Vector2(position);
	}
	
}
