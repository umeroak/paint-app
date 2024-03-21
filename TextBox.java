import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextBox extends JTextField {
    private Point location;
    private Dimension size;
    private boolean selected;
    private Point mouseLocation; // Declare mouseLocation as a field

    public TextBox(Point location, Dimension size) {
        this.location = location;
        this.size = size;
        this.setBounds(location.x, location.y, size.width, size.height);
        this.setFont(new Font("Arial", Font.PLAIN, 12));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Set only the background to be transparent
        setBackground(new Color(0, 0, 0, 0)); // Transparent background color
        setOpaque(false); // Make the background transparent

        // Add mouse listeners for moving and resizing
        addMouseListeners();
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

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseLocation = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseLocation = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseLocation != null) {
                    // Calculate new location based on mouse movement
                    int deltaX = e.getX() - mouseLocation.x;
                    int deltaY = e.getY() - mouseLocation.y;
                    Point newLocation = new Point(location.x + deltaX, location.y + deltaY);

                    // Set the new location
                    setLocation(newLocation);
                }
            }
        });
    }
}
