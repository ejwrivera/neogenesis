package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.TimeUtils;
// TODO: Auto-generated Javadoc
// for displaying the primary game screen - should be as logic-less as possible
/**
 * The Class GameScreen.
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
	/** Toggles if the game paused. */
	private boolean paused;
	/** Toggles the display of the info at the top, biomass and location */
	private boolean displayHUD;
	
	
	/**
	 * Instantiates a new game screen.
	 * @param game the game
	 */
	public GameScreen(final Neogenesis game) {
		this.game = game;
		
		// DebugValues.debug=true; // set to true to use current debug values
		// DebugValues.populateDebugValues(2); // 1 = godzilla mode, 2 = quick start
		// initialize maps
		mobs = new ObjectMap<ID, Mobile>();
		consumables = new ObjectMap<ID, Consumable>();
		collidables = new ObjectMap<ID, Collidable>();
		drawables = new ObjectMap<ID, Drawable>();
		// load the sound effect and music
		sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		music.setLooping(true);
		
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 400, 300);
		zoomCamera=0;
		zoomSpeed=10*DebugValues.getCameraZoomRate();
		// create a Rectangle to logically represent Eve
		eve = new Eve(new Rectangle(200, 150, 0, 0), camera, DebugValues.getEveStartingBiomass());
		addToMaps(eve.getID(), eve);	
		zoomLevel = (int)eve.getRectangle().getWidth()/32;
		
		camera.translate(eve.getRectangle().getWidth()/2, eve.getRectangle().getHeight()/2);
		camera.zoom*=DebugValues.getCameraZoomStart();
		paused = false;
		displayHUD = false;
		// spawn the first creature
		spawnCreature();
	}

	/**
	 * Adds a creature to the maps.
	 * @param id the id of the creature
	 * @param creature the creature
	 */
	private void addToMaps(ID id, Creature creature) {
		mobs.put(creature.getID(), creature);
		consumables.put(creature.getID(), creature);
		collidables.put(creature.getID(), creature);
		drawables.put(creature.getID(), creature);
	}

	/**
	 * Removes a creature from the maps.
	 * @param id the id of the creature
	 */
	private void removeFromMaps(ID id) {
		mobs.remove(id);
		consumables.remove(id);
		collidables.remove(id);
		drawables.remove(id);
	}
	
	/**
	 * Spawn creature.
	 */
	private void spawnCreature() {
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
		Creature creature = new Creature(new Rectangle(MathUtils.random(0, 2400), MathUtils.random(0, 1800), 0, 0), size);
		if (!creature.collidesWith(eve.getRectangle())){
			addToMaps(creature.getID(), creature);
		}
		lastSpawnTime = TimeUtils.nanoTime();
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
		if (displayHUD){
			game.font.draw(game.batch, "Biomass: " + eve.getBiomass(), camera.position.x-200*camera.zoom, camera.position.y+150*camera.zoom);
			game.font.draw(game.batch, "Location: " + eve.getRectangle().x +", "+eve.getRectangle().y, camera.position.x-200*camera.zoom+(200*camera.zoom), camera.position.y+150*camera.zoom);
		}
		for (Drawable drawable : drawables.values()) {
			Rectangle drawBox = drawable.getRectangle();
			game.batch.draw(drawable.getTexture(), drawBox.x, drawBox.y, drawBox.width, drawBox.height);
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
		for (ID id: consumables.keys()){
			if (!toRemove.contains(id)){
				ObjectSet<ID> newRemove = consumables.get(id).consume (consumables.values());
				if (newRemove.size!=0){
					sound.play();
				}
				toRemove.addAll(newRemove);
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

	public void orientCamera(){
		while (zoomLevel < (int)eve.getRectangle().getWidth()/32){
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