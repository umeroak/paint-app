// DrawingPanel.java (updated with undo and redo methods)
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {
    private List<List<Point>> scribbleLines = new ArrayList<>();
    private List<Point> currentLine = new ArrayList<>();
    private Color drawingColor = Color.BLACK;

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
        g2d.setStroke(new BasicStroke(2)); // Set default stroke width

        for (List<Point> line : scribbleLines) {
            if (line.size() > 1) {
                Point prevPoint = line.get(0);
                for (int i = 1; i < line.size(); i++) {
                    Point currentPoint = line.get(i);
                    g2d.setColor(drawingColor);
                    g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                    prevPoint = currentPoint;
                }
            }
        }

        if (currentLine.size() > 1) {
            g2d.setColor(drawingColor);
            Point prevPoint = currentLine.get(0);
            for (int i = 1; i < currentLine.size(); i++) {
                Point currentPoint = currentLine.get(i);
                g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                prevPoint = currentPoint;
            }
        }

        g2d.dispose();
    }

    public void undo() {
        if (!scribbleLines.isEmpty()) {
            scribbleLines.remove(scribbleLines.size() - 1);
            repaint();
        }
    }

    public void redo() {
        // Not implemented for this example
    }

    public void setDrawingColor(Color color) {
        this.drawingColor = color;
    }

    public Color getDrawingColor() {
        return drawingColor;
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
