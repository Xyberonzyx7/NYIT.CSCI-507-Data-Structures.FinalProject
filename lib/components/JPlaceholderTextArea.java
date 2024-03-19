package lib.components;

import javax.swing.*;
import java.awt.*;

public class JPlaceholderTextArea extends JTextArea {
    private String placeholder;

    public JPlaceholderTextArea() {
    }

    public JPlaceholderTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
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