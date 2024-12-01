package exercice.paintxswing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PaintApplication extends JFrame {
    private DrawingPanel canvasPanel;
    private JTextField widthField; // Champ pour la largeur
    private JTextField heightField; // Champ pour la hauteur
    private JTextField brushSizeField; // Champ pour la taille du pinceau

    public PaintApplication() {
        // Configuration de la fenêtre principale
        setTitle("Paint Appix");
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

        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem saveItem = new JMenuItem("Sauvegarder");
        saveItem.addActionListener(e -> canvasPanel.saveImage());
        JMenuItem loadItem = new JMenuItem("Charger");
        loadItem.addActionListener(e -> canvasPanel.loadImage());
        

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        // Menu Formes
        JMenu shapesMenu = new JMenu("Outils");

        // Ajouter l'option de suppression dans le menu Formes
        JMenuItem deleteItem = new JMenuItem("Supprimer");
        deleteItem.addActionListener(e -> canvasPanel.deleteSelectedShape());
        shapesMenu.add(deleteItem);


        JMenuItem selectAndEditItem = new JMenuItem("Sélection et Édition");
        selectAndEditItem.addActionListener(e -> canvasPanel.setCurrentShape("SelectAndEdit"));
        shapesMenu.add(selectAndEditItem);

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

        // Menu Dimensions
        JMenu dimensionMenu = new JMenu("Dimensions");
        JLabel widthLabel = new JLabel("Largeur :");
        widthField = new JTextField(5); // Champ pour la largeur
        JLabel heightLabel = new JLabel("Hauteur :");
        heightField = new JTextField(5); // Champ pour la hauteur
        JButton applyButton = new JButton("Appliquer");
        applyButton.addActionListener(e -> applyDimensions()); // Appliquer les dimensions

        dimensionMenu.add(widthLabel);
        dimensionMenu.add(widthField);
        dimensionMenu.add(heightLabel);
        dimensionMenu.add(heightField);
        dimensionMenu.add(applyButton);

        // Ajouter les menus à la barre
        menuBar.add(fileMenu);
        menuBar.add(shapesMenu);
        menuBar.add(colorMenu);
        menuBar.add(dimensionMenu);

        // Menu Pinceau
        JMenu brushMenu = new JMenu("Pinceau");
        JLabel brushSizeLabel = new JLabel("Taille du pinceau :");
        brushSizeField = new JTextField(5);
        JButton brushSizeApplyButton = new JButton("Appliquer");
        brushSizeApplyButton.addActionListener(e -> applyBrushSize());

        brushMenu.add(brushSizeLabel);
        brushMenu.add(brushSizeField);
        brushMenu.add(brushSizeApplyButton);

        // Ajouter le menu pinceau à la barre de menu
        menuBar.add(brushMenu);

        return menuBar;
    }

    // Méthode pour appliquer les dimensions
private void applyDimensions() {
    try {
        int newWidth = Integer.parseInt(widthField.getText());
        int newHeight = Integer.parseInt(heightField.getText());
        canvasPanel.resizeSelectedShape(newWidth, newHeight); // Appel à la méthode publique de DrawingPanel
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}


    // Méthode pour appliquer la taille du pinceau
    private void applyBrushSize() {
        try {
            int newSize = Integer.parseInt(brushSizeField.getText());
            canvasPanel.setBrushSize(newSize);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur numérique valide pour la taille du pinceau.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintApplication paintApplication = new PaintApplication();
            paintApplication.setVisible(true);
        });
    }
}
