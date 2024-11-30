package exercice.paintxswing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private BufferedImage canvas;
    private Graphics2D g2d;
    private String currentShape = "Rectangle";
    private Color currentColor = Color.BLACK;
    private int startX, startY;
    private boolean isDragging = false;

    public DrawingPanel() {
        // Initialisation du canvas (image en mémoire pour le dessin)
        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Ajout d'un écouteur de la souris pour dessiner des formes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentShape.equals("Move")) {
                    startX = e.getX();
                    startY = e.getY();
                    isDragging = true;
                } else {
                    drawShape(e.getX(), e.getY());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && currentShape.equals("Move")) {
                    int deltaX = e.getX() - startX;
                    int deltaY = e.getY() - startY;
                    moveCanvas(deltaX, deltaY);
                    startX = e.getX();
                    startY = e.getY();
                    repaint();
                }
            }
        });
    }

    // Définir la forme actuelle à dessiner (Rectangle, Cercle, Triangle, Gomme, Déplacement)
    public void setCurrentShape(String shape) {
        this.currentShape = shape;
    }

    // Définir la couleur actuelle à utiliser pour le dessin
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    // Dessiner la forme actuelle à la position spécifiée
    private void drawShape(int x, int y) {
        g2d.setPaint(currentColor);
        switch (currentShape) {
            case "Rectangle":
                g2d.fillRect(x, y, 100, 50);
                break;
            case "Cercle":
                g2d.fillOval(x, y, 50, 50);
                break;
            case "Triangle":
                int[] xPoints = {x, x - 25, x + 25};
                int[] yPoints = {y, y + 50, y + 50};
                g2d.fillPolygon(xPoints, yPoints, 3);
                break;
            case "Eraser":
                g2d.setPaint(Color.WHITE);
                g2d.fillRect(x, y, 20, 20);
                break;
        }
    }

    // Déplacer le contenu du canvas
    private void moveCanvas(int deltaX, int deltaY) {
        BufferedImage tempCanvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tempCanvas.createGraphics();
        g.drawImage(canvas, deltaX, deltaY, null);
        g.dispose();
        g2d = tempCanvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas = tempCanvas;
    }

    // Redéfinir la méthode paintComponent pour dessiner le canvas sur le JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }
}
