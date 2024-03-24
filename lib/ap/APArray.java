package lib.ap;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.components.*;
import lib.script.Scene;
import lib.script.EAction;
import lib.script.EShape;
import lib.script.Script;

// Animation Planner Array (AP Array), generate scripts
public class APArray {

	// storage
	private int[] nStorage;
	private List<Point> locations;
	private final int MARGIN = 100;
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
			script.add(generateCmd("sq"+i, EShape.SQUARE, EAction.ADD, new Point(0, 0), locations.get(i), ""));
			script.add(generateCmd("cir"+i, EShape.CIRCLE, EAction.ADD, new Point(0, 0), locations.get(i), Integer.toString(i)));
		}
		return script;
	}	
	
	public void modifyArray(int index, int number){
	}
	
	private Scene generateCmd(String szName, EShape shape, EAction action, Point start, Point end, String txt){
		Scene scene = new Scene();
		scene.szName = szName;
		scene.shape = shape;
		scene.action = action;
		scene.start = start;
		scene.end = end;
		scene.txt = txt;
		return scene;

	}
}
