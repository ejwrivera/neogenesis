package com.badlogic.neogenesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
// TODO: Auto-generated Javadoc
// for displaying the Game Over screen
/**
 * The Class GameOverScreen.
 */
public class GameOverScreen implements Screen {

	/** The game. */
	private final Neogenesis game;
	/** The stage. */
	private Stage stage = new Stage();
	/** The skin. */
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
	/** The eve image. */
	private Texture eveImage;
	/** The win width. */
	private final int winWidth;
	/** The win height. */
	private final int winHeight;
	/** The clocktick. */
	private int clocktick=0;
	/** The camera. */
	OrthographicCamera camera;

	/**
	 * Instantiates a new game over screen.
	 * @param game the game
	 */
	public GameOverScreen(final Neogenesis game) {
		this.game = game;
		camera = new OrthographicCamera();
		winWidth = 800;
		winHeight = 600;
		camera.setToOrtho(false, winWidth, winHeight);
		skin.add("logo", new Texture("eve.png"));
		eveImage = new Texture(Gdx.files.internal("eve.png"));
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
        stage.draw();
        
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.font.setScale(4);
		game.font.draw(game.batch, String.valueOf(clocktick), 100, 550);
		game.batch.draw(eveImage, 500, 500);
		if (clocktick++<300&& !Gdx.input.isTouched()){
			game.font.draw(game.batch, "GAME OVER", 100, 150);
		}
		else {
			game.font.setScale(1);
			game.setScreen(new MainMenuScreen(game));
		}
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