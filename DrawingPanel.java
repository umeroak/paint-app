import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;


public class DrawingPanel extends JPanel {
    private ArrayList<TextBox> textBoxes;
    private TextBox textBox;
    private Point textBoxOffset;

    private Brush previouBrush;

    private int shape = 0;
    private BufferedImage offscreenBuffer;

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
    private boolean restoreMode = false; // Flag to indicate eraser mode
    private double scaleFactor = 1.0; // Scale factor for zooming
    private List<Shape> undoShapes = new ArrayList<>();

    private boolean dropper = false;
    
    private Brush currentBrush = new Brush(Brush.BrushType.DEFAULT, 10); // Default brush
    private List<Integer> type = new ArrayList<>();// added type arrayList for brush type -shafiul change this into brush arrraylist for easy and simple access
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

    private List<BufferedImage> canvasList = new ArrayList<>();
    private List<BufferedImage> undoList = new ArrayList<>();
    private List<BufferedImage> redoList = new ArrayList<>();

    private boolean change = false;
    private boolean dofill = false;
    private boolean undoredo = false;

    private boolean mouse = false;


    private TexturePaint texture;

    private boolean shapeToggle = false;

    private boolean button = false;

    private boolean imaUndo = false;

    private boolean first;

    private boolean undone = false;



    private BufferedImage CrayonIcon, FillBucketIcon, HighLighterIcon, MarkerIcon, OvalIcon, PaintBrushIcon, PenIcon,
            PencilIcon, RectangleIcon, SprayPaintIcon, StraightLineIcon, EraserIcon, ButtonIcon, RestoreIcon, DropperIcon;

    private BufferedImage crayonTexture;

    private List<BetterButton> buttonList = new ArrayList<>();

    private BetterButton defaultButton, markerButton, pencilButton, penButton, crayonButton, spraypaintButton,
            highlighterButton, rectButton, ovalButton, straightButton, fillButton, eraserButton, buttonButton, restoreButton, dropperButton;

    public List<Fill> fill = new ArrayList<>();

    private int pointer = 0;


