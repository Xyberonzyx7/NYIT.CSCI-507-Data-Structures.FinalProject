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
			Motion squareMotion = new Motion();
			squareMotion.movefrom = new Point(0, 0);
			squareMotion.moveto = locations.get(i);
			script.addScene(generateScene(map.get(i+1000), EShape.SQUARE, EAction.ADD, squareMotion));
			Motion circleMotion = new Motion();
			circleMotion.movefrom = new Point(0, 0);
			circleMotion.moveto = locations.get(i);
			circleMotion.showtext = Integer.toString(nums[i]);
			script.addScene(generateScene(map.get(i), EShape.CIRCLE, EAction.ADD, circleMotion));
		}
		return script;
	}	
	
	public Script modifyArray(int index, int number){
		Script script = new Script();

		script.addScene(generateScene(map.get(index), EShape.CIRCLE, EAction.DELETE, new Motion()));
		map.remove(index);

		Motion putMotion = new Motion();
		putMotion.movefrom = new Point(0, 0);
		putMotion.moveto = locations.get(index);
		putMotion.showtext = Integer.toString(number);
		map.put(index, generateUniqueID());
		script.addScene(generateScene(map.get(index), EShape.CIRCLE, EAction.ADD, putMotion));


		// animation planning
		return script;
	}
}
