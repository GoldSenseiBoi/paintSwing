package exercice.paintxswing;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Créer une instance de l'application et la rendre visible
        SwingUtilities.invokeLater(() -> {
            PaintApplication paintApplicationInstance = new PaintApplication();
            paintApplicationInstance.setVisible(true);
        });
    }
}
