package lib.script;

import java.awt.Color;
import java.awt.Point;

public class Scene {
	public int id;
	public EShape shape;
	public EAction action;
	public Point movefrom;
	public Point moveto;
	public int extendto;
	public int shrinkto;
	public Color colorto;
	public double rotateto;
	public Point start;
	public Point end;
	public double angle;
	public String showtext;
	public int delaystart; // in ms
}
