package lib.animation;

import java.awt.Color;
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
	private int codeID;
	private String code;
	private Rectangle codeArea;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;

	public APArray(Rectangle rectAnimationArea){
		
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		objCount = 0;
		codeArea = new Rectangle(rectAnimationArea.width - 450, 20, 450, 450);
		codeID = generateUniqueID();
		code = "Algorithm replace(int[] arr, int index, int num):\n";
		code += "    arr[index] = num;\n";
		code += "    End;";
		pointerID = generateUniqueID();
		pointer = ">>";
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;

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

		// code zone
		Motion codeMotion = new Motion();
		codeMotion.movefrom = new Point(codeArea.x, codeArea.y);
		codeMotion.showtext = code;
		script.addScene(generateScene(codeID, EShape.TEXT, EAction.ADD, codeMotion));
		script.addScene(generateScene(codeID, EShape.TEXT, EAction.TEXT, codeMotion));
		Motion pointerMotion = new Motion();
		pointerMotion.movefrom = new Point(codeArea.x - 40, codeArea.y);
		pointerMotion.showtext = pointer;
		pointerMotion.colorto = Color.RED;
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.ADD, pointerMotion));
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.TEXT, pointerMotion));
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.COLOR, pointerMotion));

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
			script.addScene(generateScene(squaresIDs[i], EShape.SQUARE, EAction.MOVE, squareMotion));
			Motion circleMotion = new Motion();
			circleMotion.movefrom = new Point(0, 0);
			circleMotion.moveto = locations.get(i);
			circleMotion.showtext = Integer.toString(nums[i]);
			script.addScene(generateScene(circleIDs[i], EShape.CIRCLE, EAction.ADD, circleMotion));
			script.addScene(generateScene(circleIDs[i], EShape.CIRCLE, EAction.MOVE, circleMotion));
		}
		return script;
	}	
	
	public Script modifyArray(int index, int number){
		Script script = new Script();

		Motion pointerAssignMotion = new Motion();
		pointerAssignMotion.moveto = new Point(pointer_x, pointer_y + wordHeight);
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.MOVE, pointerAssignMotion));

		script.addScene(generateScene(circleIDs[index], EShape.CIRCLE, EAction.DELETE, new Motion()));

		Motion putMotion = new Motion();
		putMotion.movefrom = new Point(0, 0);
		putMotion.moveto = locations.get(index);
		putMotion.showtext = Integer.toString(number);
		circleIDs[index] = generateUniqueID();
		script.addScene(generateScene(circleIDs[index], EShape.CIRCLE, EAction.ADD, putMotion));
		script.addScene(generateScene(circleIDs[index], EShape.CIRCLE, EAction.MOVE, putMotion));

		script.addScene(generateWaitScene(1000));

		Motion pointerEndMotion = new Motion();
		pointerEndMotion.moveto = new Point(pointer_x, pointer_y + 2 * wordHeight);
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.MOVE, pointerEndMotion));

		script.addScene(generateWaitScene(1000));

		Motion pointerResetMotion = new Motion();
		pointerResetMotion.moveto = new Point(pointer_x, pointer_y);
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.MOVE, pointerResetMotion));

		// animation planning
		return script;
	}
}
