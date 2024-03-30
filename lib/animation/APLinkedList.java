package lib.animation;

import java.awt.Color;
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
	private final int LOWERY = 200;

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

	public Script insertAt(int index, int data){

		Script script = new Script();

		// protection
		if(index >= sll_node.getSize()){
			return script;
		}
		
		// modify the list before animation
		sll_node.insertAt(index, generateUniqueID());
		sll_arrow.insertAt(index, generateUniqueID());

		// new node motion
		Motion newNodeMotion = new Motion();
		newNodeMotion.movefrom = new Point(0, 0);
		newNodeMotion.moveto = new Point((int) locations.get(0).getX(), LOWERY);
		newNodeMotion.showtext = Integer.toString(data);
		script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.ADD, newNodeMotion));

		// move node from 0 to index
		Motion newNodeMoveMotion = new Motion();
		for(int i = 0; i < index; i++){
			// newNodeMoveMotion.movefrom = new Point((int) locations.get(i).getX(), NewNodeY);
			newNodeMoveMotion.moveto = new Point((int) locations.get(i+1).getX(), LOWERY);
			newNodeMoveMotion.delaystart = 2000;
			script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.MOVE, newNodeMoveMotion));
		}

		// wait
		script.addScene(generateWaitScene(2000));

		// move all nodes start from index one space to the right (including arrows)
		Motion oldNodeMoveMotion = new Motion();
		for(int i = index+1; i < sll_node.getSize(); i++){
			oldNodeMoveMotion.moveto = locations.get(i);
			script.addScene(generateScene(sll_node.getAt(i), EShape.CIRCLE, EAction.MOVE, oldNodeMoveMotion));
		}
		
		Motion oldArrowMoveMotion = new Motion();
		for(int i = index+1; i < sll_arrow.getSize(); i++){
			oldArrowMoveMotion.moveto = getMiddlePoint(locations.get(i), locations.get(i+1));
			oldArrowMoveMotion.angle = 180;
			script.addScene(generateScene(sll_arrow.getAt(i), EShape.ARROW, EAction.MOVE, oldArrowMoveMotion));
		}

		// move + extends prior arrow
		if (index-1>= 0) {
			Motion extendOldArrowMotion = new Motion();
			extendOldArrowMotion.moveto = getMiddlePoint(locations.get(index - 1), locations.get(index + 1));
			script.addScene(generateScene(sll_arrow.getAt(index - 1), EShape.ARROW, EAction.MOVE, extendOldArrowMotion));
			extendOldArrowMotion = new Motion();
			extendOldArrowMotion.extendto = 150;
			script.addScene(generateScene(sll_arrow.getAt(index - 1), EShape.ARROW, EAction.EXTEND, extendOldArrowMotion));
		}

		// new an arrow for the new node
		Motion newArrowMotion = new Motion();
		newArrowMotion.movefrom = new Point(0, 0);
		newArrowMotion.moveto = getMiddlePoint(new Point((int) locations.get(index).getX(), LOWERY), locations.get(index+1));
		newArrowMotion.angle = 225;
		script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.ADD, newArrowMotion));

		script.addScene(generateWaitScene(2000));

		// delete old node's arrow
		if(index - 1 >= 0){
			Motion deleteOldArrowMotion = new Motion();
			deleteOldArrowMotion.moveto = new Point(-10, -10);
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, deleteOldArrowMotion));
			script.addScene(generateWaitScene(2000));
		}
		
		// new node insert motion
		Motion newNodeInsertMotion = new Motion();
		newNodeInsertMotion.moveto = locations.get(index);
		script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.MOVE, newNodeInsertMotion));
		
		// new arrow rotate
		Motion newArrowRotateMotion = new Motion();
		newArrowRotateMotion.moveto = getMiddlePoint(locations.get(index), locations.get(index+1));
		newArrowRotateMotion.rotateto = 180;
		script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.MOVE, newArrowRotateMotion));
		script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.ROTATE, newArrowRotateMotion));

		// wait
		script.addScene(generateWaitScene(2000));
		
		// new another arrow for the old nodes (reuse the old arrow)
		if(index-1 >= 0){
			Motion newPriorArrowMotion = new Motion();
			newPriorArrowMotion.movefrom = new Point(0, 0);
			newPriorArrowMotion.moveto = getMiddlePoint(locations.get(index-1),locations.get(index)); 
			newPriorArrowMotion.shrinkto = 50;
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, newPriorArrowMotion));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.SHRINK, newPriorArrowMotion));
		}
		
		return script;
	}

	public Script removeAt(int index){
		Script script = new Script();

		if(index >= sll_node.getSize()){
			return script;
		}

		// loop to index
		for(int i = 0; i < index+1; i++){
			
			Motion highlightMotion = new Motion();
			highlightMotion.colorto = Color.RED;
			script.addScene(generateScene(sll_node.getAt(i), EShape.CIRCLE, EAction.COLOR, highlightMotion));
			
			if(i > 0){
				Motion unhighlightMotion = new Motion();
				unhighlightMotion.colorto = Color.BLUE;
				script.addScene(generateScene(sll_node.getAt(i-1), EShape.CIRCLE, EAction.COLOR, unhighlightMotion));
			}
			
			// wait
			script.addScene(generateWaitScene(2000));
		}

		// extract the target node
		Point extractPoint = new Point((int)locations.get(index).getX(), LOWERY);
		Motion extractNodeMotion = new Motion();
		extractNodeMotion.moveto = extractPoint;
		script.addScene(generateScene(sll_node.getAt(index), EShape.CIRCLE, EAction.MOVE, extractNodeMotion));

		if(index-1 >= 0){
			Motion extractPriorArrow = new Motion();
			extractPriorArrow.rotateto = 135;
			extractPriorArrow.moveto = getMiddlePoint( locations.get(index-1), extractPoint);
			extractPriorArrow.extendto = 71;
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.ROTATE, extractPriorArrow));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, extractPriorArrow));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.EXTEND, extractPriorArrow));
		}

		if(index < sll_arrow.getSize()){
			Motion extractIndexArrow = new Motion();
			extractIndexArrow.rotateto = 225;
			extractIndexArrow.moveto = getMiddlePoint(locations.get(index+1), extractPoint);
			extractIndexArrow.extendto = 71;
			script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.ROTATE, extractIndexArrow));
			script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.MOVE, extractIndexArrow));
			script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.EXTEND, extractIndexArrow));
		}

		// wait
		script.addScene(generateWaitScene(2000));

		// prior arrow to index's next node
		if(index - 1 >= 0){
			Motion priorArrowToNextNode = new Motion();
			priorArrowToNextNode.rotateto = 180;
			priorArrowToNextNode.extendto = 150;
			priorArrowToNextNode.moveto = getMiddlePoint(locations.get(index-1), locations.get(index+1));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.ROTATE, priorArrowToNextNode));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, priorArrowToNextNode));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.EXTEND, priorArrowToNextNode));
		}

		// remove target node and its arrow (if target node is the last node, remove its prior arrow too)
		Motion removeTargetNode = new Motion();
		script.addScene(generateScene(sll_node.getAt(index), EShape.ARROW, EAction.DELETE, removeTargetNode));
		if(index < sll_arrow.getSize()){
			script.addScene(generateScene(sll_arrow.getAt(index), EShape.ARROW, EAction.DELETE, removeTargetNode));
		}

		if(index - 1 >= 0 && index == sll_arrow.getSize()){
			Motion motion = new Motion();
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.DELETE, motion));
		}

		// wait
		script.addScene(generateWaitScene(2000));

		// move the rest of the nodes backward
		for(int i = index+1; i < sll_node.getSize(); i++){
			Motion motion = new Motion();
			motion.moveto = locations.get(i-1);
			script.addScene(generateScene(sll_node.getAt(i), EShape.CIRCLE, EAction.MOVE, motion));
		}

		// move the rest of the arrows backward
		for(int i = index+1; i < sll_arrow.getSize(); i++){
			Motion motion = new Motion();
			motion.moveto = getMiddlePoint(locations.get(i-1), locations.get(i));
			script.addScene(generateScene(sll_arrow.getAt(i), EShape.ARROW, EAction.MOVE, motion));
		}

		// shrink the prior arrow
		if(index-1 >= 0){
			Motion shrinkPriorArrow = new Motion();
			shrinkPriorArrow.moveto = getMiddlePoint(locations.get(index - 1), locations.get(index));
			shrinkPriorArrow.shrinkto = 50;
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.MOVE, shrinkPriorArrow));
			script.addScene(generateScene(sll_arrow.getAt(index-1), EShape.ARROW, EAction.SHRINK, shrinkPriorArrow));
		}

		// remove target's node from list (if the target node is the last node, remove its prior arrow)
		sll_node.removeAt(index);
		if(index - 1 >= 0 && index == sll_arrow.getSize()){
			sll_arrow.removeAt(index-1);
		}else{
			sll_arrow.removeAt(index);
		}
		return script;
	}

	private Point getMiddlePoint(Point p1, Point p2){
		return new Point((int)((p1.getX() + p2.getX()) / 2), (int)((p1.getY() + p2.getY())/ 2));
	}
}
