package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;
/**
 * The Class GameScreen. For displaying the primary game screen - should be as logic-less as possible.
 */
public class GameScreen implements Screen {
	
	/** The game. */
	final private Neogenesis game;
	/** The sound. */
	private Sound sound;
	/** The music. */
	private Music music;
	/** The camera. */
	private OrthographicCamera camera;
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
	/** The zoom camera. */
	private int zoomCamera;
	/** The zoom speed. */
	private int zoomSpeed;
	/** The zoom level. */
	private int zoomLevel;
	/** The starting amount of Food. */
	private int foodAmount;
	/** Toggles whether magnitude is checked for consuming */
	private boolean magnitudeConsuming;
	/** Toggles if the game paused. */
	private boolean paused;
	/** Toggles the display of the info at the top, biomass and location */
	private boolean displayHUD;
	
	
	
	/**
	 * Instantiates a new game screen.
	 * @param game the game
	 */
	public GameScreen(final Neogenesis game, boolean loadGame) {
		this.game = game;
		
		DebugValues.debug=true; // set to true to use current debug values
		DebugValues.populateDebugValues(2); // 1 = godzilla mode, 2 = quick start
		
		// load the sound effect and music
		sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		music.setLooping(true);
		
		// initialize maps
		mobs = new ObjectMap<ID, Mobile>();
		consumers = new ObjectMap<ID, Consumer>();
		consumables = new ObjectMap<ID, Consumable>();
		collidables = new ObjectMap<ID, Collidable>();
		drawables = new ObjectMap<ID, Drawable>();
		
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		zoomCamera=0;
		zoomSpeed=10*DebugValues.getCameraZoomRate();
		// create a Circle to logically represent Eve
		int biomass;
		if (loadGame){
			biomass = game.saveManager.loadDataValue("biomass",int.class);
		}
		else {
			biomass = DebugValues.getEveStartingBiomass();
		}
		
		eve = new Eve(new Circle(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0), camera, biomass);
		addToMaps(eve);	
		zoomLevel = (int)eve.getCircle().radius/16;
		
		//camera.translate(eve.getCircle().radius/4, eve.getCircle().radius/4);
		camera.zoom*=DebugValues.getCameraZoomStart();
		paused = false;
		displayHUD = false;
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

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			paused = !paused;
			if (paused){
				music.pause();
			}
			else {
				music.play();
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.TAB)){
			displayHUD=!displayHUD;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			music.stop();
			game.saveManager.saveDataValue("biomass", eve.getBiomass());
			game.setScreen(new MainMenuScreen(game));
		}
		draw();
		if (!paused){
			gameIncrement();
			orientCamera();
		}
	}
	
	public void draw(){
		Gdx.gl.glClearColor(0, .2f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		// render in the coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		// begin a new batch and draw Eve and all the creatures
		game.batch.begin();
		game.font.setScale(camera.zoom);
		game.font.setUseIntegerPositions(false);
		if (displayHUD){
			game.font.draw(game.batch, "Biomass: " + eve.getBiomass(), camera.position.x-300*camera.zoom, camera.position.y+220*camera.zoom);
			game.font.draw(game.batch, "Location: " + MathUtils.ceil(eve.getCircle().x) +", "+MathUtils.ceil(eve.getCircle().y), camera.position.x-300*camera.zoom+(230*camera.zoom), camera.position.y+220*camera.zoom);
			game.font.draw(game.batch, "FPS: " + MathUtils.ceil(1/Gdx.graphics.getDeltaTime()), camera.position.x-200*camera.zoom+(400*camera.zoom), camera.position.y+220*camera.zoom);
			if (zoomCamera>0){
				game.font.draw(game.batch, "Zooming", camera.position.x-300*camera.zoom+(230*camera.zoom), camera.position.y+220*camera.zoom-(20*camera.zoom));
			}
		}
				
		ShaderAttributes.LIGHT_POS.x=.38f;
		ShaderAttributes.LIGHT_POS.y=.34f;	
		//send a Vector4f to GLSL
		game.shader.setUniformf("LightPos", ShaderAttributes.LIGHT_POS);
		
		
		for (Drawable drawable : drawables.values()) {
			Circle drawBox = drawable.getCircle();
			game.batch.draw(drawable.getTexture(), drawBox.x-drawBox.radius, drawBox.y-drawBox.radius, drawBox.radius*2, drawBox.radius*2);
		}
		game.batch.end();
		
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
						sound.play();
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
						sound.play();
					}
					toRemove.addAll(newRemove);
				}
			}
		}
		for (ID id: toRemove){
			if (eve.getID()==id){
				music.stop();
				game.setScreen(new GameOverScreen(game));
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

	public void orientCamera(){
		while (zoomLevel < (int)eve.getCircle().radius/16){
			zoomCamera+=5*zoomSpeed;
			zoomLevel++;
		}
		if (zoomCamera>0){
			camera.zoom+=.1/zoomSpeed;
			zoomCamera--;
		}
		camera.translate(eve.getLastMovement());
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		game.batch.setProjectionMatrix(camera.combined);
		game.shaderResize(width, height);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		music.play();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		sound.dispose();
		music.dispose();
		TextureMap.dispose();
	}

}