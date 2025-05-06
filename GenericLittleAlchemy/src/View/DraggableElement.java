package View;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;

import Main.*;
import Model.*;

public class DraggableElement extends JLayeredPane {

    private final Element element;
    private final JLabel imageLabel;
    public final KnownBarPanel kbp;
    public final MixingAreaPanel map;
    public final View view;
    public final Model model;
    
    private boolean isInKnownBarPanel;
    
    public DraggableElement(DraggableElement de) {
    	this(de.element, de.kbp, de.map, de.view, de.model, true);
    	de.getParent().add(this);
    	setLocation(de.getLocation());
    }
    
    public DraggableElement(Element elem, KnownBarPanel kbp, MixingAreaPanel map, View view, Model model, boolean isInKnownBarPanel) {
        this.element = elem;
        this.view = view;
        this.model = model;

        // Attempt to load the image
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("data/Element Icons/" + elem.iconFilepath));
        } catch (IOException e) {
            if (!elem.iconFilepath.equals(""))
            {
            	System.err.println("Couldn't load image for element: " + elem.id + " from " + elem.iconFilepath);
                e.printStackTrace();
            }
        }

        // Use a JLabel to display the image
        imageLabel = new JLabel();
        if (image != null) {
        	image = makeRoundedCorner(image, 30); // Apply rounded corners with 30px radius
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            imageLabel.setBounds(0, 0, 146, 134);
            setPreferredSize(new Dimension(146, 134));
            setMaximumSize(new Dimension(146, 134));
        } else {
            // Fallback if image is missing
            imageLabel.setText(elem.id);
            imageLabel.setOpaque(false);
            imageLabel.setBackground(new Color(0xaf9f90));
            imageLabel.setForeground(Color.BLACK);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setBounds(0, 0, 146, 134);
            setPreferredSize(new Dimension(146, 134));
            setMaximumSize(new Dimension(146, 134));
        }

        add(imageLabel, JLayeredPane.DEFAULT_LAYER);
        setLayout(null);
        setOpaque(false);
        
        this.kbp = kbp;
        this.map = map;
        var dragListener = new DragListener(this, List.of(map));
        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
        
        this.isInKnownBarPanel = isInKnownBarPanel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 30; // Roundness radius

        // Background fill
        g2.setColor(new Color(0xaf9f90)); // Use background color set via setBackground
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();
    }

    
    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Clear the background with full transparency
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect(0, 0, w, h);

        // Create the rounded rectangle mask
        g2.setClip(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        // Draw the original image with the clipping mask applied
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }



    public Element getElement() {
        return element;
    }
    
    // Drag listener for JLayeredPane
    class DragListener extends MouseAdapter {
        private final DraggableElement component;
        private Point initialClick;
        private Container originalParent;
        private List<JPanel> dropTargets;

        public DragListener(DraggableElement component, List<JPanel> dropTargets) {
            this.component = component;
            this.dropTargets = dropTargets;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint();
            component.requestFocusInWindow();

            originalParent = component.getParent();

            // Move to root pane's layered pane
            Window window = SwingUtilities.getWindowAncestor(component);
            if (window instanceof RootPaneContainer rpc) {
                JLayeredPane layeredPane = rpc.getLayeredPane();

                Point componentLocation = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), layeredPane);

                originalParent.remove(component);
                layeredPane.add(component, JLayeredPane.DRAG_LAYER);
                component.setBounds(componentLocation.x, componentLocation.y, component.getWidth(), component.getHeight());
                layeredPane.moveToFront(component);
                layeredPane.repaint();
            }
            
            view.repopulateKnownElements();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (initialClick == null) return;

            int newX = component.getX() + e.getX() - initialClick.x;
            int newY = component.getY() + e.getY() - initialClick.y;
            component.setLocation(newX, newY);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Window window = SwingUtilities.getWindowAncestor(component);
            if (!(window instanceof RootPaneContainer rpc)) return;

            JLayeredPane layeredPane = rpc.getLayeredPane();
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouseLocation, layeredPane);

            // Try to find a drop target panel under the mouse
            for (JPanel target : dropTargets) {
                Rectangle bounds = SwingUtilities.convertRectangle(target.getParent(), target.getBounds(), layeredPane);
                if (bounds.contains(mouseLocation)) {
                    // Convert to target panel's coordinates
                    Point converted = SwingUtilities.convertPoint(layeredPane, component.getLocation(), target);

                    layeredPane.remove(component);
                    target.add(component);
                    component.setLocation(converted);
                    target.revalidate();
                    target.repaint();
                    originalParent.revalidate();
                    originalParent.repaint();

                    // 🔍 Check for collision with other components in the panel
                    Rectangle compBounds = component.getBounds();
                    DraggableElement nearest = null;
                    double nearestDist = Double.MAX_VALUE;

                    for (Component other : target.getComponents()) {
                        if (other == component) continue;
                        if (compBounds.intersects(other.getBounds())) {
                            // Measure center-to-center distance
                            Point c1 = new Point(compBounds.x + compBounds.width / 2, compBounds.y + compBounds.height / 2);
                            Rectangle oBounds = other.getBounds();
                            Point c2 = new Point(oBounds.x + oBounds.width / 2, oBounds.y + oBounds.height / 2);

                            double dist = c1.distance(c2);
                            if (dist < nearestDist && other instanceof DraggableElement) {
                                nearestDist = dist;
                                nearest = (DraggableElement)other;
                            }
                        }
                    }

                    if (nearest != null) {
                        Element result = model.combineElements(component.element, (nearest).element);
                        if (result != null)
                        {
                        	var de = new DraggableElement(result, null, (MixingAreaPanel)dropTargets.get(0), view, model, false);
                        	de.setBounds(nearest.getBounds().x, nearest.getBounds().y, 146, 134);
                        	target.add(de);
                        	
                        	target.remove(nearest);
                        	target.remove(component);
                        }
                    }

                    return;
                }
            }

            // ❌ Not over any panel: delete
            layeredPane.remove(component);
            window.revalidate();
            window.repaint();
        }

        
    }

}


