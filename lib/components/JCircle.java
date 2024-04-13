package lib.components;

import lib.tools.FPoint;

import java.awt.*;

public class JCircle extends JShape {
	private FPoint now;
    private int radius; // Radius of the circle
    private Color color; // Circle color (you can customize this)
	private String txt;

    public JCircle(int x, int y, String txt) {
		now = new FPoint(x, y);
        now.x = x;
        now.y = y;
		this.txt = txt;
        this.radius = 20;
        this.color = Color.BLUE; // Default color (you can change it)
    }

	@Override
	public void textto(String text){
		this.txt = text;
		repaint();
	}

	@Override
	public void colorto(Color color){
		this.color = color;
		repaint();
	}

    private void move(float dx, float dy) {
        // Move the circle by dx units horizontally and dy units vertically
        now.x += dx;
        now.y += dy;
        repaint(); // Redraw the circle
    }

	@Override
	public void moveto(float x, float y){
		float dx = (x - now.x) / (float)step;
		float dy = (y - now.y) / (float)step;

		if (Math.abs(x - now.x) < TOLERANCE_PIXEL) {
			dx = x - now.x;
		}

		if (Math.abs(y - now.y) < TOLERANCE_PIXEL) {
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

	@Override
	public Color c(){
		return color;
	}

	@Override
	public String t(){
		return txt;
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

		// draw circle
        g.setColor(color);
        g.fillOval((int) now.x - radius, (int) now.y - radius, 2 * radius, 2 * radius);

		// draw txt
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 14));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int x = (int) now.x - metrics.stringWidth(txt) / 2;
		int y = (int) now.y - metrics.getHeight() / 2 + metrics.getAscent();
		g.drawString( txt, x, y);
    }
}
