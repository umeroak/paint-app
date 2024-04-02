import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainFrame extends JFrame {
    private DrawingPanel drawingPanel;


    public MainFrame() {
        setTitle("Simple Painting App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set default size
        setLocationRelativeTo(null); // Center the frame

        drawingPanel = new DrawingPanel();
        getContentPane().add(drawingPanel);
        new KeyboardHandler(drawingPanel);

        createButton();
        createMenuBar();

        setVisible(true);
    }

    private void createButton()
    {
        JPanel buttonPanel = new JPanel();

        JButton increase = new JButton("INCREASE");
        increase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.increaseSize();
            }
        });
        JButton decrease = new JButton("DECREASE");
        decrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.decreaseSize();
            }
        });
        JButton addTextBoxButton = new JButton("Add Text Box");
        addTextBoxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to add a text box to the drawing panel
                drawingPanel.showTextBox(new Point(100, 100), new Dimension(100, 50)); // Example initial location and size
            }
        });
    
        JButton removeTextBoxButton = new JButton("Remove Text Box");
        removeTextBoxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to remove the text box from the drawing panel
                drawingPanel.hideTextBox();
            }
        });



        buttonPanel.add(increase);
        buttonPanel.add(decrease);
        buttonPanel.add(addTextBoxButton);
        buttonPanel.add(removeTextBoxButton);

        // Add the buttonPanel to the JFrame's content pane
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Image File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif");
                fileChooser.setFileFilter(filter);
                int option = fileChooser.showOpenDialog(MainFrame.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        drawingPanel.openImage(file);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Image File");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                int option = fileChooser.showSaveDialog(MainFrame.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        drawingPanel.saveImage(file);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(saveMenuItem);

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(quitMenuItem);
 
        JMenu editMenu = new JMenu("Edit");
        JMenuItem eraserMenuItem = new JMenuItem("Eraser");
        eraserMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.toggleEraserMode();
            }
        });
        editMenu.add(eraserMenuItem);

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.undo();
            }
        });
        editMenu.add(undoMenuItem);
        
        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.redo();
            }
        });
        editMenu.add(redoMenuItem);

        JMenu zoomMenu = new JMenu("Zoom");
        JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
        zoomInMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.zoomIn();
            }
        });

        zoomMenu.add(zoomInMenuItem);
        
        JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
        zoomOutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.zoomOut();
            }
        });
        zoomMenu.add(zoomOutMenuItem);
       
        JMenu colorMenu = new JMenu("Color");
        JMenuItem colorMenuItem = new JMenuItem("Choose Color");
        colorMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(MainFrame.this, "Choose Color", Color.BLACK); // Default color if none selected
                if (color != null) {
                    drawingPanel.setCurrentColor(color); // Use setCurrentColor method to set the drawing color
                }
            }
        });
        colorMenu.add(colorMenuItem);


        JMenu shapeMenu = new JMenu("Shape");
        JMenuItem rectangleMenuItem = new JMenuItem("Rectangle");
        rectangleMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentShape(Shape.RECTANGLE);
            }
        });
        shapeMenu.add(rectangleMenuItem);

        JMenuItem ovalMenuItem = new JMenuItem("Oval");
        ovalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentShape(Shape.OVAL);
            }
        });
        shapeMenu.add(ovalMenuItem);

        JMenuItem straightLineMenuItem = new JMenuItem("Straight Line");
        straightLineMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentShape(Shape.STRAIGHT_LINE);
            }
        });
        shapeMenu.add(straightLineMenuItem);

        JMenu highlighterMenu = new JMenu("Highlighter");
        JMenuItem highlighterMenuItem = new JMenuItem("Highlighter");
        highlighterMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.HIGHLIGHTER, 10)); // Set the same properties as the transparent brush
            }
        });
        highlighterMenu.add(highlighterMenuItem);

        JMenu brushMenu = new JMenu("Brush");
        JMenuItem normalBrushMenuItem = new JMenuItem("Default");
        normalBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.DEFAULT, 10));
            }
        });
        brushMenu.add(normalBrushMenuItem);

        JMenuItem markerBrushMenuItem = new JMenuItem("Marker");
        markerBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.MARKER, 8));
            }
        });
        brushMenu.add(markerBrushMenuItem);

        JMenuItem pencilBrushMenuItem = new JMenuItem("Pencil");
        pencilBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.PENCIL, 3));
            }
        });
        brushMenu.add(pencilBrushMenuItem);

        JMenuItem penBrushMenuItem = new JMenuItem("Pen");
        penBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.PEN, 5));
            }
        });
        brushMenu.add(penBrushMenuItem);

        JMenuItem crayonBrushMenuItem = new JMenuItem("Crayon");
        crayonBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.CRAYON, 6));
            }
        });
        brushMenu.add(crayonBrushMenuItem);

        JMenuItem sprayPaintBrushMenuItem = new JMenuItem("Spray Paint"); // New brush menu item
        sprayPaintBrushMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentBrush(new Brush(Brush.BrushType.SPRAY_PAINT, 10)); // Use a default size for spray paint
            }
        });
        brushMenu.add(sprayPaintBrushMenuItem);

        // Add more shape options as needed...
        menuBar.add(zoomMenu);
        menuBar.add(fileMenu);
        menuBar.add(shapeMenu);
        menuBar.add(colorMenu);
        menuBar.add(editMenu);
        menuBar.add(brushMenu);
        menuBar.add(highlighterMenu);
        setJMenuBar(menuBar);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}