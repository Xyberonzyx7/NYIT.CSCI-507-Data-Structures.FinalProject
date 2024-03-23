package lib.ap;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.components.*;
import lib.script.Command;
import lib.script.ECmd;
import lib.script.EShape;
import lib.script.Script;

// Animation Planner Array (AP Array), generate scripts
public class APArray {

	// storage
	private int[] nStorage;
	private List<Point> locations;
	private final int MARGIN = 200;
	private final int HORIZONTAL_SPACE = 60;
	private final int VERTICAL_SPACE = 200;

	public APArray(Rectangle rectAnimationArea){

		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

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

		nStorage = nums;

		// animation planning
		for(int i = 0; i < nStorage.length; i++){
			script.add(generateCmd("sq"+i, EShape.SQUARE, ECmd.ADD, new Point(0, 0), locations.get(i)));
			script.add(generateCmd("cir"+i, EShape.CIRCLE, ECmd.ADD, new Point(0, 0), locations.get(i)));
		}
		return script;
	}	
	
	public void modifyArray(int index, int number){
	}
	
	private Command generateCmd(String szName, EShape shape, ECmd cmd, Point start, Point end){
		Command command = new Command();
		command.szName = szName;
		command.shape = shape;
		command.cmd = cmd;
		command.start = start;
		command.end = end;
		return command;

	}
}
