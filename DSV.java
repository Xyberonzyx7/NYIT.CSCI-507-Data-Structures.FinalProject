import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import lib.components.*;
import lib.script.Scene;
import lib.script.EAction;
import lib.script.Script;
import lib.ap.*;

public class DSV {

	// general setting
	private final Rectangle RECT_SCREEN = new Rectangle(0, 0, 1200, 800);
	private final Rectangle RECT_ANIMATION = new Rectangle(0, 0, 1000, 770);
	private final Rectangle RECT_DROPDOWN = new Rectangle(1000, 0, 200, 30);
	private final Rectangle RECT_OPERATION = new Rectangle(1000, 30, 200, 770);

	private final Font TITLEFONT = new Font("Arial", Font.BOLD, 14);

	// main components
	private JFrame frame;
	private JPanel panAnimation;
	private JPanel panDropdown;
	private JPanel panOPCurrent;
	private JPanel panOPArray;
	private JPanel panOPQueue;
	private JPanel panOPStack;
	private JPanel panOPLinkedList;
	private JPanel panOPTree;
	private JPanel panOPGraph;

	// Animation Planner
	private APArray apArray;

	public DSV() {
		initAnimationArea();
		initDropdownArea();
		initArrayPanel();
		initQueuePanel();
		initStackPanel();
		initLinkedListPanel();
		initTreePanel();
		initGraphPanel();
		initFrame();
		initAP();
	}

	private void initAP() {
		apArray = new APArray(RECT_ANIMATION);
	}

	private void initFrame() {

		// frame
		frame = new JFrame("Data Structure Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(RECT_SCREEN);
		frame.setLayout(null);

		// Add the animation panel to the frame
		frame.add(panAnimation);
		frame.add(panDropdown);
		frame.add(panOPArray);
		frame.add(panOPQueue);
		frame.add(panOPStack);
		frame.add(panOPLinkedList);
		frame.add(panOPTree);
		frame.add(panOPGraph);

		frame.setVisible(true);
	}

	private void initAnimationArea() {

		// animation area
		panAnimation = new JPanel();
		panAnimation.setLayout(null); // Set layout to null to freely position components
		panAnimation.setBounds(RECT_ANIMATION);
	}

	private void initDropdownArea() {

		// new panel
		panDropdown = new JPanel();
		panDropdown.setLayout(null);
		panDropdown.setBounds(RECT_DROPDOWN);

		// new components
		String[] choices = { "Array", "Queue", "Stack", "Linked List", "Tree", "Graph" };
		JComboBox<String> comboBox = new JComboBox<>(choices);
		comboBox.setBounds(2, 2, 180, 28);
		comboBox.setLightWeightPopupEnabled(false); // fix showing two dropdown list
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSelectedPanel(EOPPanel.values()[comboBox.getSelectedIndex()]);
			}
		});

		// add to panel
		panDropdown.add(comboBox);
	}

	private void initArrayPanel() {

		// new panel
		panOPArray = new JPanel();
		panOPArray.setLayout(null);
		panOPArray.setBounds(RECT_OPERATION);
		panOPArray.setVisible(true);

		// new components
		AutoLayout autolayout = new AutoLayout();
		JLabel lb_default = new JLabel("Default Array (int[])");
		JLabel lb_modification = new JLabel("Modification");
		JLabel lb_index = new JLabel("Index");
		JLabel lb_num = new JLabel("Number");
		JPlaceholderTextArea ta_default = new JPlaceholderTextArea();
		JPlaceholderTextField tf_index = new JPlaceholderTextField();
		JPlaceholderTextField tf_num = new JPlaceholderTextField();
		JButton btn_create = new JButton("Create");
		JButton btn_modify = new JButton("Modify");
		lb_default.setFont(TITLEFONT);
		ta_default.setText("[1,2,3,4]");
		lb_modification.setFont(TITLEFONT);
		tf_index.setPlaceholder("e.g. 0");
		tf_num.setPlaceholder("e.g. 10");
		btn_create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szDefault = ta_default.getText();

				if (szDefault.isEmpty()) {
					popHint("Default array is not valid.");
					return;
				}

				try {
					String[] szNumbers = szDefault.replaceAll("[^0-9]+", " ").trim().split("\\s+");
					int[] nNumbers = Arrays.stream(szNumbers).mapToInt(Integer::parseInt).toArray();
					Script script = apArray.initArray(nNumbers);
					ScriptInterpreter si = new ScriptInterpreter();
					Movie clip = si.read(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Default array is not valid.");
					return;
				}

			}
		});
		btn_modify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szIndex = tf_index.getText();
				String szNumber = tf_num.getText();

				if (szIndex.isEmpty() || szNumber.isEmpty()) {
					popHint("Index or Number is not valid.");
					return;
				}

				try {
					int nIndex = Integer.parseInt(szIndex);
					int nNumber = Integer.parseInt(szNumber);
				} catch (NumberFormatException exception) {
					popHint("Index or Number is not valid.");
					return;
				}
			}
		});

		autolayout.setBounds();
		autolayout.setBounds(lb_default);
		autolayout.setBounds(ta_default, 250);
		autolayout.setBounds(btn_create);
		autolayout.setBounds();
		autolayout.setBounds(lb_modification);
		autolayout.setBounds(lb_index);
		autolayout.setBounds(tf_index);
		autolayout.setBounds(lb_num);
		autolayout.setBounds(tf_num);
		autolayout.setBounds(btn_modify);

		// init : [1,2,3,4]

		// add components
		panOPArray.add(lb_default);
		panOPArray.add(ta_default);
		panOPArray.add(lb_modification);
		panOPArray.add(lb_index);
		panOPArray.add(tf_index);
		panOPArray.add(lb_num);
		panOPArray.add(tf_num);
		panOPArray.add(btn_create);
		panOPArray.add(btn_modify);

		panOPCurrent = panOPArray;
	}

	private void initQueuePanel() {
		panOPQueue = new JPanel();
		panOPQueue.setLayout(null);
		panOPQueue.setBounds(RECT_OPERATION);
		panOPQueue.setVisible(false);
	}

	private void initStackPanel() {
		panOPStack = new JPanel();
		panOPStack.setLayout(null);
		panOPStack.setBounds(1000, 30, 200, 770);
		panOPStack.setVisible(false);
	}

	private void initLinkedListPanel() {
		panOPLinkedList = new JPanel();
		panOPLinkedList.setLayout(null);
		panOPLinkedList.setBounds(RECT_OPERATION);
		panOPLinkedList.setVisible(false);
	}

	private void initTreePanel() {
		panOPTree = new JPanel();
		panOPTree.setLayout(null);
		panOPTree.setBounds(1000, 30, 200, 770);
		panOPTree.setVisible(false);
	}

	private void initGraphPanel() {
		panOPGraph = new JPanel();
		panOPGraph.setLayout(null);
		panOPGraph.setBounds(1000, 30, 200, 770);
		panOPGraph.setVisible(false);
	}

	private void showSelectedPanel(EOPPanel panel) {
		switch (panel) {
			case ARRAY:
				panOPArray.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPArray;
				break;
			case QUEUE:
				panOPQueue.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPQueue;
				break;
			case STACK:
				panOPStack.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPStack;
				break;
			case LINKEDLIST:
				panOPLinkedList.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPLinkedList;
				break;
			case TREE:
				panOPTree.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPTree;
				break;
			case GRAPH:
				panOPGraph.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPGraph;
				break;
			default:
				panOPCurrent.setVisible(true);
				break;
		}
	}

	private void popHint(String szMsg) {
		JOptionPane.showMessageDialog(null, szMsg, "Hint", JOptionPane.INFORMATION_MESSAGE);
	}

	private enum EOPPanel {
		ARRAY,
		QUEUE,
		STACK,
		LINKEDLIST,
		TREE,
		GRAPH
	}

	private void runMovie(Movie movie) {
		List<Clip> clips = movie.getClips();

		for (int i = 0; i < clips.size(); i++) {
			JShape shape = clips.get(i).shape;
			int destinationX = (int) clips.get(i).end.getX();
			int destinationY = (int) clips.get(i).end.getY();

			panAnimation.add(shape);

			Timer clipTimer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// shape.move(2, 2);
					shape.moveto(destinationX, destinationY);
					
					if (shape.x() == destinationX) {
						// Stop the timer for this specific clip
						((Timer) e.getSource()).stop();
					}

					panAnimation.repaint(); // Purpose: clear the panel
				}
			});

			// Start the timer for this clip
			clipTimer.start();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DSV();
		});
	}
}

