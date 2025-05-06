package View;
import java.awt.*;
import javax.swing.*;

public class GameAreaPanel extends JPanel {
	
	public final MixingAreaPanel mixingArea;
	public final KnownBarPanel knownBar;
	public final JScrollPane knownBarScrollPane;
	
	public GameAreaPanel() {
	    setLayout(new BorderLayout());

	    mixingArea = new MixingAreaPanel();
	    knownBar = new KnownBarPanel();
	    	    
	    knownBarScrollPane = new JScrollPane(knownBar);
	    knownBarScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    knownBarScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mixingArea, knownBarScrollPane);
	    add(splitPane, BorderLayout.CENTER);
	    
	    splitPane.setResizeWeight(1.0);

	    setBackground(Color.GREEN);
	}

	
}
