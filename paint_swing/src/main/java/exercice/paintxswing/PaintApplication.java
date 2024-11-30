package exercice.paintxswing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class PaintApplication extends JFrame {
    private ToolPanel mainToolPanel;
    private DrawingPanel canvasPanel;

    public PaintApplication() {
        // Configuration de la fenêtre principale
        setTitle("Paint Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran

        // Initialiser la barre d'outils et la zone de dessin
        mainToolPanel = new ToolPanel(e -> canvasPanel.repaint());
        canvasPanel = new DrawingPanel();
        canvasPanel.setBackground(Color.WHITE);

        // Ajouter la barre d'outils en haut et la zone de dessin au centre
        add(mainToolPanel, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
    }
}
