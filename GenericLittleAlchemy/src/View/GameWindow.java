package View;
import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {
	
	public final GameAreaPanel gameArea;
	
	public GameWindow ()
	{
		// populate the window
		gameArea = new GameAreaPanel();
		add(gameArea);

		// adjust the window
		setSize(gameArea.getPreferredSize());
	    setMinimumSize(gameArea.getMinimumSize());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
