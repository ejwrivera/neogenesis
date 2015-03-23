package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * The Class GameWorld.
 */
public class GameWorld {
	/** The eve. */
	private Eve eve;
	
	private Array<GameObject> gameObjects;
	
	/** The last spawn time. */
	private long lastSpawnTime;
	
	/**  Toggles whether magnitude is checked for colliding. 
	private boolean magnitudeColliding;*/
	/**  Toggles the display of the info at the top, biomass and location. */
	public boolean displayHUD;
	/** Toggles if the game paused. */
	public boolean paused;
	/** Used to determine when the game is over. */
	public boolean gameOver;
	/** Used to determine when the game is exited. */
	public boolean gameExit;
	
	/** The sound stack. */
	public int soundStack;
	
	/**
	 * Instantiates a new game world.
	 * @param game the game
	 * @param loadGame whether this is a new or loaded game
	 */
	public GameWorld(final Neogenesis game, boolean loadGame) {
		DebugValues.debug=true; // set to true to use current debug values
		DebugValues.populateDebugValues(2); // 1 = godzilla mode, 2 = quick start
		
		gameObjects = new Array<GameObject>();	
		
		// create Eve
		int biomass = (Integer) (loadGame ? game.saveManager.loadDataValue("biomass",int.class) : DebugValues.getEveStartingBiomass());
		
		eve = new Eve(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), biomass);
		gameObjects.add(eve);	
	
		// initialize flags
		displayHUD = false;
		paused = false;
		gameOver = false;
		gameExit = false;
		soundStack = 0;
		//magnitudeColliding = DebugValues.getMagnitudeColliding();
		
		int creatureAmount = DebugValues.getCreatureAmount();
		int foodAmount = DebugValues.getFoodAmount();
		int rockAmount = DebugValues.getRockAmount();
		// spawn the first mega creatures
		int tries = 0;
		while (!spawnCreature(1000)&&tries<100){
			tries++;
		}
		// spawn the small creatures
		for (int ii = 0; ii < creatureAmount; ii++){
			spawnCreature();
		}
		// spawn the food
		for (int ii = 0; ii < foodAmount; ii++){
			spawnPlant();
		}
		// spawn the rocks
		for (int ii = 0; ii < rockAmount; ii++){
			spawnRock();
		}
	}
	
	/**
	 * Spawn creature, size randomly determined
	 * @return true, if successful
	 */
	private boolean spawnCreature() {
		int size;
		
		if (MathUtils.random(1,500)==500){
			size = MathUtils.random(50, 400)*5;
		}
		else if (MathUtils.random(1,50)==50){
			size = MathUtils.random(4, 20)*5;
		}
		else if (MathUtils.random(1,2)==2){
			size = 5;
		}
		else{
			size = MathUtils.random(2, 3)*5;
		}
		return spawnCreature (size);
	}
	
	/**
	 * Spawn creature.
	 * @param size the size
	 * @return true, if successful
	 */
	private boolean spawnCreature (int size){
		boolean spawned = false;
		Creature creature = new Creature(new Vector2(MathUtils.random(0, 9600), MathUtils.random(0, 7200)), size);
		if (!creature.collidesWith(eve.getCollide())){
			gameObjects.add(creature);
			spawned = true;
			lastSpawnTime = TimeUtils.nanoTime();
		}
		return spawned;
	}
	
	/**
	 * Spawn food.
	 * @return true, if successful
	 */
	private boolean spawnPlant() {
		gameObjects.add(new Plant(1, new Circle(MathUtils.random(0, 9600), MathUtils.random(0, 7200), 4)));
		return true;
	}

	/**
	 * Spawn rock.
	 * @return true, if successful
	 */
	private boolean spawnRock() {
		Rock rock = new Rock(new Rectangle(MathUtils.random(0, 9600), MathUtils.random(0, 7200), 15, 15));
		boolean spawned=false;
		if (!isCollision(rock.getCollidable())){
			gameObjects.add(rock);
			spawned=true;
		}
		return spawned;
	}
	
	/**
	 * Checks if is collision.
	 * @param collidableToCheck the collidable to check
	 * @return true, if is collision
	 */
	private boolean isCollision(ICollidable collidableToCheck){
		for (GameObject collidable: gameObjects){
			if (collidableToCheck.collidesWith(collidable)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Game loop.
	 */
	public void gameLoop(){
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			paused = !paused;
		}
		if (Gdx.input.isKeyJustPressed(Keys.TAB)){
			displayHUD=!displayHUD;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			gameExit = true;
		}
		if (!paused){
			gameIncrement();
		}
	}
	
	/**
	 * Increment the game state to the next game state.  
	 */
	public void gameIncrement(){
		eve.setInput(Gdx.input);
		Array<GameObject> toRemove = new Array<GameObject>();
		// everything that lives must live
		for (GameObject object: gameObjects){
			object.live();
			if (!object.isAlive()){
				toRemove.add(object);
				if (object instanceof Eve){
					gameOver = true;
				}
				if (object.getCorpse().plant!=null){
					gameObjects.add(object.getCorpse().plant);
				}
			}
		}
		
		gameObjects.removeAll(toRemove, true);
		
		Array<GameObject> collidablesToCheck = new Array<GameObject>(gameObjects);
		for (GameObject object: gameObjects){
			object.move();
			
			collidablesToCheck.removeValue(object, true);
			if (object.stillCollidable()){
				object.collidesWith(collidablesToCheck); 
			}
			
			if (object.emittingSound()){
				soundStack++;
				object.emitSound();
			}
		}
		
		
		
		// check if we need to create a new creature
		if (TimeUtils.nanoTime()-lastSpawnTime > 200000000/DebugValues.getSpawnRate())
			spawnCreature();
	}

	/**
	 * Generate consumable magnitude map.
	 * @return the map of integer to collidables in that size class
	 *
	private IntMap<ObjectSet<Collidable>> generateCollidableMagnitudeMap() {
		IntMap<ObjectSet<Collidable>> magnitudeMap = new IntMap<ObjectSet<Collidable>>();
		for (Collidable collidable: collidables.values()){
			int magnitude = collidable.getMagnitude();
			if (!magnitudeMap.containsKey(magnitude)){
				magnitudeMap.put(magnitude, new ObjectSet<Collidable>());
			}
			magnitudeMap.get(magnitude).add(collidable);
		}
		return magnitudeMap;
	}*/
	
	/**
	 * Gets the drawables.
	 * @return the drawables for rendering
	 */
	public Array<GameObject> getDrawables(){
		return gameObjects;
	}
	
	/**
	 * Gets the eve.
	 * @return the eve
	 */
	public Eve getEve() {
		return eve;
	}
	
}
