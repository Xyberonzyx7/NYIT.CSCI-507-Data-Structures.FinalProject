package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;

// import lib.datastructure.Tree;
import lib.script.*;
import lib.tools.QuickSort;
import lib.tools.ReorderForBST;
import lib.datastructure.TreeNode;
import lib.datastructure.Tree;

public class APBinarySearchTree extends AnimationPlanner {

	List<Point> locations;
	Tree<Point> locationsTree;
	Tree<ValuePair> dataTree; 	// int: id, int: data, Point: location
	Tree<ValuePair> arrowTree; 	// int: id, int: arrow length, Point: location
	private final int VERTICAL_SPACE = 80;
	private final int LEVELLIMIT = 5;
	private final Point STANDBYPOINT = new Point(110, 110);
	private Rectangle codeArea;
	private int codeID;
	private String code_add;
	private String code_delete;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;

	public APBinarySearchTree(Rectangle rectAnimationArea) {

		// locations
		locations = new ArrayList<>();
		int level = 0;
		while (level < 5) {
			int dx = (int) (rectAnimationArea.getWidth() / (Math.pow(2, level) + 1));
			for (int i = 1; i <= Math.pow(2, level); i++) {
				locations.add(new Point(dx * i, ((level + 4) * VERTICAL_SPACE) + 40));
			}
			level++;
		}

		// location tree
		locationsTree = new Tree<Point>();
		locationsTree.root = generateNLevelTree(LEVELLIMIT, 0);
		assignLocationsToTree(locationsTree.root, new ArrayDeque<TreeNode<Point>>(), new int[] { 0 });

		// data tree
		dataTree = new Tree<>();
		dataTree.root = new TreeNode<ValuePair>(null);

		// arrow tree
		arrowTree = new Tree<>();
		arrowTree.root = new TreeNode<ValuePair>(null);

		// other variables
		codeArea = new Rectangle(rectAnimationArea.width - 410, 20, 410, 450);
		codeID = generateUniqueID();
		code_add = "STANDBY LINE\n";
		code_add += "Algorithm ADD(NODE, VALUE)\n";
		code_add += "{\n";
		code_add += "    if (NODE.value > VALUE) then\n";
		code_add += "        NODE.left = ADD(NODE.left, VALUE);\n";
		code_add += "    else if (NODE.value < VALUE) then\n";
		code_add += "        NODE.right = ADD(NODE.right, VALUE);\n";
		code_add += "    else if (NODE.value == VALUE) then\n";
		code_add += "        write (\"Duplicate Data\")\n";
		code_add += "    else\n";
		code_add += "        // (NODE == null)\n";
		code_add += "        NODE = new TreeNode(VALUE);\n";
		code_add += "    return NODE;\n";
		code_add += "}\n";

		code_delete = "STANDBY LINE\n";
		code_delete += "Algorithm DELETE(NODE, VALUE)\n";
		code_delete += "{\n";
		code_delete += "    if (NODE == null) then\n";
		code_delete += "        return NODE;\n";
		code_delete += "    else if (NODE.value > VALUE) then\n";
		code_delete += "        NODE.left = DELETE(NODE.left, VALUE);\n";
		code_delete += "    else if (NODE.value < VALUE) then\n";
		code_delete += "        NODE.right = DELETE(NODE.right, VALUE);\n";
		code_delete += "    else\n";
		code_delete += "        if (NODE.left == null) then\n";
		code_delete += "            return NODE.right;\n";
		code_delete += "        else if (NODE.right == null) then\n";
		code_delete += "            return NODE.left;\n";
		code_delete += "        else\n";
		code_delete += "            NODE.value = min value of NODE.right;\n";
		code_delete += "            NODE.right = DELETE(NODE.right, NODE.value);\n";
		code_delete += "    return NODE\n";
		code_delete += "}	\n";

		pointerID = generateUniqueID();
		pointer = ">>";
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;
	}

