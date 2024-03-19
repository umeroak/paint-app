import java.awt.*;
import java.util.Random;

public class Brush {
    public enum BrushType {
        DEFAULT,
        HIGHLIGHTER,
        MARKER,
        PENCIL,
        PEN,
        CRAYON,
        SPRAY_PAINT
    }

    private BrushType brushType;
    private int size;
    
   


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

    public void setSize(int x)
    {
        size = x;
    }

    public void paint(Graphics2D g2d, int x, int y, int normalx, int normaly) {
        switch (brushType) {
            case DEFAULT:
            g2d.setStroke(new BasicStroke(size));
            g2d.drawLine(x, y, normalx, normaly);
                break;
            case HIGHLIGHTER:
                Color highlighterColor = new Color (g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), 20);
                g2d.setColor(highlighterColor);
                g2d.drawLine(x - size / 2, y - size / 2, normalx, normaly);
                break;
            case MARKER:
                int markerSize = size * 2; 
                Color markerColor = new Color(g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), 100); 
                g2d.setColor(markerColor);
                g2d.drawLine(x - markerSize / 2, y - markerSize / 2, normalx, normaly);
                
                break;
            case PENCIL:
            g2d.setColor(g2d.getColor());
        
            g2d.drawLine(x, y, x, y);
                break;
            case PEN:
            g2d.setColor(g2d.getColor());
                g2d.drawLine(x - size / 2, y - size / 2, normalx, normaly); 
                break;
            case CRAYON:
                g2d.setColor(g2d.getColor());
                Random random = new Random();
    
                // Set parameters for crayon strokes
                int strokeLength = size; // Length of each stroke
                int gapLength = size / 2; // Gap between strokes
    
                // Simulate crayon strokes with irregularity
                int currentX = normalx;
                int currentY = normaly;
    
                while (strokeLength > 0) {
                    // Randomize the stroke length and gap length slightly
                    int actualStrokeLength = strokeLength + random.nextInt(size) - size / 2;
                    int actualGapLength = gapLength + random.nextInt(size) - size / 2;
    
                    // Calculate end point of the stroke
                    int endX = currentX + (int) ((actualStrokeLength + random.nextInt(size) - size / 2) * Math.cos(Math.toRadians(random.nextInt(360))));
                    int endY = currentY + (int) ((actualStrokeLength + random.nextInt(size) - size / 2) * Math.sin(Math.toRadians(random.nextInt(360))));
    


                    
                    g2d.drawLine(currentX, currentY, x, y);
    
                    // Update current position
                    currentX = endX + (int) ((actualGapLength + random.nextInt(size) - size / 2) * Math.cos(Math.toRadians(random.nextInt(360))));
                    currentY = endY + (int) ((actualGapLength + random.nextInt(size) - size / 2) * Math.sin(Math.toRadians(random.nextInt(360))));
    
                    // Decrease remaining stroke length
                    strokeLength -= actualStrokeLength + actualGapLength;

                }
                break;
            case SPRAY_PAINT:
                Random random1 = new Random();
                int spraySize = size * 3; // Adjust spray size as needed
                for (int i = 0; i < spraySize; i++) {
                    int offsetX = random1.nextInt(size * 2) - size;
                    int offsetY = random1.nextInt(size * 2) - size;
                    g2d.drawLine(x + offsetX, y + offsetY, normalx, normaly);
                }
                break;
                default:
        }
        
        
    }
}