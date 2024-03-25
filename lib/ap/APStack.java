package lib.ap;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.spec.EdDSAParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import lib.script.*;

public class APStack {

	private HashMap<Integer, Integer> map;
	private Stack<Integer> stack;	// stack of unique id
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 60;
	private int middleX;
	private int objCount;
	private int capacity;

	public APStack(Rectangle rectAnimationArea){
		middleX = (int) (rectAnimationArea.getWidth() / 2);
		int nYMin = (int)(rectAnimationArea.getY() + MARGIN);
		int nYMax = (int)(rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);
		int y = nYMax;

		// init vairable
		map = new HashMap<>();
		stack = new Stack<>();
		locations = new ArrayList<Point>();
		objCount = 0;

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
			script.addScene(generateScene(map.get(i+1000), EShape.SQUARE, EAction.ADD, new Point(0, 0), locations.get(i), null));
		}

		return script;
	}

	public Script push(int number){
		Script script = new Script();

		if(stack.size() == capacity){
			return script;
		}

		// add component to map
		stack.push(generateUniqueID());
		script.addScene(generateScene(stack.peek(), EShape.CIRCLE, EAction.ADD, new Point(middleX, 0), locations.get(stack.size()-1), Integer.toString(number)));
		return script;
	}

	public Script pop(){
		Script script = new Script();
		return script;
	}

	private Scene generateScene(int id, EShape shape, EAction action, Point start, Point end, String txt){
		Scene scene = new Scene();
		scene.id = id;
		scene.shape = shape;
		scene.action = action;
		scene.start = start;
		scene.end = end;
		scene.txt = txt;
		return scene;
	}

	private int generateUniqueID() {
		objCount++;
		return objCount;
	}
	
}
