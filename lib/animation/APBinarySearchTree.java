package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;

// import lib.datastructure.Tree;
import lib.script.*;
import lib.datastructure.TreeNode;
import lib.datastructure.Tree;

public class APBinarySearchTree extends AnimationPlanner {

	List<Point> locations;
	Tree<Point> locationsTree;
	Tree<ValuePair> dataTree; // int: id, int: data, Point: location
	Tree<ValuePair> arrowTree; // int: id, int: arrow length, Point: location
	private final int VERTICAL_SPACE = 110;
	private final int LEVELLIMIT = 5;
	private final Point STANDBYPOINT = new Point(110, 110);

	public APBinarySearchTree(Rectangle rectAnimationArea) {

		// locations
		locations = new ArrayList<>();
		int level = 0;
		while (level < 5) {
			int dx = (int) (rectAnimationArea.getWidth() / (Math.pow(2, level) + 1));
			for (int i = 1; i <= Math.pow(2, level); i++) {
				locations.add(new Point(dx * i, ((level + 1) * VERTICAL_SPACE)));
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
	}

	public Script initBinarySearchTree(int[] nums) {
		Script script = new Script();

		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> locationNode = locationsTree.root;

		for (int i = 0; i < nums.length; i++) {

			node = dataTree.root;
			arrow = arrowTree.root;
			locationNode = locationsTree.root;

			if (i == 0) {

				// data node
				node.data = new ValuePair(generateUniqueID(), nums[i], locationNode.data);

				// arrow node (won't use this one, but we need a root)
				arrow.data = new ValuePair(-1, 0, locationNode.data);

				// animation planning
				Motion motion = new Motion();
				motion.movefrom = new Point(0, 0);
				motion.moveto = locationNode.data;
				motion.showtext = Integer.toString(nums[i]);
				script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.ADD, motion));
				continue;
			}

			while (true) {
				if (nums[i] > node.data.num) {
					if (node.right == null) {
						node.right = new TreeNode<ValuePair>(null);
						node.right.data = new ValuePair(generateUniqueID(), nums[i], locationNode.right.data);
						arrow.right = new TreeNode<ValuePair>(null);
						arrow.right.data = new ValuePair(generateUniqueID(),
								getLength(node.data.location, node.right.data.location),
								getMiddlePoint(node.data.location, node.right.data.location));

						// animation planning
						Motion motion = new Motion();
						motion.movefrom = new Point(0, 0);
						motion.moveto = locationNode.right.data;
						motion.showtext = Integer.toString(nums[i]);
						script.addScene(generateScene(node.right.data.id, EShape.CIRCLE, EAction.ADD, motion));

						Motion arrowMotion = new Motion();
						arrowMotion.movefrom = new Point(0, 0);
						arrowMotion.moveto = getMiddlePoint(node.data.location, node.right.data.location);
						arrowMotion.angle = getAngle(node.data.location, node.right.data.location);
						arrowMotion.lengthto = getLength(node.data.location, node.right.data.location) - 50;
						script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
						script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
						break;
					} else {
						node = node.right;
						locationNode = locationNode.right;
						arrow = arrow.right;
					}
				} else if (nums[i] < node.data.num) {
					if (node.left == null) {
						node.left = new TreeNode<ValuePair>(null);
						node.left.data = new ValuePair(generateUniqueID(), nums[i], locationNode.left.data);
						arrow.left = new TreeNode<ValuePair>(null);
						arrow.left.data = new ValuePair(generateUniqueID(),
								getLength(node.data.location, node.left.data.location),
								getMiddlePoint(node.data.location, node.left.data.location));

						// animation planning
						Motion motion = new Motion();
						motion.movefrom = new Point(0, 0);
						motion.moveto = locationNode.left.data;
						motion.showtext = Integer.toString(nums[i]);
						script.addScene(generateScene(node.left.data.id, EShape.CIRCLE, EAction.ADD, motion));

						Motion arrowMotion = new Motion();
						arrowMotion.movefrom = new Point(0, 0);
						arrowMotion.moveto = getMiddlePoint(node.data.location, node.left.data.location);
						arrowMotion.angle = getAngle(node.data.location, node.left.data.location);
						arrowMotion.lengthto = getLength(node.data.location, node.left.data.location) - 50;
						script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
						script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
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

		// new node to standby position
		TreeNode<ValuePair> newNode = new TreeNode<>(null);
		newNode.data = new ValuePair(generateUniqueID(), num, STANDBYPOINT);

		// animation
		Motion standbyMotion = new Motion();
		standbyMotion.movefrom = new Point(0, 0);
		standbyMotion.moveto = STANDBYPOINT;
		standbyMotion.showtext = Integer.toString(num);
		script.addScene(generateScene(newNode.data.id, EShape.CIRCLE, EAction.ADD, standbyMotion));
		script.addScene(generateScene(newNode.data.id, EShape.CIRCLE, EAction.MOVE, standbyMotion));

		// wait
		script.addScene(generateWaitScene(2000));

		while (true) {

			// animation planning highlight node to for comparing
			Motion highlight = new Motion();
			highlight.colorto = Color.RED;
			script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, highlight));

			// wait
			script.addScene(generateWaitScene(2000));

			// unhighlight self
			Motion unHighlight = new Motion();
			unHighlight.colorto = Color.BLUE;
			script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, unHighlight));

			if (num > node.data.num) {
				if (node.right == null) {
					node.right = newNode;
					node.right.data.location = locationNode.right.data;
					arrow.right = new TreeNode<ValuePair>(null);
					arrow.right.data = new ValuePair(generateUniqueID(),
							getLength(node.data.location, node.right.data.location),
							getMiddlePoint(node.data.location, node.right.data.location));

					// animation planning
					Motion motion = new Motion();
					motion.movefrom = new Point(0, 0);
					motion.moveto = locationNode.right.data;
					motion.showtext = Integer.toString(num);
					script.addScene(generateScene(node.right.data.id, EShape.CIRCLE, EAction.ADD, motion));

					Motion arrowMotion = new Motion();
					arrowMotion.movefrom = new Point(0, 0);
					arrowMotion.moveto = getMiddlePoint(node.data.location, node.right.data.location);
					arrowMotion.angle = getAngle(node.data.location, node.right.data.location);
					arrowMotion.lengthto = getLength(node.data.location, node.right.data.location) - 50;
					script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
					script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
					break;
				} else {
					node = node.right;
					locationNode = locationNode.right;
					arrow = arrow.right;
				}
			} else if (num < node.data.num) {
				if (node.left == null) {
					node.left = newNode;
					node.left.data.location = locationNode.left.data;
					arrow.left = new TreeNode<ValuePair>(null);
					arrow.left.data = new ValuePair(generateUniqueID(),
							getLength(node.data.location, node.left.data.location),
							getMiddlePoint(node.data.location, node.left.data.location));

					// animation planning
					Motion motion = new Motion();
					motion.movefrom = new Point(0, 0);
					motion.moveto = locationNode.left.data;
					motion.showtext = Integer.toString(num);
					script.addScene(generateScene(node.left.data.id, EShape.CIRCLE, EAction.ADD, motion));

					Motion arrowMotion = new Motion();
					arrowMotion.movefrom = new Point(0, 0);
					arrowMotion.moveto = getMiddlePoint(node.data.location, node.left.data.location);
					arrowMotion.angle = getAngle(node.data.location, node.left.data.location);
					arrowMotion.lengthto = getLength(node.data.location, node.left.data.location) - 50;
					script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
					script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
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
		return script;
	}

	public Script delete(int num) {
		Script script = new Script();
		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> location = locationsTree.root;

		delete(script, EDir.NONE, node, node, arrow, arrow, location, location, num);

		return script;
	}

	private void delete(Script script, EDir dir, TreeNode<ValuePair> parentNode, TreeNode<ValuePair> node, TreeNode<ValuePair> parentArrow, TreeNode<ValuePair> arrow, TreeNode<Point> parentLocation, TreeNode<Point> location, int num) {
		if (node == null) {
			return;
		}

		// animation planning
		// highlight current node
		Motion highlight = new Motion();
		highlight.colorto = Color.RED;
		script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, highlight));

		// wait
		script.addScene(generateWaitScene(2000));

		// unhighlight current node
		Motion unhighlight = new Motion();
		unhighlight.colorto = Color.BLUE;
		script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, unhighlight));

