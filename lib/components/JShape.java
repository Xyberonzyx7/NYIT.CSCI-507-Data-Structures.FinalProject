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

	public void move(float x, float y){}
	public void moveto(float x, float y){}
}
