package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import lib.script.*;


// Animation Planner Array (AP Array), generate scripts
public class APArray extends AnimationPlanner{

	private int[] squaresIDs;
	private int[] circleIDs;
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
		squaresIDs = new int[nums.length];
		circleIDs = new int[nums.length];
		for(int i = 0; i < nums.length; i++){
			squaresIDs[i] = generateUniqueID();
			circleIDs[i] = generateUniqueID();
		}

		// animation planning
		for(int i = 0; i < nums.length; i++){
			Motion squareMotion = new Motion();
			squareMotion.movefrom = new Point(0, 0);
			squareMotion.moveto = locations.get(i);
			script.addScene(generateScene(squaresIDs[i], EShape.SQUARE, EAction.ADD, squareMotion));
			Motion circleMotion = new Motion();
			circleMotion.movefrom = new Point(0, 0);
			circleMotion.moveto = locations.get(i);
			circleMotion.showtext = Integer.toString(nums[i]);
			script.addScene(generateScene(circleIDs[i], EShape.CIRCLE, EAction.ADD, circleMotion));
		}
		return script;
	}	
	
	public Script modifyArray(int index, int number){
		Script script = new Script();

		script.addScene(generateScene(circleIDs[index], EShape.CIRCLE, EAction.DELETE, new Motion()));

		Motion putMotion = new Motion();
		putMotion.movefrom = new Point(0, 0);
		putMotion.moveto = locations.get(index);
		putMotion.showtext = Integer.toString(number);
		circleIDs[index] = generateUniqueID();
		script.addScene(generateScene(circleIDs[index], EShape.CIRCLE, EAction.ADD, putMotion));


		// animation planning
		return script;
	}
}
