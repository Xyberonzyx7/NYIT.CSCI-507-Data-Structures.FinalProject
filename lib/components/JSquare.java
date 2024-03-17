package lib.components;

import javax.swing.*;
import java.awt.*;

public class JSquare extends JPanel {
	private int x; // X-coordinate of square's top-left corner
	private int y; // Y-coordinate of square's top-left corner
	private int sideLength; // Length of each side of the square
	private Color color; // Square color
	private int glowRadius; // Radius of the glow effect

	public JSquare(int x, int y, int sideLength, int glowRadius) {
		this.x = x;
		this.y = y;
		this.sideLength = sideLength;
		this.color = Color.GREEN; // Default color (you can change it)
		this.glowRadius = glowRadius;
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

		// Draw the glowing effect
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(color);
		g2d.fillRect(x, y, sideLength, sideLength);

		// Create a gradient for the glow effect
		RadialGradientPaint gradient = new RadialGradientPaint(
				x + sideLength / 2, y + sideLength / 2, glowRadius,
				new float[] { 0.0f, 1.0f },
				new Color[] { color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0) });

		// Apply the gradient to the shape
		g2d.setPaint(gradient);
		g2d.fillOval(x - glowRadius, y - glowRadius, sideLength + 2 * glowRadius, sideLength + 2 * glowRadius);
	}

}
