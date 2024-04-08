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
        createRGBTextFields();

        setVisible(true);
    }
    private void createRGBTextFields() {
        JPanel textFieldPanel = new JPanel();
    
    // Create the text fields for R, G, and B values
    JTextField redTextField = new JTextField(3); // 3 columns
    JTextField greenTextField = new JTextField(3); // 3 columns
    JTextField blueTextField = new JTextField(3); // 3 columns
    
    // Label for red text field
    JLabel redLabel = new JLabel("R:");
    // Label for green text field
    JLabel greenLabel = new JLabel("G:");
    // Label for blue text field
    JLabel blueLabel = new JLabel("B:");
    
    // Add labels and text fields to the panel
    textFieldPanel.add(redLabel);
    textFieldPanel.add(redTextField);
    textFieldPanel.add(greenLabel);
    textFieldPanel.add(greenTextField);
    textFieldPanel.add(blueLabel);
    textFieldPanel.add(blueTextField);
    
    // Add submit button
    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get values from text fields
            int red = Integer.parseInt(redTextField.getText());
            int green = Integer.parseInt(greenTextField.getText());
            int blue = Integer.parseInt(blueTextField.getText());
            
            // Set color in the drawing panel
            drawingPanel.setCustomColor(red, green, blue);
        }
    });
    textFieldPanel.add(submitButton);
    
    // Add the panel to the JFrame's content pane
    getContentPane().add(textFieldPanel, BorderLayout.SOUTH);
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
        getContentPane().add(buttonPanel, BorderLayout.NORTH);

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

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //drawingPanel.undo();
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


        

        // Add more shape options as needed...
        menuBar.add(fileMenu);
        menuBar.add(colorMenu);
        menuBar.add(editMenu);

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