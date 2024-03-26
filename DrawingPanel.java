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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private List<Integer> undoSizes = new ArrayList<>();
    private int brushSize = 10;
    private int latestType = 0;
    private BufferedImage colorSpecturm;
    private Color clickedColor = Color.BLACK;
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int followx = 0;
    private int followy = 0;
    

    private boolean firstcase = true;

    private BufferedImage rectangleImage;
    private BufferedImage ovalImage;

    private List<BetterButton> buttonList = new ArrayList<>();

    private BetterButton defaultButton, markerButton,pencilButton,penButton,crayonButton,spraypaintButton,highlighterButton,rectButton,ovalButton,straightButton;



    public List<Fill> fill = new ArrayList<>();
    private boolean shouldPerformFill = false;
    

    //BetterButton rect = new BetterButton(50,50,50,50);

    //BetterButton oval = new BetterButton(50,150,50,50);

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600)); // Set DEFAULT size
        addMouseListener(new DrawingMouseListener());
        addMouseMotionListener(new DrawingMouseMotionListener());
        setLayout(null);
        try {
            //rectangleImage = ImageIO.read(new File("Rectangle.png"));
            //ovalImage = ImageIO.read(new File("Oval.png"));
            colorSpecturm = ImageIO.read(new File("Color.png"));
            //backgroundImage = ImageIO.read(new File("background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        createButton();
    }
    public void createButton()
    {
        defaultButton = new BetterButton(10,10,35,35);
        markerButton = new BetterButton(50,10,35,35);
        penButton = new BetterButton(90,10,35,35);
        pencilButton = new BetterButton(130,10,35,35);
        crayonButton = new BetterButton(10,50,35,35);
        spraypaintButton = new BetterButton(50,50,35,35);
        highlighterButton = new BetterButton(90,50,35,35);
        rectButton = new BetterButton(130,50,35,35);
        ovalButton = new BetterButton(10,90,35,35);
        straightButton = new BetterButton(50,90,35,35);


        buttonList.add(defaultButton);
        buttonList.add(markerButton);
        buttonList.add(penButton);
        buttonList.add(pencilButton);
        buttonList.add(crayonButton);
        buttonList.add(spraypaintButton);
        buttonList.add(highlighterButton);
        buttonList.add(rectButton);
        buttonList.add(ovalButton);
        buttonList.add(straightButton);
        
        //defaultButton = new BetterButton(10,10,35,35);
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

        int toolbarWidth = Math.min(getWidth() / 4, 200);
        
        // Draw the toolbar rectangle
        g2d.setColor(new Color(236,236,236));
        g2d.fillRect(0, 0, toolbarWidth, getHeight());

        // Draw existing lines
        drawAll(g2d, 0);
        drawCurrent(g2d);
        g2d.setColor(new Color(236,236,236));
        g2d.fillRect(0, 0, toolbarWidth, getHeight());

        for(int i=0;i<buttonList.size();i++)
        {
            buttonList.get(i).Paint(g2d);
        }
        
        if(colorSpecturm!=null)
        {
            g.drawImage(colorSpecturm, 20,200,null);
        }

        g2d.setColor(clickedColor);
        g2d.fillRect(20,400,20,20);
        


        for (Shape shape : shapes) {
            drawShape(g2d, shape);
        }

        // Draw current shape if any...
        if (currentShape != null) {
            drawShape(g2d, currentShape);
        }
        g2d.setColor(Color.WHITE);
        g2d.fillOval(followx-2, followy-2, 2*2,2*2);

        g2d.dispose();
    }
    public void drawCurrent(Graphics2D g2d)
    {
        if (currentLine.size() > 1)
        {
            int linesize = size.get(size.size()-1);//altered this paints depending on type now -shafiul
            g2d.setColor(currentColor);
            Brush temp;
            
            switch(latestType)
                {
                    case 1:
                    temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                    break;
                    case 2:
                    temp = new Brush(Brush.BrushType.MARKER, linesize);
                    break;
                    case 3:
                    temp = new Brush(Brush.BrushType.PEN, linesize);
                    break;
                    case 4:
                    temp = new Brush(Brush.BrushType.PENCIL, linesize);
                    break;
                    case 5:
                    temp = new Brush(Brush.BrushType.CRAYON, linesize);
                    break;
                    case 6:
                    temp = new Brush(Brush.BrushType.SPRAY_PAINT, linesize);
                    break;
                    case 7:
                    temp = new Brush(Brush.BrushType.HIGHLIGHTER, linesize);
                    break;
                    default:
                    temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                    break;
                }
                temp.setColor(currentColor);
                if(latestType==6)
                {
                    for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint))
                        {
                            temp.paint(g2d, prevPoint.x, prevPoint.y, 0, 0);
                        }
                        prevPoint = currentPoint;
                    }
                }
                else
                {
                    for (int i = 1; i < currentLine.size(); i++) {
                        Point currentPoint = currentLine.get(i);
                        Point prevPoint = currentLine.get(i-1);
                        if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint))
                        {
                            temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y); //if its the normal brush uses drawLine
                        }
                        prevPoint = currentPoint;
                    }
                }
            }
            
    }
    public void drawAll(Graphics2D g2d, int count)
    {  
        try
        {

        for (List<Point> line : scribbleLines) {
            if (line.size() > 0) {
                int linesize = size.get(count);
                g2d.setColor(lineColors.get(line));
                Brush temp;
                switch(type.get(count))
                {
                    case 1:
                    temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                    break;
                    case 2:
                    temp = new Brush(Brush.BrushType.MARKER, linesize);
                    break;
                    case 3:
                    temp = new Brush(Brush.BrushType.PEN, linesize);
                    break;
                    case 4:
                    temp = new Brush(Brush.BrushType.PENCIL, linesize);
                    break;
                    case 5:
                    temp = new Brush(Brush.BrushType.CRAYON, linesize);
                    break;
                    case 6:
                    temp = new Brush(Brush.BrushType.SPRAY_PAINT, linesize);
                    break;
                    case 7:
                    temp = new Brush(Brush.BrushType.HIGHLIGHTER,linesize);
                    break;
                    default:
                    temp = new Brush(Brush.BrushType.DEFAULT, linesize);
                    break;
                }
                if(type.get(count)==6)
                {
                    for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint))
                        {
                            temp.paint(g2d, prevPoint.x, prevPoint.y, 0, 0);
                        }
                        prevPoint = currentPoint;
                    }
                }
                else
                {
                    for (int i = 1; i < line.size(); i++) {
                        Point currentPoint = line.get(i);
                        Point prevPoint = line.get(i-1);
                        if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint))
                        {
                            temp.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y); //if its the normal brush uses drawLine
                        }
                        prevPoint = currentPoint;
                    }
                }
                count++;
            }
        }
    }
            catch(Exception e)
            {
                System.out.println(scribbleLines.size() + " " +type.size()+" "+lineColors.size()+" ");
            }
    }

    public void zoomIn() {
        int toolbarWidth = Math.min(getWidth() / 4, 200);
        int drawingAreaWidth = getWidth() - toolbarWidth; // Exclude the toolbar area from the drawing area
        scaleFactor *= 1.1; // Increase scale factor for zooming in
        // Limit zooming to prevent the toolbar area from being affected
        if (scaleFactor * drawingAreaWidth > drawingAreaWidth) {
            scaleFactor = (double) drawingAreaWidth / drawingAreaWidth;
        }
        repaint();
    }
    
    public void zoomOut() {
        int toolbarWidth = Math.min(getWidth() / 4, 200);
        int drawingAreaWidth = getWidth() - toolbarWidth; // Exclude the toolbar area from the drawing area
        scaleFactor /= 1.1; // Decrease scale factor for zooming out
        // Limit zooming to prevent the toolbar area from being affected
        if (scaleFactor * drawingAreaWidth < drawingAreaWidth) {
            scaleFactor = (double) drawingAreaWidth / drawingAreaWidth;
        }
        repaint();
    }

    public void setShouldPerformFill(boolean shouldPerformFill) {
        this.shouldPerformFill = shouldPerformFill;
    }
    
    public boolean shouldPerformFill() {
        return shouldPerformFill;
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
                    undoSizes.add(size.remove(size.size()-1));
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
                    size.add(undoSizes.remove(undoSizes.size()-1));
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
    public void fill(int width, int height, int x, int y) {
        int[][] pixelArray = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelArray[i][j] = currentColor.getRGB();
            }
        }

        Color originalColor = new Color(pixelArray[x][y]);

        fill.add(new Fill());
        
        Fill fillInstance = fill.get(fill.size() - 1); 
        fillInstance.floodFill(pixelArray, x, y, originalColor, currentColor);

        setBackground(currentColor); 
        repaint(); 
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

        int toolbarWidth = Math.min(getWidth() / 4, 200);
        
        
        x1 = Math.max(x1, toolbarWidth); 
        x2 = Math.max(x2, toolbarWidth); 
        x1 = Math.min(x1, getWidth()); 
        x2 = Math.min(x2, getWidth());   //adjusting boundaries
        y1 = Math.max(y1, 0); 
        y2 = Math.max(y2, 0); 
        y1 = Math.min(y1, getHeight()); 
        y2 = Math.min(y2, getHeight()); 

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
            if (isWithinScreenBounds(e.getPoint())) 
            {
                
                if (currentShape != null) {
                    int x = e.getX();
                    int y = e.getY();
                    currentShape = new Shape(x, y, x, y, currentColor, new BasicStroke(currentStrokeWidth), currentShape.getShape());
                    undoList.add("Shape");
                }else if (textBox != null && textBox.contains(e.getPoint())) {
                    textBoxOffset = new Point(e.getX() - textBox.getX(), e.getY() - textBox.getY());
                } 
                else if (shouldPerformFill && !fill.isEmpty()) { 
                    shouldPerformFill = false;
                    int[][] pixelArray = new int[getWidth()][getHeight()]; 
                    Color originalColor = new Color(pixelArray[e.getX()][e.getY()]); 
        
                    // Trigger fill operation
                    fill.get(0).floodFill(pixelArray, e.getX(), e.getY(), originalColor, currentColor);
                    setBackground(currentColor);
                    undoList.add("Fill");
                    repaint();
                }
            else{
                
                if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT && defaultButton.getStatus()) {
                    type.add(1);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);
                }
                else if (currentBrush.getBrushType() == Brush.BrushType.MARKER && markerButton.getStatus()){

                    type.add(2);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PEN && penButton.getStatus()){

                    type.add(3);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.PENCIL && pencilButton.getStatus()){

                    type.add(4);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.CRAYON && crayonButton.getStatus()){

                    type.add(5);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT && spraypaintButton.getStatus()){

                    type.add(6);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);


                }
                else if (currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER && highlighterButton.getStatus()){

                    type.add(7);
                    currentLine.add(e.getPoint());
                    undoList.add("Pen");
                    currentLine.clear();
                    size.add(brushSize);
                }
           
                List<Color> colors = new ArrayList<>();
                colors.add(currentColor); // Store the initial color
            }
            }
            if(!isWithinScreenBounds(e.getPoint()))
            { 
                if (e.getX() >= 20 && e.getX() < 20 + colorSpecturm.getWidth() &&
                    e.getY() >= 200 && e.getY() < 200 + colorSpecturm.getHeight()) {
                    // Get the color at the clicked coordinates
                    int rgb = colorSpecturm.getRGB(e.getX() - 20, e.getY() - 200);
                    // Extract the individual RGB components
                    r = (rgb >> 16) & 0xFF;
                    g = (rgb >> 8) & 0xFF;
                    b = rgb & 0xFF;
                    // Create a new color with the extracted RGB components
                    clickedColor = new Color(r, g, b);
                    // Set the current drawing color to the clicked color
                    setCurrentColor(clickedColor);
                    // Optionally, do something with the clicked color
                    System.out.println("Clicked color: " + clickedColor);
                    followx = e.getX();
                    followy = e.getY();
                    repaint();
                }
                int old = latestType;
                for(int i=0;i<buttonList.size();i++)//do this shit
                {
                       
                    if(buttonList.get(i).Collision(e.getPoint()))
                    {
                        repaint();
                        buttonList.get(i).Clicked(true);
                        switch(i)
                        {
                            case 0:
                            setCurrentBrush(new Brush(Brush.BrushType.DEFAULT,brushSize));
                            
                            latestType = 1;
                            break;
                            case 1:
                            setCurrentBrush(new Brush(Brush.BrushType.MARKER, brushSize));
                            latestType = 2;
                            break;
                            case 2:
                            setCurrentBrush(new Brush(Brush.BrushType.PEN, brushSize));
                            latestType = 3;
                            break;
                            case 3:
                            setCurrentBrush(new Brush(Brush.BrushType.PENCIL, brushSize));
                            latestType = 4;
                            break;
                            case 4:
                            setCurrentBrush(new Brush(Brush.BrushType.CRAYON, brushSize));
                            latestType = 5;
                            break;
                            case 5:
                            setCurrentBrush(new Brush(Brush.BrushType.SPRAY_PAINT, brushSize));
                            latestType = 6;
                            break;
                            case 6:
                            setCurrentBrush(new Brush(Brush.BrushType.HIGHLIGHTER, brushSize));
                            latestType = 7;
                            break;
                            case 7:
                            latestType = 8;
                            setCurrentShape(Shape.RECTANGLE);
                            break;
                            case 8:
                            latestType = 9;
                            setCurrentShape(Shape.OVAL);
                            break;
                            case 9:
                            latestType = 10;
                            setCurrentShape(Shape.STRAIGHT_LINE);
                            default:
                            

                        }
                    }

                }
            
                if(old!=latestType)
                {
                    buttonList.get(old-1).Clicked(false);
                    repaint();
                }


            }
        }
    

        @Override
        public void mouseReleased(MouseEvent e) {
            textBoxOffset = null;
            if(isWithinScreenBounds(e.getPoint()))
            {
            if (currentShape != null) {
                int x = e.getX();
                int y = e.getY();
                currentShape.setX2(x);
                currentShape.setY2(y);
                shapes.add(currentShape);
                currentShape = null;
                repaint();
            }
             else {
                //lineColors.put(new ArrayList<>(currentLine), currentColor);
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
        
        public void mouseMoved(MouseEvent e) {
            for (int i = 0; i < buttonList.size(); i++) {
                if (buttonList.get(i).Collision(e.getPoint())) {
                    // Do something when the mouse is over the button
                    // For example, change the button color or display a tooltip
                    // You can also trigger other actions based on the button
                    System.out.println("Mouse over button " + i);
                    repaint();
                }
            }
        }
        
        }
        private boolean isWithinScreenBounds(Point point) {
            int toolbarWidth = Math.min(getWidth() / 4, 200);
            return point.x >= toolbarWidth && point.x <= getWidth() && point.y >= 0 && point.y <= getHeight();
        }

        private Color getColorAt(int x, int y) {
            Robot robot;
            try {
                robot = new Robot();
                return robot.getPixelColor(x, y);
            } catch (AWTException e) {
                e.printStackTrace();
                return null;
            }
        }
    }