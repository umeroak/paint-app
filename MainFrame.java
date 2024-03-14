import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame {
    private DrawingPanel drawingPanel;

    public MainFrame() {
        setTitle("Simple Painting App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set default size
        setLocationRelativeTo(null); // Center the frame

        drawingPanel = new DrawingPanel();
        getContentPane().add(drawingPanel);

        createMenuBar();

        setVisible(true);
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
                // Implement save functionality
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

        // Add more shape options as needed...

        menuBar.add(fileMenu);
        menuBar.add(shapeMenu);
        menuBar.add(colorMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}