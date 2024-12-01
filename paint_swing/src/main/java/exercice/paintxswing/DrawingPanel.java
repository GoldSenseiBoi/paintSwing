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
    private boolean isDrawing = false; // Indique si le dessin est en cours
    private int brushSize = 10; // Taille par défaut du pinceau
    

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
                    // Commencer le dessin
                    isDrawing = true;
                    drawShape(e.getX(), e.getY()); // Dessiner immédiatement au clic
                }
                repaint();
            }
    
            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null; // Terminer le déplacement ou redimensionnement
                resizing = false;
                isDrawing = false; // Terminer le dessin
            }
        });
    
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedShape != null && dragStart != null && currentShape.equals("SelectAndEdit")) {
                    if (resizing) {
                        int newWidth = e.getX() - (int) selectedShape.getShape().getBounds2D().getX();
                        int newHeight = e.getY() - (int) selectedShape.getShape().getBounds2D().getY();
                        resizeSelectedShape(newWidth, newHeight); // Utiliser la méthode publique ici
                    } else {
                        // Déplacer la forme
                        int deltaX = e.getX() - dragStart.x;
                        int deltaY = e.getY() - dragStart.y;
                        moveShape(selectedShape, deltaX, deltaY);
                        dragStart = e.getPoint();
                    }
                    repaint();
                } else if (isDrawing) {
                    // Dessiner en continu pendant le glissement de la souris
                    drawShape(e.getX(), e.getY());
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

    // Configurer les paramètres du pinceau
    public void setBrushSize(int size) {
        this.brushSize = size;
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
                newShape = new Ellipse2D.Double(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize); // Cercle pour le pinceau
                break;
            case "Eraser":
                newShape = new Rectangle2D.Double(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
                break;
        }

        if (newShape != null) {
            shapes.add(new ColoredShape(newShape, currentShape.equals("Eraser") ? Color.WHITE : currentColor));
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
    protected boolean isOnResizeHandle(int x, int y, ColoredShape shape) {
        if (shape.getShape() instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape.getShape();
            return getHandles(rect).stream().anyMatch(handle -> handle.contains(x, y));
        } else if (shape.getShape() instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) shape.getShape();
            return getHandles(ellipse).stream().anyMatch(handle -> handle.contains(x, y));
        } else if (shape.getShape() instanceof Polygon) {
            Polygon polygon = (Polygon) shape.getShape();
            return getHandles(polygon).stream().anyMatch(handle -> handle.contains(x, y));
        }
        return false;
    }

    
// Méthode publique pour redimensionner une forme sélectionnée
public void resizeSelectedShape(int newWidth, int newHeight) {
    if (selectedShape != null) {
        Shape shape = selectedShape.getShape();
        if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            rect.setFrame(rect.getX(), rect.getY(), newWidth, newHeight);
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) shape;
            ellipse.setFrame(ellipse.getX(), ellipse.getY(), newWidth, newHeight);
        } else if (shape instanceof Polygon) {
            Polygon polygon = (Polygon) shape;
            // Simplification pour redimensionner proportionnellement autour du premier point
            polygon.xpoints[1] = polygon.xpoints[0] + newWidth / 2;
            polygon.xpoints[2] = polygon.xpoints[0] - newWidth / 2;
            polygon.ypoints[1] = polygon.ypoints[0] + newHeight;
            polygon.ypoints[2] = polygon.ypoints[0] + newHeight;
            polygon.invalidate(); // Recalculer les limites
        }
        repaint();
    } else {
        JOptionPane.showMessageDialog(this, "Aucune forme sélectionnée.", "Erreur", JOptionPane.WARNING_MESSAGE);
    }
}


    
    

    // Obtenir les poignées de redimensionnement d'une forme rectangulaire
    private ArrayList<Rectangle2D> getHandles(Shape shape) {
        ArrayList<Rectangle2D> handles = new ArrayList<>();
        if (shape instanceof Rectangle2D) {
            Rectangle2D rect = (Rectangle2D) shape;
            handles.add(new Rectangle2D.Double(rect.getMinX() - handleSize / 2, rect.getMinY() - handleSize / 2, handleSize, handleSize)); // Coin supérieur gauche
            handles.add(new Rectangle2D.Double(rect.getMaxX() - handleSize / 2, rect.getMinY() - handleSize / 2, handleSize, handleSize)); // Coin supérieur droit
            handles.add(new Rectangle2D.Double(rect.getMinX() - handleSize / 2, rect.getMaxY() - handleSize / 2, handleSize, handleSize)); // Coin inférieur gauche
            handles.add(new Rectangle2D.Double(rect.getMaxX() - handleSize / 2, rect.getMaxY() - handleSize / 2, handleSize, handleSize)); // Coin inférieur droit
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D ellipse = (Ellipse2D) shape;
            handles.add(new Rectangle2D.Double(ellipse.getMinX(), ellipse.getMinY(), handleSize, handleSize)); // Simplifié pour les coins principaux
        } else if (shape instanceof Polygon) {
            Polygon polygon = (Polygon) shape;
            for (int i = 0; i < polygon.npoints; i++) {
                handles.add(new Rectangle2D.Double(polygon.xpoints[i] - handleSize / 2, polygon.ypoints[i] - handleSize / 2, handleSize, handleSize));
            }
        }
        return handles;
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

            // Dessiner les poignées pour toutes les formes
            ArrayList<Rectangle2D> handles = getHandles(selectedShape.getShape());
            for (Rectangle2D handle : handles) {
                g2.setPaint(Color.BLUE);
                g2.fill(handle);
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

        // Supprimer la forme sélectionnée
    public void deleteSelectedShape() {
        if (selectedShape != null) {
            shapes.remove(selectedShape); // Supprime la forme de la liste
            selectedShape = null; // Désélectionne après suppression
            repaint(); // Redessine le panneau
        } else {
            JOptionPane.showMessageDialog(this, "Aucune forme sélectionnée à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
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
