package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.spec.EdDSAParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import lib.script.*;

public class APStack extends AnimationPlanner {

	private HashMap<Integer, Integer> map; 	// squares
	private Stack<Integer> stack;			// circles
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 60;
	private int middleX;
	private int capacity;
	private Point disappearPoint;

	public APStack(Rectangle rectAnimationArea){
		middleX = (int) (rectAnimationArea.getWidth() / 2);
		int nYMin = (int)(rectAnimationArea.getY() + MARGIN);
		int nYMax = (int)(rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);
		int y = nYMax;

		// init vairable
		map = new HashMap<>();
		stack = new Stack<>();
		locations = new ArrayList<Point>();
		disappearPoint = new Point(middleX, -20);

		// get placeble locations
		while ( y >= nYMin){
			locations.add(new Point(middleX, y));
			y -= VERTICAL_SPACE;
		} 
	}

	public Script initStack(int capacity){
		Script script = new Script();		

		this.capacity = capacity;

		// init map with squares
		map.clear();
		for(int i = 0; i < this.capacity; i++){
			map.put(i+1000, generateUniqueID());
		}

		// animation planning
		for(int i = 0; i < this.capacity; i++){
			Motion squareMotion = new Motion();
			squareMotion.movefrom = new Point(0, 0);
			squareMotion.moveto = locations.get(i);
			script.addScene(generateScene(map.get(i+1000), EShape.SQUARE, EAction.ADD, squareMotion));
			script.addScene(generateScene(map.get(i+1000), EShape.SQUARE, EAction.MOVE, squareMotion));
		}

		return script;
	}

	public Script push(int number){
		Script script = new Script();

		if(stack.size() == capacity){
			return script;
		}

		// add component to stack
		stack.push(generateUniqueID());
		Motion circleMotion = new Motion();
		circleMotion.movefrom = new Point(middleX, 0);
		circleMotion.moveto = locations.get(stack.size() - 1);
		circleMotion.showtext = Integer.toString(number);
		script.addScene(generateScene(stack.peek(), EShape.CIRCLE, EAction.ADD, circleMotion));
		script.addScene(generateScene(stack.peek(), EShape.CIRCLE, EAction.MOVE, circleMotion));
		return script;
	}

	public Script pop(){
		Script script = new Script();

		if(stack.size() == 0){
			return script;
		}

		// pop component from stack
		int popedID = stack.pop();
		Motion circleMotion = new Motion();
		circleMotion.moveto = disappearPoint;
		script.addScene(generateScene(popedID, EShape.CIRCLE, EAction.MOVE, circleMotion));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateScene(popedID, EShape.CIRCLE, EAction.DELETE, circleMotion));
		return script;
	}
}