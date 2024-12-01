package exercice.paintxswing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private String currentShape = "Rectangle";
    private Color currentColor = Color.BLACK;

    // Liste des formes dessinées
    private final ArrayList<ColoredShape> shapes = new ArrayList<>();
    private ColoredShape selectedShape = null; // Forme sélectionnée pour modification
    private Point dragStart = null; // Point initial pour déplacement ou redimensionnement
    private boolean resizing = false; // Indique si une forme est en cours de redimensionnement
    private final int handleSize = 10; // Taille des poignées de redimensionnement

    public DrawingPanel() {
        // Écouteurs pour dessiner et interagir avec les formes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentShape.equals("SelectAndEdit")) {
                    // Sélectionner une forme existante
                    selectedShape = findShapeAt(e.getX(), e.getY());
                    if (selectedShape != null) {
                        resizing = isOnResizeHandle(e.getX(), e.getY(), selectedShape);
                        dragStart = e.getPoint();
                    }
                } else {
                    // Dessiner une nouvelle forme
                    drawShape(e.getX(), e.getY());
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null; // Terminer le déplacement ou redimensionnement
                resizing = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedShape != null && dragStart != null && currentShape.equals("SelectAndEdit")) {
                    if (resizing) {
                        // Redimensionner la forme
                        resizeShape(selectedShape, e.getX(), e.getY());
                    } else {
                        // Déplacer la forme
                        int deltaX = e.getX() - dragStart.x;
                        int deltaY = e.getY() - dragStart.y;
                        moveShape(selectedShape, deltaX, deltaY);
                        dragStart = e.getPoint();
                    }
                    repaint();
                }
            }
        });
    }

    // Définir la forme actuelle à dessiner
    public void setCurrentShape(String shape) {
        this.currentShape = shape;
    }

    // Définir la couleur actuelle
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    // Dessiner une nouvelle forme
    private void drawShape(int x, int y) {
        Shape newShape = null;

        switch (currentShape) {
            case "Rectangle":
                newShape = new Rectangle2D.Double(x, y, 100, 50);
                break;
            case "Cercle":
                newShape = new Ellipse2D.Double(x, y, 50, 50);
                break;
            case "Triangle":
                int[] xPoints = {x, x - 25, x + 25};
                int[] yPoints = {y, y + 50, y + 50};
                newShape = new Polygon(xPoints, yPoints, 3);
                break;
            case "Brush":
                newShape = new Ellipse2D.Double(x, y, 10, 10); // Petit cercle pour le pinceau
                break;
            case "Eraser":
                newShape = new Rectangle2D.Double(x, y, 20, 20);
                currentColor = Color.WHITE; // Couleur blanche pour effacer
                break;
        }

        if (newShape != null) {
            shapes.add(new ColoredShape(newShape, currentColor));
        }
    }

    // Trouver une forme à une position donnée
    private ColoredShape findShapeAt(int x, int y) {
        for (ColoredShape shape : shapes) {
            if (shape.getShape().contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    // Déplacer une forme existante
    private void moveShape(ColoredShape shape, int deltaX, int deltaY) {
        Shape s = shape.getShape();
        if (s instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) s;
            rect.setFrame(rect.getX() + deltaX, rect.getY() + deltaY, rect.getWidth(), rect.getHeight());
        } else if (s instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) s;
            ellipse.setFrame(ellipse.getX() + deltaX, ellipse.getY() + deltaY, ellipse.getWidth(), ellipse.getHeight());
        } else if (s instanceof Polygon) {
            Polygon polygon = (Polygon) s;
            polygon.translate(deltaX, deltaY);
        }
    }

    // Vérifier si la souris est sur une poignée de redimensionnement
    private boolean isOnResizeHandle(int x, int y, ColoredShape shape) {
        if (shape.getShape() instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape.getShape();
            Rectangle2D handle = new Rectangle2D.Double(rect.getMaxX() - handleSize, rect.getMaxY() - handleSize, handleSize, handleSize);
            return handle.contains(x, y);
        }
        // Ajouter des vérifications pour d'autres formes si nécessaire
        return false;
    }

    // Redimensionner une forme existante
    private void resizeShape(ColoredShape shape, int mouseX, int mouseY) {
        if (shape.getShape() instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape.getShape();
            double newWidth = mouseX - rect.getX();
            double newHeight = mouseY - rect.getY();
            rect.setFrame(rect.getX(), rect.getY(), Math.max(newWidth, handleSize), Math.max(newHeight, handleSize));
        }
        // Ajouter la logique pour d'autres formes si nécessaire
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dessiner toutes les formes
        for (ColoredShape shape : shapes) {
            g2.setPaint(shape.getColor());
            g2.fill(shape.getShape());
        }

        // Ajouter une surbrillance à la forme sélectionnée et dessiner les poignées
        if (selectedShape != null) {
            g2.setPaint(Color.RED);
            g2.draw(selectedShape.getShape());

            // Poignées pour redimensionnement (seulement pour les rectangles pour l'instant)
            if (selectedShape.getShape() instanceof Rectangle2D) {
                Rectangle2D rect = (Rectangle2D) selectedShape.getShape();
                g2.setPaint(Color.BLUE);
                g2.fill(new Rectangle2D.Double(rect.getMaxX() - handleSize, rect.getMaxY() - handleSize, handleSize, handleSize));
            }
        }
    }

    // Sauvegarder l'image
    public void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder l'image");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = image.createGraphics();
                paint(g2); // Dessiner le contenu actuel du panneau sur l'image
                g2.dispose();
                ImageIO.write(image, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Image sauvegardée avec succès !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde de l'image.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Charger une image
    public void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger une image");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                BufferedImage loadedImage = ImageIO.read(fileToLoad);
                Graphics g = getGraphics();
                g.drawImage(loadedImage, 0, 0, this);
                g.dispose();
                JOptionPane.showMessageDialog(this, "Image chargée avec succès !");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'image.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Classe interne pour stocker une forme avec sa couleur
    private static class ColoredShape {
        private final Shape shape;
        private final Color color;

        public ColoredShape(Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public Shape getShape() {
            return shape;
        }

        public Color getColor() {
            return color;
        }
    }
}
