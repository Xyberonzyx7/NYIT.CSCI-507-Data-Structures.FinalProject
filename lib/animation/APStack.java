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
	private Stack circles;			// circles
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 60;
	private int middleX;
	private Point disappearPoint;
	private int codeID;
	private String code_push;
	private String code_pop;
	private Rectangle codeArea;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;

	public APStack(Rectangle rectAnimationArea){
		middleX = (int) (rectAnimationArea.getWidth() / 2);
		int nYMin = (int)(rectAnimationArea.getY() + MARGIN);
		int nYMax = (int)(rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);
		int y = nYMax;

		// init vairable
		squares = new Stack();
		circles = new Stack();
		locations = new ArrayList<Point>();
		disappearPoint = new Point(middleX, -20);

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
		code_pop += "    if (TOP = 0) then\n";
		code_pop += "        write (\"Underflow\");\n";
		code_pop += "    else\n";
		code_pop += "        ITEM = STACK [TOP];\n";
		code_pop += "        TOP := TOP - 1;\n";
		code_pop += "};";

		pointerID = generateUniqueID();
		pointer = ">>";
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;

		// get placeble locations
		while ( y >= nYMin){
			locations.add(new Point(middleX, y));
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
		pointerMotion.movefrom = new Point(pointer_x, pointer_y);
		pointerMotion.showtext = pointer;
		pointerMotion.colorto = Color.RED;
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.ADD, pointerMotion));
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.TEXT, pointerMotion));
		script.addScene(generateScene(pointerID, EShape.TEXT, EAction.COLOR, pointerMotion));

		script.addScene(generateMovePointerScene(0));


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

		return script;
	}

	public Script push(int number){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_push));
		script.addScene(generateMovePointerScene(3));		// code: if (top = maxstk) then

		script.addScene(generateWaitScene(1000));

		if(circles.isFull()){
			script.addScene(generateMovePointerScene(4));	// code: write("overflow");
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMovePointerScene(0)); 	// code: standby
			return script;
		}

		script.addScene(generateMovePointerScene(5));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMovePointerScene(6)); // code: top = top + 1aitScene(1000));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMovePointerScene(7)); // code: stack [top] = item;

		circles.push(generateUniqueID());
		Motion circleMotion = new Motion();
		circleMotion.movefrom = new Point(middleX, 0);
		circleMotion.moveto = locations.get(circles.size() - 1);
		circleMotion.showtext = Integer.toString(number);
		script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.ADD, circleMotion));
		script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.MOVE, circleMotion));

		script.addScene(generateWaitScene(1000));
		script.addScene(generateMovePointerScene(0)); // code: stack [top] = item;	script.addScene(generateScene(circles.peek(), EShape.CIRCLE, EAction.MOVE, circleMotion));
		return script;
	}

	public Script pop(){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_pop));
		script.addScene(generateMovePointerScene(3));		// code: if (top = 0) then

		script.addScene(generateWaitScene(1000));

		if(circles.isEmpty()){
			script.addScene(generateMovePointerScene(4));	// code: write ("underflow")
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMovePointerScene(0));	// code: standby
			return script;
		}

		script.addScene(generateMovePointerScene(5)); // code: else
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMovePointerScene(6)); // code: item = stack[top]

		// pop component from stack
		int poppedID = circles.pop();
		Motion circleMotion = new Motion();
		circleMotion.moveto = disappearPoint;
		script.addScene(generateScene(poppedID, EShape.CIRCLE, EAction.MOVE, circleMotion));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateScene(poppedID, EShape.CIRCLE, EAction.DELETE, circleMotion));

		script.addScene(generateMovePointerScene(7)); // code: top = top - 1
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMovePointerScene(0)); // code: top = top - 1
		script.addScene(generateWaitScene(1000));
		return script;
	}

	private Scene generateMovePointerScene(int line){
		Motion motion = new Motion();
		motion.moveto = new Point(pointer_x, pointer_y + wordHeight * line);
		return generateScene(pointerID, EShape.TEXT, EAction.MOVE, motion);
	}

	private Scene generateChangeCodeScene(String code){
		Motion motion = new Motion();
		motion.showtext = code;
		return generateScene(codeID, EShape.TEXT, EAction.TEXT, motion);
	}
}