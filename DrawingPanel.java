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
    private ArrayList<TextBox> textBoxes;
    private TextBox textBox;
    private Point textBoxOffset;
    private List<List<Point>> scribbleLines = new ArrayList<>();
    private List<Point> currentLine = new ArrayList<>();
    private Color currentColor = Color.BLACK; // Default drawing color
    private float currentStrokeWidth = 2.0f; // Default stroke width
    private Map<List<Point>, Color> lineColors = new HashMap<>();
    private BufferedImage backgroundImage; // Variable to hold loaded image
    private List<Shape> shapes = new ArrayList<>();
    private Shape currentShape;
    private int toolbarsize = 200;
    private List<List<Point>> undoneLines = new ArrayList<>(); // List to store undone lines
    private boolean eraserMode = false; // Flag to indicate eraser mode
    private double scaleFactor = 1.0; // Scale factor for zooming
    private List<Shape> undoShapes = new ArrayList<>();
    private List<String> undoList = new ArrayList<>();
    private List<String> redoList = new ArrayList<>();
    private Brush currentBrush = new Brush(Brush.BrushType.DEFAULT, 10); // Default brush
    private List<Integer> type = new ArrayList<>();//                                               added type arrayList for brush type -shafiul
    private List<Integer> size = new ArrayList<>();// arraylist of size, set size, increase size = setsize getsize+5
    private int brushSize = 10;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600)); // Set DEFAULT size
        addMouseListener(new DrawingMouseListener());
        addMouseMotionListener(new DrawingMouseMotionListener());
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(currentStrokeWidth)); // Set current stroke width

        // Apply zoom
        g2d.scale(scaleFactor, scaleFactor);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }

        // Draw existing lines
        int count = 0;
        for (List<Point> line : scribbleLines) {
            if (line.size() > 1) {
                int linesize = size.get(count);
                g2d.setColor(lineColors.get(line));
                Brush temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                if(type.get(count)==1)
                {
                    
                    g2d.setStroke(new BasicStroke(linesize));
                    for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y); //if its the normal brush uses drawLine
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 2)
                {
                    
                     temp = new Brush(Brush.BrushType.HIGHLIGHTER, linesize);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                
                else if(type.get(count) == 3)
                {
                    
                     temp = new Brush(Brush.BrushType.MARKER, linesize);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 4)
                {
                    
                     temp = new Brush(Brush.BrushType.PENCIL, linesize);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 5)
                {
                    
                     temp = new Brush(Brush.BrushType.PEN, linesize);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 6)
                {
                    
                     temp = new Brush(Brush.BrushType.CRAYON, linesize);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 7)
                {
                    
                     temp = new Brush(Brush.BrushType.SPRAY_PAINT, 5);//else uses the respective type -shafiul
                     for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, 0, 0);
                        prevPoint = currentPoint;
                    }
                }
                
                count++;//incrementing to get each type currently defined as 1=normal 2=circle 3=transparent
            }
        }

        // Draw current line
        if (currentLine.size() > 1) {
            int linesize = size.get(size.size()-1);//altered this paints depending on type now -shafiul
            g2d.setColor(currentColor);
                Brush temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                temp.setColor(currentColor);
                if(type.get(count)==1)
                {
   
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 2)
                {
                     temp = new Brush(Brush.BrushType.HIGHLIGHTER, linesize);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 3)
                {
                     temp = new Brush(Brush.BrushType.MARKER, linesize);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 4)
                {
                     temp = new Brush(Brush.BrushType.PENCIL, linesize);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);         
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 5)
                {
                     temp = new Brush(Brush.BrushType.PEN, linesize);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 6)
                {
                     temp = new Brush(Brush.BrushType.CRAYON, linesize);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                    }
                }
                else if(type.get(count) == 7)
                {
                     temp = new Brush(Brush.BrushType.SPRAY_PAINT, 5);//doing the same here-shafiul
                     for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                        prevPoint = currentPoint;
                        
                    }
                }
                
                count++;
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

    public void zoomIn() {
        scaleFactor *= 1.1; // Increase scale factor for zooming in
        repaint();
    }

    public void zoomOut() {
        scaleFactor /= 1.1; // Decrease scale factor for zooming out
        repaint();
    }
    

    public void increaseSize()
    {
        //currentBrush.setSize(currentBrush.getSize()+5);
        brushSize+=5;
        System.out.println(brushSize);

    }
    public void decreaseSize()
    {
        if (brushSize!=0){
            brushSize-=5;
            System.out.println(brushSize);
        }
        else{
            brushSize=0;
        }
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
    public void showTextBox(Point location, Dimension size) {
        textBox = new TextBox(location, size);
        add(textBox);
        revalidate();
        repaint();
    }

    public void hideTextBox() {
        if (textBox != null) {
            remove(textBox);
            textBox = null;
            repaint();
        }
    }

    public void undo() {
        
        if(!undoList.isEmpty())
        {
            if(undoList.get(undoList.size()-1)=="Pen")
            {
                if (!scribbleLines.isEmpty()) {
                    undoneLines.add(scribbleLines.remove(scribbleLines.size() - 1)); // Move the undone line to undoneLines
                    repaint();
                }
            }
            else
            {
                if(!shapes.isEmpty())
                {
                    undoShapes.add(shapes.remove(shapes.size()-1));
                    repaint();
                }
            }
            redoList.add(undoList.remove(undoList.size()-1));
        }
    }

    public void redo() {
        
        if(!redoList.isEmpty())
        {
            if(redoList.get(redoList.size()-1)=="Pen")
            {
                if (!undoneLines.isEmpty()) {
                    scribbleLines.add(undoneLines.remove(undoneLines.size() - 1)); // Move the undone line back to scribbleLines
                    repaint();
                }
            }
            else
            {
                if(!undoShapes.isEmpty())
                {
                    shapes.add(undoShapes.remove(undoShapes.size()-1));
                    repaint();
                }
            }
            undoList.add(redoList.remove(redoList.size()-1));
        }
    }
    
    

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setStrokeWidth(float width) {
        this.currentStrokeWidth = width;
    }

    public void setCurrentBrush(Brush brush) {
        this.currentBrush = brush;
    }

    public void toggleEraserMode() {
        eraserMode = !eraserMode;
        if (eraserMode) {
            currentColor = getBackground(); // Dynamically fetches the panel's background color
            currentStrokeWidth = 2.0f; // Adjust the stroke width for the eraser here
        } else {
            currentColor = Color.BLACK; // Reset to default drawing color
            currentStrokeWidth = 2.0f; // Reset to default stroke width
        }
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
            case Shape.STRAIGHT_LINE:
                g2d.drawLine(x1, y1, x2, y2);
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
                undoList.add("Shape");
            }else if (textBox != null && textBox.contains(e.getPoint())) {
                textBoxOffset = new Point(e.getX() - textBox.getX(), e.getY() - textBox.getY());
            } else {
                size.add(brushSize);
                if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT) {
                    type.add(1);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER){

                    type.add(2);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.MARKER){

                    type.add(3);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PENCIL){

                    type.add(4);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PEN){

                    type.add(5);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.CRAYON){

                    type.add(6);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT){

                    type.add(7);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();


                }

                List<Color> colors = new ArrayList<>();
                colors.add(currentColor); // Store the initial color
               

            }
        }
    

        @Override
        public void mouseReleased(MouseEvent e) {
            textBoxOffset = null;
            if (currentShape != null) {
                int x = e.getX();
                int y = e.getY();
                currentShape.setX2(x);
                currentShape.setY2(y);
                shapes.add(currentShape);
                currentShape = null;
                repaint();
            } else {
                lineColors.put(new ArrayList<>(currentLine), currentColor);
                if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT) {
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.MARKER) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PENCIL) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PEN) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PEN) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.CRAYON) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT) {//same here
                    scribbleLines.add(new ArrayList<>(currentLine));
                    lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current line
                    currentLine.clear();
                    repaint();
                }
            }
        }
    }

    private class DrawingMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentShape != null) {
                // If a shape is being drawn, update its coordinates and repaint
                int x = e.getX();
                int y = e.getY();
                currentShape.setX2(x);
                currentShape.setY2(y);
                repaint();
            } else if (textBox != null && textBoxOffset != null) {
                int newX = e.getX() - textBoxOffset.x;
                int newY = e.getY() - textBoxOffset.y;
                textBox.setLocation(new Point(newX, newY));
                repaint();
                
            } else {
                // If drawing freehand lines
                if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT ||
                    currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER || 
                    currentBrush.getBrushType() == Brush.BrushType.MARKER || 
                    currentBrush.getBrushType() == Brush.BrushType.PENCIL ||
                    currentBrush.getBrushType() == Brush.BrushType.PEN ||
                    currentBrush.getBrushType() == Brush.BrushType.CRAYON ||
                    currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT) {
                    
                    // Add the current mouse position to the current line
                    currentLine.add(e.getPoint());
                    
                    // Repaint the component to reflect the changes
                    repaint();
                    
                    // If the current line already exists in lineColors, add the current color to its list of colors
                    
                }
            }
        
        
        
        }
        }
    }
