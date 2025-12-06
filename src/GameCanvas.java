import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

public class GameCanvas extends Canvas {
    private final Game game;
    private Snake snake;
    private Apple apple;
    private BufferedImage offScreenImage;
    private Graphics2D offScreenGraphics;
    private final Dimension canvasDimension;
    private int appleParticleFrameCounter = 0;
    private final ResourceManager resources;
    private Random random;

    public GameCanvas(Game game, ResourceManager resources, Snake snake, Apple apple, Dimension canvasDimension) {
        this.game = game;
        this.canvasDimension = canvasDimension;
        this.snake = snake;
        this.apple = apple;
        // setSize(canvasDimension.width * Config.BLOCK_SIZE, canvasDimension.height *
        // Config.BLOCK_SIZE);
        this.resources = resources;
        this.random = new Random();
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = new BufferedImage(canvasDimension.width * Config.BLOCK_SIZE,
                    canvasDimension.height * Config.BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
            offScreenGraphics = offScreenImage.createGraphics();
            offScreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        clear();
        paintSnake();
        paintApple();

        if (game.isAppleEatenInThisFrame() && appleParticleFrameCounter < Config.PARTICLE_LIFETIME) {
            Vector2i eatenAppleVector = game.getEatenAppleVector();
            paintAppleParticles(eatenAppleVector);
        } else {
            appleParticleFrameCounter = 0;
        }

        int windowWidth = getWidth();
        int windowHeight = getHeight();

        if (windowWidth == 0 || windowHeight == 0) {
            return;
        }

        String infoText = "Score: " + (snake.getBody().size() - 3) + " AI Mode: "
                + (game.isAIModeOpen() ? "Open" : "Closed") + ((game.isGamePaused()) ? " Paused" : "");

        BufferedImage buffImg = new BufferedImage(windowWidth, windowHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D buffg2d = buffImg.createGraphics();
        buffg2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        buffg2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        buffg2d.drawImage(offScreenImage, 0, 0, windowWidth, windowHeight, null);

        buffg2d.setFont(new Font("Arial", Font.BOLD, Config.FONT_SIZE));
        buffg2d.setColor(Color.BLACK);
        buffg2d.drawString(infoText, 25, 25);

        g.drawImage(buffImg, 0, 0, windowWidth, windowHeight, null);
    }

    private void paintAppleParticles(Vector2i eatenAppleVector) {
        int appleX = eatenAppleVector.x;
        int appleY = eatenAppleVector.y;

        offScreenGraphics.setColor(Color.RED);

        // TODO: create outofcanvas method in Game.java for universal access and check
        // for outofcanvas for particles
        for (int i = 0; i < Config.PARTICLE_COUNT; i++) {
            int particleX = random.nextInt(appleX * Config.BLOCK_SIZE - Config.PARTICLE_DEVIATION,
                    appleX * Config.BLOCK_SIZE + Config.PARTICLE_DEVIATION + 1);
            int particleY = random.nextInt(appleY * Config.BLOCK_SIZE - Config.PARTICLE_DEVIATION,
                    appleY * Config.BLOCK_SIZE + Config.PARTICLE_DEVIATION + 1);

            offScreenGraphics.drawOval(particleX, particleY, Config.PARTICLE_SIZE, Config.PARTICLE_SIZE);
        }

    }

    private void paintSnake() {
        List<Vector2i> body = snake.getBody();
        Vector2i head = body.getFirst();
        Image img;

        if (body.size() > 1) {
            Vector2i neck = body.get(1);

            if (head.y < neck.y) {
                img = resources.getTexture("head_up");
            } else if (head.y > neck.y) {
                img = resources.getTexture("head_down");
            } else if (head.x < neck.x) {
                img = resources.getTexture("head_left");
            } else {
                img = resources.getTexture("head_right");
            }
        } else {
            Direction direction = snake.getDirection();
            if (direction == Direction.UP) {
                img = resources.getTexture("head_up");
            } else if (direction == Direction.DOWN) {
                img = resources.getTexture("head_down");
            } else if (direction == Direction.LEFT) {
                img = resources.getTexture("head_left");
            } else {
                img = resources.getTexture("head_right");
            }
        }
        offScreenGraphics.drawImage(img, head.x * Config.BLOCK_SIZE, head.y * Config.BLOCK_SIZE,
                snake.getPartWidth() * Config.BLOCK_SIZE, snake.getPartHeight() * Config.BLOCK_SIZE, null);

        Vector2i previous = head;
        Vector2i current;
        Vector2i next;
        for (int i = 1; i < body.size() - 1; i++) {
            current = body.get(i);
            next = body.get(i + 1);
            Image bodyImage;

            if (previous.x == next.x) {
                bodyImage = resources.getTexture("body_vertical");
                // offScreenGraphics.fillRect((int) part.x + 5, (int) part.y,
                // snake.getPartWidth() - 10,
                // snake.getPartHeight());
            } else if (previous.y == next.y) {
                bodyImage = resources.getTexture("body_horizontal");
                // offScreenGraphics.fillRect((int) part.x, (int) part.y + 5,
                // snake.getPartWidth(),
                // snake.getPartHeight() - 10);
            } else if ((previous.x < current.x && next.y < current.y) || (next.x < current.x && previous.y < next.y)) {
                bodyImage = resources.getTexture("body_topleft");
            } else if ((previous.x < current.x && next.y > current.y)
                    || (next.x < current.x && previous.y > current.y)) {
                bodyImage = resources.getTexture("body_bottomleft");
            } else if ((previous.x > current.x && next.y < current.y)
                    || (next.x > current.x && previous.y < current.y)) {
                bodyImage = resources.getTexture("body_topright");
            } else {
                bodyImage = resources.getTexture("body_bottomright");
            }

            offScreenGraphics.drawImage(bodyImage, current.x * Config.BLOCK_SIZE, current.y * Config.BLOCK_SIZE,
                    snake.getPartWidth() * Config.BLOCK_SIZE,
                    snake.getPartHeight() * Config.BLOCK_SIZE, null);

            previous = current;
        }

        if (body.size() >= 2) {

            Image tailImage;

            Vector2i tail = body.getLast();
            if (previous.x == tail.x && previous.y > tail.y) {
                tailImage = resources.getTexture("tail_up");
            } else if (previous.x == tail.x && previous.y < tail.y) {
                tailImage = resources.getTexture("tail_down");
            } else if (previous.y == tail.y && previous.x > tail.x) {
                tailImage = resources.getTexture("tail_left");
            } else {
                tailImage = resources.getTexture("tail_right");
            }

            offScreenGraphics.drawImage(tailImage, tail.x * Config.BLOCK_SIZE, tail.y * Config.BLOCK_SIZE,
                    snake.getPartWidth() * Config.BLOCK_SIZE,
                    snake.getPartHeight() * Config.BLOCK_SIZE, null);
        }

    }

    private void clear() {

        if (game.isAIModeOpen()) {
            offScreenGraphics.setColor(Config.COLOR_BG_AI);
        } else {
            offScreenGraphics.setColor(Config.COLOR_BG_NORMAL);
        }
        offScreenGraphics.fillRect(0, 0, canvasDimension.width * Config.BLOCK_SIZE,
                canvasDimension.height * Config.BLOCK_SIZE);
    }

    private void paintApple() {
        offScreenGraphics.setColor(Color.RED);
        // offScreenGraphics.fillOval((int) apple.getLocationX(), (int)
        // apple.getLocationY(), (int) apple.radius,
        // (int) apple.radius);
        offScreenGraphics.drawImage(resources.getTexture("apple"), apple.getLocationX() * Config.BLOCK_SIZE,
                apple.getLocationY() * Config.BLOCK_SIZE, apple.getRadius() * Config.BLOCK_SIZE,
                apple.getRadius() * Config.BLOCK_SIZE, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}