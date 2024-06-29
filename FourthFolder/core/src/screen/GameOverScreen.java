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

public class GameOverScreen implements Screen {
    MazeProject game;
    int score;
    int highscore;

    private static final int BANNER_WIDTH = 350;
    private static final int BANNER_HEIGHT = 100;

    Texture gameOverBanner;
    BitmapFont scorefont;



    public GameOverScreen (MazeProject game, int score) {
        this.game = game;
        this.score = score;
        //Get highscore from save file
        Preferences prefs = Gdx.app.getPreferences("MazeProject");
        this.highscore = prefs.getInteger("highscore", 0);

        //check if score beats highscore
        if(score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        //Load textures and fonts
        gameOverBanner = new Texture("game_over.png");
        scorefont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        game.batch.begin();

        game.batch.draw(gameOverBanner, (float) Gdx.graphics.getWidth() / 2 - (float) BANNER_WIDTH / 2,
                Gdx.graphics.getHeight() - BANNER_HEIGHT - 15,
                BANNER_WIDTH, BANNER_HEIGHT);
        GlyphLayout scoreLayout = new GlyphLayout(scorefont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scorefont, "HighScore: \n" + highscore, Color.WHITE, 0, Align.left, false);
        scorefont.draw(game.batch, scoreLayout, (float) Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15 * 2);
        scorefont.draw(game.batch, highscoreLayout, (float) Gdx.graphics.getWidth() / 2 - highscoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

        GlyphLayout tryAgainLayout = new GlyphLayout(scorefont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scorefont, "Main Menu");

        float tryAgainX = (float) Gdx.graphics.getWidth() / 2 - tryAgainLayout.width / 2;
        float tryAgainY = (float) Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2;
        float mainMenuX = (float) Gdx.graphics.getWidth() / 2 - mainMenuLayout.width / 2;
        float mainMenuY = (float) Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        //if try again and main menu is being pressed
        if(Gdx.input.isTouched()) {
            //try again
            if(touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new GameScreen(game));
                return;
            }
            if(touchX > mainMenuX && touchX < mainMenuX + tryAgainLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        //draw buttons
        scorefont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scorefont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        game.batch.end();
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

    }
}
