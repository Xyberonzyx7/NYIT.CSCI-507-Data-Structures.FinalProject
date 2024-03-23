package lib.components;

import javax.swing.*;
import java.awt.*;

public class JShape extends JPanel {

	private final Rectangle RECT_ANIMATION = new Rectangle(0, 0, 1000, 770);

	public JShape(){
		setBackground(new Color(0, 0, 0, 0));
		setBounds(RECT_ANIMATION);
	}
}

