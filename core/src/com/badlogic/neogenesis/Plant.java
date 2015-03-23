package com.badlogic.neogenesis;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * The Plant class. For non-motile organic stuff.
 */
public class Plant extends GameObject {
	
	/** The size. */
	private float size;
	/** The biomass. */
	private int biomass;
	/** The alive. */
	private boolean alive;
	
	/**
	 * Instantiates a new plant.
	 *
	 * @param biomass the biomass
	 * @param position the position
	 */
	public Plant(int biomass, Circle position){
		super(new Visible(TextureMap.getTexture("food")), new Audible(), new Movable(new Vector2(position.x, position.y), new PlantAITEMP()), new Collidable(position), new Living());
		this.biomass=biomass;
		this.size = position.radius;
		;
		alive = true;
	}
	
	/**
	 * Gets the nutrition.
	 * @return the nutrition
	 */
	public int getNutrition(){
		return biomass;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Mobile#move()
	 */
	@Override
	public void move() {
		((Movable)moveLogic).setCircle(getCircle());
		super.move();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Collidable)
	 */
	@Override
	public void collidedWith(ICollidable other){
		super.collidedWith(other);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#collidedWith(com.badlogic.neogenesis.Rock)
	 */
	@Override
	public void collidedWith(Rock rock) {
		Vector2 oldPosition = new Vector2(((Movable)moveLogic).getPosition().x, ((Movable)moveLogic).getPosition().y);
		((Movable)moveLogic).lastMovement = ((Movable)moveLogic).lastMovement.rotate(180);
		Vector2 newPosition = new Vector2(oldPosition).add(((Movable)moveLogic).lastMovement);
		((Movable)moveLogic).getPosition().x=newPosition.x;
		((Movable)moveLogic).getPosition().y=newPosition.y;
		((Movable)moveLogic).lastMovement = new Vector2(0,0);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Collidable#getShape()
	 */
	@Override
	public Shape2D getShape() {
		return getCircle();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#die()
	 */
	public void die(){
		alive=false;
		collideLogic = new NonCollidable();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#getCorpse()
	 */
	public Corpse getCorpse(){
		if (!alive){
			return new Corpse();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#live()
	 */
	@Override
	public void live() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#isAlive()
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Gets the circle.
	 * @return the circle
	 */
	private Circle getCircle() {
		return new Circle(((Movable)moveLogic).getPosition().x, ((Movable)moveLogic).getPosition().y, size);
	}
	
	public Vector2 getPosition() {
		return ((Movable)moveLogic).getPosition();
	}
	
}
