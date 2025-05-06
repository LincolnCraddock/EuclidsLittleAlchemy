package View;

import javax.swing.*;

import Main.*;
import Model.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.TreeMap;

public class EncyclopediaWindow extends JFrame {
    private final JList<String> imageList;
    private final ImagePanel imageViewer;
    private final TreeMap<String, ImageIcon> images;

    public EncyclopediaWindow(Model model) {		
        super("Encyclopedia");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1800, 400);
        
        // close this window when it loses focus
 		var closer = new WindowFocusListener ()
 		{
 			@Override
 			public void windowGainedFocus(WindowEvent e) {}

 			@Override
 			public void windowLostFocus(WindowEvent e)
 			{
 				dispose();
 			}
 		};
 		addWindowFocusListener(closer);

        // Example image data
        images = new TreeMap<>();
        for (Element e : model.knownElements)
        {
        	images.put(e.id, new ImageIcon("data/Element Dictionary Images/" + e.id + ".png"));
        }

        // Create list
        imageList = new JList<>(model.knownElements.stream().map(x -> x.id).toArray(String[]::new));
        JScrollPane listScrollPane = new JScrollPane(imageList);

        // Create viewer
        imageViewer = new ImagePanel();
        JScrollPane viewerScrollPane = new JScrollPane(imageViewer);
        

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, viewerScrollPane);


        splitPane.setDividerLocation(150);
        add(splitPane);

        // List selection logic
        imageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String key = imageList.getSelectedValue();
                ImageIcon icon = images.get(key);
                if (icon != null) {
                    imageViewer.setImage(icon.getImage());
                }
            }
        });


    }
}
