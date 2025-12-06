import java.awt.Color;

public class Config {

    // Window & Grid Settings
    public static final String WINDOW_TITLE = "Snake Game with AI";
    public static final int COLUMNS = 20; // Width in blocks
    public static final int ROWS = 15; // Height in blocks
    public static final int BLOCK_SIZE = 40; // Pixels per block

    // Gameplay Settings
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 10; // Updates per second
    public static final int SNAKE_START_X = 8;
    public static final int SNAKE_START_Y = 8;
    public static final int APPLE_RADIUS = 1; // Logical radius (usually 1 block)

    // AI Settings
    public static final int AI_MOVE_LIMIT = 400; // Moves before AI gives up (anti-loop)

    // Graphics Settings
    // Particles
    public static final int PARTICLE_LIFETIME = 10;
    public static final int PARTICLE_COUNT = 5;
    public static final int PARTICLE_SIZE = 5;
    public static final int PARTICLE_DEVIATION = 10;

    // Colors
    public static final Color COLOR_BG_AI = Color.ORANGE;
    public static final Color COLOR_BG_NORMAL = Color.LIGHT_GRAY;
    public static final Color COLOR_TEXT = Color.BLACK;
    public static final int FONT_SIZE = 30;
}
