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
    private List<Shape> shapes = new ArrayList<>();
    private Shape currentShape;
    private List<List<Point>> undoneLines = new ArrayList<>(); // List to store undone lines
    private double scaleFactor = 1.0; // Scale factor for zooming

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

        // Apply zoom
        g2d.scale(scaleFactor, scaleFactor);

        // Draw background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }

        // Draw existing lines
        for (List<Point> line : scribbleLines) {
            if (line.size() > 1) {
                g2d.setColor(currentColor);
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

        for (Shape shape : shapes) {
            drawShape(g2d, shape);
        }

        // Draw current shape if any...
        if (currentShape != null) {
            drawShape(g2d, currentShape);
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
            undoneLines.add(scribbleLines.remove(scribbleLines.size() - 1)); // Move the undone line to undoneLines
            repaint();
        }
    }

    public void redo() {
        if (!undoneLines.isEmpty()) {
            scribbleLines.add(undoneLines.remove(undoneLines.size() - 1)); // Move the undone line back to scribbleLines
            repaint();
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setStrokeWidth(float width) {
        this.currentStrokeWidth = width;
    }

    private void drawShape(Graphics2D g2d, Shape shape) {
        g2d.setColor(shape.getColor());
        g2d.setStroke(shape.getStroke());

        int x1 = shape.getX1();
        int y1 = shape.getY1();
        int x2 = shape.getX2();
        int y2 = shape.getY2();

        switch (shape.getShape()) {
            case Shape.RECTANGLE:
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
            case Shape.OVAL:
                g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                break;
            // Add more shape types if needed...
        }
    }

    public void setCurrentShape(int shapeType) {
        currentShape = new Shape(0, 0, 0, 0, Color.BLACK, new BasicStroke(2.0f), shapeType);
        repaint();
    }

    private class DrawingMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (currentShape != null) {
                int x = e.getX();
                int y = e.getY();
                currentShape = new Shape(x, y, x, y, currentColor, new BasicStroke(currentStrokeWidth), currentShape.getShape());
            } else {
                currentLine.clear();
                currentLine.add(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (currentShape != null) {
                int x = e.getX();
                int y = e.getY();
                currentShape.setX2(x);
                currentShape.setY2(y);
                shapes.add(currentShape);
                currentShape = null;
                repaint();
            } else {
                scribbleLines.add(new ArrayList<>(currentLine));
                currentLine.clear();
                repaint();
            }
        }
    }

    private class DrawingMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentShape != null) {
                int x = e.getX();
                int y = e.getY();
                currentShape.setX2(x);
                currentShape.setY2(y);
                repaint();
            } else {
                currentLine.add(e.getPoint());
                repaint();
            }
        }
    }

    public void zoomIn() {
        scaleFactor *= 1.1; // Increase scale factor for zooming in
        repaint();
    }

    public void zoomOut() {
        scaleFactor /= 1.1; // Decrease scale factor for zooming out
        repaint();
    }
}
