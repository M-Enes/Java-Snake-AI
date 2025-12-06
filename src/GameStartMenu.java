import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameStartMenu extends Panel {

    private Game game;
    private int width;
    private int height;
    private final Color backgroundColor = Color.GRAY;
    private BufferedImage buttonRed;
    private BufferedImage buttonGrey;
    private Font font = new Font("Arial", Font.PLAIN, 40);
    private Graphics graphics;
    private Button startButton;

    public GameStartMenu(Game game, int width, int height) {
        super();
        this.game = game;
        this.width = width;
        this.height = height;
        try {
            this.buttonGrey = ImageIO
            .read(new File("graphics/kenney_ui-pack/PNG/Grey/Default/button_rectangle_depth_line.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.startButton = new Button("Start game");
        // startButton.setSize(200, 100);
        // startButton.setBounds(200, 200, 200, 100);
        startButton.setPreferredSize(new Dimension(200, 100));
        startButton.setFont(font);
        // startButton.setVisible(true);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();
            }
        });
        
        setLayout(new GridBagLayout());
        this.add(startButton);
        setBackground(backgroundColor);
        // setSize(width, height);
        setVisible(true);
        this.validate();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // startButton.setBounds(200, 200, 200, 100);

    }

}
