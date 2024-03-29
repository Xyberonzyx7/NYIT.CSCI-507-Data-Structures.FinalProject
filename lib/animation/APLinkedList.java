package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.spec.EdDSAParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import lib.datastructure.SinglyLinkedList;
import lib.script.*;

public class APLinkedList extends AnimationPlanner {
	
	private SinglyLinkedList sll_node;
	private SinglyLinkedList sll_arrow;
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 100;
	private final int HORIZONTAL_SPACE = 100;
	private final int NewNodeY = 200;
	private int nodeCount = 0;

	public APLinkedList(Rectangle rectAnimationArea){
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		sll_node = new SinglyLinkedList();
		sll_arrow = new SinglyLinkedList();
		locations = new ArrayList<>();

		// get placeable locations
		locations = new ArrayList<>();
		int x = nXMin;
		int y = nYMin;
		while (y < nYMax){

			locations.add(new Point(x, y));
			x += HORIZONTAL_SPACE;

			if(x > nXMax){
				x = nXMin;
				y += VERTICAL_SPACE;
			}
		}
	}

	public Script initLinkedList(int[] nums){
		Script script = new Script();

		// init sll_node
		sll_node = new SinglyLinkedList();
		for(int i = 0; i < nums.length; i++){
			sll_node.insertAt(i, generateUniqueID());
			nodeCount += 1;
		}

		// init sll_arrow
		for(int i = 0; i < nums.length - 1; i++){
			sll_arrow.insertAt(i, generateUniqueID());
		}

		// animation planning
		for(int i = 0; i < nums.length; i++){
			Motion circleMotion = new Motion();
			circleMotion.movefrom = new Point(0, 0);
			circleMotion.moveto = locations.get(i);
			circleMotion.showtext = Integer.toString(nums[i]);
			script.addScene(generateScene(sll_node.getAt(i), EShape.CIRCLE, EAction.ADD, circleMotion)); 
		}

		for(int i = 0; i < nums.length - 1; i++){
			Motion arrowMotion = new Motion();
			arrowMotion.movefrom = new Point(0, 0);
			arrowMotion.moveto = new Point(getMiddlePoint(locations.get(i), locations.get(i+1)));
			arrowMotion.angle = 180;
			script.addScene(generateScene(sll_arrow.getAt(i), EShape.ARROW, EAction.ADD, arrowMotion));
		}

		return script;
	}

	public Script insert(int index, int data){
		Script script = new Script();

		// new a node
		sll_node.insertAt(index, generateUniqueID());
		Motion newNodeMotion = new Motion();
		newNodeMotion.movefrom = new Point(0, 0);
		newNodeMotion.moveto = new Point((int) locations.get(0).getX(), NewNodeY);
		newNodeMotion.showtext = Integer.toString(data);
		script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.ADD, newNodeMotion));

		// move the node from 0 to index
		Motion newNodeMoveMotion = new Motion();
		for(int i = 0; i < index; i++){
			// newNodeMoveMotion.movefrom = new Point((int) locations.get(i).getX(), NewNodeY);
			newNodeMoveMotion.moveto = new Point((int) locations.get(i+1).getX(), NewNodeY);
			newNodeMoveMotion.delaystart = 2000;
			script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.MOVE, newNodeMoveMotion));
		}

		// move all nodes start from index one space right
		Motion oldNodeMoveMotion = new Motion();
		Motion oldArrowMoveMotion = new Motion();
		for(int i = index+1; i < sll_node.getSize(); i++){
			oldNodeMoveMotion.moveto = locations.get(i);
			// oldArrowMoveMotion.delaystart = 2000;
			script.addScene(generateScene(sll_node.getAt(i), EShape.CIRCLE, EAction.MOVE, oldNodeMoveMotion));
		}
		
		for(int i = index; i < sll_arrow.getSize(); i++){
			oldArrowMoveMotion.moveto = getMiddlePoint(locations.get(i+1), locations.get(i+2));
			oldArrowMoveMotion.angle = 180;
			script.addScene(generateScene(sll_arrow.getAt(i), EShape.ARROW, EAction.MOVE, oldArrowMoveMotion));
		}

		// move + extends prior arrow
		Motion extendOldArrowMotion = new Motion();
		extendOldArrowMotion.moveto = getMiddlePoint(locations.get(index-1), locations.get(index+1));
		script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, extendOldArrowMotion));	
		extendOldArrowMotion = new Motion();
		extendOldArrowMotion.extendto = 150;
		script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.EXTEND, extendOldArrowMotion));


		// new an arrow for the new node
		sll_arrow.insertAt(index, generateUniqueID());
		Motion newArrowMotion = new Motion();
		newArrowMotion.movefrom = new Point(0, 0);
		newArrowMotion.moveto = getMiddlePoint(new Point((int) locations.get(index).getX(), NewNodeY), locations.get(index+1));
		newArrowMotion.angle = 225;
		script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.ADD, newArrowMotion));

		// delete old node's arrow

		// new an arrow for the old nodes

		return script;
	}

	private Point getMiddlePoint(Point p1, Point p2){
		return new Point((int)((p1.getX() + p2.getX()) / 2), (int)((p1.getY() + p2.getY())/ 2));
	}
}
