package lib.components;
import javax.swing.*;
import java.awt.*;

public class JSquare extends JShape {
    private int x; // X-coordinate of square's top-left corner
    private int y; // Y-coordinate of square's top-left corner
    private Color color; // Square color
	private final int STROKE = 5;
	private final int SIDELENGTH = 60;

    public JSquare(int d, int e) {
        this.x = d;
        this.y = e;
        this.color = Color.DARK_GRAY; // Default color (you can change it)
	}

    public void move(int dx, int dy) {
        // Move the square by dx units horizontally and dy units vertically
        x += dx;
        y += dy;
        repaint(); // Redraw the square
    }

    public void setColor(Color color) {
        this.color = color;
        repaint(); // Redraw the square
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the border (hollow square)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
		g2d.setStroke(new BasicStroke(STROKE));
        g2d.drawRect(x, y, SIDELENGTH, SIDELENGTH); // Draw the border
    }
}