	public Script initBinarySearchTree(int[] nums) {
		Script script = new Script();

		// ensure that the user’s input data matches the criteria of the binary search tree
		QuickSort qs = new QuickSort();
		qs.quickSort(nums, 0, nums.length - 1);
		ReorderForBST reorderForBST = new ReorderForBST();
		reorderForBST.sortedArrayToBSTArray(nums);

		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> locationNode = locationsTree.root;

		script.addScene(generateAddScene(codeID, EShape.TEXT, codeArea.x, codeArea.y));
		script.addScene(generateChangeCodeScene(code_add));
		script.addScene(generateAddScene(pointerID, EShape.TEXT, pointer_x, pointer_y));
		script.addScene(generateTextScene(pointerID, EShape.TEXT, pointer));
		script.addScene(generateColorScene(pointerID, EShape.TEXT, Color.RED));

		for (int i = 0; i < nums.length; i++) {

			node = dataTree.root;
			arrow = arrowTree.root;
			locationNode = locationsTree.root;

			if (i == 0) {

				// data node
				node.data = new ValuePair(generateUniqueID(), nums[i], locationNode.data, 0);

				// arrow node (won't use this one, but we need a root)
				arrow.data = new ValuePair(-1, 0, locationNode.data, 0);

				// animation planning
				script.addScene(generateInitNodeScene(node));
				continue;
			}

			while (true) {
				if (nums[i] > node.data.num) {
					if (node.right == null) {
						node.right = new TreeNode<ValuePair>(null);
						node.right.data = new ValuePair(generateUniqueID(), nums[i], locationNode.right.data, 0);
						arrow.right = new TreeNode<ValuePair>(null);
						arrow.right.data = new ValuePair(generateUniqueID(),
								getLength(node.data.location, node.right.data.location) - 50,
								getMiddlePoint(node.data.location, node.right.data.location),
								getAngle(node.data.location, node.right.data.location));

						// animation
						script.addScene(generateInitNodeScene(node.right));	
						script.addScene(generateInitArrowScene(arrow.right));
						break;
					} else {
						node = node.right;
						locationNode = locationNode.right;
						arrow = arrow.right;
					}
				} else if (nums[i] < node.data.num) {
					if (node.left == null) {
						node.left = new TreeNode<ValuePair>(null);
						node.left.data = new ValuePair(generateUniqueID(), nums[i], locationNode.left.data, 0);
						arrow.left = new TreeNode<ValuePair>(null);
						arrow.left.data = new ValuePair(generateUniqueID(),
								getLength(node.data.location, node.left.data.location) - 50,
								getMiddlePoint(node.data.location, node.left.data.location),
								getAngle(node.data.location, node.left.data.location));

						// animation planning
						script.addScene(generateInitNodeScene(node.left));
						script.addScene(generateInitArrowScene(arrow.left));
						break;
					} else {
						node = node.left;
						locationNode = locationNode.left;
						arrow = arrow.left;
					}
				} else {
					break;
				}
			}
		}
		return script;
	}

