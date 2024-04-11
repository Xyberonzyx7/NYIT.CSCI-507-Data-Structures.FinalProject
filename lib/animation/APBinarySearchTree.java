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
	Tree<ValuePair> dataTree; 	// int: id, int: data, Point: location
	Tree<ValuePair> arrowTree; 	// int: id, int: arrow length, Point: location
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
				locations.add(new Point(dx * i, ((level + 2) * VERTICAL_SPACE)));
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

			// data node
			node.data.id = newNode.data.id;
			node.data.num = newNode.data.num;
			node.data.location = locationNode.data;
			node.data.angle = newNode.data.angle;

			// arrow node (won't use this one, but we need a root)
			arrow.data = new ValuePair(-1, 0, locationNode.data, 0);

			// animation planning
			script.addScene(generateMoveScene(node.data.id, EShape.CIRCLE, node.data.location));
			return script;
		}

		while (true) {

			script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));

			if (num > node.data.num) {
				if (node.right == null) {
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
							getLength(node.data.location, node.left.data.location) - 50,
							getMiddlePoint(node.data.location, node.left.data.location),
							getAngle(node.data.location, node.left.data.location));

					
					// animation planning
					script.addScene(generateAddNodeScene(node.left));
					script.addScene(generateAddArrowScene(arrow.left));
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
		script.addScene(generateHighlightScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.RED));

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
			else if(node.left == null && node.right != null){
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
				return;

			}else if(node.left != null && node.right == null){

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

				return;
			}

			// Node with two children: Get the inorder successor (smallest in the right subtree)
			node.data.num = minValue(script, node.right);

			// animation planning - flash the target node to indicate its value is about to change
			script.addScene(generateFlashingScene(node.data.id, EShape.CIRCLE, Color.BLUE, Color.ORANGE));
			script.addScene(generateTextScene(node.data.id, EShape.CIRCLE, Integer.toString(node.data.num)));

			delete(script, EDir.RIGHT, node, node.right, arrow, arrow.right, location, location.right, node.data.num);

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
