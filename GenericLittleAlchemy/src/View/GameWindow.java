package View;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Model.*;

public class GameWindow extends JFrame {
	
	public final GameAreaPanel gameArea;
	
	public GameWindow (Model model)
	{
		// populate the window
		gameArea = new GameAreaPanel();
		add(gameArea);

		// adjust the window
		setSize(gameArea.getPreferredSize());
	    setMinimumSize(gameArea.getMinimumSize());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// menu bar
		var menuBar = new JMenuBar();
		var encyclopediaOption = new JMenuItem("See Encyclopedia");

		menuBar.add(encyclopediaOption);
		setJMenuBar(menuBar);
		
		encyclopediaOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new EncyclopediaWindow(model).setVisible(true);
			}});
	}
	
}
