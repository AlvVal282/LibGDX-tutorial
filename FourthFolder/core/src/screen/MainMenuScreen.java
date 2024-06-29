package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mazeproject.game.MazeProject;
import tools.ScrollingBackground;

public class MainMenuScreen implements Screen {
    final MazeProject game;

    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture playButtonActive;
    Texture playButtonInactive;

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 230;
    public MainMenuScreen(final MazeProject game) {
        this.game = game;
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setGoalSpeed(ScrollingBackground.DEFAULT_SPEED);

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                int x = MazeProject.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
                if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x
                        && MazeProject.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT
                        && MazeProject.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {

                    mainMenuScreen.dispose();
                    Gdx.app.exit();
                }

                x = MazeProject.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
                if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x
                        && MazeProject.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT
                        && MazeProject.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {

                        mainMenuScreen.dispose();
                        game.setScreen(new GameScreen(game));
                }
                    return super.touchUp(screenX, screenY, pointer, button);
            }
        });

    }
    @Override
    public void show() {
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta,game.batch);


        int x = MazeProject.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x
        && MazeProject.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && MazeProject.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        } else {
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }
        x = MazeProject.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
        if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x
                && MazeProject.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && MazeProject.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);

        }
        game.batch.end();
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
}
