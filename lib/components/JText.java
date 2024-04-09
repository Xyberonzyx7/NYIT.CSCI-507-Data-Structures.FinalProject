package lib.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import lib.tools.FPoint;

public class JText extends JShape {

	private FPoint point;
	private String text;
	private Color color;

	public JText(int x, int y){
		point = new FPoint(x, y);
		this.text = "";
		this.color = Color.BLUE;
	}

	@Override
	public void moveto(float x, float y){
		int step = 3;
		float dx = (x - point.x) / (float) step;
		float dy = (y - point.y) / (float) step;

		if(Math.abs(x - point.x) < 1){
			dx = x - point.x;
		}

		if(Math.abs(y - point.y) < 1){
			dy = y - point.y;
		}
		move(dx, dy);
	}

	private void move(float dx, float dy){
		point.x += dx;
		point.y += dy;
		repaint();
	}

	@Override
	public float x(){
		return point.x;
	}

	@Override
	public float y(){
		return point.y;
	}

	@Override
	public void colorto(Color color){
		this.color = color;
	}

	@Override
	public Color c(){
		return this.color;
	}

	@Override
	public void textto(String text){
		this.text = text;
		repaint();
	}

	@Override
	public String t(){
		return this.text;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g); // Set font and color (optional)
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.setColor(color);

		// Draw the text at coordinates (x, y)
		int tmp_y = (int)(point.y - g.getFontMetrics().getHeight() / 2 - 4);
		for (String line : text.split("\n")) {
			g.drawString(line, (int)point.x, tmp_y += g.getFontMetrics().getHeight());
		}
	}
}
