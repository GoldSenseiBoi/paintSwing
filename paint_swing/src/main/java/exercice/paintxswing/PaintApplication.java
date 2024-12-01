package exercice.paintxswing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class PaintApplication extends JFrame {
    private DrawingPanel canvasPanel;

    public PaintApplication() {
        // Configuration de la fenêtre principale
        setTitle("Paint Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran

        // Initialiser la zone de dessin
        canvasPanel = new DrawingPanel();
        canvasPanel.setBackground(Color.WHITE);

        // Ajouter les menus
        setJMenuBar(createMenuBar());

        // Ajouter la zone de dessin
        add(canvasPanel, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Formes
        JMenu shapesMenu = new JMenu("Formes");
        JMenuItem rectangleItem = new JMenuItem("Rectangle");
        rectangleItem.addActionListener(e -> canvasPanel.setCurrentShape("Rectangle"));

        JMenuItem circleItem = new JMenuItem("Cercle");
        circleItem.addActionListener(e -> canvasPanel.setCurrentShape("Cercle"));

        JMenuItem triangleItem = new JMenuItem("Triangle");
        triangleItem.addActionListener(e -> canvasPanel.setCurrentShape("Triangle"));

        JMenuItem brushItem = new JMenuItem("Pinceau");
        brushItem.addActionListener(e -> canvasPanel.setCurrentShape("Brush"));

        JMenuItem eraserItem = new JMenuItem("Gomme");
        eraserItem.addActionListener(e -> canvasPanel.setCurrentShape("Eraser"));

        shapesMenu.add(rectangleItem);
        shapesMenu.add(circleItem);
        shapesMenu.add(triangleItem);
        shapesMenu.add(brushItem);
        shapesMenu.add(eraserItem);

        // Menu Couleurs
        JMenu colorMenu = new JMenu("Couleurs");
        JMenuItem chooseColorItem = new JMenuItem("Choisir une couleur");
        chooseColorItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", canvasPanel.getCurrentColor());
            if (selectedColor != null) {
                canvasPanel.setCurrentColor(selectedColor);
            }
        });

        colorMenu.add(chooseColorItem);

        // Ajouter les menus à la barre
        menuBar.add(shapesMenu);
        menuBar.add(colorMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintApplication paintApplication = new PaintApplication();
            paintApplication.setVisible(true);
        });
    }
}
