package tools;

public class CollisionRect {
    private float x;
    private float y;
    private final int WIDTH;
    private final int HEIGHT;

    public CollisionRect (float x, float y, int WIDTH, int HEIGHT) {
        this.x = x;
        this.y = y;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }
    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collidesWith(CollisionRect rect) {
        return x < rect.x + rect.WIDTH
                && y < rect.y + rect.HEIGHT
                && x + WIDTH > rect.x
                && y + HEIGHT > rect.HEIGHT;
    }
}
