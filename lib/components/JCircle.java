package lib.components;

import javax.swing.*;

import lib.tools.FPoint;

import java.awt.*;

public class JCircle extends JShape {
	private FPoint now;
    private int radius; // Radius of the circle
    private Color color; // Circle color (you can customize this)

    public JCircle(int x, int y) {
		now = new FPoint(x, y);
        now.x = x;
        now.y = y;
        this.radius = 20;
        this.color = Color.BLUE; // Default color (you can change it)
    }

	@Override
    public void move(float dx, float dy) {
        // Move the circle by dx units horizontally and dy units vertically
        now.x += dx;
        now.y += dy;
        repaint(); // Redraw the circle
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

	@Override
	public float x(){
		return now.x;
	}

	@Override
	public float y(){
		return now.y;
	}

    public void setColor(Color color) {
        this.color = color;
        repaint(); // Redraw the circle
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval((int) now.x - radius, (int) now.y - radius, 2 * radius, 2 * radius);
    }
}
