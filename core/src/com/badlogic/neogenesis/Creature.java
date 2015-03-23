package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The Class Creature. Base class of all critters, currently concrete, eventually abstract
 */
public class Creature extends GameObject {
	
	/* properties */
	
	/** The clocktick. */
	protected int clocktick;
	
	
	/** Whether a creature is alive or dead. */
	protected boolean alive;
	/** The health. */
	protected int health;
	/** The creature's biomass. */
	protected int biomass;
	/** The abilities. */
	protected ObjectMap<String, Boolean> abilities;
	
	private Sense sense;	
	
	/* temporary properties */

	/** The impetus amount. */
	protected int impetusAmount;
	
	/**
	 * Instantiates a new creature.
	 * @param startPos the starting position
	 * @param biomass the starting biomass
	 */
	public Creature(Vector2 startPos, int biomass){
		super(new Visible(TextureMap.getTexture("creature")), new Audible(), new Movable(startPos, new HerbivoreAI()), new Collidable(new Circle(startPos, biomass/2)), new Living());
		
		this.biomass = biomass;
		
		clocktick = 0;
		alive = true;
		health = 5;
		abilities = new ObjectMap<String, Boolean>();
		abilities.put("sense", false);
		abilities.put("boost", false);
		abilities.put("photosynthesis", false);
		abilities.put("impetus", false);
		
		sense = new Sense (startPos, 200);
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
	
	public Corpse getCorpse(){
		if (!isAlive()){
			return new Corpse(new Plant(biomass, getCircle()));
		}
		return null;
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
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Consumable#getBiomass()
	 */
	public int getBiomass() {
		return biomass;
	}
	
	/**
	 * Gets the drawable/collidable circle.
	 * @return the circle
	 */
	protected Circle getCircle() {
		return new Circle(((Movable)moveLogic).getPosition(), biomass/2);
	}
	
	public Array<GameObject> collidesWith(Array<GameObject> otherCollidables) {
		Array<GameObject> collidedWith = super.collidesWith(otherCollidables);
		Prey potentialPrey=null;
		Vector2 potentialDirection=null;
		for (GameObject collidable: otherCollidables){
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
				collidable.collidedWith((GameObject)this);
			}
		}
		((Movable)moveLogic).setMovingTowards(potentialDirection);
		return collidedWith;
	}
	
	@Override
	public void collidedWith(Rock rock) {
		super.collidedWith(rock);
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
		if (collidesWith(rock.getCollidable())){
			position.x++;
			((Movable)moveLogic).lastMovement = new Vector2(1,0);
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public void collidedWith(GameObject other){
		super.collidedWith(other);
	}
	
}