	public Script add(int num) {
		Script script = new Script();
		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> locationNode = locationsTree.root;

		script.addScene(generateChangeCodeScene(code_add));

		// new node to standby position
		TreeNode<ValuePair> newNode = new TreeNode<>(null);
		newNode.data = new ValuePair(generateUniqueID(), num, STANDBYPOINT, 0);

		// animation
		script.addScene(generateAddScene(newNode.data.id, EShape.CIRCLE, 0, 0));
		script.addScene(generateMoveScene(newNode.data.id, EShape.CIRCLE, STANDBYPOINT));
		script.addScene(generateTextScene(newNode.data.id, EShape.CIRCLE, Integer.toString(num)));

		// wait
		script.addScene(generateWaitScene(1000));

		// root is empty
		if (node.data.num == Integer.MIN_VALUE) {

			script.addScene(generateMoveCodePointerScene(3));
			script.addScene(generateMoveCodePointerScene(5));
			script.addScene(generateMoveCodePointerScene(7));
			script.addScene(generateMoveCodePointerScene(9));
			script.addScene(generateMoveCodePointerScene(11));	// code: node = new treenode(value);

			// data node
			node.data.id = newNode.data.id;
			node.data.num = newNode.data.num;
			node.data.location = locationNode.data;
			node.data.angle = newNode.data.angle;

			// arrow node (won't use this one, but we need a root)
			arrow.data = new ValuePair(-1, 0, locationNode.data, 0);

			// animation planning
			script.addScene(generateMoveScene(node.data.id, EShape.CIRCLE, node.data.location));
			script.addScene(generateWaitScene(1000));

			script.addScene(generateMoveCodePointerScene(12));	// code: return Node;
			script.addScene(generateMoveCodePointerScene(0));	// code: standby line
			return script;
		}

		while (true) {

			script.addScene(generateMoveCodePointerScene(1));	// code: Algorithm ADD
			
			if (num < node.data.num) {
				script.addScene(generateMoveCodePointerScene(3));	// code: if (node.value > value) then
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				if (node.left == null) {
					script.addScene(generateMoveCodePointerScene(4));
					script.addScene(generateMoveCodePointerScene(1));
					script.addScene(generateMoveCodePointerScene(3));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(5));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(7));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(9));
					script.addScene(generateMoveCodePointerScene(11));

					node.left = newNode;
					node.left.data.location = locationNode.left.data;
					arrow.left = new TreeNode<ValuePair>(null);
					arrow.left.data = new ValuePair(generateUniqueID(),
							getLength(node.data.location, node.left.data.location) - 50,
							getMiddlePoint(node.data.location, node.left.data.location),
							getAngle(node.data.location, node.left.data.location));

					
					// animation planning
					script.addScene(generateAddNodeScene(node.left));
					script.addScene(generateAddArrowScene(arrow.left));
					script.addScene(generateWaitScene(1000));

					script.addScene(generateMoveCodePointerScene(12));
					break;

				} else {
					script.addScene(generateMoveCodePointerScene(4));
					node = node.left;
					locationNode = locationNode.left;
					arrow = arrow.left;
				}

