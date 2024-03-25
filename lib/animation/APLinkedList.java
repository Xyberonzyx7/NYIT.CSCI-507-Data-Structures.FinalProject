package lib.animation;

import java.awt.Point;
import java.awt.Rectangle;
import java.security.spec.EdDSAParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import lib.datastructure.SinglyLinkedList;
import lib.script.*;

public class APLinkedList extends AnimationPlanner {
	
	private SinglyLinkedList sll;
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 100;
	private final int HORIZONTAL_SPACE = 100;

	public APLinkedList(Rectangle rectAnimationArea){
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMin = (int) (rectAnimationArea.getY() + MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		sll = new SinglyLinkedList();
		locations = new ArrayList<>();

		// get placeable locations
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

	public Script initLinkedList(int[] nums){
		Script script = new Script();

		// init sll
		sll = new SinglyLinkedList();
		for(int i = 0; i < nums.length; i++){
			sll.insertAt(i, generateUniqueID());
		}

		// animation planning
		for(int i = 0; i < nums.length; i++){
			script.addScene(generateScene(sll.getAt(i), EShape.CIRCLE, EAction.ADD, new Point(0, 0), locations.get(i), Integer.toString(nums[i])));
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
}
