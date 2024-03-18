import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@SuppressWarnings("deprecation")
public class KeyboardHandler {
    private DrawingPanel drawingPanel;

    public KeyboardHandler(DrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        registerKeyboardShortcuts();
    }

    private void registerKeyboardShortcuts() {
        Action undoAction = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.undo();
            }
        };

        Action redoAction = new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.redo();
            }
        };

        InputMap inputMap = drawingPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = drawingPanel.getActionMap();

        // Define keyboard shortcuts
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

        // Register shortcuts with actions
        inputMap.put(undoKeyStroke, "undoAction");
        actionMap.put("undoAction", undoAction);

        inputMap.put(redoKeyStroke, "redoAction");
        actionMap.put("redoAction", redoAction);
    }
}
