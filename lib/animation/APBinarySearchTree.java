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
	Tree<ValuePair> arrowTree;	// int: id, int: arrow length, Point: location
	private final int VERTICAL_SPACE = 110;
	private final int LEVELLIMIT = 5;
	public APBinarySearchTree(Rectangle rectAnimationArea){

		// locations
		locations = new ArrayList<>();
		int level = 0;
		while(level < 5){
			int dx = (int)(rectAnimationArea.getWidth() / (Math.pow(2, level)+1));
			for(int i = 1; i <= Math.pow(2, level); i++){
				locations.add(new Point(dx * i, ((level+1) * VERTICAL_SPACE)));
			}
			level++;
		}

		// location tree
		locationsTree = new Tree<Point>();
		locationsTree.root = generateNLevelTree(LEVELLIMIT, 0);
		assignLocationsToTree(locationsTree.root, new ArrayDeque<TreeNode<Point>>(), new int[]{0});

		// data tree
		dataTree = new Tree<>();
		dataTree.root = new TreeNode<ValuePair>(null);

		// arrow tree
		arrowTree = new Tree<>();
		arrowTree.root = new TreeNode<ValuePair>(null); 
	}

	public Script initBinarySearchTree(int[] nums){
		Script script = new Script();

		TreeNode<ValuePair> arrow = arrowTree.root;
		TreeNode<ValuePair> node = dataTree.root;
		TreeNode<Point> locationNode = locationsTree.root;

		// // test
		// System.out.println(getAngle(new Point(0, 0), new Point(10, 0)));
		// System.out.println(getAngle(new Point(0, 0), new Point(10, 10)));
		// System.out.println(getAngle(new Point(0, 0), new Point(0, 10)));
		// System.out.println(getAngle(new Point(0, 0), new Point(-10, 10)));
		// System.out.println(getAngle(new Point(0, 0), new Point(-10, 0)));
		// System.out.println(getAngle(new Point(0, 0), new Point(-10, -10)));
		// System.out.println(getAngle(new Point(0, 0), new Point(0, -10)));
		// System.out.println(getAngle(new Point(0, 0), new Point(10, -10)));

		for(int i = 0; i < nums.length; i++){
			
			node = dataTree.root;
			arrow = arrowTree.root;
			locationNode = locationsTree.root;

			if(i == 0){
				
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

			while(true){
				if(nums[i] > node.data.num){
					if(node.right == null){
						node.right = new TreeNode<ValuePair>(null);
						node.right.data = new ValuePair(generateUniqueID(), nums[i], locationNode.right.data);
						arrow.right = new TreeNode<ValuePair>(null);
						arrow.right.data = new ValuePair(generateUniqueID(), getLength(node.data.location, node.right.data.location), getMiddlePoint(node.data.location, node.right.data.location));

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
						arrowMotion.extendto = getLength(node.data.location, node.right.data.location) - 50;
						script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
						script.addScene(generateScene(arrow.right.data.id, EShape.ARROW, EAction.EXTEND, arrowMotion));
						break;
					}else{
						node = node.right;
						locationNode = locationNode.right;
					}
				}else if(nums[i] < node.data.num){
					if(node.left == null){
						node.left = new TreeNode<ValuePair>(null);
						node.left.data = new ValuePair(generateUniqueID(), nums[i], locationNode.left.data);
						arrow.left = new TreeNode<ValuePair>(null);
						arrow.left.data = new ValuePair(generateUniqueID(), getLength(node.data.location, node.left.data.location), getMiddlePoint(node.data.location, node.left.data.location));

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
						arrowMotion.extendto = getLength(node.data.location, node.left.data.location) - 50;
						script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.ADD, arrowMotion));
						script.addScene(generateScene(arrow.left.data.id, EShape.ARROW, EAction.EXTEND, arrowMotion));
						break;
					}else{
						node = node.left;
						locationNode = locationNode.left;
					}
				}else{
					break;
				}
			}
		}

		return script;
	}

	private TreeNode<Point> generateNLevelTree(int N, int currentLevel){
		if(currentLevel >= N){
			return null;
		}
		TreeNode<Point> node = new TreeNode<Point>(new Point(0, 0));
		node.left = generateNLevelTree(N, currentLevel+1);
		node.right = generateNLevelTree(N, currentLevel+1);
		return node;
	}
	
	private void assignLocationsToTree(TreeNode<Point> node, ArrayDeque<TreeNode<Point>> visitedQueue, int[] count){
		if(node == null){
			return;
		}
		
		node.data = locations.get(count[0]);
		count[0]++;
	
		if(node.left != null) visitedQueue.addLast(node.left);
		if(node.right != null) visitedQueue.addLast(node.right);
	
		if(visitedQueue.isEmpty() == false) {
			assignLocationsToTree(visitedQueue.removeFirst(), visitedQueue, count);
		}
		else{
			return;
		}
	}
}

class ValuePair{
	public int id;
	public int num;
	public Point location;
	public ValuePair(int id, int num, Point location){
		this.id = id;
		this.num = num;
		this.location = location;
	}
}
