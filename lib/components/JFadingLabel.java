package lib.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFadingLabel extends JLabel {
    private static final int FADE_DURATION_MS = 5000; // Duration of the fade (in milliseconds)
    private static final int NUM_FADE_STEPS = 20; // Number of steps for the fade animation

    public JFadingLabel(String text) {
        super(text);
        setFont(new Font("Arial", Font.PLAIN, 14));
    }

	@Override
	public void setText(String text) {
		super.setText(text);
	}

    public void startFade() {
        float initialAlpha = getForeground().getAlpha() / 255.0f;
        float step = initialAlpha / NUM_FADE_STEPS;

        Timer timer = new Timer(FADE_DURATION_MS / NUM_FADE_STEPS, null);
        timer.addActionListener(new ActionListener() {
            private int stepCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                float newAlpha = initialAlpha - step * stepCount;
                setForeground(new Color(getForeground().getRed(),
                        getForeground().getGreen(),
                        getForeground().getBlue(),
                        (int) (newAlpha * 255)));
                repaint();

                stepCount++;
                if (stepCount >= NUM_FADE_STEPS) {
					setText("");
                    timer.stop();
                }
            }
        });
        timer.start();
    }
}
