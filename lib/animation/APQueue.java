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

public class APQueue {
	private HashMap<Integer, Integer> map;	// square
	private CircularQueue queue;
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int HORIZONTAL_SPACE = 60;
	private int middleY;
	private int objCount;
	private int capacity;
	private Point disappPoint;

	public APQueue(Rectangle rectAnimationArea){
		middleY = (int)(rectAnimationArea.getHeight() / 2);
		int nXMin = (int)(rectAnimationArea.getX() + MARGIN);
		int nXMax = (int)(rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int x = nXMin;

		// init variable
		capacity = 0;
		objCount = 0;
		disappPoint = new Point(middleY, -20);
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
			script.addScene(generateScene(map.get(i), EShape.SQUARE, EAction.ADD, new Point(0, 0), locations.get(i), null));
		}

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

	private Integer generateUniqueID() {
		objCount++;
		return objCount;
	}
}
