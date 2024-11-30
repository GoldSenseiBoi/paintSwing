package exercice.paintxswing;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JToolBar;

public class ToolPanel extends JToolBar {

    private String currentShape = "Rectangle";
    private Color currentColor = Color.BLACK;

    public ToolPanel(ActionListener toolActionListener) {
        super("Outils de dessin");

        // Bouton pour dessiner un rectangle
        JButton drawRectangleButton = new JButton("Rectangle");
        drawRectangleButton.addActionListener(e -> {
            currentShape = "Rectangle";
            toolActionListener.actionPerformed(e);
        });

        // Bouton pour dessiner un cercle
        JButton drawCircleButton = new JButton("Cercle");
        drawCircleButton.addActionListener(e -> {
            currentShape = "Cercle";
            toolActionListener.actionPerformed(e);
        });

        // Bouton pour dessiner un triangle
        JButton drawTriangleButton = new JButton("Triangle");
        drawTriangleButton.addActionListener(e -> {
            currentShape = "Triangle";
            toolActionListener.actionPerformed(e);
        });

        // Bouton pour sélectionner la couleur
        JButton selectColorButton = new JButton("Couleur");
        selectColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", currentColor);
            if (selectedColor != null) {
                currentColor = selectedColor;
                toolActionListener.actionPerformed(e);
            }
        });

        // Ajouter les boutons à la barre d'outils
        add(drawRectangleButton);
        add(drawCircleButton);
        add(drawTriangleButton);
        add(selectColorButton);
    }

    public String getCurrentShape() {
        return currentShape;
    }

    public Color getCurrentColor() {
        return currentColor;
    }
}