class ScriptInterpreter {

	public Movie read(Script script) {

		Movie movie = new Movie();

		for (int i = 0; i < script.size(); i++) {
			Clip clip = new Clip();

			Scene scene = script.get(i);

			// get name
			String szName = scene.szName;
			clip.szName = szName;

			// get object
			JShape shape;
			switch (scene.shape) {
				case SQUARE:
					shape = new JSquare((int) scene.start.getX(), (int) scene.start.getY());
					break;
				case CIRCLE:
					shape = new JCircle((int) scene.start.getX(), (int) scene.start.getY(), scene.txt);
					break;
				default:
					shape = null;
			}
			clip.shape = shape;

			clip.action = scene.action;
			clip.start = scene.start;
			clip.end = scene.end;

			movie.add(clip);
		}

		return movie;
	}
}

class Movie {
	private List<Clip> clips;

	public Movie() {
		clips = new ArrayList<>();
	}

	public void add(Clip clip) {
		this.clips.add(clip);
	}

	public List<Clip> getClips() {
		return this.clips;
	}
}

class Clip {
	public String szName;
	JShape shape;
	EAction action;
	Point start;
	Point end;
}

class AutoLayout {
	private final int X = 2;
	private int y;
	private final int W = 180;
	private final int H = 30;

	public AutoLayout() {
		this.y = 2;
	}

	public void setBounds() {
		this.y = this.y + 2 + this.H / 2;
	}

	public void setBounds(JComponent component) {
		setBounds(component, this.H);
	}

	public void setBounds(JComponent component, int height) {
		component.setBounds(this.X, this.y, this.W, height);
		this.y = y + 2 + height;
	}
}
