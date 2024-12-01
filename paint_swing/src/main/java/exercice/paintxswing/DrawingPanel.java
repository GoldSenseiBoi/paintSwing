package exercice.paintxswing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private String currentShape = "Rectangle";
    private Color currentColor = Color.BLACK;

    // Liste pour stocker les formes dessinées
    private final ArrayList<ColoredShape> shapes = new ArrayList<>();
    private boolean isDrawingBrush = false;

    public DrawingPanel() {
        // Écouteurs pour dessiner des formes ou utiliser le pinceau
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentShape.equals("Brush")) {
                    isDrawingBrush = true;
                    drawShape(e.getX(), e.getY());
                } else {
                    drawShape(e.getX(), e.getY());
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawingBrush = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawingBrush) {
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

    // Dessiner la forme ou le pinceau
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dessiner chaque forme avec la bonne couleur
        for (ColoredShape coloredShape : shapes) {
            g2.setPaint(coloredShape.getColor());
            g2.fill(coloredShape.getShape());
        }
    }

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