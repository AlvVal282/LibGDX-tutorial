package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mazeproject.game.MazeProject;
import entities.Asteroid;
import entities.Bullet;
import entities.Explosion;
import tools.CollisionRect;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    // Player position and state variables
    private float x;
    private final float y;
    private int roll;
    private int score;
    private float stateTime;
    private float rollTimer;
    private float shootTimer;
    private float asteroidSpawnTimer;
    private float health = 1f; // 0 = dead, 1 = full health

    // Entity lists
    private final ArrayList<Bullet> bullets;
    private final ArrayList<Asteroid> asteroids;
    private final ArrayList<Explosion> explosions;

    // Assets
    private final Texture blank;
    private final BitmapFont scoreFont;
    private final CollisionRect playerRect;

    // Constants
    private static final int SHIP_WIDTH_PIXEL = 17;
    private static final int SHIP_HEIGHT_PIXEL = 32;
    private static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    private static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
    private static final float SHIP_ANIMATION_SPEED = 0.5f;
    private static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
    private static final float SHOOT_WAIT_TIME = 0.3f;
    private static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    private static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;

    private final Animation<TextureRegion>[] rolls;
    private static final float SPEED = 300;

    private final Random random;
    private final MazeProject game;

    public GameScreen(MazeProject game) {
        this.game = game;
        y = 15;
        x = (float) MazeProject.WIDTH / 2 - (float) SHIP_WIDTH / 2;
        score = 0;

        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        explosions = new ArrayList<>();
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        playerRect = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        blank = new Texture("blank.png");
        roll = 2;
        rollTimer = 0;
        shootTimer = 0;
        rolls = new Animation[5];

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
        rolls[0] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]); // All left
        rolls[1] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
        rolls[2] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]); // No Tilt
        rolls[3] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
        rolls[4] = new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]); // All Right

        game.scrollingBackground.setSpeedFixed(false);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Shooter code
        handleShooting(delta);

        // Asteroids spawning
        handleAsteroidSpawning(delta);

        // Update entities
        updateEntities(delta);

        // Player movement
        handlePlayerMovement(delta);

        // Collision detection
        detectCollisions();

        // Rendering
        renderGame(delta);
    }

    private void handleShooting(float delta) {
        shootTimer += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME) {
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
    }

    private void handleAsteroidSpawning(float delta) {
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(MazeProject.WIDTH - Asteroid.WIDTH)));
        }
    }

    private void updateEntities(float delta) {
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove) {
                asteroidsToRemove.add(asteroid);
            }
        }

        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.remove) {
                bulletsToRemove.add(bullet);
            }
        }

        ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove) {
                explosionsToRemove.add(explosion);
            }
        }

        asteroids.removeAll(asteroidsToRemove);
        bullets.removeAll(bulletsToRemove);
        explosions.removeAll(explosionsToRemove);
    }

    private void handlePlayerMovement(float delta) {
        boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) && x >= 0;
        boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) && x <= MazeProject.WIDTH - SHIP_WIDTH;

        if (movingLeft) {
            x -= SPEED * delta;
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer -= delta;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else if (roll < 2) {
            rollTimer += delta;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        }

        if (movingRight) {
            x += SPEED * delta;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && roll < 4) {
                rollTimer = 0;
                roll++;
            }

            rollTimer += delta;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else if (roll > 2) {
            rollTimer -= delta;
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        }

        playerRect.move(x, y);
    }

    private void detectCollisions() {
        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                    score += 100;
                }
            }
        }

        for (Asteroid asteroid : asteroids) {
            if (asteroid.getCollisionRect().collidesWith(playerRect)) {
                asteroidsToRemove.add(asteroid);
                health -= 0.1f;

                if (health <= 0) {
                    this.dispose();
                    game.setScreen(new GameOverScreen(game, score));
                }
            }
        }

        asteroids.removeAll(asteroidsToRemove);
        bullets.removeAll(bulletsToRemove);
    }

    private void renderGame(float delta) {
        stateTime += delta;
        game.batch.begin();
        game.scrollingBackground.updateAndRender(delta, game.batch);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, score + "");
        scoreFont.draw(game.batch, scoreLayout, (float) Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - scoreLayout.height - 10);

        for (Bullet bullet : bullets) {
            bullet.render(game.batch);
        }
        for (Asteroid asteroid : asteroids) {
            asteroid.render(game.batch);
        }
        for (Explosion explosion : explosions) {
            explosion.render(game.batch);
        }

        renderHealthBar();

        game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
        game.batch.end();
    }

    private void renderHealthBar() {
        if (health > .6f) {
            game.batch.setColor(Color.GREEN);
        } else if (health > .2f) {
            game.batch.setColor(Color.ORANGE);
        } else {
            game.batch.setColor(Color.RED);
        }
        game.batch.draw(blank, 0, 0, Gdx.graphics.getWidth() * health, 5);
        game.batch.setColor(Color.WHITE);
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