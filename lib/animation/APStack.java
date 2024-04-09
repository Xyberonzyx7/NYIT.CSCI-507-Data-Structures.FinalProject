package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.script.*;

import lib.datastructure.Stack;

public class APStack extends AnimationPlanner {

	private Stack squares; 	// squares
	private Stack circles;	// circles
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 60;
	private int component_x;
	private Point disappearPoint;
	private int codeID;
	private String code_push;
	private String code_pop;
	private Rectangle codeArea;
	private int pointerTxtID;
	private String pointerTxt;
	private int pointerTxt_x;
	private int pointerTxt_y;
	private int maxStackTxtID;
	private String maxStackTxt;
	private int maxStackTxt_x;
	private int emptyStackTxtID;
	private String emptyStackTxt;
	private int emptyStackTxt_x;
	private int topTxtID;
	private String topTxt;
	private int topTxt_x;
	private int arrowID;
	private int arrow_x;

	public APStack(Rectangle rectAnimationArea){
		component_x = (int) (rectAnimationArea.getWidth() / 2) - 150;
		int nYMin = (int)(rectAnimationArea.getY() + MARGIN - 50);
		int nYMax = (int)(rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);
		int y = nYMax;

		// init vairable
		squares = new Stack();
		circles = new Stack();
		locations = new ArrayList<Point>();
		disappearPoint = new Point(component_x, -20);

		codeID = generateUniqueID();
		codeArea = new Rectangle(rectAnimationArea.width - 350, 20, 350, 450);
		code_push = "STANDBY LINE\n"; 
		code_push += "Algorithm PUSH(STACK, ITEM)\n";
		code_push += "{\n";
		code_push += "    if (TOP = MAXSTK) then\n";
		code_push += "        write (\"Overflow\");\n";
		code_push += "    else\n";
		code_push += "        TOP := TOP + 1;\n";
		code_push += "        STACK [TOP] := ITEM;\n";
		code_push += "}";
		code_pop = "STANDBY LINE\n"; 
		code_pop += "Algorith POP (STACK, ITEM)\n";
		code_pop += "{\n";
		code_pop += "    if (TOP = EMPTYSTK) then\n";
		code_pop += "        write (\"Underflow\");\n";
		code_pop += "    else\n";
		code_pop += "        ITEM = STACK [TOP];\n";
		code_pop += "        TOP := TOP - 1;\n";
		code_pop += "};";

		pointerTxtID = generateUniqueID();
		pointerTxt = ">>";
		pointerTxt_x = codeArea.x - 40;
		pointerTxt_y = codeArea.y;

		maxStackTxtID = generateUniqueID();
		emptyStackTxtID = generateUniqueID();
		topTxtID = generateUniqueID();
		arrowID = generateUniqueID();

		maxStackTxt_x = component_x + 50;
		emptyStackTxt_x = component_x + 50;
		topTxt_x = component_x - 200;
		arrow_x = component_x - 100;

		maxStackTxt = "MAXSTK";
		emptyStackTxt = "EMPTYSTK";
		topTxt = "TOP";

		// get placeble locations
		while ( y >= nYMin){
			locations.add(new Point(component_x, y));
			y -= VERTICAL_SPACE;
		} 
	}

	public Script initStack(int capacity){
		Script script = new Script();		

		squares.setSize(capacity);
		circles.setSize(capacity);

		// code zone
		Motion codeMotion = new Motion();
		codeMotion.movefrom = new Point(codeArea.x, codeArea.y);
		codeMotion.showtext = code_push;
		script.addScene(generateScene(codeID, EShape.TEXT, EAction.ADD, codeMotion));
		script.addScene(generateScene(codeID, EShape.TEXT, EAction.TEXT, codeMotion));
		Motion pointerMotion = new Motion();
		pointerMotion.movefrom = new Point(pointerTxt_x, pointerTxt_y);
		pointerMotion.showtext = pointerTxt;
		pointerMotion.colorto = Color.RED;
		script.addScene(generateScene(pointerTxtID, EShape.TEXT, EAction.ADD, pointerMotion));
		script.addScene(generateScene(pointerTxtID, EShape.TEXT, EAction.TEXT, pointerMotion));
		script.addScene(generateScene(pointerTxtID, EShape.TEXT, EAction.COLOR, pointerMotion));

		script.addScene(generateMoveCodePointerScene(0));

		// init map with squares
		while(squares.isEmpty() == false){
			int poppedID = squares.pop();
			script.addScene(generateDeleteScene(poppedID, EShape.SQUARE));
		}

		while(circles.isEmpty() == false){
			int poppedID = circles.pop();
			script.addScene(generateDeleteScene(poppedID, EShape.CIRCLE));
		}

		for(int i = 0; i < capacity; i++){
			squares.push(generateUniqueID());

			// animation planning
			Motion squareMotion = new Motion();
			squareMotion.movefrom = new Point(0, 0);
			squareMotion.moveto = locations.get(i);
			script.addScene(generateScene(squares.peek(), EShape.SQUARE, EAction.ADD, squareMotion));
			script.addScene(generateScene(squares.peek(), EShape.SQUARE, EAction.MOVE, squareMotion));
		}

		// add top, arrow, maxsteak, emptysteak indicator
		script.addScene(generateAddScene(topTxtID, EShape.TEXT, topTxt_x, (int)locations.get(0).getY() + VERTICAL_SPACE));
		script.addScene(generateTextScene(topTxtID, EShape.TEXT, topTxt));
		script.addScene(generateAddScene(arrowID, EShape.ARROW, arrow_x, (int)locations.get(0).getY() + VERTICAL_SPACE, 180));
		script.addScene(generateAddScene(maxStackTxtID, EShape.TEXT, maxStackTxt_x, (int)locations.get(capacity).getY() + VERTICAL_SPACE));
		script.addScene(generateTextScene(maxStackTxtID, EShape.TEXT, maxStackTxt));
		script.addScene(generateAddScene(emptyStackTxtID, EShape.TEXT, emptyStackTxt_x, (int)locations.get(0).getY() + VERTICAL_SPACE));
		script.addScene(generateTextScene(emptyStackTxtID, EShape.TEXT, emptyStackTxt));

		return script;
	}

