import javax.swing.*;
import java.awt.*;

public class TextBox extends JTextField {
    private Point location;
    private Dimension size;
    private boolean selected;

    public TextBox(Point location, Dimension size) {
        this.location = location;
        this.size = size;
        this.setBounds(location.x, location.y, size.width, size.height);
        this.setFont(new Font("Arial", Font.PLAIN, 12));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void setLocation(Point location) {
        this.location = location;
        this.setBounds(location.x, location.y, size.width, size.height);
    }

    public void setSize(Dimension size) {
        this.size = size;
        this.setBounds(location.x, location.y, size.width, size.height);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected()) {
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        super.paintComponent(g);
    }
}
