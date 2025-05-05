package View;
import java.awt.*;
import javax.swing.*;

public class GameAreaPanel extends JPanel {
	
	public final MixingAreaPanel mixingArea;
	public final KnownBarPanel knownBar;
	
	public GameAreaPanel ()
	{
		setLayout(new BorderLayout ());
		
		// populate the panel
		mixingArea = new MixingAreaPanel();
		knownBar = new KnownBarPanel();
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mixingArea, knownBar);
		add(splitPane, BorderLayout.CENTER);
		
		// adjust the split pane
		splitPane.setResizeWeight(1.0);
			
		setBackground(Color.GREEN);		
	}
	
}
