import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.awt.image.BufferedImage;

public class BetterButton implements Serializable, ActionListener {
    private int height;
    private int width;
    private int positionx;
    private int positiony;
    private Color color;
    Rectangle rect;
    private boolean collide, clicked;
    private ActionListener actionListener;
    private BufferedImage icon;

    public BetterButton(int positionx, int positiony, int height, int width, BufferedImage icon )
    {
        this.height = height;
        this.width = width;
        this.positionx = positionx;
        this.positiony = positiony;
        collide = false;
        clicked = false;
        color = Color.BLACK;
        this.icon = icon;
        rect = new Rectangle(positionx, positiony, height, width);
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
    public Rectangle returnRect()
    {
        return rect;
    }
    public void Paint(Graphics g2d)
    {
        if(collide || clicked)
        {
            Graphics2D g2d2 = (Graphics2D) g2d;

        // Set the stroke to draw the rectangle outline
        g2d2.setStroke(new BasicStroke(1));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(positionx, positiony, height, width);
        }
        
        g2d.setColor(new Color(255, 255, 204, 0));
        //g2d.setColor(color);
        g2d.fillRect(positionx, positiony, height, width);
        if (icon != null) {
            g2d.drawImage(icon, positionx, positiony, width, height, null);
        }
    }
    public void Clicked(boolean status)
    {
        clicked = status;
    }
    public boolean getStatus()
    {
        return clicked;
    }
    public boolean Collision(Point e)
    {
        if(rect.contains(e))
        {
            collide = true;
            return true;
        }
        else
        {
            collide = false;
            return false;
        }
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    // Implementation of actionPerformed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (actionListener != null) {
            actionListener.actionPerformed(e);
        }
    }

    
}
