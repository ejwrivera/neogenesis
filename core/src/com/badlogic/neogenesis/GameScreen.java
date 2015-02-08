package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
/**
 * The Class GameScreen. For displaying the primary game screen - should be as logic-less as possible.
 */
public class GameScreen implements Screen {
	
	/** The game. */
	final private Neogenesis game;
	/** The stage. */
	private Stage stage = new Stage();
	/** The table. */
	private Table upgradeTable = new Table();
	/** The sound. */
	private Sound sound;
	/** The music. */
	private Music music;
	/** The camera. */
	private OrthographicCamera camera;
	/** The eve. */
	private Eve eve;	
	/** The skin. */
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	/** The buttons to select upgrades. */
	private OrderedMap<TextButton, String> upgradeButtons;
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
	
	private Texture unusedBiomassTexture;
	private Texture usedBiomassTexture;
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
				
		// create the camera and the SpriteBatch and buttons
		upgradeButtons = new OrderedMap<TextButton, String>();
		camera = new OrthographicCamera();
		CameraHandler.camera = camera;
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		zoomCamera=0;
		zoomSpeed=10*DebugValues.getCameraZoomRate();	
		zoomLevel = 0;
		
		camera.zoom*=DebugValues.getCameraZoomStart();
		paused = false;
		
		upgradeTable.setFillParent(true);
		upgradeTable.setPosition(-260, 0);
        stage.addActor(upgradeTable);

        Gdx.input.setInputProcessor(stage);
        unusedBiomassTexture = TextureMap.getTexture("unused_biomass");
        usedBiomassTexture = TextureMap.getTexture("used_biomass");
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
		paused = world.paused;
		while (world.soundStack>0){
			sound.play();
			world.soundStack--;
		}
		if (world.gameExit){
			game.saveManager.saveDataValue("biomass", eve.getBiomass());
			switchScreen(new MainMenuScreen(game));
		}
		else if (world.gameOver){
			switchScreen(new GameOverScreen(game));
		}
		if (!paused){
			orientCamera();	
		}
	}
	
	public void switchScreen(Screen screen){
		music.stop();
		camera.zoom=1;
		game.font.setScale(camera.zoom);
		game.toggleShader();
		game.setScreen(screen);
	}
	
	public void draw(){
		Gdx.gl.glClearColor(0, .2f, .4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        game.batch.begin();
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        if (eve.hasSense()){
			ShaderAttributes.LIGHT_POS.x=.38f;
			ShaderAttributes.LIGHT_POS.y=.34f;	
		}
		else {
			ShaderAttributes.LIGHT_POS.x=-1f;
			ShaderAttributes.LIGHT_POS.y=-1f;
		}
        
        //send a Vector4f to GLSL
        game.shader.setUniformf("LightPos", ShaderAttributes.LIGHT_POS);
        
		for (Drawable drawable : world.getDrawables().values()) {
			Circle drawBox = drawable.getCircle();
			game.batch.draw(drawable.getTexture(), drawBox.x-drawBox.radius, drawBox.y-drawBox.radius, drawBox.radius*2, drawBox.radius*2);
		}
		
        game.batch.end();
        
        stage.act();
        stage.draw();
        
        game.toggleShader();
        
        game.batch.begin();
		
       
        
		camera.update();
		// render in the coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		// begin a new batch and draw Eve and all the creatures
		
		 
		
		
		game.font.setScale(camera.zoom);
		game.font.setUseIntegerPositions(false);
		if (world.displayHUD){
			game.font.draw(game.batch, "Biomass: " + eve.getBiomass(), camera.position.x-300*camera.zoom, camera.position.y+220*camera.zoom);
			
			int usedBiomass = eve.getUsedBiomass();
			int unusedBiomass = eve.getBiomass();
			displayBiomass(unusedBiomass, unusedBiomassTexture);
			displayBiomass(usedBiomass, usedBiomassTexture);
			
			game.font.draw(game.batch, "Location: " + MathUtils.ceil(eve.getCircle().x) +", "+MathUtils.ceil(eve.getCircle().y), camera.position.x-300*camera.zoom+(230*camera.zoom), camera.position.y+220*camera.zoom);
			game.font.draw(game.batch, "FPS: " + MathUtils.ceil(1/Gdx.graphics.getDeltaTime()), camera.position.x-200*camera.zoom+(400*camera.zoom), camera.position.y+220*camera.zoom);
			if (zoomCamera>0){
				game.font.draw(game.batch, "Zooming", camera.position.x-300*camera.zoom+(230*camera.zoom), camera.position.y+220*camera.zoom-(20*camera.zoom));
			}
		}
		
		for (String upgrade: eve.getAvailableUpgrades()){
			if (!upgradeButtons.containsValue(upgrade, true)){
				TextButton newButton = new TextButton(upgrade, skin);
				upgradeButtons.put(newButton, upgrade);
			}
		}
		
		Array<TextButton> toRemove = new Array<TextButton>();
		for (TextButton button: upgradeButtons.keys()){
			for (EventListener listener: button.getListeners()){
				if (((ClickListener) listener).isPressed()){
					eve.addUpgrade(upgradeButtons.get(button));
					toRemove.add(button);
				}	
			}
			upgradeTable.add(button).row();
		}
		for (TextButton button: toRemove){
			upgradeTable.removeActor(button);
			upgradeButtons.remove(button);
		}
		game.batch.end();
		game.toggleShader();
	}

	private void displayBiomass(int biomassDisplay, Texture displayTexture){
		int column = 0;
		int row = 0;
		int displayLevel = 10000;
		float adjust = 0;
		float cameraX = camera.position.x-300*camera.zoom;
		float cameraY = camera.position.y+150*camera.zoom;
		while(biomassDisplay>0){
			while(biomassDisplay < displayLevel){
				adjust+=5*camera.zoom;
				displayLevel/=10;	
			}
			// 50 when camera zoom is 1 and displayLevel = 10000
			float biomassSize = MathUtils.log(10, displayLevel*10)*10*camera.zoom;
			
			game.batch.draw(displayTexture, cameraX+adjust, cameraY+adjust, biomassSize, biomassSize);
			cameraX += 50*camera.zoom;
			column++;
			if (column==10){
				column = 0;
				row++;
				cameraX = camera.position.x-300*camera.zoom;
				cameraY = camera.position.y+(150-50*row)*camera.zoom;
			}
			biomassDisplay-=displayLevel;
		}
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