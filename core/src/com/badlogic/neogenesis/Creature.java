package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * The Class Creature. Base class of all critters, currently concrete, eventually abstract
 */
public class Creature extends GameObject implements Consumer, Consumable, Living, Destructible {
	
	/* properties */
	

	/** The creature's id. */
	protected ID id;
	/** The clocktick. */
	protected int clocktick;
	
	
	/** Whether a creature is alive or dead. */
	protected boolean alive;
	/** The health. */
	protected int health;
	/** The creature's biomass. */
	protected int biomass;
	/** The creature's sense. */
	protected Sense sense;	
	/** The abilities. */
	protected ObjectMap<String, Boolean> abilities;
	
	/* temporary properties */
	
	/** The things in the creature's belly. */
	protected ObjectSet<Consumable> belly;
	/** The in belly of. */
	protected Consumer inBellyOf;
	/** The impetus amount. */
	protected int impetusAmount;
	
	/**
	 * Instantiates a new creature.
	 * @param startPos the starting position
	 * @param biomass the starting biomass
	 */
	public Creature(Vector2 startPos, int biomass){
		super(new Visible(TextureMap.getTexture("creature")), new Audible(), new Movable(startPos, new HerbivoreAI()), new Collidable2(), new Living2());
		
		this.biomass = biomass;
		id = IDFactory.getNewID();
		sense = new Sense(startPos,200);
		
		clocktick = 0;
		alive = true;
		health = 5;
		abilities = new ObjectMap<String, Boolean>();
		abilities.put("sense", false);
		abilities.put("boost", false);
		abilities.put("photosynthesis", false);
		abilities.put("impetus", false);
		
		belly = new ObjectSet<Consumable>();
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
	 * @see com.badlogic.neogenesis.Collidable#stillCollidable()
	 */
	@Override
	public boolean stillCollidable() {
		return alive;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Mobile#move()
	 * This needs a major overhaul; this should be at most fifteen lines without branching
	 */
	@Override
	public void move() {
		((Movable)moveLogic).setCircle(getCircle());
		super.move();
		((Visible)drawLogic).setShape(getCircle());
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Destructible#isDestroyed()
	 */
	@Override
	public boolean isDestroyed() {
		return isAlive();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#isAlive()
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#die()
	 */
	public void die(){
		alive=false;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#getCorpse()
	 */
	public Corpse getCorpse(){
		if (!alive){
			return new Corpse(new Plant(biomass, getCircle()));
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#live()
	 */
	@Override
	public void live() {
		clocktick++;
		if (inBellyOf!=null && clocktick%100==0){
			health--;
		}
		if (clocktick%500==0 && !(this instanceof Eve)){
			health--;
		}
		
		if (health <= 0){
			die();
		}
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
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getPosition()
	 */
	@Override
	public Vector2 getPosition() {
		return new Vector2(((Movable)moveLogic).getPosition());
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.utils.Array)
	 */
	@Override
	public Array<Collidable> collidesWith(Array<Collidable> otherCollidables) {
		Array<Collidable> collidedWith = new Array<Collidable>();
		Consumable potentialPrey=null;
		Vector2 potentialDirection=null;
		for (Collidable collidable: otherCollidables){
			// have the collidable collide with the sense and have the sense sniff it out
			collidable.collidedWith(sense);
			if (potentialPrey!=sense.getSensed()&&sense.getSensed().getBiomass()<biomass){
				potentialPrey=sense.getSensed();
				if (potentialDirection==null){
					potentialDirection = potentialPrey.getPosition(); 
					((Movable)moveLogic).setHunting(true);
				}
				else {
					if (((Movable)moveLogic).getPosition().dst(potentialDirection)>((Movable)moveLogic).getPosition().dst(potentialPrey.getPosition())){
						potentialDirection=potentialPrey.getPosition();
						((Movable)moveLogic).setHunting(true);
					}
				}
			}
			if (collidesWith(collidable)){
				collidedWith.add(collidable);
				collidedWith(collidable);
				collidable.collidedWith((Collidable)this);
			}
		}
		((Movable)moveLogic).setMovingTowards(potentialDirection);
		return collidedWith;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.neogenesis.Collidable)
	 */
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
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	public void collidedWith(Collidable other){
		other.collidedWith((Consumer)this);
		other.collidedWith((Consumable)this);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumer)
	 */
	@Override
	public void collidedWith(Consumer consumer) {}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Consumable)
	 */
	@Override
	public void collidedWith(Consumable consumable) {
		if (inBellyOf==null && consumable.getBiomass()<biomass && consumable.beIngested(this)){
			ingest(consumable);
		}
		((Movable)moveLogic).setHunting(false);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
		// this needs to be changed so that force is applied
		Vector2 position = ((Movable)moveLogic).getPosition();
		
		Vector2 oldPosition = new Vector2(position.x, position.y);
		if ( (((Movable)moveLogic).lastMovement.x > 1 || ((Movable)moveLogic).lastMovement .x < -1) || (((Movable)moveLogic).lastMovement.y > 1 || ((Movable)moveLogic).lastMovement.y < -1)){
			((Movable)moveLogic).lastMovement = ((Movable)moveLogic).lastMovement.rotate(180);
			Vector2 newPosition = new Vector2(oldPosition).add(((Movable)moveLogic).lastMovement);
			position.x=newPosition.x;
			position.y=newPosition.y;
		}
		// squeezes the stuck thing out the right side of the rock, needs to properly squeeze out depending on direction of rock collision
		if (collidesWith(rock)){
			position.x++;
			((Movable)moveLogic).lastMovement = new Vector2(1,0);
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getShape()
	 */
	@Override
	public Shape2D getShape() {
		return getCircle();
	}
	
	/**
	 * Digests food.
	 * @param food the food
	 */
	public void digest(Food food) {
		biomass += food.getNutrition();
	}
	
	/**
	 * Digest, 'bites' a consumable to get some food to actually digest
	 * @param consumableToDigest the consumable to digest
	 */
	public void digest(Consumable consumableToDigest){
		digest(consumableToDigest.beBitten());
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#beIngested(com.badlogic.neogenesis.Consumer)
	 */
	@Override
	public boolean beIngested(Consumer consumer) {
		inBellyOf = consumer;
		((Movable)moveLogic).setInBellyOf(consumer);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumer#getCenter()
	 */
	@Override
	public Vector2 getCenter() {
		return new Vector2(((Movable)moveLogic).getPosition());
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
		if (!belly.contains(consumableToIngest)){
			belly.add(consumableToIngest);
		}
	}
	
	/**
	 * Gets the drawable/collidable circle.
	 * @return the circle
	 */
	protected Circle getCircle() {
		return new Circle(((Movable)moveLogic).getPosition(), biomass/2);
	}
}
