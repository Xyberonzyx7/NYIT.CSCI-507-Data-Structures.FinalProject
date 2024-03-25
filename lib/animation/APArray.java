package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import lib.script.*;


// Animation Planner Array (AP Array), generate scripts
public class APArray extends AnimationPlanner{

	private HashMap<Integer, Integer> map; // key = inex, value = id
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int HORIZONTAL_SPACE = 60;
	private final int VERTICAL_SPACE = 200;

	public APArray(Rectangle rectAnimationArea){
		
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		map = new HashMap<>();
		objCount = 0;

		// get placable available locations
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

	public Script initArray(int[] nums){
		Script script = new Script();

		
		// init map
		map.clear();
		for(int i = 0; i < nums.length; i++){
			map.put( i+1000, generateUniqueID()); // square frame not use often, put at 1000+ to not to affect array operation
			map.put( i, generateUniqueID());
		}

		// animation planning
		for(int i = 0; i < nums.length; i++){
			script.addScene(generateScene(map.get(i+1000), EShape.SQUARE, EAction.ADD, new Point(0, 0), locations.get(i), ""));
			script.addScene(generateScene(map.get(i), EShape.CIRCLE, EAction.ADD, new Point(0, 0), locations.get(i), Integer.toString(nums[i])));
		}
		return script;
	}	
	
	public Script modifyArray(int index, int number){
		Script script = new Script();

		script.addScene(generateScene(map.get(index), EShape.CIRCLE, EAction.DELETE, null, null, ""));
		map.remove(index);

		map.put(index, generateUniqueID());
		script.addScene(generateScene(map.get(index), EShape.CIRCLE, EAction.ADD, new Point(0, 0), locations.get(index), Integer.toString(number)));


		// animation planning
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
}