    private BufferedImage canvas;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600)); // Set DEFAULT size
        addMouseListener(new DrawingMouseListener());
        addMouseMotionListener(new DrawingMouseMotionListener());
        setLayout(null);
        first = true;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Assuming 'canvas' is the primary drawing buffer being used throughout
                if (getWidth() > 0 && getHeight() > 0) {
                    BufferedImage newCanvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = newCanvas.createGraphics();
                    
                    // Preserve the existing drawing by painting it onto the resized canvas
                    if (canvas != null) {
                        g2d.drawImage(canvas, 0, 0, null);
                    }
                    g2d.dispose();
                    
                    // Update the reference to the resized canvas
                    canvas = newCanvas;
                    
                    // It might be necessary to update offscreenBuffer as well
                    offscreenBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    
                    repaint();
                }
            }
        });
        
        try {
            colorSpecturm = ImageIO.read(new File("Icons/Color.png"));
            CrayonIcon = ImageIO.read(new File("Icons/Crayon.png"));
            FillBucketIcon = ImageIO.read(new File("Icons/FillBucket.jpg"));
            HighLighterIcon = ImageIO.read(new File("Icons/HighLighter.jpg"));
            MarkerIcon = ImageIO.read(new File("Icons/Marker.png"));
            OvalIcon = ImageIO.read(new File("Icons/Oval.jpg"));
            PaintBrushIcon = ImageIO.read(new File("Icons/PaintBrush.jpg"));
            PenIcon = ImageIO.read(new File("Icons/Pen.jpg"));
            PencilIcon = ImageIO.read(new File("Icons/Pencil.jpg"));
            RectangleIcon = ImageIO.read(new File("Icons/Rectangle.jpg"));
            SprayPaintIcon = ImageIO.read(new File("Icons/SprayPaint.jpg"));
            StraightLineIcon = ImageIO.read(new File("Icons/StraightLine.jpg"));
            EraserIcon = ImageIO.read(new File("Icons/Eraser.jpg"));
            RestoreIcon = ImageIO.read(new File("Icons/restore.png"));
            DropperIcon = ImageIO.read(new File("Icons/dropper.jpg"));

            crayonTexture = ImageIO.read(new File("Crayon.png"));

            ButtonIcon = ImageIO.read(new File("Icons/Button.png"));
            // Close the input stream after reading the image
            //backgroundImage = ImageIO.read(new File("Icons/background.jpeg"));

            
        } catch (IOException e) {
            e.printStackTrace();
        }
        texture = new TexturePaint(crayonTexture,
                new Rectangle(0, 0, crayonTexture.getWidth(), crayonTexture.getHeight()));

        createButton();
    }

    public void createButton() {


        
        defaultButton = new BetterButton(10, 10, 35, 35, PaintBrushIcon);
        markerButton = new BetterButton(50, 10, 35, 35, MarkerIcon);
        penButton = new BetterButton(90, 10, 35, 35, PenIcon);
        pencilButton = new BetterButton(130, 10, 35, 35, PencilIcon);
        crayonButton = new BetterButton(10, 50, 35, 35, CrayonIcon);
        spraypaintButton = new BetterButton(50, 50, 35, 35, SprayPaintIcon);
        highlighterButton = new BetterButton(90, 50, 35, 35, HighLighterIcon);
        rectButton = new BetterButton(130, 50, 35, 35, RectangleIcon);
        ovalButton = new BetterButton(10, 90, 35, 35, OvalIcon);
        straightButton = new BetterButton(50, 90, 35, 35, StraightLineIcon);
        eraserButton = new BetterButton(90, 90, 35, 35, EraserIcon);
        fillButton = new BetterButton(130, 90, 35, 35, FillBucketIcon);
        buttonButton = new BetterButton(10, 130, 35, 35, ButtonIcon);
        restoreButton = new BetterButton(50, 130, 35, 35, RestoreIcon);
        dropperButton = new BetterButton(90, 130, 35, 35, DropperIcon);



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
        buttonList.add(eraserButton);
        buttonList.add(fillButton);
        buttonList.add(buttonButton);
        buttonList.add(restoreButton);
        buttonList.add(dropperButton);

        latestType = 1;
        buttonList.get(0).Clicked(true);//create brush objects to set, refactor code, lots of unnescary stuff
        currentBrush =(new Brush(Brush.BrushType.DEFAULT, brushSize));
        previouBrush = currentBrush;
        currentShape = new Shape(0, 0, 0, 0, Color.BLACK, new BasicStroke(2.0f), 0);
        shapeToggle = false;
        
    }
    public void createCanvas()
    {
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    public void createNewCanvas(BufferedImage originalCanvas) 
    {
        canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(currentStrokeWidth)); // Set current stroke width
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        g2d.drawRect(0, 0, panelWidth - 1, panelHeight - 1);


        // Apply zoom
        //g2d.scale(scaleFactor, scaleFactor);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }

        int toolbarWidth = Math.min(getWidth() / 4, 200);

        // Draw the toolbar rectangle
        g2d.setColor(new Color(236, 236, 236));
        g2d.fillRect(0, 0, toolbarWidth, getHeight());

        // Draw existing lines
        //drawAll(g2d, 0);
        if (first) {
            createCanvas();
            //backgroundImage = canvas;
            canvasList.add(deepCopy(canvas));
            first = false;
            
        }
        
        g2d.drawImage(canvas, 0, 0, null);
        drawCurrent(g2d);
        
        g2d.setColor(new Color(236, 236, 236));
        g2d.fillRect(0, 0, toolbarWidth, getHeight());

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).Paint(g2d);
        }

        if (colorSpecturm != null) {
            g.drawImage(colorSpecturm, 20, 200, null);
        }

        g2d.setColor(clickedColor);
        g2d.fillRect(20, 400, 20, 20);

        if (shapeToggle && mouse) {
            
            drawShape(g2d, currentShape);
        }

        // Draw current shape if any...
        if(!mouse)
        {
            drawCurrentShape(g2d);
        }
        

        g2d.setColor(Color.WHITE);
        g2d.fillOval(followx - 2, followy - 2, 2 * 2, 2 * 2);


        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Color.BLACK);
        g2d.drawLine(toolbarWidth, 0, toolbarWidth, getHeight());

        g2d.dispose();
    }
    public void setCustomColor(int r, int g, int b)
    {
        clickedColor = new Color(r,g,b);
        repaint();
    }

    public void drawCurrentShape(Graphics2D g2d)// make it look nicer
    {
        //BufferedImage canvas2 = canvas;
        g2d = canvas.createGraphics();
        if (shapeToggle && change) {
            drawShape(g2d, currentShape);//var mouseReleased = true when released false when dragged or pressed, then use that to determine, final x1,x2,y1,y2 then draw that here, have drawcurrent in main to give illusion of control
            change = false;
            
        }
        if(undoredo)
        {
            canvasList.add(deepCopy(canvas));
            undoredo = false;
            pointer++;
        }
        //canvas = canvas2;
        
        g2d.dispose();
        if(shapeToggle)
        {
            repaint();
        }
        
        //repaint();
    }


    

    // fuck have to add the fucking bubbles

    public void drawCurrent(Graphics2D g2d) {
        //createCanvas();
        //BufferedImage canvas2 = canvas;
        g2d = canvas.createGraphics();

        
        if (currentLine.size() > 1) {

            g2d.setColor(currentColor);
                currentBrush.setSize(brushSize);

            if (!restoreMode) {
                //int linesize = size.get(size.size() - 1);// altered this paints depending on type now -shafiul
                
                    for (int i = 1; i < currentLine.size()-1; i++) {
                        Point currentPoint = currentLine.get(i+1);
                        Point prevPoint = currentLine.get(i);
                        if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint)) {
                            g2d.setStroke(new BasicStroke(brushSize)); // Set the stroke size
                            if(latestType == 2){
                               currentBrush.setSize(8); //marker
                            }
                            else if(latestType == 3){
                                currentBrush.setSize(5); // pen
                             }
                            else if(latestType == 4){
                                currentBrush.setSize(3); // pencil
                             }
                             
                            
                           else if(latestType == 7)
                            {
                                g2d.setColor(new Color(clickedColor.getRed(), clickedColor.getGreen(), clickedColor.getBlue(), 5));
                                
                            }
                            if(latestType == 4)
                            {
                                currentBrush.setSize(brushSize/4);
                                g2d.setColor(new Color(clickedColor.getRed(), clickedColor.getGreen(), clickedColor.getBlue(), 50));
                            }
                            

                            else
                            {
                                g2d.setColor(new Color(clickedColor.getRed(), clickedColor.getGreen(), clickedColor.getBlue()));
                            }
                             // Set the color with alpha
                            //g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                            if(latestType == 5)
                            {
                                currentBrush.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y, texture); 
                                g2d.setColor(new Color(clickedColor.getRed(), clickedColor.getGreen(), clickedColor.getBlue(), 50));
                                g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                            }
                            else
                            {
                                currentBrush.paint(g2d, prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y, texture); 
                            }
                            
                            // if its the
                        }
                        //prevPoint = currentPoint;
                        currentLine.remove(i);
                    }
                    //canvasList.add(canvas);
                    System.out.println(canvasList.size());
                }
        if (restoreMode) {
            g2d.setColor(Color.WHITE);
            for (int i = 1; i < currentLine.size(); i++) {
                Point currentPoint = currentLine.get(i);
                Point prevPoint = currentLine.get(i - 1);
                if (isWithinScreenBounds(prevPoint) && isWithinScreenBounds(currentPoint)) {
                    if (backgroundImage!=null) {
                        restore(g2d, currentPoint.x, currentPoint.y, brushSize); // Use eraser at current point
                    }
                }
            }
            //canvasList.add(canvas);
            System.out.println(canvasList.size());
        }
        //BufferedImage canvas2 = createNewCanvas(canvas);
        if(undoredo)
        {
            canvasList.add(deepCopy(canvas));
            undoredo = false;
            pointer++;
            createCanvas();
        }
        //repaint();
        
        //canvas = canvas2;
        g2d.dispose();
        
        }
    
    }
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
       }

       private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
    
        return outputImage;
    }
    public void captureState() {
        // Remove "future" states if the current pointer does not point to the last state
        while (canvasList.size() > pointer + 1) {
            canvasList.remove(canvasList.size() - 1);
        }
        
        // Now, capture the current canvas state
        BufferedImage currentState = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = currentState.createGraphics();
        g2d.drawImage(canvas, 0, 0, null);
        g2d.dispose();
        
        // Add the captured state to the list and update the pointer
        canvasList.add(currentState);
        pointer = canvasList.size() - 1; // Adjust the pointer to the new latest state
    }
    
      
    public void undo() {
        if (pointer > 0) { // Ensure there is something to undo
            pointer--;
            BufferedImage toRestore = canvasList.get(pointer);
            
            // No need to resize during undo since we want to revert to the exact previous state
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = canvas.createGraphics();
            g2.drawImage(toRestore, 0, 0, this);
            g2.dispose();
            repaint();
        }
    }
    
    public void redo() {
        if (pointer < canvasList.size() - 1) { // Ensure there is something to redo
            pointer++;
            BufferedImage toRestore = canvasList.get(pointer);
            
            // Similarly, redo to the exact state without resizing
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = canvas.createGraphics();
            g2.drawImage(toRestore, 0, 0, this);
            g2.dispose();
            repaint();
        }
    }
    

    /*
    public void zoomIn() {
        BufferedImage image = Scalr.resize(canvas, Scalr.Method.BALANCED, 50, 50);
        Graphics2D g = imag.createGraphics();
        
    }
    */
    /*
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
    */

    public void increaseSize() {
        // currentBrush.setSize(currentBrush.getSize()+5);
        if(brushSize<100)
        {
            brushSize += 5;
            if(brushSize>100)
            {
                brushSize=100;
            }
        }    
    }

    public void decreaseSize() {
        if (brushSize >0) {
            brushSize -= 5;
            System.out.println(brushSize);
            if(brushSize<0)
            {
                brushSize = 1;
            }
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
    
    

    public static void floodFillImage(BufferedImage image,int x, int y, Color color) 
    {
        int srcColor = image.getRGB(x, y);
        boolean[][] hits = new boolean[image.getHeight()][image.getWidth()];

        Queue<Point> queue = new LinkedList<Point>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) 
        {
            Point p = queue.remove();

            if(floodFillImageDo(image,hits,p.x,p.y, srcColor, color.getRGB()))
            {     
                queue.add(new Point(p.x,p.y - 1)); 
                queue.add(new Point(p.x,p.y + 1)); 
                queue.add(new Point(p.x - 1,p.y)); 
                queue.add(new Point(p.x + 1,p.y)); 
            }
        }
    }

    private static boolean floodFillImageDo(BufferedImage image, boolean[][] hits,int x, int y, int srcColor, int tgtColor) 
    {
        if (y < 0) return false;
        if (x < 0) return false;
        if (y > image.getHeight()-1) return false;
        if (x > image.getWidth()-1) return false;

        if (hits[y][x]) return false;

        if (image.getRGB(x, y)!=srcColor)
            return false;

        // valid, paint it

        image.setRGB(x, y, tgtColor);
        hits[y][x] = true;
        return true;
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

    public void restore(Graphics2D g2d, int x, int y, int size) {
        int halfEraserSize = size / 2;
        int startX = Math.max(0, x - halfEraserSize);
        int startY = Math.max(0, y - halfEraserSize);
        int width = Math.min(getWidth() - startX, size);
        int height = Math.min(getHeight() - startY, size);
        // list add true or false to it to indicate eraser
        g2d.drawImage(backgroundImage.getSubimage(startX, startY, width, height), startX, startY, this);

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
        x2 = Math.min(x2, getWidth()); // adjusting boundaries
        y1 = Math.max(y1, 0);
        y2 = Math.max(y2, 0);
        y1 = Math.min(y1, getHeight());
        y2 = Math.min(y2, getHeight());

        switch (shape.getShape()) {
            case Shape.RECTANGLE:
            if(button)
            {
                g2d.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            }
            else
            {
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            }
                
                break;
            case Shape.OVAL:
                if(button)
                {
                    g2d.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                }
                else
                {
                    g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                }
                
                break;
            case Shape.STRAIGHT_LINE:
                g2d.drawLine(x1, y1, x2, y2);
                break;

            case Shape.TRIANGLE:
                int[] xPoints = {x1, (x1 + x2) / 2, x2};
                int[] yPoints = {y2, y1, y2};
                g2d.drawPolygon(xPoints, yPoints, 3);
                break;

            case Shape.HEXAGON:
                int xCenter = (x1 + x2) / 2;
                int yCenter = (y1 + y2) / 2;
                int radius = Math.abs(x2 - x1) / 2;
                int numSides = 6;
                int[] xHexagon = new int[numSides];
                int[] yHexagon = new int[numSides];
                for (int i = 0; i < numSides; i++) {
                    double angle = 2 * Math.PI / numSides * i;
                    xHexagon[i] = (int) (xCenter + radius * Math.cos(angle));
                    yHexagon[i] = (int) (yCenter + radius * Math.sin(angle));
                }
                g2d.drawPolygon(xHexagon, yHexagon, numSides);
                break;

            case Shape.PENTAGON:
                xCenter = (x1 + x2) / 2;
                yCenter = (y1 + y2) / 2;
                radius = Math.abs(x2 - x1) / 2;
                numSides = 5;
                int[] xPentagon = new int[numSides];
                int[] yPentagon = new int[numSides];
                for (int i = 0; i < numSides; i++) {
                    double angle = 2 * Math.PI / numSides * (i - 0.5);
                    xPentagon[i] = (int) (xCenter + radius * Math.cos(angle));
                    yPentagon[i] = (int) (yCenter + radius * Math.sin(angle));
                }
                g2d.drawPolygon(xPentagon, yPentagon, numSides);
                break;

            case Shape.OCTAGON:
                xCenter = (x1 + x2) / 2;
                yCenter = (y1 + y2) / 2;
                radius = Math.abs(x2 - x1) / 2;
                numSides = 8;
                int[] xOctagon = new int[numSides];
                int[] yOctagon = new int[numSides];
                for (int i = 0; i < numSides; i++) {
                    double angle = 2 * Math.PI / numSides * i;
                    xOctagon[i] = (int) (xCenter + radius * Math.cos(angle));
                    yOctagon[i] = (int) (yCenter + radius * Math.sin(angle));
                }
                g2d.drawPolygon(xOctagon, yOctagon, numSides);
                break;
            // Add more shape types if needed...
        }
    }
          

    public void setCurrentShape(int shapeType) {
        currentShape = new Shape(0, 0, 0, 0, Color.BLACK, new BasicStroke(2.0f), shapeType);
        //repaint();
    }

    private class DrawingMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (isWithinScreenBounds(e.getPoint())) {
                if(undone)
                {
                    canvasList.clear();
                    pointer = 0;
                    canvasList.add(deepCopy(canvas));
                    undoredo = false;
                    //pointer++;
                    undone = false;
                    //currentShape = null;
                    //canvasList.add(canvas);
                }

                if (shapeToggle) {
                    mouse = true;
                int x = e.getX();
                int y = e.getY();
                currentShape = new Shape(x, y, x, y, currentColor, new BasicStroke(currentStrokeWidth),
                        currentShape.getShape());
                    
                } else if (textBox != null && textBox.contains(e.getPoint())) {
                    textBoxOffset = new Point(e.getX() - textBox.getX(), e.getY() - textBox.getY());
                } else if (dofill) {
                    floodFillImage(canvas, e.getX(), e.getY(), clickedColor);
                    //dofill = false;
                    
                    repaint();
                } else {
                    if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT && defaultButton.getStatus() && !dropper) {
                        type.add(1);
                        
                        
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                        restoreMode = false;
                        dofill = false;
                        shapeToggle = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.MARKER && markerButton.getStatus() && !dropper) {
                        
                        type.add(2);
                        
                        
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());


                        size.add(brushSize);
                        restoreMode = false;
                        dofill = false;
                        shapeToggle = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.PEN && penButton.getStatus() && !dropper) {

                        type.add(3);
                        
                        
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                        restoreMode = false;
                        dofill = false;
                        shapeToggle = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.PENCIL && pencilButton.getStatus() && !dropper) {

                        type.add(4);
                        
                        
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                        restoreMode = false;
                        shapeToggle = false;
                        dofill = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.CRAYON && crayonButton.getStatus() && !dropper) {

                        type.add(5);
                        
                        
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                        restoreMode = false;
                        shapeToggle = false;
                        
                        dofill = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT
                            && spraypaintButton.getStatus() && !dropper) {

                        type.add(6);
                        
                        shapeToggle = false;
                       
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());


                        size.add(brushSize);
                        restoreMode = false;
                        dofill = false;

                    } else if (currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER
                            && highlighterButton.getStatus() && !dropper) {
                                
                        shapeToggle = false;
                        type.add(7);
                        
                       
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                        restoreMode = false;
                        dofill = false;
                    
                    } else if (eraserButton.getStatus() && !dropper) {
                        shapeToggle = false;
                        dofill = false;
                        restoreMode = false;
                        type.add(8);
                        
                        
                        currentLine.clear();


                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                    }
                    else if (fillButton.getStatus() && !dropper) {
                        shapeToggle = false;
                        dofill = true;
                        restoreMode = false;
                        type.add(8);
                        
                        
                        
                        currentLine.clear();


                        
                    
                    }
                    else if (restoreButton.getStatus() && !dropper) {
                        shapeToggle = false;
                        dofill = false;
                        restoreMode = true;
                        type.add(9);
                         
                        currentLine.clear();

                        currentLine.add(e.getPoint());
                        currentLine.add(e.getPoint());

                        size.add(brushSize);
                    }
                    else if(dropper)
                    {
                        shapeToggle = false;
                        dofill = false;
                        restoreMode = false;
                        type.add(10);
                        
                        currentLine.clear();

                        // Get the color at the clicked coordinates
                        int rgb = canvas.getRGB(e.getX() , e.getY() );
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
                        //followx = e.getX();
                        //followy = e.getY();
                        //dropper = false;
                        currentBrush = previouBrush;
                       //repaint();
                            
                    
                    }
                    
                    //eraserList.add(eraserMode);
                    List<Color> colors = new ArrayList<>();
                    colors.add(currentColor); // Store the initial color
                    repaint();
                }
            }
            if (!isWithinScreenBounds(e.getPoint())) {
                
                if (e.getX() >= 20 && e.getX() < 20 + colorSpecturm.getWidth() &&
                        e.getY() >= 200 && e.getY() < 200 + colorSpecturm.getHeight()) {
                    // Get the color at the clicked coordinates
                    int rgb = colorSpecturm.getRGB(e.getX() - 20, e.getY() - 200);
                    // Extract the individual RGB componentsmouse
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
                for (int i = 0; i < buttonList.size(); i++)// do this shit
                {

                    if (buttonList.get(i).Collision(e.getPoint())) {
                        //repaint();
                        buttonList.get(i).Clicked(true);
                        previouBrush = currentBrush;
                        switch (i) {
                            case 0:
                                setCurrentBrush(new Brush(Brush.BrushType.DEFAULT, brushSize));
                                previouBrush = currentBrush;
                                shapeToggle = false;
                                dofill = false;
                                dropper = false;
                                latestType = 1;
                                break;
                            case 1:
                                setCurrentBrush(new Brush(Brush.BrushType.MARKER, 8));
                                latestType = 2;
                                shapeToggle = false;
                                dropper = false;
                                dofill = false;
                                break;
                            case 2:
                                setCurrentBrush(new Brush(Brush.BrushType.PEN, 5));
                                latestType = 3;
                                shapeToggle = false;
                                dropper = false;
                                dofill = false;
                                break;
                            case 3:
                                setCurrentBrush(new Brush(Brush.BrushType.PENCIL, 3));
                                latestType = 4;
                                shapeToggle = false;
                                dropper = false;
                                dofill = false;
                                break;
                            case 4:
                                setCurrentBrush(new Brush(Brush.BrushType.CRAYON, brushSize));
                                latestType = 5;
                                shapeToggle = false;
                                dropper = false;
                                dofill = false;
                                break;
                            case 5:
                                setCurrentBrush(new Brush(Brush.BrushType.SPRAY_PAINT, brushSize));
                                latestType = 6;
                                shapeToggle = false;
                                dropper = false;
                                dofill = false;
                                break;
                            case 6:
                                setCurrentBrush(new Brush(Brush.BrushType.HIGHLIGHTER, 10));
                                latestType = 7;
                                shapeToggle = false;
                                dofill = false;
                                dropper = false;
                                break;
                            case 7:
                                latestType = 8;
                                setCurrentShape(Shape.RECTANGLE);
                                shape = 0;
                                shapeToggle = true;
                                dofill = false;
                                dropper = false;
                                break;
                            case 8:
                                latestType = 9;
                                setCurrentShape(Shape.OVAL);
                                shape = 1;
                                shapeToggle = true;
                                dofill = false;
                                dropper = false;
                                break;
                            case 9:
                                latestType = 10;
                                setCurrentShape(Shape.STRAIGHT_LINE);
                                shape = 2;
                                shapeToggle = true;
                                dofill = false;
                                dropper = false;
                                break;

                            case 10:
                                setCurrentBrush(new Brush(Brush.BrushType.ERASER, brushSize));
                                latestType = 11;
                                restoreMode = false;
                                shapeToggle = false;
                                dofill = false;
                                dropper = false;
                                break;
                            case 11:
                                latestType = 12;
                                dofill = true;
                                shapeToggle = false;
                                dropper = false;
                                break;
                            case 12:
                                //latestType = 13;
                                //shapeToggle = false;
                                restoreMode = false;
                                dropper = false;
                                if(button==false)
                                {
                                    button = true;
                                    buttonList.get(12).Clicked(true);
                                }
                                else
                                {
                                    button = false;
                                    buttonList.get(12).Clicked(false);
                                }
                                currentShape = new Shape(0, 0, 0, 0, Color.BLACK, new BasicStroke(2.0f), shape);
                                //shapeToggle = !shapeToggle;
                            
                            case 13:
                                latestType = 14;
                                restoreMode = true;
                                dropper = false;
                                break;
                            case 14:
                                latestType = 15;
                                dropper = true;
                                break;
                            default:// fix logic here clean up code too messy

                        }
                    }

                }

                if (old != latestType) {
                    buttonList.get(old - 1).Clicked(false);
                }
                repaint();

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            textBoxOffset = null;
            mouse = false;
            if(!isWithinScreenBounds(e.getPoint()))
            {
                if(shapeToggle)
                {
                    change = true;
                    int x = e.getX();
                    int y = e.getY();
                    currentShape.setX2(x);
                    currentShape.setY2(y);
                    shapes.add(currentShape);
                    repaint();
                }
            }
            if (isWithinScreenBounds(e.getPoint())) {
                if (shapeToggle) {
                    int x = e.getX();
                    int y = e.getY();
                    currentShape.setX2(x);
                    currentShape.setY2(y);
                    shapes.add(currentShape);
                    change = true;
                    
                    repaint();
                } else {
                    // lineColors.put(new ArrayList<>(currentLine), currentColor);
                    if (currentBrush.getBrushType() == Brush.BrushType.DEFAULT && defaultButton.getStatus()) {
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line
                      
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.HIGHLIGHTER
                            && highlighterButton.getStatus()) {// same here
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line
                        
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.MARKER && markerButton.getStatus()) {// same
                                                                                                                   // here
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                             
                                                                              // line
                        restoreMode = false;
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.PENCIL && pencilButton.getStatus()) {// same
                                                                                                                   // here
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line
                       
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.PEN && penButton.getStatus()) {// same
                                                                                                             // here
                        
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line
                        
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.CRAYON && crayonButton.getStatus()) {// same
                                                                                                                   // here
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line
                        
                        currentLine.clear();
                        repaint();
                    } else if (currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT
                            && spraypaintButton.getStatus()) {
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor); // Store the color for the current
                                                                                    // line

                        
                        currentLine.clear();
                        repaint();
                    } else if (eraserButton.getStatus() && currentBrush.getBrushType() == Brush.BrushType.ERASER) {
                        
                        scribbleLines.add(new ArrayList<>(currentLine));
                        lineColors.put(new ArrayList<>(currentLine), currentColor);
                        currentLine.clear();
                        repaint();
                    }
                    else if (fillButton.getStatus()) {
                        
                        
                    
                        
                        currentLine.clear();
                        //size.add(brushSize);
                    }
                    else if (restoreButton.getStatus()) {
                        
                        
                    
                        
                        currentLine.clear();
                        //size.add(brushSize);
                    }
                    
                }
                //canvasList.add(canvas);//add boolean switch 
                undoredo = true;
                change = true;
            }
        }
    }

    private class DrawingMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if(!dofill && !dropper)
            {

                if (shapeToggle) {
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
                            currentBrush.getBrushType() == Brush.BrushType.SPRAY_PAINT ||currentBrush.getBrushType() == Brush.BrushType.ERASER ||  restoreMode) {

                        // Add the current mouse position to the current line
                        currentLine.add(e.getPoint());

                        // Repaint the component to reflect the changes
                        repaint();

                        // If the current line already exists in lineColors, add the current color to
                        // its list of colors

                    }
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            for (int i = 0; i < buttonList.size(); i++) {
                if (buttonList.get(i).Collision(e.getPoint())) {
                    // Do something when the mouse is over the button
                    // For example, change the button color or display a tooltip
                    // You can also trigger other actions based on the button
                    //System.out.println(eraserMode);
                    repaint();
                }
            }
        }

    }



    private boolean isWithinScreenBounds(Point point) {
        int toolbarWidth = Math.min(getWidth() / 4, 200);
        return point.x >= toolbarWidth && point.x <= getWidth() && point.y >= 0 && point.y <= getHeight();
    }
}