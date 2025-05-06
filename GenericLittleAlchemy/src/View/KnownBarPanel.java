package View;
import java.awt.*;
import javax.swing.*;

public class KnownBarPanel extends JPanel {
	
	public KnownBarPanel ()
	{
		setMaximumSize(new Dimension(getMaximumSize().width, Integer.MAX_VALUE));
		
		//setBackground(new Color (0xff8040));
		setBackground(new Color(0xf0e5d0));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}
	
	@Override
	public Dimension getMinimumSize() {
	    Dimension size = super.getSize();
	    size.width = 250; // fixed width
	    return size;
	}
	
	@Override
	public Dimension getPreferredSize() {
		int len = 0;
		for (Component c : getComponents())
			len += c.getPreferredSize().height;
	    Dimension size = new Dimension(250, len);
	    size.width = 250; // fixed width
	    return size;
	}

	
}
