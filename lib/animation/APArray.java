package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.script.*;


// Animation Planner Array (AP Array), generate scripts
public class APArray extends AnimationPlanner{

	private int[] squaresIDs;
	private int[] circleIDs;
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int HORIZONTAL_SPACE = 60;
	private final int VERTICAL_SPACE = 60;
	private int capacity;
	private int codeID;
	private String code;
	private Rectangle codeArea;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;
	private int startID;
	private String startTxt;
	private int endID;
	private String endTxt;

	public APArray(Rectangle rectAnimationArea){
		
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		objCount = 0;
		codeArea = new Rectangle(rectAnimationArea.width - 450, 20, 450, 450);
		codeID = generateUniqueID();
		code = "STANDBY LINE\n";
		code += "Algorithm SET(ARRAY, INDEX, ITEM):\n";
		code += "{\n";
		code += "    if (INDEX > MAXSIZE or INDEX < 0) then\n";
		code += "        write (\"Index Out of Range\");\n";
		code += "    else\n";
		code += "        ARRAY[INDEX] = ITEM;\n";
		code += "}";
		pointerID = generateUniqueID();
		pointer = ">>";
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;

		startID = generateUniqueID();
		startTxt = "0";
		endID = generateUniqueID();
		endTxt = "MAXSIZE";

		// get placable available locations
		locations = new ArrayList<>();
		int x = nXMin;
		int y = (nYMin + nYMax) / 2;
		while (x < nXMax){
			locations.add(new Point(x, y));
			x += HORIZONTAL_SPACE;
		}
	}

	public Script initArray(int[] nums){
		Script script = new Script();

		// add code to animation panel
		script.addScene(generateAddScene(codeID, EShape.TEXT, codeArea.x, codeArea.y));
		script.addScene(generateTextScene(codeID, EShape.TEXT, code));
		script.addScene(generateAddScene(pointerID, EShape.TEXT, pointer_x, pointer_y));
		script.addScene(generateTextScene(pointerID, EShape.TEXT, pointer));
		script.addScene(generateColorScene(pointerID, EShape.TEXT, Color.RED));

		capacity = nums.length;

		squaresIDs = new int[capacity];
		circleIDs = new int[capacity];
		for(int i = 0; i < capacity; i++){
			squaresIDs[i] = generateUniqueID();
			circleIDs[i] = generateUniqueID();
		}

		// add circles and squares to animation panel
		for(int i = 0; i < capacity; i++){
			script.addScene(generateAddScene(squaresIDs[i], EShape.SQUARE, 0, 0));
			script.addScene(generateMoveScene(squaresIDs[i], EShape.SQUARE, locations.get(i).x, locations.get(i).y));
			script.addScene(generateAddScene(circleIDs[i], EShape.CIRCLE, 0, 0));
			script.addScene(generateMoveScene(circleIDs[i], EShape.CIRCLE, locations.get(i).x, locations.get(i).y));
			script.addScene(generateTextScene(circleIDs[i], EShape.CIRCLE, Integer.toString(nums[i])));
		}

		// add start and end indicator to animation panel
		script.addScene(generateAddScene(startID, EShape.TEXT, locations.get(0).x-5, locations.get(0).y + VERTICAL_SPACE));
		script.addScene(generateTextScene(startID, EShape.TEXT, startTxt));
		script.addScene(generateAddScene(endID, EShape.TEXT, locations.get(capacity-1).x - 45, locations.get(capacity-1).y + VERTICAL_SPACE));
		script.addScene(generateTextScene(endID, EShape.TEXT, endTxt));
		return script;
	}	
	
	public Script modifyArray(int index, int number){

		Script script = new Script();

		script.addScene(generateMoveCodePointerScene(3));		// code: if (INDEX > MAXSIZE or INDEX < 0) then
		script.addScene(generateWaitScene(1000));

		if(index >= capacity || index < 0){
			script.addScene(generateMoveCodePointerScene(4));	// code: write ("Out Range");
			script.addScene(generateWaitScene(1000));
			script.addScene(generatePopup("Index Out of Range"));
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMoveCodePointerScene(0));	// code: standby
			return script;
		}

		script.addScene(generateMoveCodePointerScene(5));		// code: else
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(6));		// code: ARRAY[INDEX] = ITEM;
		script.addScene(generateWaitScene(1000));

		// array assign animation
		script.addScene(generateDeleteScene(circleIDs[index], EShape.CIRCLE));
		circleIDs[index] = generateUniqueID();
		script.addScene(generateAddScene(circleIDs[index], EShape.CIRCLE, 0, 0));	
		script.addScene(generateMoveScene(circleIDs[index], EShape.CIRCLE, locations.get(index).x, locations.get(index).y));	
		script.addScene(generateTextScene(circleIDs[index], EShape.CIRCLE, Integer.toString(number)));
		script.addScene(generateWaitScene(1000));

		script.addScene(generateMoveCodePointerScene(0));		// code: ARRAY[INDEX] = ITEM;

		// animation planning
		return script;
	}

	private Scene generateMoveCodePointerScene(int line){
		Motion motion = new Motion();
		motion.moveto = new Point(pointer_x, pointer_y + line * wordHeight);
		return generateScene(pointerID, EShape.TEXT, EAction.MOVE, motion);
	}
}
