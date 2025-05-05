package View;

import java.awt.Component;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

import javax.swing.*;

import Main.*;
import Model.*;

public abstract class View {
	
	GameWindow window;
	
	Set<Element> knownElements;
	public Model model;
	
	public View()
	{
		window = new GameWindow();
		
		window.setVisible(true);
		
		// TODO: delete this listener later
		// close this window when it loses focus
		var test = new WindowFocusListener ()
		{
			@Override
			public void windowGainedFocus(WindowEvent e) {}

			@Override
			public void windowLostFocus(WindowEvent e)
			{
				System.exit(0);
			}
		};
		window.addWindowFocusListener(test);
	}
	
	public void setModel(Model model)
	{
		this.model = model;
	}
	
	public void repopulateKnownElements()
	{
		window.gameArea.knownBar.removeAll(); // Clear existing elements

		for (Element elem : knownElements) {
			JLabel label = new JLabel(elem.name, SwingConstants.CENTER);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			label.setFont(new Font("SansSerif", Font.BOLD, 14));

			DraggableElement draggable = new DraggableElement(elem, window.gameArea.knownBar, window.gameArea.mixingArea, this, model, true);
			draggable.setAlignmentX(Component.CENTER_ALIGNMENT);

			window.gameArea.knownBar.add(Box.createVerticalStrut(10));
			window.gameArea.knownBar.add(label);
			window.gameArea.knownBar.add(draggable);
		}

		window.gameArea.knownBar.revalidate();
		window.gameArea.knownBar.repaint();
	}
	
	public void repopulateKnownElements(Set<Element> knownElements) {
		this.knownElements = knownElements;
		repopulateKnownElements();
	}


}
