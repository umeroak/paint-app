import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class Brush {
    public enum BrushType {
        DEFAULT,
        HIGHLIGHTER,
        MARKER,
        PENCIL,
        PEN,
        CRAYON,
        SPRAY_PAINT,
        ERASER
    }

    private BrushType brushType;
    private int size;
    private static final long FIXED_SEED = 12345;
    private Color color = Color.BLACK;

    public Brush(BrushType brushType, int size) {
        this.brushType = brushType;
        this.size = size;

    }

    public BrushType getBrushType() {
        return brushType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int x) {
        size = x;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void paint(Graphics2D g2d, int x, int y, int normalx, int normaly, TexturePaint texturePaint) {
        switch (brushType) {
            case DEFAULT:
                g2d.setColor(g2d.getColor());
                g2d.setStroke(new BasicStroke(size));
                g2d.drawLine(x, y, normalx, normaly);
                break;
            case HIGHLIGHTER:
            Color highlighterColor = new Color(g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), 20);
            g2d.setColor(highlighterColor);
            
            // Calculate the distance between points
            double dx = normalx - x;
            double dy = normaly - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Determine the number of segments based on distance
            int numSegments = (int) (distance / (size / 2));
            if (numSegments < 1) numSegments = 1; // Ensure at least one segment
            
            // Calculate the step size for each segment
            double stepX = dx / numSegments;
            double stepY = dy / numSegments;
            
            // Draw each segment
            double startX = x;
            double startY = y;
            for (int i = 0; i < numSegments; i++) {
                double endX = startX + stepX;
                double endY = startY + stepY;
                g2d.drawLine((int) (startX - size / 2), (int) (startY - size / 2), (int) (endX - size / 2), (int) (endY - size / 2));
                startX = endX;
                startY = endY;
            }
            break;
            case MARKER:
                int markerSize = size * 1;
                g2d.setColor(g2d.getColor());
                g2d.drawLine(x - markerSize / 2, y - markerSize / 2, normalx, normaly);
                g2d.setStroke(new BasicStroke(size));

                break;
            case PENCIL:
            g2d.setStroke(new BasicStroke(size));
                g2d.setColor(g2d.getColor());
                
                g2d.drawLine(x, y, normalx, normaly);
                break;
            case PEN:
                g2d.setColor(g2d.getColor());
                g2d.setStroke(new BasicStroke(size));
                g2d.drawLine(x - size / 2, y - size / 2, normalx, normaly);
              
                break;
            
            case CRAYON:

                g2d.setPaint(texturePaint);
                g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawLine(x, y, normalx, normaly);
                
                break;
            
            case SPRAY_PAINT:
            
                g2d.setColor(g2d.getColor());
 // Choose any seed value you want

// Create a Random object with the fixed seed
                Random random = new Random();
                int spraySize = size * 1; 
                for (int i = 0; i < spraySize; i++) {
                    int offsetX = random.nextInt(size * 2) - size;
                    int offsetY = random.nextInt(size * 2) - size;
                    g2d.fillOval(x + offsetX, y + offsetY, 1, 1);
                }
                break;
            case ERASER:
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(size));
                g2d.drawLine(x, y, normalx, normaly);
                
            default:
        }

    }
}
