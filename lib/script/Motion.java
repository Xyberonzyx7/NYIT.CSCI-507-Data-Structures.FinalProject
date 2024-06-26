package lib.script;

import java.awt.Color;
import java.awt.Point;

public class Motion {
	public Point movefrom = null; 	// whole obj moving from this point
	public Point moveto = null;		// whole obj moving to this point
	public int lengthto = 50;		// extend the shape: such as arrow's length
	public double rotateto = 180;		// rorate the shape: such as arrow's rotation
	public Color colorto = Color.BLUE;	// change obj's color
	public Point start = null;		// obj shape start: such as arrow's bottom
	public Point end = null;		// obj shape end: such as arrow's head
	public double angle = 0;			// obj shape starting angle
									// 0 degree: <--
									// 90 degree: pointing down
									// 180 degree: -->
									// 270 degree: pointing up
	public String showtext = "";	// text put on the obj
	public int delaystart = 0;		// delay start for current motion in millisecond
}
