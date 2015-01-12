package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Circle;
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
	/** The zoom camera. */
	private int zoomCamera;
	/** The zoom speed. */
	private int zoomSpeed;
	/** The zoom level. */
	private int zoomLevel;
	/** Toggles if the game paused. */
	private boolean paused;
	/** The game world where all business logic takes place. */
	private GameWorld world;
	
	/**
	 * Instantiates a new game screen.
	 * @param game the game
	 */
	public GameScreen(final Neogenesis game, boolean loadGame) {
		this.game = game;
		
		DebugValues.debug=true; // set to true to use current debug values
		DebugValues.populateDebugValues(2); // 1 = godzilla mode, 2 = quick start
		
		world = new GameWorld(game, loadGame);
		eve = world.getEve();
		
		// load the sound effect and music
		sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		music.setLooping(true);
				
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		zoomCamera=0;
		zoomSpeed=10*DebugValues.getCameraZoomRate();	
		zoomLevel = (int)eve.getCircle().radius/16;
		
		camera.zoom*=DebugValues.getCameraZoomStart();
		paused = false;
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		draw();
		world.gameLoop();
		if (paused!=world.paused){
			if (!paused){
				music.pause();
			}
			else {
				music.play();
			}
		}
		while (world.soundStack>0){
			sound.play();
			world.soundStack--;
		}
		paused = world.paused;
		if (world.gameExit){
			music.stop();
			game.saveManager.saveDataValue("biomass", eve.getBiomass());
			game.setScreen(new MainMenuScreen(game));
		}
		else if (world.gameOver){
			music.stop();
			game.setScreen(new GameOverScreen(game));
		}
		if (!paused){
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
		if (world.displayHUD){
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
		
		
		for (Drawable drawable : world.getDrawables().values()) {
			Circle drawBox = drawable.getCircle();
			game.batch.draw(drawable.getTexture(), drawBox.x-drawBox.radius, drawBox.y-drawBox.radius, drawBox.radius*2, drawBox.radius*2);
		}
		game.batch.end();
		
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