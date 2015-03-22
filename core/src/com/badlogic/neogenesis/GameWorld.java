package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * The Class GameWorld.
 */
public class GameWorld {
	/** The eve. */
	private Eve eve;
	/** The living things. */
	private ObjectMap<ID, Living> theLiving;
	/** The mobile objects. */
	private ObjectMap<ID, Mobile> mobs;	
	/** Things to check for collision. */
	private ObjectMap<ID, Collidable> collidables;
	/** Things to draw. */
	private ObjectMap<ID, Drawable> drawables;
	/** Things to emit sound. */
	private ObjectMap<ID, Audible> audibles;
	/** Things to potentially clean up. */
	private ObjectMap<ID, Destructible> destructibles;
	
	/** The last spawn time. */
	private long lastSpawnTime;
	
	/**  Toggles whether magnitude is checked for colliding. */
	private boolean magnitudeColliding;
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
		
		// initialize maps
		theLiving = new ObjectMap<ID, Living>();
		mobs = new ObjectMap<ID, Mobile>();
		collidables = new ObjectMap<ID, Collidable>();
		drawables = new ObjectMap<ID, Drawable>();
		audibles = new ObjectMap<ID, Audible>();
		destructibles = new ObjectMap<ID, Destructible>();
		
		// create Eve
		int biomass = (Integer) (loadGame ? game.saveManager.loadDataValue("biomass",int.class) : DebugValues.getEveStartingBiomass());
		
		eve = new Eve(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), biomass);
		addToMaps(eve);	
	
		// initialize flags
		displayHUD = false;
		paused = false;
		gameOver = false;
		gameExit = false;
		soundStack = 0;
		magnitudeColliding = DebugValues.getMagnitudeColliding();
		
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
	 * Adds a creature to the maps.
	 * @param creature the creature
	 */
	private void addToMaps(Creature creature) {
		ID id = creature.getID();
		theLiving.put(id, creature);
		mobs.put(id, creature);
		collidables.put(id, creature);
		drawables.put(id, creature);
		destructibles.put(id, creature);
		audibles.put(id, creature);
	}
	
	/**
	 * Adds a plant to the maps.
	 * @param plant the plant
	 */
	private void addToMaps(Plant plant) {
		ID id = plant.getID();
		theLiving.put(id, plant);
		collidables.put(id, plant);
		drawables.put(id, plant);
		mobs.put(id, plant);
		destructibles.put(id, plant);
	}
	/**
	 * Adds a rock to the maps.
	 * @param rock the rock
	 */
	private void addToMaps(Rock rock) {
		ID id = rock.getID();
		collidables.put(id, rock);
		drawables.put(id, rock);
	}
	
	/**
	 * Removes something from all the appropriate maps
	 * @param id the id of the thing
	 */
	private void removeFromMaps(ID id) {
		if (theLiving.containsKey(id)){
			theLiving.remove(id);
		}
		if (mobs.containsKey(id)){
			mobs.remove(id);
		}
		if (collidables.containsKey(id)){
			collidables.remove(id);
		}
		if (drawables.containsKey(id)){
			drawables.remove(id);
		}	
		if (audibles.containsKey(id)){
			audibles.remove(id);
		}
		if (destructibles.containsKey(id)){
			destructibles.remove(id);
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
		if (!creature.collidesWith((Collidable)eve)){
			addToMaps(creature);
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
		addToMaps(new Plant(1, new Circle(MathUtils.random(0, 9600), MathUtils.random(0, 7200), 4)));
		return true;
	}

	/**
	 * Spawn rock.
	 * @return true, if successful
	 */
	private boolean spawnRock() {
		Rock rock = new Rock(new Rectangle(MathUtils.random(0, 9600), MathUtils.random(0, 7200), 15, 15));
		boolean spawned=false;
		if (!isCollision(rock)){
			addToMaps(rock);
			spawned=true;
		}
		return spawned;
	}
	
	/**
	 * Checks if is collision.
	 * @param collidableToCheck the collidable to check
	 * @return true, if is collision
	 */
	private boolean isCollision(Collidable collidableToCheck){
		for (Collidable collidable: collidables.values()){
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
		ObjectSet<ID> toRemove = new ObjectSet<ID>();
		// everything that lives must live
		for (Living thing : theLiving.values()) {
			thing.live();
			if (!thing.isAlive()&&thing.getCorpse().plant!=null){
				addToMaps(thing.getCorpse().plant);
			}
		}
		for (Destructible thing: destructibles.values()){
			if (!thing.isDestroyed()){
				toRemove.add(thing.getID());
			}
		}
		
		for (ID id: toRemove){
			if (eve.getID()==id){
				gameOver = true;
			}
			removeFromMaps(id);
		}
		
		// move everything that moves
		for (Mobile mob : mobs.values()) {
			mob.move();
		}
		
		// check if we need to create a new creature
		if (TimeUtils.nanoTime()-lastSpawnTime > 200000000/DebugValues.getSpawnRate())
			spawnCreature();
		// check for collisions
		if (magnitudeColliding){
			IntMap<ObjectSet<Collidable>> magnitudeMap = generateCollidableMagnitudeMap();
			for (Collidable collidable: collidables.values()){
				if (collidable.stillCollidable()){
					ObjectSet<Collidable> appropriatelySizedCollidables = new ObjectSet<Collidable>();
					for (int ii = -1; ii <= 0; ii++){
						if (magnitudeMap.containsKey(collidable.getMagnitude()+ii)){
							appropriatelySizedCollidables.addAll(magnitudeMap.get(collidable.getMagnitude()+ii));
						}
					}
				}
			}
		}
		else {
			Array<Collidable> collidablesToCheck = new Array<Collidable>(collidables.values().toArray());
			for (Collidable collidable: collidables.values()){
				collidablesToCheck.removeValue(collidable, true);
				if (collidable.stillCollidable()){
					collidable.collidesWith(collidablesToCheck); 
				}
				
			}
		}
		
		for (Audible audible: audibles.values()){
			if (audible.emittingSound()){
				soundStack++;
				audible.emitSound();
			}	
		}
	}

	/**
	 * Generate consumable magnitude map.
	 * @return the map of integer to collidables in that size class
	 */
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
	}
	
	/**
	 * Gets the drawables.
	 * @return the drawables for rendering
	 */
	public ObjectMap<ID, Drawable> getDrawables(){
		return drawables;
	}
	
	/**
	 * Gets the eve.
	 * @return the eve
	 */
	public Eve getEve() {
		return eve;
	}
	
}
