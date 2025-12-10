import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceManager {
    private final Map<String, BufferedImage> textures = new HashMap<>();
    private final Map<String, Clip> soundClips = new HashMap<>();

    public void loadResources() {
        try {
            loadTexture("head_up", "graphics/head_up.png");
            loadTexture("head_down", "graphics/head_down.png");
            loadTexture("head_right", "graphics/head_right.png");
            loadTexture("head_left", "graphics/head_left.png");

            loadTexture("body_horizontal", "graphics/body_horizontal.png");
            loadTexture("body_vertical", "graphics/body_vertical.png");
            loadTexture("body_topright", "graphics/body_topright.png");
            loadTexture("body_topleft", "graphics/body_topleft.png");
            loadTexture("body_bottomright", "graphics/body_bottomright.png");
            loadTexture("body_bottomleft", "graphics/body_bottomleft.png");

            loadTexture("tail_up", "graphics/tail_up.png");
            loadTexture("tail_down", "graphics/tail_down.png");
            loadTexture("tail_right", "graphics/tail_right.png");
            loadTexture("tail_left", "graphics/tail_left.png");

            loadTexture("apple", "graphics/apple.png");

            // TODO: add UI element resources here

            loadSound("crunch", "sounds/koops-apple-crunch.wav");
            loadSound("hit", "sounds/hit.wav");
            loadSound("music", "sounds/nokiasnakemusic.wav");


        } catch (Exception e) {
            System.out.println("Failed to load resources. Game might crash.");
            e.printStackTrace();
        }
    }

    private void loadTexture(String key, String path) throws IOException {
        BufferedImage image = ImageIO.read(new File(path));
        textures.put(key, image);
    }

    private void loadSound(String key, String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            soundClips.put(key, clip);
        } catch (Exception e) {
            System.out.println("Could not load sound: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Returns the image.
     * WARNING: Do not modify the image. 
     */
    public BufferedImage getTexture(String key) {
        return textures.get(key);
    }

    /**
     * Plays a sound.
     * If the sound is already playing, it restarts it.
     */
    public void playSound(String key) {
        Clip clip = soundClips.get(key);
        if (clip != null) {
            // stops and rewinds if clip is playing
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /*
    * Loops a sound.
    * If the sound is already playing, it does not do anything.
    */
    public void loopSound(String key) {
        Clip clip = soundClips.get(key);
        if (clip != null) {
            if(clip.isRunning()) {
                return;
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the sound.
     */
    public void stopSound(String key) {
        Clip clip = soundClips.get(key);
        if(clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
