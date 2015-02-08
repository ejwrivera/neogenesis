package com.badlogic.neogenesis;

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
	protected int undigestedBiomass;
		
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
		undigestedBiomass = 0;
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
	 * @see com.badlogic.neogenesis.Consumable#beConsumed()
	 */
	@Override
	public Food beConsumed() {
		die();
		return new Food(biomass, 1);
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
	public void consume (Consumable consumablesToConsume){
		consume(consumablesToConsume.beConsumed());
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Mobile#move()
	 */
	@Override
	public Vector3 move() {
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
	public void consume(Food food) {
		undigestedBiomass += food.getNutrition()/10;
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
		if (consumable.getBiomass()<biomass){
			consume (consumable.beConsumed());
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
			undigestedBiomass++;
		}
		if (undigestedBiomass > 0){
			biomass++;
			undigestedBiomass--;
		}
		position.radius=biomass/2;
	}
	
}
