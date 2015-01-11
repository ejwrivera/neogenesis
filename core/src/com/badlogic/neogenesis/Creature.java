package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator;

/**
 * The Class Creature. Base class of all critters, currently concrete, eventually abstract
 */
public class Creature implements Consumable, Consumer, Mobile, Drawable {

	/** The creature's position. */
	protected Circle position;
	/** The last movement. */
	protected Vector2 lastMovement;
	/** The creature's id. */
	protected ID id;
	/** The creature's biomass. */
	protected Integer biomass;
	/** The creature's texture. */
	public Texture texture;
	/** The biomass in the creature's belly. */
	protected Integer undigestedBiomass;
	
	protected AI AI;
	
	/**
	 * Instantiates a new creature.
	 * @param startPosAndSize the start pos and size
	 */
	public Creature(Circle startPosAndSize){
		this(startPosAndSize, 5);
	}
	
	/**
	 * Instantiates a new creature.
	 * @param startPosAndSize the start pos and size
	 * @param biomass the biomass
	 */
	public Creature(Circle startPosAndSize, int biomass){
		position = startPosAndSize;
		id = IDFactory.getNewID();
		this.biomass = biomass;
		texture = TextureMap.getTexture("creature");
		lastMovement = new Vector2(0, 0);
		position.radius=biomass;
		undigestedBiomass = 0;
		AI = new HerbivoreAI();
	}
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Identifiable#getID()
	 */
	@Override
	public ID getID(){
		return id;
	}
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidesWith(com.badlogic.gdx.math.Circle)
	 */
	@Override
	public Boolean collidesWith(Circle other) {
		return position.overlaps(other);
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
		return new Food(biomass);
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
	public ObjectSet<ID> consume (ObjectSet<Consumable> appropriatelySizedConsumables){
		if (undigestedBiomass > 0){
			biomass++;
			undigestedBiomass--;
		}
		position.radius=biomass/2;
		ObjectSet<ID> toRemove = new ObjectSet<ID>();
		ObjectSetIterator<Consumable> consumables = appropriatelySizedConsumables.iterator();
		while (consumables.hasNext()) {
			Consumable consumable = consumables.next();
			if (!toRemove.contains(consumable.getID()) && id!=consumable.getID() && consumable.collidesWith(position)) {
				if (biomass>consumable.getBiomass()){
					consume(consumable.beConsumed());
					toRemove.add(consumable.getID());
				}
			}
		}
		return toRemove;
	}
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Mobile#move()
	 */
	@Override
	public Vector3 move() {
		if (MathUtils.random(1,20)==20){
			AI.amble(position);
		}else {
			lastMovement = AI.forage(position);
			position.x+=lastMovement.x;
			position.y+=lastMovement.y;
		}
		return new Vector3(lastMovement, 0);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Drawable#getCircle()
	 */
	@Override
	public Circle getCircle() {
		return position;
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
}
