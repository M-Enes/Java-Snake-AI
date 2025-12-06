import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game {

    private static final double UPDATE_INTERVAL = 1_000_000_000.0 / Config.TARGET_UPS;
    private static final double DRAW_INTERVAL = 1_000_000_000.0 / Config.TARGET_FPS;
    /*** this dimensions value should not be changed */
    private final Dimension canvasDimension = new Dimension(Config.COLUMNS, Config.ROWS);
    private boolean gameContinue = true;
    private boolean isGamePaused = false;
    private Frame frame;
    private Insets insets;
    private GameCanvas gameCanvas;
    private GameStartMenu startMenu;
    private Snake snake;
    private SnakeAI snakeAI;
    private Apple apple;
    private boolean isAIModeOpen = true;
    private boolean isAppleEatenInThisFrame = false;
    private Vector2i eatenAppleVector;
    private ResourceManager resourceManager;
    private CardLayout cardLayout;

    public boolean isAppleEatenInThisFrame() {
        return isAppleEatenInThisFrame;
    }

    public Vector2i getEatenAppleVector() {
        return eatenAppleVector;
    }

    public boolean isAIModeOpen() {
        return isAIModeOpen;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setup();
        game.runGameLoop();

    }

    public void runGameLoop() {
        long lastTime = System.nanoTime();
        double delta = 0;

        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (this.gameContinue) {
            long currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / UPDATE_INTERVAL;
            lastTime = currentTime;

            // avoid lag spike
            if (delta > 5) {
                delta = 0;
            }

            while (delta >= 1) {
                updateLogic();
                updates++;
                delta--;
            }

            updateGraphics();
            frames++;

            // FPS Monitor (Once per second)
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.printf("FPS: %d\n", frames, updates);
                frames = 0;
                updates = 0;
            }

            long frameEndTime = System.nanoTime();
            long timeTaken = frameEndTime - currentTime;
            long timeToSleep = (long) (DRAW_INTERVAL - timeTaken);

            if (timeToSleep > 0) {
                try {
                    // convert nanoseconds to milliseconds
                    Thread.sleep(timeToSleep / 1_000_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setup() {
        resourceManager = new ResourceManager();
        resourceManager.loadResources();

        resourceManager.loopSound("music");

        frame = new Frame(Config.WINDOW_TITLE);
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        snake = new Snake(canvasDimension, Config.SNAKE_START_X, Config.SNAKE_START_Y, 1, 1);
        snakeAI = new SnakeAI(snake);
        apple = new Apple(canvasDimension, Config.APPLE_RADIUS);
        for (int i = 0; i < 2; i++) {
            snake.addTail();
        }
        apple.spawn(snake.getBody());

        gameCanvas = new GameCanvas(this, resourceManager, snake, apple, canvasDimension);
        startMenu = new GameStartMenu(this, canvasDimension.width * Config.BLOCK_SIZE,
                canvasDimension.height * Config.BLOCK_SIZE);

        frame.pack();

        frame.setVisible(true);
        insets = frame.getInsets();
        int realWidth = insets.left + canvasDimension.width * Config.BLOCK_SIZE + insets.right;
        int realHeight = insets.top + canvasDimension.height * Config.BLOCK_SIZE + insets.bottom;

        frame.setSize(realWidth, realHeight);

        gameCanvas.setLocation(insets.left, insets.top);
        startMenu.setLocation(insets.left, insets.top);

        frame.add(startMenu, "StartMenu");
        frame.add(gameCanvas, "GameCanvas");

        gameCanvas.setVisible(false);
        startMenu.setVisible(true);

        frame.validate();

        snake.speedVector = new Vector2i(0, 0);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        gameCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                System.out.println("Typed: " + keyChar);
                if (keyChar == 'f' || keyChar == 'F') {
                    if (snake.speedVector.equals(new Vector2i(0, 0))) {
                        System.out.println("Normal speed");
                        snake.speedVector = new Vector2i(1, 1);
                        isGamePaused = false;
                    } else {
                        snake.speedVector = new Vector2i(0, 0);
                        isGamePaused = true;
                    }
                    return;
                }
                if (keyChar == 't' || keyChar == 'T') {
                    if (isAIModeOpen == true) {
                        isAIModeOpen = false;
                    } else {
                        isAIModeOpen = true;
                    }
                    return;
                }
                if (isAIModeOpen || snake.speedVector.equals(new Vector2i(0, 0))) {
                    return;
                }
                snake.changeDirection(keyChar);
            }
        });
    }

    public void startGame() {
        // startMenu.setVisible(false);
        // gameCanvas.setVisible(true);
        cardLayout.show(frame, "GameCanvas");
        gameCanvas.requestFocus();

        // int innerWidth = frame.getWidth() - insets.left - insets.right;
        // int innerHeight = frame.getHeight() - insets.top - insets.bottom;
        // gameCanvas.setSize(innerWidth, innerHeight);
        // gameCanvas.repaint();
        // snake.reset() should be here
        snake.speedVector = new Vector2i(1, 1);

    }

    public void restart() {

    }

    private void updateGraphics() {
        // if(startMenu.isVisible()) {
        startMenu.repaint();
        // }
        // if(gameCanvas.isVisible()) {
        gameCanvas.repaint();
        // }
    }

    private void updateLogic() {
        if (isAIModeOpen && !isGamePaused) {
            Direction nextDirection = snakeAI.getNextMoveDirection(apple);
            snake.changeDirection(nextDirection);
        }

        snake.move();

        if (snake.isOutOfCanvas() || snake.checkBodyCollision()) {
            gameOver();
        }

        if (snake.getHeadLocation().equals(apple.getLocation())) {
            snake.addTail();
            resourceManager.playSound("crunch");
            isAppleEatenInThisFrame = true;
            eatenAppleVector = new Vector2i(apple.getLocation());

            snakeAI.resetMoveCounter();
            apple.spawn(snake.getBody());
        } else {
            isAppleEatenInThisFrame = false;
        }
    }

    public void gameOver() {
        System.out.println("Game Over :(");
        resourceManager.playSound("hit");
        try {
            Thread.sleep(1000); // sleep for a little time to play the sound
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public int getGameCanvasWidth() {
        return gameCanvas.getWidth();
    }

    public int getGameCanvasHeight() {
        return gameCanvas.getHeight();
    }

}
