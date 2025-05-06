package View;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    public void setImage(Image img) {
        this.image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int imgWidth = image.getWidth(this);
            int imgHeight = image.getHeight(this);

            if (imgWidth > 0 && imgHeight > 0) {
                // Compute scale to fit while preserving aspect ratio
                double scaleX = (double) panelWidth / imgWidth;
                double scaleY = (double) panelHeight / imgHeight;
                double scale = Math.min(scaleX, scaleY); // fit within panel

                int drawWidth = (int) (imgWidth * scale);
                int drawHeight = (int) (imgHeight * scale);

                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                g.drawImage(image, x, y, drawWidth, drawHeight, this);
            }
        }
    }
}
