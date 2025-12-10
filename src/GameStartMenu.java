import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameStartMenu extends Panel {

    private Game game;
    private final Color backgroundColor = Color.GRAY;
    private Font font = new Font("Arial", Font.PLAIN, 40);
    private Button startButton;

    public GameStartMenu(Game game, int width, int height) {
        super();
        this.game = game;
        
        this.startButton = new Button("Start game");
        startButton.setPreferredSize(new Dimension(200, 100));
        startButton.setFont(font);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();
            }
        });
        
        setLayout(new GridBagLayout());
        this.add(startButton);
        setBackground(backgroundColor);
        setVisible(true);
        this.validate();
    }

}
