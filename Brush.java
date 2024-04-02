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
        SPRAY_PAINT
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
                g2d.setColor(g2d.getColor());
                g2d.drawLine(x - size / 2, y - size / 2, normalx, normaly);
                break;
            case MARKER:
                int markerSize = size * 2;
                g2d.setColor(g2d.getColor());
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

                g2d.setPaint(texturePaint);
                g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawLine(x, y, normalx, normaly);
                
                break;
            
            case SPRAY_PAINT:
                g2d.setColor(g2d.getColor());
                Random randomSpray = new Random(FIXED_SEED);
                int spraySize = size * 8;
                for (int i = 0; i < spraySize; i++) {
                    int offsetX = randomSpray.nextInt(size * 2) - size;
                    int offsetY = randomSpray.nextInt(size * 2) - size;
                    g2d.fillRect(x + offsetX, y + offsetY, 1, 1);
                }
                break;
            default:
        }

    }
}
