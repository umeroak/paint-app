import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeyboardHandler {
    private DrawingPanel drawingPanel;

    public KeyboardHandler(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        registerKeyboardShortcuts();
    }

    private void registerKeyboardShortcuts() {
        InputMap inputMap = drawingPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = drawingPanel.getActionMap();

        // Undo shortcut
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        inputMap.put(undoKeyStroke, "undoAction");
        actionMap.put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //drawingPanel.undo();
            }
        });

        // Redo shortcut
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        inputMap.put(redoKeyStroke, "redoAction");
        actionMap.put("redoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.redo();
            }
        });
    }
}