	public Script push(int number){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_push));
		script.addScene(generateMoveCodePointerScene(3));		// code: if (top = maxstk) then

		script.addScene(generateWaitScene(1000));

		if(circles.isFull()){
			script.addScene(generateMoveCodePointerScene(4));	// code: write("overflow");
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMoveCodePointerScene(0)); 	// code: standby
			return script;
		}

		circles.push(generateUniqueID());

		script.addScene(generateMoveCodePointerScene(5));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(6)); 		// code: top = top + 1aitScene(1000));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveDataPointerScene(circles.size()-1));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(7)); 		// code: stack [top] = item;
		script.addScene(generateWaitScene(1000));

		Motion circleMotion = new Motion();
		circleMotion.movefrom = new Point(component_x, 0);
		circleMotion.moveto = locations.get(circles.size() - 1);
		circleMotion.showtext = Integer.toString(number);
		script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.ADD, circleMotion));
		script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.MOVE, circleMotion));

		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(0)); // code: stack [top] = item;	script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.MOVE, circleMotion));
		return script;
	}

	public Script pop(){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_pop));
		script.addScene(generateMoveCodePointerScene(3));		// code: if (top = 0) then

		script.addScene(generateWaitScene(1000));

		if(circles.isEmpty()){
			script.addScene(generateMoveCodePointerScene(4));	// code: write ("underflow")
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMoveCodePointerScene(0));	// code: standby
			return script;
		}

		int poppedID = circles.pop();

		script.addScene(generateMoveCodePointerScene(5)); // code: else
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(6)); // code: item = stack[top]
		script.addScene(generateWaitScene(1000));

		// pop component from stack
		Motion circleMotion = new Motion();
		circleMotion.moveto = disappearPoint;
		script.addScene(generateScene(poppedID, EShape.CIRCLE, EAction.MOVE, circleMotion));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateScene(poppedID, EShape.CIRCLE, EAction.DELETE, circleMotion));

		script.addScene(generateMoveCodePointerScene(7)); // code: top = top - 1
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveDataPointerScene(circles.size() - 1));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(0)); // code: standby
		return script;
	}

	private Scene generateMoveCodePointerScene(int line){
		Motion motion = new Motion();
		motion.moveto = new Point(pointerTxt_x, pointerTxt_y + wordHeight * line);
		return generateScene(pointerTxtID, EShape.TEXT, EAction.MOVE, motion);
	}

	private Scene generateChangeCodeScene(String code){
		Motion motion = new Motion();
		motion.showtext = code;
		return generateScene(codeID, EShape.TEXT, EAction.TEXT, motion);
	}

	private List<Scene> generateMoveDataPointerScene(int top){ // top = 0 first item, top = 1 second item...
		
		List<Scene> scenes = new ArrayList<>();
		Motion topMotion = new Motion();
		topMotion.moveto = new Point(topTxt_x, (int)locations.get(0).getY() - top * VERTICAL_SPACE);
		scenes.add(generateScene(topTxtID, EShape.TEXT, EAction.MOVE, topMotion));
		Motion arrowMotion = new Motion();
		arrowMotion.moveto = new Point(arrow_x, (int)locations.get(0).getY() - top * VERTICAL_SPACE);
		scenes.add(generateScene(arrowID, EShape.ARROW, EAction.MOVE, arrowMotion));

		return scenes;
	}
}