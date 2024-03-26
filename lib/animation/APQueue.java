package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.spec.EdDSAParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import lib.datastructure.CircularQueue;
import lib.script.*;

public class APQueue extends AnimationPlanner {
	private HashMap<Integer, Integer> map;	// store square id
	private CircularQueue queue;			// store circle id
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int HORIZONTAL_SPACE = 60;
	private int middleY;
	private int capacity;
	private Point disappearPoint;

	public APQueue(Rectangle rectAnimationArea){
		middleY = (int)(rectAnimationArea.getHeight() / 2);
		int nXMin = (int)(rectAnimationArea.getX() + MARGIN);
		int nXMax = (int)(rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int x = nXMin;

		// init variable
		capacity = 0;
		disappearPoint = new Point(-20, -20);
		locations = new ArrayList<>();
		map = new HashMap<>();

		// get placeable locations
		while(x <= nXMax){
			locations.add(new Point(x, middleY));
			x += HORIZONTAL_SPACE;
		}
	}

	public Script initQueue(int capacity){
		Script script = new Script();

		this.capacity = capacity;
		queue = new CircularQueue(capacity);

		// init map with squares
		map.clear();
		for(int i = 0; i < capacity; i++){
			map.put(i, generateUniqueID());
		}

		// animation planning
		for(int i = 0; i < capacity; i++){
			Motion squareMotion = new Motion();
			squareMotion.movefrom = new Point(0, 0);
			squareMotion.moveto = locations.get(i);
			script.addScene(generateScene(map.get(i), EShape.SQUARE, EAction.ADD, squareMotion));
		}

		return script;
	}

	public Script enqueue(int number){
		Script script = new Script();

		if(queue.isFull()){
			return script;
		}

		// add component to queue
		queue.enqueue(generateUniqueID());
		Motion circleMotion = new Motion();
		circleMotion.movefrom = new Point(0, 0);
		circleMotion.moveto = locations.get(queue.rearIndex());
		circleMotion.showtext = Integer.toString(number);
		script.addScene(generateScene(queue.peekRear(), EShape.CIRCLE, EAction.ADD, circleMotion));
		return script;
	}

	public Script dequeue(){
		Script script = new Script();

		if(queue.isEmpty()){
			return script;
		}

		// dequeue component from the front index
		int dequeuedID = queue.dequeue();
		Motion circleMotion = new Motion();
		circleMotion.moveto = disappearPoint;
		script.addScene(generateScene(dequeuedID, EShape.CIRCLE, EAction.DELETE, circleMotion));

		return script;
	}
}
