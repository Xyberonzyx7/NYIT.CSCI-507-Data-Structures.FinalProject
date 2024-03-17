import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import lib.components.*;

public class DSV {
    private JFrame frame;
    private JArrow arrow;
    private JCircle circle;
	private JSquare square;

    public DSV() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Arrow and Circle Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create an arrow at position (100, 100) with initial length 50
        arrow = new JArrow();
		arrow.setBackground(new Color(0,0,0,0));
        circle = new JCircle(200, 150, 20);
		circle.setBackground(new Color(0, 0, 0, 0));
		square = new JSquare(100, 100, 20, 40);
		square.setBackground(new Color(0, 0,0 ,0));

        // Animation Panel
        JPanel panel = new JPanel();
        panel.setLayout(null); // Set layout to null to freely position components

        // Add arrow and circle to the animation panel
        panel.add(arrow);
        panel.add(circle);
		panel.add(square);

        // Set the bounds of arrow and circle manually
        arrow.setBounds(0, 0, 800,300); // Change the values according to your preference
        circle.setBounds(0,0, 800, 300); // Change the values according to your preference
		square.setBounds(0, 0, 800, 300);

        // Add the animation panel to the frame
        frame.add(panel);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Extend the arrow
                arrow.extendArrow();

                // Move the circle horizontally (for demonstration purposes)
                circle.move(10, 0);

				// move the square
				square.move(0, 0);

                panel.repaint();
            }
        });
        timer.start();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DSV();
        });
    }
}
