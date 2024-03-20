import java.awt.*;
import java.io.Serializable;

public class Shape implements Serializable {
    public static final int RECTANGLE = 0;
    public static final int OVAL = 1;
    public static final int STRAIGHT_LINE = 2;


    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private Color color;
    private BasicStroke stroke;
    private int shape;

    public Shape(int x1, int y1, int x2, int y2, Color color, BasicStroke stroke, int shape) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
    }

    public int getShape() {
        return shape;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }
}