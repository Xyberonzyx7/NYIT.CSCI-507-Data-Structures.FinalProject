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
			arrowMotion.moveto = new Point((int)(locations.get(i).getX() + locations.get(i+1).getX()) / 2, (int)(locations.get(i).getY() + locations.get(i+1).getY()) / 2);
			arrowMotion.angle = 180;
			script.addScene(generateScene(sll_arrow.getAt(i), EShape.ARROW, EAction.ADD, arrowMotion));
		}

		return script;
	}
}
