package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mazeproject.game.MazeProject;
import entities.Asteroid;
import entities.Bullet;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    float x;
    float y;
    int roll;
    float stateTime;
    float rollTimer;
    float shootTimer;
    float asteroidSpawnTimer;

    ArrayList<Bullet> bullets;
    ArrayList<Asteroid> asteroids;

    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
    public static final float SHIP_ANIMATION_SPEED = 0.5f;
    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
    public static final float SHOOT_WAIT_TIME = 0.3f;
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;





    Animation[] rolls;
    public static final float SPEED = 300;

    Random random;
    MazeProject game;

    public GameScreen(MazeProject game) {
        this.game = game;
        y = 15;
        x = (float) MazeProject.WIDTH / 2 - (float) SHIP_WIDTH / 2;

        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        roll = 2;
        rollTimer = 0;
        shootTimer = 0;
        rolls = new Animation[5];

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
        rolls[0] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]); //All left
        rolls[1] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
        rolls[2] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);//No Tilt
        rolls[3] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
        rolls[4] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);// All Right

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
        //Shooter Code
        shootTimer += delta;
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;
            int offset;
            switch (roll) {
                case 0:
                    case 4:
                        offset = 16;
                        break;
                case 1:
                    case 3:
                        offset = 8;
                        break;
                        default:
                            offset = 4;
                                break;
        }
            bullets.add(new Bullet(x + offset));
            bullets.add(new Bullet(x + SHIP_WIDTH - offset));
        }
        //asteroids
        asteroidSpawnTimer -= delta;
        if(asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(MazeProject.WIDTH - Asteroid.WIDTH)));
        }
        //update asteroids
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        for(Asteroid asteroid: asteroids) {
            asteroid.update(delta);
            if(asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }
        asteroids.removeAll(asteroidsToRemove);
        //Update bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for(Bullet bullet: bullets) {
            bullet.update(delta);
            if(bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletsToRemove);

        //Movement code
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && x >= 0) {
            x -= SPEED * Gdx.graphics.getDeltaTime();

            //update roll if button just clicked
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && roll > 0) { //left
                rollTimer = 0;
                roll--;
            }

            //update roll
            rollTimer -= Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else if(roll < 2) {
            //update to make it go back to center
            rollTimer += Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && x <= MazeProject.WIDTH - SHIP_WIDTH) {
            x += SPEED * Gdx.graphics.getDeltaTime();

            //update roll if button just clicked
            if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT) && roll < 4) { //right
                rollTimer = 0;
                roll++;
            }

            rollTimer += Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else if(roll > 2) {
            rollTimer -= Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        }
        stateTime += delta;

        game.batch.begin();

        for(Bullet bullet: bullets) {
            bullet.render(game.batch);
        }
        for(Asteroid asteroid: asteroids) {
            asteroid.render(game.batch);
        }
        game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime,true), x, y, SHIP_WIDTH, SHIP_HEIGHT);

        game.batch.end();
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     */
    @Override
    public void pause() {

    }

    /**
     */
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

    }
}
