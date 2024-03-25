import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

	// variables
	private APArray apArray;
	HashMap<Integer, JShape> mapArrayCast; // key: id, value: shape

	public DSV() {
		initVariable();
		initAnimationArea();
		initDropdownArea();
		initArrayPanel();
		initQueuePanel();
		initStackPanel();
		initLinkedListPanel();
		initTreePanel();
		initGraphPanel();
		initFrame();
	}

	private void initVariable() {
		apArray = new APArray(RECT_ANIMATION);
		mapArrayCast = new HashMap<Integer, JShape>();
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
					
					// clear the panAnimation
					clearPanAnimation();

					// add new components
					String[] szNumbers = szDefault.replaceAll("[^0-9]+", " ").trim().split("\\s+");
					int[] nNumbers = Arrays.stream(szNumbers).mapToInt(Integer::parseInt).toArray();
					Script script = apArray.initArray(nNumbers);
					Movie clip = readScript(script);
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
					Script script = apArray.modifyArray(nIndex, nNumber);
					Movie clip = readScript(script);
					runMovie(clip);
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

	private void clearPanAnimation(){
		panAnimation.removeAll();
		panAnimation.repaint();

		mapArrayCast.clear();
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

			Clip clip = clips.get(i);

			switch(clip.action){
				case ADD:
					runAdd(clip.id, clip.shape);
					runMoveTo(clip.id, clip.shape, clip.end);
				break;
				case DELETE:
					runDelete(clip.id, clip.shape);
				break;
				default:
			}

		}
	}

	private void runMoveTo(int id, JShape shape, Point destination){

		Timer clipTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// shape.move(2, 2);
				shape.moveto((float)destination.getX(), (float)destination.getY());
	
				if (shape.x() == destination.getX() && shape.y() == destination.getY()) {
					// Stop the timer for this specific clip
					((Timer) e.getSource()).stop();
				}
	
				panAnimation.repaint(); // Purpose: clear the panel
			}
		});
	
		// Start the timer for this clip
		clipTimer.start();
	}

	private void runAdd(int id, JShape shape) {
		
		// add to panAnimation
		panAnimation.add(shape);

		// add to map
		mapArrayCast.put(id, shape);

		panAnimation.repaint();
	}

	private void runDelete(int id, JShape shape){

		// delete from panAnimation
		panAnimation.remove(shape);

		// delete from map
		mapArrayCast.remove(id);

		panAnimation.repaint();
	}

	private Movie readScript(Script script){

		Movie movie = new Movie();
		
		for (int i = 0; i < script.SceneSize(); i++) {

			Clip clip = new Clip();
		
			Scene scene = script.getScene(i);
			
			if(mapArrayCast.containsKey(scene.id)){
				clip.shape = mapArrayCast.get(scene.id);
			}else{
				switch (scene.shape) {
					case SQUARE:
						clip.shape = new JSquare((int) scene.start.getX(), (int) scene.start.getY());
					break;
					case CIRCLE:
						clip.shape = new JCircle((int) scene.start.getX(), (int) scene.start.getY(), scene.txt);
					break;
					default:
						clip.shape = null;
				}
			}

			clip.id = scene.id;
			clip.action = scene.action;
			clip.start = scene.start;
			clip.end = scene.end;
		
			movie.add(clip);
		}
		return movie;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DSV();
		});
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
	public int id;
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
