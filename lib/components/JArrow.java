package lib.components;

import javax.swing.*;
import java.awt.*;

public class JArrow extends JPanel {
    private int arrowLength = 50; // Initial length of the arrow
    private double arrowAngle = Math.toRadians(45); // Initial angle (45 degrees)

    public void extendArrow() {
        arrowLength += 10; // Increase the length
        repaint(); // Redraw the arrow
    }

    public void shrinkArrow() {
        arrowLength -= 10; // Decrease the length
        repaint(); // Redraw the arrow
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

		int cx = getWidth() / 2;
		int cy = getHeight() / 2;

		// Calculate arrow end point
		int arrowEndX = cx + (int) (arrowLength * Math.cos(arrowAngle));
		int arrowEndY = cy - (int) (arrowLength * Math.sin(arrowAngle));

		g2d.drawLine(cx, cy, arrowEndX, arrowEndY); // Draw the arrow line

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
