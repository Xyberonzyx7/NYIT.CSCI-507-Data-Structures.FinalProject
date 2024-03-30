package lib.components;

import javax.swing.*;
import java.awt.*;

public class JShape extends JPanel {

	private final Rectangle RECT_ANIMATION = new Rectangle(0, 0, 1000, 770);

	public JShape(){
		setBackground(new Color(0, 0, 0, 0));
		setBounds(RECT_ANIMATION);
	}

	public float x(){ return 0;}
	public float y(){ return 0;}
	public float l(){ return 0;}
	public double a(){ return 0;}
	public Color c(){ return Color.BLUE;};

	public void move(float x, float y){}
	public void moveto(float x, float y){}
	public void extend(float dlength){}
	public void extendto(float length){}
	public void shrink(float dlength){}
	public void shrinkto(float length){}
	public void rotate(double dangle){}
	public void rotateto(double angle){}
	public void colorto(Color color){}
}

