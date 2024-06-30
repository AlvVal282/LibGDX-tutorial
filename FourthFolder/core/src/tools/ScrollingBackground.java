package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScrollingBackground {

    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 80;

    private float imageScale;
    private boolean speedFixed;
    Texture image;
    private float y1;
    private float y2;
    public static int speed; //In pixels / seconds
    private int goalSpeed;


    public ScrollingBackground() {
        image = new Texture("stars_background.png");

        y1 = 0;
        y2 = image.getHeight();
        speed = 0;
        goalSpeed = DEFAULT_SPEED;
        imageScale = 0;
        speedFixed = true;
    }
    public void updateAndRender(float deltaTime, SpriteBatch batch) {
        //speed adjustment to reach goal
        if(speed < goalSpeed) {
            speed += (int) (GOAL_REACH_ACCELERATION * deltaTime);
            if(speed > goalSpeed) {
                speed = goalSpeed;
            }
        } else if (speed > goalSpeed) {
            speed -= (int) (GOAL_REACH_ACCELERATION * deltaTime);
            if(speed < goalSpeed) {
                speed = goalSpeed;
            }
        }
        if(!speedFixed) {
            speed += (int) (ACCELERATION * deltaTime);
        }
        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        //check if image is off the screen
        if(y1 + image.getHeight() * imageScale <= 0) {
            y1 = y2 + image.getHeight() * imageScale;
        }
        if(y2 + image.getHeight() * imageScale <= 0) {
            y2 = y1 + image.getHeight() * imageScale;
        }

        //render
        batch.draw(image, 0, y1, Gdx.graphics.getWidth(), imageScale * image. getHeight());
        batch.draw(image, 0, y2, Gdx.graphics.getWidth(), imageScale * image. getHeight());

    }
    public void resize(int width, int height) {
        imageScale = (float) width / image.getWidth();
    }
    public void setGoalSpeed(int goalSpeed) {
        this.goalSpeed = goalSpeed;
    }
    public void setSpeedFixed(boolean speedFixed) {
        this.speedFixed = speedFixed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

