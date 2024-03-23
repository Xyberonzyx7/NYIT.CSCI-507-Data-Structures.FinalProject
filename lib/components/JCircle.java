package lib.components;

import javax.swing.*;
import java.awt.*;

public class JCircle extends JShape {
    private int x; // X-coordinate of circle's center
    private int y; // Y-coordinate of circle's center
    private int radius; // Radius of the circle
    private Color color; // Circle color (you can customize this)

    public JCircle(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = 20;
        this.color = Color.BLUE; // Default color (you can change it)
    }

    public void move(int dx, int dy) {
        // Move the circle by dx units horizontally and dy units vertically
        x += dx;
        y += dy;
        repaint(); // Redraw the circle
    }

    public void setColor(Color color) {
        this.color = color;
        repaint(); // Redraw the circle
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }
}