				script.addScene(generateColorScene(node.data.id, EShape.CIRCLE, Color.BLUE));
				continue;
			} 
			
			
			if (num > node.data.num) {
				script.addScene(generateMoveCodePointerScene(3));
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				script.addScene(generateMoveCodePointerScene(5));
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				if (node.right == null) {
					
					script.addScene(generateMoveCodePointerScene(6));
					script.addScene(generateMoveCodePointerScene(1));
					script.addScene(generateMoveCodePointerScene(3));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(5));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(7));
					script.addScene(generateHighlightScene(newNode.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
					script.addScene(generateMoveCodePointerScene(9));
					script.addScene(generateMoveCodePointerScene(11));

					node.right = newNode;
					node.right.data.location = locationNode.right.data;
					arrow.right = new TreeNode<ValuePair>(null);
					arrow.right.data = new ValuePair(generateUniqueID(),
							getLength(node.data.location, node.right.data.location) - 50,
							getMiddlePoint(node.data.location, node.right.data.location),
							getAngle(node.data.location, node.right.data.location));
					
					// animation planning
					script.addScene(generateAddNodeScene(node.right));
					script.addScene(generateAddArrowScene(arrow.right));
					script.addScene(generateWaitScene(1000));

					script.addScene(generateMoveCodePointerScene(12));
					break;

				} else {

					script.addScene(generateMoveCodePointerScene(6));

					node = node.right;
					locationNode = locationNode.right;
					arrow = arrow.right;
				}

				script.addScene(generateColorScene(node.data.id, EShape.CIRCLE, Color.BLUE));
				continue;
			} 
			
			if (num == node.data.num){

				script.addScene(generateMoveCodePointerScene(3));
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				script.addScene(generateMoveCodePointerScene(5));
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				script.addScene(generateMoveCodePointerScene(7));
				script.addScene(generateDoubleHighlightScene(node.data.id, newNode.data.id, Color.BLUE, Color.RED));
				script.addScene(generateMoveCodePointerScene(8));
				script.addScene(generatePopup("Duplicate Data"));
				script.addScene(generateDeleteScene(newNode.data.id, EShape.CIRCLE));
				script.addScene(generateWaitScene(1000));
				script.addScene(generateMoveCodePointerScene(12));
				break;
			}
		}

		script.addScene(generateMoveCodePointerScene(0));

		return script;
	}

	public Script delete(int num) {
		Script script = new Script();
		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> location = locationsTree.root;

		script.addScene(generateChangeCodeScene(code_delete));

		delete(script, EDir.NONE, node, node, arrow, arrow, location, location, num);

		script.addScene(generateMoveCodePointerScene(0));

		return script;
	}

	private void delete(Script script, EDir dir, TreeNode<ValuePair> parentNode, TreeNode<ValuePair> node, TreeNode<ValuePair> parentArrow, TreeNode<ValuePair> arrow, TreeNode<Point> parentLocation, TreeNode<Point> location, int num) {

		if (node == null) {
			script.addScene(generateMoveCodePointerScene(1));
			script.addScene(generateMoveCodePointerScene(3));
			script.addScene(generateMoveCodePointerScene(4));
			return;
		}

		// animation planning
		// highlight current node
		script.addScene(generateMoveCodePointerScene(1));
		script.addScene(generateMoveCodePointerScene(3));
		script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
		script.addScene(generateMoveCodePointerScene(5));
		script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
		if (num < node.data.num) {
			script.addScene(generateMoveCodePointerScene(6));
			delete(script, EDir.LEFT, node, node.left, arrow, arrow.left, location, location.left, num);
			return;
		} 
		
		script.addScene(generateMoveCodePointerScene(7));
		script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
		if (num > node.data.num) {
			script.addScene(generateMoveCodePointerScene(8));
			delete(script, EDir.RIGHT, node, node.right, arrow, arrow.right, location, location.right, num);
			return;
		}
		
		script.addScene(generateMoveCodePointerScene(9));

		if (num == node.data.num){

			script.addScene(generateMoveCodePointerScene(10));
			script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));

			// Node with only one child or no child
			if (node.left == null && node.right == null) {

				script.addScene(generateMoveCodePointerScene(11));

				// animation planning - directly remove
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.data.id, EShape.ARROW));
				script.addScene(generateWaitScene(1000));

				if(dir == EDir.LEFT){
					parentNode.left = null;
					parentArrow.left = null;
					node.data.num = Integer.MIN_VALUE;
				}else if(dir == EDir.RIGHT){
					parentNode.right = null;
					parentArrow.right = null;
					node.data.num = Integer.MIN_VALUE;
				}else{
					node.data.num = Integer.MIN_VALUE;
				}
				return;
			}

			if(node.left == null && node.right != null){

				script.addScene(generateMoveCodePointerScene(11));

				arrow.right.data.location = getMiddlePoint(parentNode.data.location, node.right.data.location);
				arrow.right.data.num = getLength(parentNode.data.location, node.right.data.location) - 50;
				arrow.right.data.angle = getAngle(parentNode.data.location, node.right.data.location);
				script.addScene(generateAdjustArrowScene(arrow.right));

				// wait
				script.addScene(generateWaitScene(1000));

				// animation planning - delete target node and arrow
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.data.id, EShape.ARROW));

				// udpate data (node: location) (arrow: no need)
				if(dir == EDir.LEFT){
					parentNode.left = node.right;
					parentArrow.left = arrow.right;
					
					reposition(script, parentNode, parentNode.left, parentArrow.left, location);
					
					node = null;
					arrow = null;
					
				}else if(dir == EDir.RIGHT){
					parentNode.right = node.right;
					parentArrow.right = arrow.right;

					reposition(script, parentNode, parentNode.right, parentArrow.right, location);

					node = null;
					arrow = null;
				}else{
					node = null;
					arrow = null;
				}
				script.addScene(generateWaitScene(1000));
				return;

			}
			
			script.addScene(generateMoveCodePointerScene(12));
			script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));
			
			if(node.left != null && node.right == null){

				script.addScene(generateMoveCodePointerScene(13));

				arrow.left.data.location = getMiddlePoint(parentNode.data.location, node.left.data.location);
				arrow.left.data.num = getLength(parentNode.data.location, node.left.data.location) - 50;
				arrow.left.data.angle = getAngle(parentNode.data.location, node.left.data.location);
				script.addScene(generateAdjustArrowScene(arrow.left));

				// wait
				script.addScene(generateWaitScene(1000));

				// animation planning - delete target node and arrow
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.data.id, EShape.ARROW));

				if(dir == EDir.LEFT){
					parentNode.left = node.left;
					parentArrow.left = arrow.left;

					reposition(script, parentNode, parentNode.left, parentArrow.left, location);

					node = null;
					arrow = null;
				}else if(dir == EDir.RIGHT){
					parentNode.right = node.left;
					parentArrow.right = arrow.left;

					reposition(script, parentNode, parentNode.right, parentArrow.right, location);

					node = null;
					arrow = null;
				}else{
					node = null;
					arrow = null;
				}
				script.addScene(generateWaitScene(1000));
				return;
			}

			script.addScene(generateMoveCodePointerScene(14));

			// Node with two children: Get the inorder successor (smallest in the right subtree)
			script.addScene(generateMoveCodePointerScene(15));
			node.data.num = minValue(script, node.right);

			// animation planning - flash the target node to indicate its value is about to change
			script.addScene(generateFlashingScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.ORANGE));
			script.addScene(generateTextScene(node.data.id, EShape.CIRCLE, Integer.toString(node.data.num)));
			
			script.addScene(generateMoveCodePointerScene(16));
			delete(script, EDir.RIGHT, node, node.right, arrow, arrow.right, location, location.right, node.data.num);
			script.addScene(generateMoveCodePointerScene(17));
		}
		return;
	}

	private void reposition(Script script, TreeNode<ValuePair> parentNode, TreeNode<ValuePair> repositionNode, TreeNode<ValuePair> repositionArrow, TreeNode<Point> repositionLocation){

		if (repositionNode == null) {
			return;
		}

		repositionNode.data.location = repositionLocation.data;

		repositionArrow.data.angle = getAngle(parentNode.data.location, repositionLocation.data);
		repositionArrow.data.num = getLength(parentNode.data.location, repositionLocation.data) - 50;
		repositionArrow.data.location = getMiddlePoint(parentNode.data.location, repositionLocation.data);

		// animaion planning
		script.addScene(generateMoveScene(repositionNode.data.id, EShape.CIRCLE, repositionNode.data.location));
		script.addScene(generateAdjustArrowScene(repositionArrow));

		reposition(script, repositionNode, repositionNode.right, repositionArrow.right, repositionLocation.right);
		reposition(script, repositionNode, repositionNode.left, repositionArrow.left, repositionLocation.left);
	}

	private int minValue(Script script, TreeNode<ValuePair> node) {

		int minValue = node.data.num;
		while (true) {

			script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));

			minValue = node.data.num;
			
			// flashing leaf
			if(node.left == null){
				script.addScene(generateFlashingScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.ORANGE));
				break;
			}

			node = node.left;
		}
		return minValue;
	}

	private TreeNode<Point> generateNLevelTree(int N, int currentLevel) {
		if (currentLevel >= N) {
			return null;
		}
		TreeNode<Point> node = new TreeNode<Point>(new Point(0, 0));
		node.left = generateNLevelTree(N, currentLevel + 1);
		node.right = generateNLevelTree(N, currentLevel + 1);
		return node;
	}

	private List<Scene> generateInitNodeScene(TreeNode<ValuePair> node) {
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateAddScene(node.data.id, EShape.CIRCLE, 0, 0));
		scenes.add(generateMoveScene(node.data.id, EShape.CIRCLE, node.data.location));
		scenes.add(generateTextScene(node.data.id, EShape.CIRCLE, Integer.toString(node.data.num)));
		return scenes;
	}

	private List<Scene> generateInitArrowScene(TreeNode<ValuePair> arrow){
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateAddScene(arrow.data.id, EShape.ARROW, 0, 0, arrow.data.angle));
		scenes.add(generateMoveScene(arrow.data.id, EShape.ARROW, arrow.data.location));
		scenes.add(generateLengthScene(arrow.data.id, EShape.ARROW, arrow.data.num));
		return scenes;
	}

	private List<Scene> generateAddNodeScene(TreeNode<ValuePair> node){
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateAddScene(node.data.id, EShape.CIRCLE, 0, 0));
		scenes.add(generateMoveScene(node.data.id, EShape.CIRCLE, node.data.location));
		return scenes;
	}

	private List<Scene> generateAddArrowScene(TreeNode<ValuePair> arrow){
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateAddScene(arrow.data.id, EShape.ARROW, 0, 0, arrow.data.angle));
		scenes.add(generateMoveScene(arrow.data.id, EShape.ARROW, arrow.data.location));
		scenes.add(generateLengthScene(arrow.data.id, EShape.ARROW, arrow.data.num));
		return scenes;
	}

	private List<Scene> generateAdjustArrowScene(TreeNode<ValuePair> arrow){
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateMoveScene(arrow.data.id, EShape.ARROW, arrow.data.location));
		scenes.add(generateLengthScene(arrow.data.id, EShape.ARROW, arrow.data.num));
		scenes.add(generateRotateScene(arrow.data.id, EShape.ARROW, arrow.data.angle));
		return scenes;
	}

	private void assignLocationsToTree(TreeNode<Point> node, ArrayDeque<TreeNode<Point>> visitedQueue, int[] count) {
		if (node == null) {
			return;
		}

		node.data = locations.get(count[0]);
		count[0]++;

		if (node.left != null)
			visitedQueue.addLast(node.left);
		if (node.right != null)
			visitedQueue.addLast(node.right);

		if (visitedQueue.isEmpty() == false) {
			assignLocationsToTree(visitedQueue.removeFirst(), visitedQueue, count);
		} else {
			return;
		}
	}

	private Scene generateChangeCodeScene(String code) {
		Motion motion = new Motion();
		motion.showtext = code;
		return generateScene(codeID, EShape.TEXT, EAction.TEXT, motion);
	}

	private List<Scene> generateMoveCodePointerScene(int line){
		List<Scene> scenes = new ArrayList<>();
		Motion motion = new Motion();
		motion.moveto = new Point(pointer_x, pointer_y + wordHeight * line);
		scenes.add(generateScene(pointerID, EShape.TEXT, EAction.MOVE, motion));
		scenes.add(generateWaitScene(1000));
		return scenes;
	}

	private List<Scene> generateDoubleHighlightScene(int id1, int id2, Color unhighlight, Color highlight){
		List<Scene> scenes = new ArrayList<>();
		Motion highlightMotion = new Motion();
		highlightMotion.colorto = highlight;
		scenes.add(generateColorScene(id1, EShape.CIRCLE, highlight));
		scenes.add(generateColorScene(id2, EShape.CIRCLE, highlight));
		scenes.add(generateWaitScene(1000));
		scenes.add(generateColorScene(id1, EShape.CIRCLE, unhighlight));
		scenes.add(generateColorScene(id2, EShape.CIRCLE, unhighlight));
		scenes.add(generateWaitScene(1000));
		return scenes;
	}

	@Override
	public List<Scene> generateHighlightScene(int id, EShape shape, Color defaultColor, Color highlightColor){
		List<Scene> scenes = new ArrayList<>();

		Motion highlight = new Motion();
		highlight.colorto = highlightColor;
		scenes.add(generateScene(id, shape, EAction.COLOR, highlight));
		scenes.add(generateWaitScene(1000));

		Motion unhighlight = new Motion();
		unhighlight.colorto = defaultColor;
		scenes.add(generateScene(id, shape, EAction.COLOR, unhighlight));
		scenes.add(generateWaitScene(1000));
		return scenes;
	}
}

enum EDir{
	NONE,
	LEFT,
	RIGHT
}

class ValuePair {
	public int id;
	public int num;
	public Point location;
	public double angle;	// could be used as starting angle or rotate-to angle

	public ValuePair(int id, int num, Point location, double angle) {
		this.id = id;
		this.num = num;
		this.location = location;
		this.angle = angle;
	}
}
