import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class SnakeAI {

    private final Snake snake;
    private int aiMoveCounter = 0;
    
    private class QueueElement {
        public Vector2i vector;
        public ArrayList<Vector2i> path;

        public QueueElement(Vector2i vector, ArrayList<Vector2i> path) {
            this.vector = vector;
            this.path = path;
        }
    }
    public SnakeAI(Snake snake) {
        this.snake = snake;
    }

    public void resetMoveCounter() {
        this.aiMoveCounter = 0;
    }


    private ArrayList<Vector2i> getBFSPath(Vector2i start, Vector2i target, TreeSet<Vector2i> obstacles) {
        System.out.println("SnakeAI.getBFSPath()");
        Queue<QueueElement> queue = new LinkedList<QueueElement>();
        TreeSet<Vector2i> visited = new TreeSet<Vector2i>();
        queue.add(new QueueElement(start, new ArrayList<Vector2i>()));
        visited.add(start);

        while (!queue.isEmpty()) {
            QueueElement element = queue.poll();
            if (element.vector.equals(target)) {
                return element.path;
            }

            for (Vector2i neighbor : getNeighbors(element.vector)) {
                if (!visited.contains(neighbor) && !obstacles.contains(neighbor)) {
                    visited.add(neighbor);
                    ArrayList<Vector2i> newPath = (ArrayList<Vector2i>)(element.path.clone());
                    newPath.add(neighbor);
                    queue.add(new QueueElement(neighbor, newPath));
                }
            }
        }

        return null;
    }

    private Direction findDirectionOfMove(Vector2i start, Vector2i move) {
        Vector2i diff = move.sub(start);
        return getMovementDirection(diff);
    }

    private Direction getMovementDirection(Vector2i movementVector) {
        if (movementVector.equals(new Vector2i(0, -1))) {
            return Direction.UP;
        } else if (movementVector.equals(new Vector2i(0, 1))) {
            return Direction.DOWN;
        } else if (movementVector.equals(new Vector2i(-1, 0))) {
            return Direction.LEFT;
        }
        return Direction.RIGHT;
    }

    private Vector2i getMovementVector(Direction direction) {
        if (direction == Direction.UP) {
            return new Vector2i(0, -1);
        } else if (direction == Direction.DOWN) {
            return new Vector2i(0, 1);
        } else if (direction == Direction.LEFT) {
            return new Vector2i(-1, 0);
        }
        return new Vector2i(1, 0);
    }

    private boolean isMoveSafe(Vector2i nextMove, Vector2i appleVector) {

        System.out.println("SnakeAI.isMoveSafe()");
        ArrayList<Vector2i> virtualSnake = new ArrayList<>();
        virtualSnake.addAll(snake.getBody());
        Vector2i virtualHead = nextMove;

        virtualSnake.add(0, virtualHead);

        TreeSet<Vector2i> obstacles = new TreeSet<>();
        Vector2i virtualTail;
        if (virtualHead.equals(appleVector)) {
            virtualTail = virtualSnake.getLast();
            obstacles.addAll(virtualSnake);
            obstacles.remove(virtualTail);
        } else {
            virtualSnake.removeLast();
            virtualTail = virtualSnake.getLast();
            obstacles.addAll(virtualSnake);
            obstacles.remove(virtualTail);
        }

        ArrayList<Vector2i> pathToTail = getBFSPath(virtualHead, virtualTail, obstacles);

        System.out.println("ismovesafe: " + pathToTail != null);

        return pathToTail != null;
    }

    private Vector2i getLongestPathToTail() {
        Vector2i head = snake.getBody().getFirst();
        Vector2i tail = snake.getBody().getLast();
        TreeSet<Vector2i> obstacles = new TreeSet<Vector2i>(snake.getBody());
        obstacles.remove(tail);

        ArrayList<Vector2i> neighbors = getNeighbors(head);
        Vector2i bestMove = null;
        int maxDist = -1;

        for (Vector2i neighbor : neighbors) {
            if (!obstacles.contains(neighbor)) {
                ArrayList<Vector2i> path = getBFSPath(neighbor, tail, obstacles);
                if (path != null) {
                    if (path.size() > maxDist) {
                        maxDist = path.size();
                        bestMove = neighbor;
                    }
                }
            }
        }
        System.out.println("maxdist: " + maxDist);

        return bestMove;
    }

    private ArrayList<Vector2i> getNeighbors(Vector2i head) {
        ArrayList<Vector2i> directionVectors = new ArrayList<>() {
            {
                add(new Vector2i(0, -1));
                add(new Vector2i(0, 1));
                add(new Vector2i(-1, 0));
                add(new Vector2i(1, 0));
            }
        };

        ArrayList<Vector2i> neighbors = new ArrayList<>();

        for (Vector2i directionVector : directionVectors) {
            Vector2i newHead = head.add(directionVector);
            if (snake.isOutOfCanvas(newHead) == false) {
                neighbors.add(newHead);
            }
        }

        return neighbors;
    }

    public Direction getNextMoveDirection(Apple apple) {
        aiMoveCounter++;
        if(aiMoveCounter >= Config.AI_MOVE_LIMIT) {
            System.out.println("AI Stuck - Committing Suicide");
            return snake.getDirection();
        }

        Vector2i head = this.snake.getBody().getFirst();
        Vector2i appleVector = apple.getLocation();
        TreeSet<Vector2i> obstacles = new TreeSet<>(snake.getBody());

        ArrayList<Vector2i> bfsPath = getBFSPath(head, appleVector, obstacles);
        if (bfsPath != null) {
            Vector2i nextMove = bfsPath.getFirst();

            if (isMoveSafe(nextMove, appleVector)) {
                return findDirectionOfMove(head, nextMove);
            }
        }

        Vector2i stallMove = getLongestPathToTail();
        if (stallMove != null) {
            return findDirectionOfMove(head, stallMove);
        }

        ArrayList<Vector2i> neighbors = getNeighbors(head);
        for (Vector2i neighbor : neighbors) {
            if (!snake.getBody().contains(neighbor)) {
                return findDirectionOfMove(head, neighbor);
            }
        }

        return snake.getDirection();
    }

}