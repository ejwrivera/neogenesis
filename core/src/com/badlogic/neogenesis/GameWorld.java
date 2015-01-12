package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;

public class GameWorld {
	/** The eve. */
	private Eve eve;	
	/** The mobs. */
	private ObjectMap<ID, Mobile> mobs;	
	/** The consumers. */
	private ObjectMap<ID, Consumer> consumers;
	/** The consumables. */
	private ObjectMap<ID, Consumable> consumables;
	/** The collidables. */
	private ObjectMap<ID, Collidable> collidables;
	/** The drawables. */
	private ObjectMap<ID, Drawable> drawables;
	/** The last spawn time. */
	private long lastSpawnTime;
	/** The starting amount of Food. */
	private int foodAmount;
	/** Toggles whether magnitude is checked for consuming */
	private boolean magnitudeConsuming;
	/** Toggles the display of the info at the top, biomass and location */
	public boolean displayHUD;
	/** Toggles if the game paused. */
	public boolean paused;
	public boolean gameOver;
	public boolean gameExit;
	
	public int soundStack;

	/**
	 * Instantiates a new game screen.
	 * @param game the game
	 */
	public GameWorld(final Neogenesis game, boolean loadGame) {
		DebugValues.debug=true; // set to true to use current debug values
		DebugValues.populateDebugValues(2); // 1 = godzilla mode, 2 = quick start
		
		// initialize maps
		mobs = new ObjectMap<ID, Mobile>();
		consumers = new ObjectMap<ID, Consumer>();
		consumables = new ObjectMap<ID, Consumable>();
		collidables = new ObjectMap<ID, Collidable>();
		drawables = new ObjectMap<ID, Drawable>();
		
		// create a Circle to logically represent Eve
		int biomass;
		if (loadGame){
			biomass = game.saveManager.loadDataValue("biomass",int.class);
		}
		else {
			biomass = DebugValues.getEveStartingBiomass();
		}
		
		eve = new Eve(new Circle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0), biomass);
		addToMaps(eve);	
	
		displayHUD = false;
		paused = false;
		gameOver = false;
		gameExit = false;
		soundStack = 0;
		magnitudeConsuming = DebugValues.getMagnitudeConsuming();
		
		foodAmount = DebugValues.getFoodAmount();
		// spawn the first mega creatures
		while (!spawnCreature(1000));
		while (!spawnCreature(2000));
		for (int ii = 0; ii < foodAmount; ii++){
			spawnFood();
		}
	}
	

	/**
	 * Adds a creature to the maps.
	 * @param id the id of the creature
	 * @param creature the creature
	 */
	private void addToMaps(Creature creature) {
		mobs.put(creature.getID(), creature);
		consumers.put(creature.getID(), creature);
		consumables.put(creature.getID(), creature);
		collidables.put(creature.getID(), creature);
		drawables.put(creature.getID(), creature);
	}
	/**
	 * Adds a food to the maps.
	 * @param id the id of the creature
	 * @param creature the creature
	 */
	private void addToMaps(Food food) {
		consumables.put(food.getID(), food);
		collidables.put(food.getID(), food);
		drawables.put(food.getID(), food);
	}
	
	
	/**
	 * Removes a creature from the maps.
	 * @param id the id of the creature
	 */
	private void removeFromMaps(ID id) {
		if (mobs.containsKey(id)){
			mobs.remove(id);
		}
		if (consumers.containsKey(id)){
			consumers.remove(id);
		}
		if (consumables.containsKey(id)){
			consumables.remove(id);
		}
		if (collidables.containsKey(id)){
			collidables.remove(id);
		}
		if (drawables.containsKey(id)){
			drawables.remove(id);
		}	
	}
	
	/**
	 * Spawn creature.
	 */
	private boolean spawnCreature() {
		int size;
		
		if (MathUtils.random(1,100)==100){
			size = MathUtils.random(50, 400)*5;
		}
		else if (MathUtils.random(1,10)==10){
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
	
	private boolean spawnCreature (int size){
		boolean spawned = false;
		Creature creature = new Creature(new Circle(MathUtils.random(0, 4800), MathUtils.random(0, 3600), 0), size);
		if (!creature.collidesWith(eve.getCircle())){
			addToMaps(creature);
			spawned = true;
			lastSpawnTime = TimeUtils.nanoTime();
		}
		return spawned;
	}
	
	private void spawnFood() {
		addToMaps(new Food(5, new Circle(MathUtils.random(0, 2400), MathUtils.random(0, 1800), 4)));
	}

	public void gameLoop(){
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			paused = !paused;
			System.out.println("EVE WHEN PAUSED IN GAMESCREEN:" + eve.position.x+","+eve.position.y);
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
	
	public void gameIncrement(){
		eve.setInput(Gdx.input);
		// move the creatures
		for (Mobile mob : mobs.values()) {
			mob.move();
		}
		// check if we need to create a new creature
		if (TimeUtils.nanoTime()-lastSpawnTime > 200000000/DebugValues.getSpawnRate())
			spawnCreature();
		// check for collisions and consume
		ObjectSet<ID> toRemove = new ObjectSet<ID>();
		
		if (magnitudeConsuming){
			IntMap<ObjectSet<Consumable>> magnitudeMap = generateConsumableMagnitudeMap();
			
			for (ID id: consumers.keys()){
				if (!toRemove.contains(id)){
					
					ObjectSet<Consumable> appropriatelySizedConsumables = new ObjectSet<Consumable>();
					for (int ii = -1; ii <= 0; ii++){
						if (magnitudeMap.containsKey(consumers.get(id).getMagnitude()+ii)){
							appropriatelySizedConsumables.addAll(magnitudeMap.get(consumers.get(id).getMagnitude()+ii));
						}
					}
					
					ObjectSet<ID> newRemove = consumers.get(id).consume(appropriatelySizedConsumables);
					if (newRemove.size!=0){
						soundStack++;
					}
					toRemove.addAll(newRemove);
				}	
			}
		}
		else{
			ObjectSet<Consumable> validConsumables = new ObjectSet<Consumable>();
			validConsumables.addAll(consumables.values().toArray());
			for (ID id: consumers.keys()){
				if (!toRemove.contains(id)){	
					ObjectSet<ID> newRemove = consumers.get(id).consume(new ObjectSet<Consumable>(validConsumables));
					if (newRemove.size!=0){
						soundStack++;
					}
					toRemove.addAll(newRemove);
				}
			}
		}
		for (ID id: toRemove){
			if (eve.getID()==id){
				gameOver = true;
			}
			removeFromMaps(id);
		}
	}

	private IntMap<ObjectSet<Consumable>> generateConsumableMagnitudeMap() {
		IntMap<ObjectSet<Consumable>> magnitudeMap = new IntMap<ObjectSet<Consumable>>();
		for (Consumable consumable: consumables.values()){
			int magnitude = consumable.getMagnitude();
			if (!magnitudeMap.containsKey(magnitude)){
				magnitudeMap.put(magnitude, new ObjectSet<Consumable>());
			}
			magnitudeMap.get(magnitude).add(consumable);
		}
		return magnitudeMap;
	}
	
	public ObjectMap<ID, Drawable> getDrawables(){
		return drawables;
	}


	public Eve getEve() {
		return eve;
	}
	
}
