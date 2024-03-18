import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import lib.components.*;

public class DSV {
	private JFrame frame;
	private JPanel pan_animation;
	private JPanel pan_dropdown;
	private JPanel pan_op_current;
	private JPanel pan_op_array;
	private JPanel pan_op_queue;
	private JPanel pan_op_stack;
	private JPanel pan_op_linkedlist;
	private JPanel pan_op_tree;
	private JPanel pan_op_graph;
	private JArrow arrow;
	private JCircle circle;
	private JSquare square;

	public DSV() {
		initialize();
	}

	private void initialize() {

		int screenWidth = 1200;
		int screenHeight = 800;

		// frame
		frame = new JFrame("Data Structure Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenWidth, screenHeight);
		frame.setLayout(null);

		// animation area
		pan_animation = new JPanel();
		pan_animation.setLayout(null); // Set layout to null to freely position components
		pan_animation.setBounds(0, 0, 1000, 800);

		// dropdown area
		pan_dropdown = new JPanel();
		pan_dropdown.setLayout(null);
		pan_dropdown.setBounds(1000, 0, 200, 30);

		// operation area
		pan_op_array = new JPanel();
		pan_op_array.setLayout(null);
		pan_op_array.setBounds(1000, 30, 200, 770);
		pan_op_array.setVisible(true);
		
		pan_op_queue = new JPanel();
		pan_op_queue.setLayout(null);
		pan_op_queue.setBounds(1000, 30, 200, 770);
		pan_op_queue.setVisible(false);
		
		pan_op_stack = new JPanel();
		pan_op_stack.setLayout(null);
		pan_op_stack.setBounds(1000, 30, 200, 770);
		pan_op_stack.setVisible(false);
		
		pan_op_linkedlist = new JPanel();
		pan_op_linkedlist.setLayout(null);
		pan_op_linkedlist.setBounds(1000, 30, 200, 770);
		pan_op_linkedlist.setVisible(false);

		pan_op_tree = new JPanel();
		pan_op_tree.setLayout(null);
		pan_op_tree.setBounds(1000, 30, 200, 770);
		pan_op_tree.setVisible(false);

		pan_op_graph = new JPanel();
		pan_op_graph.setLayout(null);
		pan_op_graph.setBounds(1000, 30, 200, 770);
		pan_op_graph.setVisible(false);
		
		pan_op_current = pan_op_array;
		
		// components (position related to its container)
		arrow = new JArrow();
		arrow.setBackground(new Color(0, 0, 0, 0));
		arrow.setBounds(0, 0, screenWidth, screenHeight); // Change the values according to your preference
		circle = new JCircle(200, 150, 20);
		circle.setBackground(new Color(0, 0, 0, 0));
		circle.setBounds(0, 0, screenWidth, screenHeight); // Change the values according to your preference
		square = new JSquare(100, 100, 20, 40);
		square.setBackground(new Color(0, 0, 0, 0));
		square.setBounds(0, 0, screenWidth, screenHeight);
		
		String[] choices = { "Array", "Queue", "Stack", "Linked List", "Tree", "Graph" };
		JComboBox<String> comboBox = new JComboBox<>(choices);
		comboBox.setBounds(2, 2, 180, 25);
		comboBox.setLightWeightPopupEnabled(false); // fix showing two dropdown list
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				showSelectedPanel(EOPPanel.values()[comboBox.getSelectedIndex()]);
			}
		});
		

		// add components to repective area
		pan_animation.add(arrow);
		pan_animation.add(circle);
		pan_animation.add(square);
		pan_dropdown.add(comboBox);


		// Add the animation panel to the frame
		frame.add(pan_animation);
		frame.add(pan_dropdown);
		frame.add(pan_op_array);
		frame.add(pan_op_queue);
		frame.add(pan_op_stack);
		frame.add(pan_op_linkedlist);
		frame.add(pan_op_tree);
		frame.add(pan_op_graph);

		Timer timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Extend the arrow
				arrow.extendArrow();

				// Move the circle horizontally (for demonstration purposes)
				circle.move(10, 0);

				// move the square
				square.move(0, 0);

				pan_animation.repaint();
			}
		});
		timer.start();

		frame.setVisible(true);
	}

	private void showSelectedPanel(EOPPanel panel){
		switch(panel){
			case ARRAY:
				pan_op_array.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_array;
				break;
			case QUEUE:
				pan_op_queue.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_queue;
				break;
			case STACK:
				pan_op_stack.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_stack;
				break;
			case LINKEDLIST:
				pan_op_linkedlist.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_linkedlist;
				break;
			case TREE:
				pan_op_tree.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_tree;
				break;
			case GRAPH:
				pan_op_graph.setVisible(true);
				pan_op_current.setVisible(false);
				pan_op_current = pan_op_graph;
				break;
			default:
				

		}

	}

	private enum EOPPanel{
		ARRAY,
		QUEUE,
		STACK,
		LINKEDLIST,
		TREE,
		GRAPH
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DSV();
		});
	}
}
