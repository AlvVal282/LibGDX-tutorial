package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mazeproject.game.MazeProject;
import tools.ScrollingBackground;

public class GameOverScreen implements Screen {
    private static final int BANNER_WIDTH = 350;
    private static final int BANNER_HEIGHT = 100;
    private static final int PADDING = 15;

    private final MazeProject game;
    private final int score;
    private final int highscore;

    private final Texture gameOverBanner;
    private final BitmapFont scoreFont;

    private GlyphLayout tryAgainLayout;
    private GlyphLayout mainMenuLayout;
    private float tryAgainX;
    private float tryAgainY;
    private float mainMenuX;
    private float mainMenuY;

    public GameOverScreen(MazeProject game, int score) {
        this.game = game;
        this.score = score;

        Preferences prefs = Gdx.app.getPreferences("MazeProject");
        this.highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        gameOverBanner = new Texture("game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        initLayouts();
    }

    private void initLayouts() {
        tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        tryAgainX = (float) Gdx.graphics.getWidth() / 2 - tryAgainLayout.width / 2;
        tryAgainY = (float) Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2;
        mainMenuX = (float) Gdx.graphics.getWidth() / 2 - mainMenuLayout.width / 2;
        mainMenuY = (float) Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - PADDING;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);
        drawBanner();
        drawScores();
        drawButtons();
        handleInput();

        game.batch.end();
    }

    private void drawBanner() {
        game.batch.draw(gameOverBanner,
                (float) Gdx.graphics.getWidth() / 2 - (float) BANNER_WIDTH / 2,
                Gdx.graphics.getHeight() - BANNER_HEIGHT - PADDING,
                BANNER_WIDTH, BANNER_HEIGHT);
    }

    private void drawScores() {
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "HighScore: \n" + highscore, Color.WHITE, 0, Align.left, false);

        scoreFont.draw(game.batch, scoreLayout,
                (float) Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2,
                Gdx.graphics.getHeight() - BANNER_HEIGHT - PADDING * 2);

        scoreFont.draw(game.batch, highscoreLayout,
                (float) Gdx.graphics.getWidth() / 2 - highscoreLayout.width / 2,
                Gdx.graphics.getHeight() - BANNER_HEIGHT - scoreLayout.height - PADDING * 3);
    }

    private void drawButtons() {
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (isTouched(tryAgainLayout, tryAgainX, tryAgainY, touchX, touchY)) {
                this.dispose();
                game.setScreen(new GameScreen(game));
                return;
            }

            if (isTouched(mainMenuLayout, mainMenuX, mainMenuY, touchX, touchY)) {
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    private boolean isTouched(GlyphLayout layout, float x, float y, float touchX, float touchY) {
        return touchX > x && touchX < x + layout.width && touchY > y - layout.height && touchY < y;
    }

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

    @Override
    public void dispose() {
        gameOverBanner.dispose();
        scoreFont.dispose();
    }
}