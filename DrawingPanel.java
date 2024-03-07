import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingPanel extends JPanel {
    private List<List<Point>> scribbleLines = new ArrayList<>();
    private List<Point> currentLine = new ArrayList<>();
    private Color currentColor = Color.BLACK; // Default drawing color
    private float currentStrokeWidth = 2.0f; // Default stroke width
    private Map<List<Point>, Color> lineColors = new HashMap<>();
    private BufferedImage backgroundImage; // Variable to hold loaded image

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600)); // Set default size
        addMouseListener(new DrawingMouseListener());
        addMouseMotionListener(new DrawingMouseMotionListener());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(currentStrokeWidth)); // Set current stroke width

        // Draw background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }

        // Draw existing lines
        for (List<Point> line : scribbleLines) {
            if (line.size() > 1) {
                g2d.setColor(lineColors.get(line));
                Point prevPoint = line.get(0);
                for (int i = 1; i < line.size(); i++) {
                    Point currentPoint = line.get(i);
                    g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                    prevPoint = currentPoint;
                }
            }
        }

        // Draw current line
        if (currentLine.size() > 1) {
            g2d.setColor(currentColor);
            Point prevPoint = currentLine.get(0);
            for (int i = 1; i < currentLine.size(); i++) {
                Point currentPoint = currentLine.get(i);
                g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                prevPoint = currentPoint;
            }
        }

        g2d.dispose();
    }

    public void saveImage(File file) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        ImageIO.write(image, "PNG", file);
    }

    public void openImage(File file) throws IOException {
        backgroundImage = ImageIO.read(file);
        repaint();
    }

    public void undo() {
        if (!scribbleLines.isEmpty()) {
            scribbleLines.remove(scribbleLines.size() - 1);
            repaint();
        }
    }

    public void redo() {
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setStrokeWidth(float width) {
        this.currentStrokeWidth = width;
    }

    private class DrawingMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            currentLine.clear();
            currentLine.add(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            scribbleLines.add(new ArrayList<>(currentLine));
            lineColors.put(new ArrayList<>(currentLine), currentColor);
            currentLine.clear();
            repaint();
        }
    }

    private class DrawingMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            currentLine.add(e.getPoint());
            repaint();
        }
    }
}
