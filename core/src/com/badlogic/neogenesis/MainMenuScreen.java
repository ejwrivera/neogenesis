package com.badlogic.neogenesis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The Class MainMenuScreen. For displaying the title screen
 */
public class MainMenuScreen implements Screen {

	/** The game. */
	private final Neogenesis game;
	/** The stage. */
	private Stage stage = new Stage();
	/** The table. */
	private Table table = new Table();
	/** The skin. */
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	/** The play an exit buttons */
	private TextButton  buttonPlay = new TextButton("Play", skin),
						buttonExit = new TextButton("Exit", skin);
	/** The window width. */
	private final int winWidth;
	/** The window height. */
	private final int winHeight;
	/** The clocktick. */
	private int clocktick=0;
	/** The camera. */
	OrthographicCamera camera;
	/**
	 * Instantiates a new main menu screen.
	 * @param game the game
	 */
	public MainMenuScreen(final Neogenesis game) {
		this.game = game;
		camera = new OrthographicCamera();
		winWidth = 800;
		winHeight = 600;
		camera.setToOrtho(false, winWidth, winHeight);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
        stage.draw();
        
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, String.valueOf(clocktick), 100, 550);
		game.font.draw(game.batch, "Neogenesis ", 100, 150);
		game.font.draw(game.batch, "---", 100, 100);
		clocktick++;
		game.batch.end();
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
		buttonPlay.addListener(new ClickListener(){
	        @Override
	        public void clicked(InputEvent event, float x, float y) {
	            ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
	        }
	    });
	    buttonExit.addListener(new ClickListener(){
	        @Override
	        public void clicked(InputEvent event, float x, float y) {
	            Gdx.app.exit();
	        }
	    });
        //The buttons are displayed in this order from top to bottom
        table.add(buttonPlay).row();
        table.add(buttonExit).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		dispose();
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
		stage.dispose();
		skin.dispose();
	}
}