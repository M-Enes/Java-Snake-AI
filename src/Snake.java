import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final Dimension canvasDimension;
    private List<Vector2i> body;

    public Vector2i movementVector = new Vector2i(0, -1);
    public Vector2i speedVector = new Vector2i(1, 1);
    private Direction direction = Direction.UP;
    private boolean isDirectionLocked = true;

    private int partWidth;
    private int partHeight;
    private int growthFactor = 1;

    public Snake(Dimension canvasDimension, int spawnX, int spawnY, int partWidth, int partHeight) {
        this.body = new ArrayList<>();
        body.add(new Vector2i(spawnX, spawnY));
        this.canvasDimension = canvasDimension;
        this.partWidth = partWidth;
        this.partHeight = partHeight;
    }

    public void reset() {
        this.speedVector = new Vector2i(1, 1);
        this.changeDirection('w');
        // TODO: Body reset logic should be here
    }

    public void changeDirection(Direction direction) {
        if (direction == Direction.UP) {
            changeDirection('w');
        } else if (direction == Direction.DOWN) {
            changeDirection('s');
        } else if (direction == Direction.RIGHT) {
            changeDirection('d');
        } else if (direction == Direction.LEFT) {
            changeDirection('a');
        }
    }

    public void changeDirection(char movementKey) {
        if (isDirectionLocked)
            return;

        // w -> Vector2i(0, -1)
        // s -> Vector2i(0, 1)
        // a -> Vector2i(-1, 0)
        // d -> Vector2i(1, 0)

        Direction newDirection;
        Vector2i newMovementVector;
        switch (movementKey) {
            case 'w':
            case 'W':
                newMovementVector = new Vector2i(0, -1);
                newDirection = Direction.UP;
                break;
            case 's':
            case 'S':
                newMovementVector = new Vector2i(0, 1);
                newDirection = Direction.DOWN;
                break;
            case 'a':
            case 'A':
                newMovementVector = new Vector2i(-1, 0);
                newDirection = Direction.LEFT;
                break;
            case 'd':
            case 'D':
                newMovementVector = new Vector2i(1, 0);
                newDirection = Direction.RIGHT;
                break;
            default:
                return;
        }

        // do not change current direction to new one if they are opposite
        if (newMovementVector.mul(new Vector2i(-1, -1)).equals(movementVector)) {
            return;
        }

        direction = newDirection;
        movementVector = newMovementVector;
        isDirectionLocked = true;
    }

    public void move() {
        if (speedVector.x == 0 && speedVector.y == 0) {
            return;
        }

        Vector2i head = body.getFirst();
        Vector2i newHead = head.add(movementVector.mul(speedVector));

        body.set(0, newHead);

        Vector2i lastLocation = head;
        for (int i = 1; i < body.size(); i++) {
            Vector2i currentLocation = body.get(i);

            body.set(i, lastLocation);
            lastLocation = currentLocation;
        }

        isDirectionLocked = false;
    }

    public void addTail() {
        Vector2i vector;
        if (body.size() == 1) {
            vector = body.getLast().sub(movementVector);
        } else {
            vector = body.getLast().add(body.getLast().sub(body.get(body.size() - 2)));
        }
        body.add(vector);
    }

    public boolean checkBodyCollision() {
        Vector2i head = body.getFirst();

        for (int i = 2; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOutOfCanvas() {
        return isOutOfCanvas(body.getFirst());
    }

    public boolean isOutOfCanvas(Vector2i head) {
        return head.x < 0 || head.x >= canvasDimension.width ||
                head.y < 0 || head.y >= canvasDimension.height;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Do not copies for performance reasons. Caller must not change the values of
     * body parts.
     * 
     * @return list of snake body parts positions
     */
    public ArrayList<Vector2i> getBody() {
        return new ArrayList<Vector2i>(body);
    }

    public int getPartWidth() {
        return partWidth;
    }

    public int getPartHeight() {
        return partHeight;
    }

    public int getGrowthFactor() {
        return growthFactor;
    }

    public Vector2i getHeadLocation() {
        return new Vector2i(body.getFirst());
    }

    public void lockDirection() {
        isDirectionLocked = true;
    }
}