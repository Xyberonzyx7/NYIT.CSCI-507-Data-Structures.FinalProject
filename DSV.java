import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.concurrent.*;

import lib.animation.*;
import lib.components.*;
import lib.script.Scene;
import lib.script.EAction;
import lib.script.Script;

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
	private JPanel panOPBinarySearchTree;
	private JPanel panOPGraph;

	// variables
	private APArray apArray;
	private APStack apStack;
	private APQueue apQueue;
	private APLinkedList apLinkedList;
	private APBinarySearchTree apBinarySearchTree;
	HashMap<Integer, JShape> mapArrayCast; // key: id, value: shape

	public DSV() {
		initVariable();
		initAnimationArea();
		initDropdownArea();
		initArrayPanel();
		initStackPanel();
		initQueuePanel();
		initLinkedListPanel();
		initBinarySearchTreePanel();
		initGraphPanel();
		initFrame();
	}

	private void initVariable() {
		apArray = new APArray(RECT_ANIMATION);
		apStack = new APStack(RECT_ANIMATION);
		apQueue = new APQueue(RECT_ANIMATION);
		apLinkedList = new APLinkedList(RECT_ANIMATION);
		apBinarySearchTree = new APBinarySearchTree(RECT_ANIMATION);
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
		frame.add(panOPStack);
		frame.add(panOPQueue);
		frame.add(panOPLinkedList);
		frame.add(panOPBinarySearchTree);
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
		String[] choices = Arrays.stream(
			EOPPanel.values())
			.map(Enum::name)
			.collect(Collectors.toList())
			.toArray(new String[0]);
		JComboBox<String> comboBox = new JComboBox<>(choices);
		comboBox.setBounds(2, 2, 180, 28);
		comboBox.setLightWeightPopupEnabled(false); // fix showing two dropdown list
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// show selected panel
				showSelectedPanel(EOPPanel.values()[comboBox.getSelectedIndex()]);

				// clear panAnimation
				clearPanAnimation();
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
		ta_default.setPlaceholder("e.g. [1,2,3,4]");
		ta_default.setLineWrap(true);
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
		autolayout.setBounds(ta_default, 200);
		autolayout.setBounds(btn_create);
		autolayout.setBounds();
		autolayout.setBounds(lb_modification);
		autolayout.setBounds(lb_index);
		autolayout.setBounds(tf_index);
		autolayout.setBounds(lb_num);
		autolayout.setBounds(tf_num);
		autolayout.setBounds(btn_modify);

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

	private void initStackPanel() {
		panOPStack = new JPanel();
		panOPStack.setLayout(null);
		panOPStack.setBounds(RECT_OPERATION);
		panOPStack.setVisible(false);

		AutoLayout autoLayout = new AutoLayout();
		JLabel lb_size = new JLabel("Size of Stack");
		JPlaceholderTextField tf_size = new JPlaceholderTextField();
		JButton btn_init = new JButton("Initialize");
		JLabel lb_push = new JLabel("Push");
		JPlaceholderTextField tf_push = new JPlaceholderTextField();
		JButton btn_push = new JButton("Push");
		JLabel lb_pop = new JLabel("Pop");
		JButton btn_pop = new JButton("Pop");
		lb_size.setFont(TITLEFONT);
		tf_size.setPlaceholder("e.g. 5");
		lb_push.setFont(TITLEFONT);
		tf_push.setPlaceholder("e.g. 0");
		lb_pop.setFont(TITLEFONT);
		btn_init.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String szSize = tf_size.getText();
				if(szSize.isEmpty()){
					popHint("Size is not valid.");
					return;
				}

				try{
					// clear the panAnimation
					clearPanAnimation();

					// add new components
					Script script = apStack.initStack(Integer.parseInt(szSize));
					Movie clip = readScript(script);
					runMovie(clip);
				}catch(NumberFormatException exception){
					popHint("Size is not valid.");
					return;
				}
			}
		});
		btn_push.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String szPush = tf_push.getText();
				if(szPush.isEmpty()){
					popHint("Value is not valid.");
					return;
				}

				try{
					// add new components
					Script script = apStack.push(Integer.parseInt(szPush));
					Movie clip = readScript(script);
					runMovie(clip);
				}catch(NumberFormatException exception){
					popHint("Value is not valid.");
					return;
				}
			}
		});
		btn_pop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){

				// add new components
				Script script = apStack.pop();
				Movie clip = readScript(script);
				runMovie(clip);
			}
		});

		autoLayout.setBounds();
		autoLayout.setBounds(lb_size);
		autoLayout.setBounds(tf_size);
		autoLayout.setBounds(btn_init);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_push);
		autoLayout.setBounds(tf_push);
		autoLayout.setBounds(btn_push);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_pop);
		autoLayout.setBounds(btn_pop);

		// add components
		panOPStack.add(lb_size);
		panOPStack.add(tf_size);
		panOPStack.add(btn_init);
		panOPStack.add(lb_push);
		panOPStack.add(tf_push);
		panOPStack.add(btn_push);
		panOPStack.add(lb_pop);
		panOPStack.add(btn_pop);
	}

	private void initQueuePanel() {
		panOPQueue = new JPanel();
		panOPQueue.setLayout(null);
		panOPQueue.setBounds(RECT_OPERATION);
		panOPQueue.setVisible(false);

		// new components
		AutoLayout autoLayout = new AutoLayout();
		JLabel lb_size = new JLabel("Size of Queue");
		JPlaceholderTextField tf_size = new JPlaceholderTextField();
		JButton btn_init = new JButton("Initialize");
		JLabel lb_enqueue = new JLabel("Enqueue");
		JPlaceholderTextField tf_enqueue = new JPlaceholderTextField();
		JButton btn_enqueue = new JButton("Enqueue");
		JLabel lb_dequeue = new JLabel("Dequeue");
		JButton btn_dequeue = new JButton("Dequeue");
		lb_size.setFont(TITLEFONT);
		tf_size.setPlaceholder("e.g. 5");
		lb_enqueue.setFont(TITLEFONT);
		tf_enqueue.setPlaceholder("e.g. 0");
		lb_dequeue.setFont(TITLEFONT);
		btn_init.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String szSize = tf_size.getText();
				if(szSize.isEmpty()){
					popHint("Size is not valid.");
					return;
				}

				try{
					// clear the panAnimation
					clearPanAnimation();

					// add new components
					Script script = apQueue.initQueue(Integer.parseInt(szSize));
					Movie clip = readScript(script);
					runMovie(clip);
				}catch(NumberFormatException exception){
					popHint("Size is not valid.");
					return;
				}
			}
		});
		btn_enqueue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szPush = tf_enqueue.getText();
				if (szPush.isEmpty()) {
					popHint("Value is not valid.");
					return;
				}

				try {
					// add new components
					Script script = apQueue.enqueue(Integer.parseInt(szPush));
					Movie clip = readScript(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Value is not valid.");
					return;
				}
			}
		});
		btn_dequeue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// add new components
				Script script = apQueue.dequeue();
				Movie clip = readScript(script);
				runMovie(clip);
			}
		});
		autoLayout.setBounds();
		autoLayout.setBounds(lb_size);
		autoLayout.setBounds(tf_size);
		autoLayout.setBounds(btn_init);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_enqueue);
		autoLayout.setBounds(tf_enqueue);
		autoLayout.setBounds(btn_enqueue);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_dequeue);
		autoLayout.setBounds(btn_dequeue);

		// add components
		panOPQueue.add(lb_size);
		panOPQueue.add(tf_size);
		panOPQueue.add(btn_init);
		panOPQueue.add(lb_enqueue);
		panOPQueue.add(tf_enqueue);
		panOPQueue.add(btn_enqueue);
		panOPQueue.add(lb_dequeue);
		panOPQueue.add(btn_dequeue);
	}

	private void initLinkedListPanel() {
		panOPLinkedList = new JPanel();
		panOPLinkedList.setLayout(null);
		panOPLinkedList.setBounds(RECT_OPERATION);
		panOPLinkedList.setVisible(false);

		AutoLayout autoLayout = new AutoLayout();
		JLabel lb_init = new JLabel("Initialize");
		JPlaceholderTextArea ta_init = new JPlaceholderTextArea();
		JButton btn_init = new JButton("Initialize");
		JLabel lb_insertAt = new JLabel("Insert At");
		JLabel lb_insertIndex = new JLabel("Index");
		JPlaceholderTextField tf_index = new JPlaceholderTextField();
		JLabel lb_data = new JLabel("Data");
		JPlaceholderTextField tf_data = new JPlaceholderTextField();
		JButton btn_insert = new JButton("Insert");
		JLabel lb_removeAt = new JLabel("Remove At");
		JLabel lb_removeIndex = new JLabel("index");
		JPlaceholderTextField tf_remove = new JPlaceholderTextField();
		JButton btn_remove = new JButton("Remove");
		lb_init.setFont(TITLEFONT);
		lb_insertAt.setFont(TITLEFONT);
		lb_removeAt.setFont(TITLEFONT);
		ta_init.setText("[1,2,3,4]");
		ta_init.setPlaceholder("e.g. [1,2,3,4]");
		tf_index.setPlaceholder("e.g. 0");
		tf_data.setPlaceholder("e.g. 100");
		tf_remove.setPlaceholder("e.g. 1");
		btn_init.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szInit = ta_init.getText();

				if (szInit.isEmpty()) {
					popHint("Default array is not valid.");
					return;
				}

				try {
					
					// clear the panAnimation
					clearPanAnimation();

					// add new components
					String[] szNumbers = szInit.replaceAll("[^0-9]+", " ").trim().split("\\s+");
					int[] nNumbers = Arrays.stream(szNumbers).mapToInt(Integer::parseInt).toArray();
					Script script = apLinkedList.initLinkedList(nNumbers);
					Movie clip = readScript(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Default array is not valid.");
					return;
				}

			}
		});
		btn_insert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szIndex = tf_index.getText();
				String szData = tf_data.getText();

				if (szIndex.isEmpty() || szData.isEmpty()) {
					popHint("Index or Data is not valid.");
					return;
				}

				try {
					// add new components
					Script script = apLinkedList.insertAt(Integer.parseInt(szIndex), Integer.parseInt(szData));
					Movie clip = readScript(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Index or Data is not valid.");
					return;
				}

			}
		});
		btn_remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szRemove = tf_remove.getText();

				if (szRemove.isEmpty()) {
					popHint("Remove index is not valid.");
					return;
				}

				try {
					// add new components
					Script script = apLinkedList.removeAt(Integer.parseInt(szRemove));
					Movie clip = readScript(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Remove index is not valid.");
					return;
				}

			}
		});
		autoLayout.setBounds();
		autoLayout.setBounds(lb_init);
		autoLayout.setBounds(ta_init, 200);
		autoLayout.setBounds(btn_init);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_insertAt);
		autoLayout.setBounds(lb_insertIndex);
		autoLayout.setBounds(tf_index);
		autoLayout.setBounds(lb_data);
		autoLayout.setBounds(tf_data);
		autoLayout.setBounds(btn_insert);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_removeAt);
		autoLayout.setBounds(lb_removeIndex);
		autoLayout.setBounds(tf_remove);
		autoLayout.setBounds(btn_remove);

		// add components
		panOPLinkedList.add(lb_init);
		panOPLinkedList.add(ta_init);
		panOPLinkedList.add(btn_init);
		panOPLinkedList.add(lb_insertAt);
		panOPLinkedList.add(lb_insertIndex);
		panOPLinkedList.add(tf_index);
		panOPLinkedList.add(lb_data);
		panOPLinkedList.add(tf_data);
		panOPLinkedList.add(btn_insert);
		panOPLinkedList.add(lb_removeAt);
		panOPLinkedList.add(lb_removeIndex);
		panOPLinkedList.add(tf_remove);
		panOPLinkedList.add(btn_remove);
	}

	private void initBinarySearchTreePanel() {
		panOPBinarySearchTree = new JPanel();
		panOPBinarySearchTree.setLayout(null);
		panOPBinarySearchTree.setBounds(RECT_OPERATION);
		panOPBinarySearchTree.setVisible(false);

		// new components
		AutoLayout autoLayout = new AutoLayout();
		JLabel lb_init = new JLabel("Initialize");
		JPlaceholderTextArea ta_init = new JPlaceholderTextArea();
		JButton btn_init = new JButton("Initialize");
		JLabel lb_add = new JLabel("Add");
		JPlaceholderTextField tf_add = new JPlaceholderTextField();
		JButton btn_add = new JButton("Add");
		JLabel lb_delete = new JLabel("Delete");
		JPlaceholderTextField tf_delete = new JPlaceholderTextField();
		JButton btn_delete = new JButton("Delete");
		lb_init.setFont(TITLEFONT);
		ta_init.setText("[47, 32, 63, 19, 41, 55, 79, 10, 23, 37, 44, 53, 59, 70, 96, 7, 12, 20, 30, 34, 38, 43, 45, 52, 54, 57, 60, 69, 74, 91, 97]");	
		ta_init.setPlaceholder("[47, 32, 63, 19, 41, 55, 79, 10, 23, 37, 44, 53, 59, 70, 96, 7, 12, 20, 30, 34, 38, 43, 45, 52, 54, 57, 60, 69, 74, 91, 97]");
		lb_add.setFont(TITLEFONT);
		tf_add.setPlaceholder("e.g. 100");
		lb_delete.setFont(TITLEFONT);
		tf_delete.setPlaceholder("e.g. 100");
		btn_init.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String szInit = ta_init.getText();

				if (szInit.isEmpty()) {
					popHint("Default array is not valid.");
					return;
				}

				try {
					
					// clear the panAnimation
					clearPanAnimation();

					// add new components
					String[] szNumbers = szInit.replaceAll("[^0-9]+", " ").trim().split("\\s+");
					int[] nNumbers = Arrays.stream(szNumbers).mapToInt(Integer::parseInt).toArray();
					Script script = apBinarySearchTree.initBinarySearchTree(nNumbers);
					Movie clip = readScript(script);
					runMovie(clip);
				} catch (NumberFormatException exception) {
					popHint("Default array is not valid.");
					return;
				}

			}
		});
		btn_add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String szAdd = tf_add.getText();

				if(szAdd.isEmpty()){
					popHint("Data is not valid.");
					return;
				}

				try{
					Script script = apBinarySearchTree.add(Integer.parseInt(szAdd));
					Movie movie = readScript(script);
					runMovie(movie);
				} catch(NumberFormatException exception){
					popHint("Data is not valid.");
					return;
				}
			}
		});
		btn_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String szDelete = tf_delete.getText();

				if(szDelete.isEmpty()){
					popHint("Data is not valid.");
					return;
				}
			
				try{
					Script script = apBinarySearchTree.delete(Integer.parseInt(szDelete));
					Movie movie = readScript(script);
					runMovie(movie);
				} catch(NumberFormatException exception){
					popHint("Data is not valid.");
					return;
				}
			}
		});
		// auto layout
		autoLayout.setBounds();
		autoLayout.setBounds(lb_init);
		autoLayout.setBounds(ta_init, 200);
		autoLayout.setBounds(btn_init);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_add);
		autoLayout.setBounds(tf_add);
		autoLayout.setBounds(btn_add);
		autoLayout.setBounds();
		autoLayout.setBounds(lb_delete);
		autoLayout.setBounds(tf_delete);
		autoLayout.setBounds(btn_delete);

		// add components
		panOPBinarySearchTree.add(lb_init);
		panOPBinarySearchTree.add(ta_init);
		panOPBinarySearchTree.add(btn_init);
		panOPBinarySearchTree.add(lb_add);
		panOPBinarySearchTree.add(tf_add);
		panOPBinarySearchTree.add(btn_add);
		panOPBinarySearchTree.add(lb_delete);
		panOPBinarySearchTree.add(tf_delete);
		panOPBinarySearchTree.add(btn_delete);
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
			case STACK:
				panOPStack.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPStack;
				break;
			case QUEUE:
				panOPQueue.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPQueue;
				break;
			case LINKEDLIST:
				panOPLinkedList.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPLinkedList;
				break;
			case TREE:
				panOPBinarySearchTree.setVisible(true);
				panOPCurrent.setVisible(false);
				panOPCurrent = panOPBinarySearchTree;
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
		STACK,
		QUEUE,
		LINKEDLIST,
		TREE,
		GRAPH
	}

	private void runMovie(Movie movie) {

		List<Clip> clips = movie.getClips();

		if(clips.size() == 0) return;

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = new Runnable(){

			int i = 0;

			@Override
			public void run(){

				Clip clip = clips.get(i);

				switch(clip.action){
					case ADD:
						runAdd(clip.id, clip.shape);
						runMoveTo(clip.id, clip.shape, clip.moveto);
					break;
					case MOVE:
						runMoveTo(clip.id, clip.shape, clip.moveto);
					break;
					case DELETE:
						// TimerCallback callback = () ->{
						// 	runDelete(clip.id, clip.shape);
						// }
						runMoveTo(clip.id, clip.shape, clip.moveto);
						runDelete(clip.id, clip.shape);
					break;
					case EXTEND:
						runExtendTo(clip.id, clip.shape, clip.extendto);
					break;
					case SHRINK:
						runShrinkTo(clip.id, clip.shape, clip.extendto);
					break;
					case ROTATE:
						runRotateTo(clip.id, clip.shape, clip.rotateto);
					break;
					case COLOR:
						runColorTo(clip.id, clip.shape, clip.colorto);
					break;
					case WAIT:
						// do nothing
					break;
					default:
				}
				i += 1;

				if(i >= clips.size()) executor.shutdown();
				else{
					executor.schedule(this, movie.getClips().get(i).delaystart, TimeUnit.MILLISECONDS);
				}
			}
		};
		executor.schedule(task, movie.getClips().get(0).delaystart, TimeUnit.MILLISECONDS);
	}

	private void runColorTo(int id, JShape shape, Color color){
		
		if(shape.c() == color){
			return;
		}

		Timer clipTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				shape.colorto(color);
				panAnimation.repaint();
				if(shape.c() == color){
					((Timer) e.getSource()).stop();
				}
			}
		});
		clipTimer.start();
	}

	private void runRotateTo(int id, JShape shape, double angle){
		if(shape.a() == angle){
			return;
		}

		Timer clipTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				shape.rotateto(angle);
				panAnimation.repaint();
				if(shape.a() == angle){
					((Timer) e.getSource()).stop();
				}
			}
		});
		clipTimer.start();
	}

	private void runMoveTo(int id, JShape shape, Point destination){

		if(destination == null){
			return;
		}

		Timer clipTimer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// shape.move(2, 2);
				shape.moveto((float)destination.getX(), (float)destination.getY());
				panAnimation.repaint();

				if (shape.x() == destination.getX() && shape.y() == destination.getY()) {
					// Stop the timer for this specific clip
					((Timer) e.getSource()).stop();
				}
			}
		});
	
		// Start the timer for this clip
		clipTimer.start();
	}

	private void runExtendTo(int id, JShape shape, int length){
		Timer clipTimer = new Timer(100, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				shape.extendto(length);
				if(shape.l() == length){
					((Timer) e.getSource()).stop();
				}
			}
		});
		clipTimer.start();
	}

	private void runShrinkTo(int id, JShape shape, int length){
		Timer clipTimer = new Timer(100, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				shape.shrinkto(length);
				if(shape.l() == length){
					((Timer) e.getSource()).stop();
				}
			}
		});
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

		// temporary remember the casts from a same script
		// because before this script is ran, the cast won't be added to the mapArrayCast.
		// This will cause the same cast to be newed more than once if the same cast appears 
		// in the script for more than one time.
		HashMap<Integer, JShape> mapTmpShapeInSameScript = new HashMap<>();
		
		for (int i = 0; i < script.SceneSize(); i++) {

			Clip clip = new Clip();
		
			Scene scene = script.getScene(i);
			
			if(mapArrayCast.containsKey(scene.id)){
				clip.shape = mapArrayCast.get(scene.id);
			} else if(mapTmpShapeInSameScript.containsKey(scene.id)){
				clip.shape = mapTmpShapeInSameScript.get(scene.id);
			} else{
				switch (scene.shape) {
					case SQUARE:
						clip.shape = new JSquare((int) scene.movefrom.getX(), (int) scene.movefrom.getY());
					break;
					case CIRCLE:
						clip.shape = new JCircle((int) scene.movefrom.getX(), (int) scene.movefrom.getY(), scene.showtext);
					break;
					case ARROW:
						clip.shape = new JArrow((int) scene.movefrom.getX(), (int) scene.movefrom.getY(), scene.angle);
					break;
					default:
						clip.shape = null;
				}

				mapTmpShapeInSameScript.put(scene.id, clip.shape);
			}

			clip.id = scene.id;
			clip.action = scene.action;
			clip.movefrom = scene.movefrom;
			clip.moveto = scene.moveto;
			clip.extendto = scene.extendto;
			clip.shrinkto = scene.shrinkto;
			clip.colorto = scene.colorto;
			clip.rotateto = scene.rotateto;
			clip.delaystart = scene.delaystart;
		
			movie.add(clip);
		}
		return movie;
	}

	interface TimerCallback{
		void onTimerComplete();
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
	int id;
	JShape shape;
	EAction action;
	Point movefrom;
	Point moveto;
	int extendto;
	int shrinkto;
	double rotateto;
	Color colorto;
	Point start;
	Point end;
	int angle;
	String showtext;
	int delaystart;
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
