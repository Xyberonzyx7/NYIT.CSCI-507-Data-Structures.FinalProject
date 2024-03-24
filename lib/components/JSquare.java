package lib.components;
import javax.swing.*;

import lib.tools.FPoint;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JSquare extends JShape {
	private FPoint now;
    private Color color; // Square color
	private final int STROKE = 5;
	private final int SIDELENGTH = 60;

    public JSquare(int x, int y) {
		now = new FPoint(x, y);
        this.color = Color.DARK_GRAY; // Default color (you can change it)
	}

	@Override
    public void move(float dx, float dy) {
        // Move the square by dx units horizontally and dy units vertically
        now.x += dx;
        now.y += dy;
        repaint(); // Redraw the square
    }

	@Override
	public void moveto(float x, float y){
		
		int step = 3;
		float dx = (x - now.x) / (float)step;
		float dy = (y - now.y) / (float)step;

		if (Math.abs(x - now.x) < 1) {
			dx = x - now.x;
		}

		if (Math.abs(y - now.y) < 1) {
			dy = y - now.y;
		}

		move(dx, dy);
	}

    public void setColor(Color color) {
        this.color = color;
        repaint(); // Redraw the square
    }

	@Override
	public float x(){
		return now.x;
	}

	@Override
	public float y(){
		return now.y;
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the border (hollow square)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
		g2d.setStroke(new BasicStroke(STROKE));
        g2d.drawRect((int)(now.x - SIDELENGTH / 2), (int)(now.y - SIDELENGTH / 2), SIDELENGTH, SIDELENGTH); // Draw the border
    }
}

