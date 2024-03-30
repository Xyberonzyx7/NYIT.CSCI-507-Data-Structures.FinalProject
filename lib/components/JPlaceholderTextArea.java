package lib.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JPlaceholderTextArea extends JTextArea {
    private String placeholder;

    public JPlaceholderTextArea() {
		super();
		init();
    }
	
	public void init(){
		// Override tab key behavior in JTextArea
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
					manager.focusNextComponent();
                    e.consume(); // Prevent default tab behavior
                }
            }
        });
		this.setFocusTraversalKeysEnabled(true);
	}

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (placeholder != null && getText().isEmpty()) {
            g.setColor(new Color(170, 170, 170));
            g.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
        }
    }
}