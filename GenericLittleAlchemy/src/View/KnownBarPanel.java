package View;
import java.awt.*;
import javax.swing.*;

public class KnownBarPanel extends JPanel {
	
	public KnownBarPanel ()
	{
		setMinimumSize(new Dimension(0, 0));
		setPreferredSize(new Dimension (250, 0));
		
		setBackground(new Color (0xff8040));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}
	
}
