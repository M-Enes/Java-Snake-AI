import java.awt.*;
import java.util.Random;
import java.util.List;

public class Apple {

    public Random random = new Random();
    private int radius = 20;
    private Vector2i location;
    private Dimension canvasDimension;

    public Apple(Dimension canvasDimension, int radius) {
        this.canvasDimension = canvasDimension;
        this.radius = radius;
    }

    public int getLocationX() {
        return location.x;
    }

    public int getLocationY() {
        return location.y;
    }

    public Vector2i getLocation() {
        return new Vector2i(location);
    }

    public int getRadius() {
        return radius;
    }

    public void spawn(List<Vector2i> snakeBody) {

        int bottomBound = canvasDimension.height;
        int rightBound = canvasDimension.width;

        boolean isAppleSpawnedOnSnake = true;

        while (isAppleSpawnedOnSnake) {
            isAppleSpawnedOnSnake = false;

            location = new Vector2i(
                    random.nextInt(rightBound / radius) * radius,
                    random.nextInt(bottomBound / radius) * radius);

            for (Vector2i part : snakeBody) {
                if (part.equals(location)) {
                    isAppleSpawnedOnSnake = true;
                }
            }
        }

        System.out.println("Apple spawned at " + location.x + " " + location.y);

    }
}
