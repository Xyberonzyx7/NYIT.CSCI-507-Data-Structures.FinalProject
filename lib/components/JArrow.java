 package lib.components;

import javax.swing.*;

import lib.tools.FPoint;

import java.awt.*;

public class JArrow extends JShape {
    private float arrowLength = 50; // Initial length of the arrow
    private double arrowAngle = Math.toRadians(90); // Initial angle (45 degrees)
	private FPoint center;

	public JArrow(int x, int y, int angle){
		center = new FPoint(x, y);
		arrowAngle = Math.toRadians(angle);
	}

	@Override
	public float l(){
		return arrowLength;
	}

	@Override
	public void extend(float dlength){
		arrowLength += dlength;
		repaint();
	}

	@Override
	public void extendto(float length){
		int step = 3;
		float dlength = (length - arrowLength) / (float)step;
		if(Math.abs(length - arrowLength) < 1){
			dlength = length - arrowLength;
		}
		extend(dlength);
	}

	@Override
	public void move(float dx, float dy){
		center.x += dx;
		center.y += dy;
		repaint();
	}

	@Override
	public void moveto(float x, float y){
		int step = 3;
		float dx = (x - center.x) / (float) step;
		float dy = (y - center.y) / (float) step;

		if (Math.abs(x - center.x) < 1) {
			dx = x - center.x;
		}

		if (Math.abs(y - center.y) < 1) {
			dy = y - center.y;
		}

		move(dx, dy);
	}

	@Override
	public float x(){
		return center.x;
	}

	@Override
	public float y(){
		return center.y;
	}

    public void setArrowAngle(double angleDegrees) {
        arrowAngle = Math.toRadians(angleDegrees); // Set the arrow angle
        repaint(); // Redraw the arrow
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(1));


		// Calculate arrow end point
		int arrowEndX = (int) center.x - (int) (arrowLength / 2 * Math.cos(arrowAngle));
		int arrowEndY = (int) center.y + (int) (arrowLength / 2 * Math.sin(arrowAngle));

		int arrowHeadX = (int) center.x + (int) (arrowLength / 2 * Math.cos(arrowAngle));
		int arrowHeadY = (int) center.y - (int) (arrowLength / 2 * Math.sin(arrowAngle));

		g2d.drawLine(arrowEndX, arrowEndY, arrowHeadX, arrowHeadY); // Draw the arrow line

		// Draw the arrowhead
		int arrowHeadLength = 10; // Length of the arrowhead
		double arrowHeadAngle = Math.toRadians(30); // Angle of the arrowhead

		int arrowHeadX1 = arrowEndX + (int) (arrowHeadLength * Math.cos(arrowAngle - arrowHeadAngle));
		int arrowHeadY1 = arrowEndY - (int) (arrowHeadLength * Math.sin(arrowAngle - arrowHeadAngle));

		int arrowHeadX2 = arrowEndX + (int) (arrowHeadLength * Math.cos(arrowAngle + arrowHeadAngle));
		int arrowHeadY2 = arrowEndY - (int) (arrowHeadLength * Math.sin(arrowAngle + arrowHeadAngle));

		g2d.drawLine(arrowEndX, arrowEndY, arrowHeadX1, arrowHeadY1);
		g2d.drawLine(arrowEndX, arrowEndY, arrowHeadX2, arrowHeadY2);
	}

}