		if (num < node.data.num) {
			delete(script, EDir.LEFT, node, node.left, arrow, arrow.left, location, location.left, num);
		} else if (num > node.data.num) {
			delete(script, EDir.RIGHT, node, node.right, arrow, arrow.right, location, location.right, num);
		} else {
			// Node with only one child or no child
			if (node.left == null && node.right == null) {

				// animation planning - directly remove
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.data.id, EShape.ARROW));

				if(dir == EDir.LEFT){
					parentNode.left = null;
					parentArrow.left = null;
					node = null;
					arrow = null;
				}else if(dir == EDir.RIGHT){
					parentNode.right = null;
					parentArrow.right = null;
					node = null;
					arrow = null;
				}else{
					node = null;
					arrow = null;
				}

				return;
			}
			else if(node.left == null && node.right != null){

				// animation planning - parent node connects to node.right
				Motion arrowMotion = new Motion();
				arrowMotion.moveto = getMiddlePoint(parentNode.data.location, node.right.data.location);
				arrowMotion.lengthto = getLength(parentNode.data.location, node.right.data.location) - 50;
				arrowMotion.rotateto = getAngle(parentNode.data.location, node.right.data.location);
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.MOVE, arrowMotion));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.ROTATE, arrowMotion));

				// wait
				script.addScene(generateWaitScene(2000));

				// animation planning - delete target node and arrow
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.right.data.id, EShape.ARROW));

				// animation planning - child node reposition to target node's location
				Motion childNodeReposition = new Motion();
				childNodeReposition.moveto = node.data.location;
				script.addScene(generateScene(node.right.data.id, EShape.CIRCLE, EAction.MOVE, childNodeReposition));

				Motion arrowReposition = new Motion();
				arrowReposition.moveto = getMiddlePoint(parentNode.data.location, node.data.location);
				arrowReposition.lengthto = getLength(parentNode.data.location, node.data.location) - 50;
				arrowReposition.rotateto = getAngle(parentNode.data.location, node.data.location);
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.MOVE, arrowReposition));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.LENGTH, arrowReposition));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.ROTATE, arrowReposition));

				if(dir == EDir.LEFT){
					parentNode.left = node.right;
					parentArrow.left = arrow.right;
					node = null;
					arrow = null;
				}else if(dir == EDir.RIGHT){
					parentNode.right = node.right;
					parentArrow.right = arrow.right;
					node = null;
					arrow = null;
				}else{
					node = null;
					arrow = null;
				}
				return;

			}else if(node.left != null && node.right == null){

				// animation planning - parent node connects to node.left
				Motion arrowMotion = new Motion();
				arrowMotion.moveto = getMiddlePoint(parentNode.data.location, node.left.data.location);
				arrowMotion.lengthto = getLength(parentNode.data.location, node.left.data.location) - 50;
				arrowMotion.rotateto = getAngle(parentNode.data.location, node.left.data.location);
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.MOVE, arrowMotion));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.LENGTH, arrowMotion));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.ROTATE, arrowMotion));

				// wait
				script.addScene(generateWaitScene(2000));

				// animation planning - delete target node and arrow
				script.addScene(generateDeleteScene(node.data.id, EShape.CIRCLE));
				script.addScene(generateDeleteScene(arrow.left.data.id, EShape.ARROW));

				// animation planning - child node reposition to target node's location
				Motion childNodeReposition = new Motion();
				childNodeReposition.moveto = node.data.location;
				script.addScene(generateScene(node.left.data.id, EShape.CIRCLE, EAction.MOVE, childNodeReposition));

				Motion arrowReposition = new Motion();
				arrowReposition.moveto = getMiddlePoint(parentNode.data.location, node.data.location);
				arrowReposition.lengthto = getLength(parentNode.data.location, node.data.location) - 50;
				arrowReposition.rotateto = getAngle(parentNode.data.location, node.data.location);
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.MOVE, arrowReposition));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.LENGTH, arrowReposition));
				script.addScene(generateScene(arrow.data.id, EShape.ARROW, EAction.ROTATE, arrowReposition));

				if(dir == EDir.LEFT){
					parentNode.left = node.left;
					parentArrow.left = arrow.left;
					node = null;
					arrow = null;
				}else if(dir == EDir.RIGHT){
					parentNode.right = node.left;
					parentArrow.right = arrow.left;
					node = null;
					arrow = null;
				}else{
					node = null;
					arrow = null;
				}

				return;
			}

			// Node with two children: Get the inorder successor (smallest in the right subtree)
			node.data.num = minValue(script, node.right);

			// animation planning - flash the target node to indicate its value is about to chnage
			script.addScene(generateFlashingScene(node.data.id, EShape.CIRCLE, EAction.COLOR, Color.BLUE, Color.ORANGE));
			Motion changeNumberMotion = new Motion();
			changeNumberMotion.showtext = Integer.toString(node.data.num);
			script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.TEXT, changeNumberMotion));

			delete(script, EDir.RIGHT, node, node.right, arrow, arrow.right, location, location.right, node.data.num);

		}
		// return node;
		return;
	}

	private int minValue(Script script, TreeNode<ValuePair> node) {
		int minValue = node.data.num;
		while (node.left != null) {

			// animation planning - highlight target
			Motion highlightMotion = new Motion();
			highlightMotion.colorto = Color.RED;
			script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, highlightMotion));

			// wait 
			script.addScene(generateWaitScene(2000));

			// unhighlight target
			Motion unhighlightMotion = new Motion();
			unhighlightMotion.colorto = Color.BLUE;
			script.addScene(generateScene(node.data.id, EShape.CIRCLE, EAction.COLOR, unhighlightMotion));

			minValue = node.left.data.num;
			node = node.left;

			// flashing leaf
			if(node.left == null){
				script.addScene(generateFlashingScene(node.data.id, EShape.CIRCLE, EAction.COLOR, Color.BLUE, Color.ORANGE));
			}
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

	public ValuePair(int id, int num, Point location) {
		this.id = id;
		this.num = num;
		this.location = location;
	}
}
