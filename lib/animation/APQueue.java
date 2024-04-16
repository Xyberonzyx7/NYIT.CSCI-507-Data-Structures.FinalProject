package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.datastructure.CircularQueue;
import lib.script.*;

public class APQueue extends AnimationPlanner {
	private CircularQueue squares;	// squares' id
	private CircularQueue circles;	// circles' id
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int HORIZONTAL_SPACE = 60;
	private final int VERTICAL_SPACE = 60;
	private int middleY;
	private Point disappearPoint;
	private int codeID;
	private Rectangle codeArea;
	private String code_enqueue;
	private String code_dequeue;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;
	private int frontID;
	private String frontTxt;
	private int rearID;
	private String rearTxt;

	public APQueue(Rectangle rectAnimationArea){
		middleY = (int)(rectAnimationArea.getHeight() / 2);
		int nXMin = (int)(rectAnimationArea.getX() + MARGIN);
		int nXMax = (int)(rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int x = nXMin;

		// init variable
		disappearPoint = new Point(-20, -20);
		locations = new ArrayList<>();
		
		codeID = generateUniqueID();
		pointerID = generateUniqueID();
		frontID = generateUniqueID();
		rearID = generateUniqueID();
		
		codeArea = new Rectangle(rectAnimationArea.width - 350, 20, 350, 450);
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;
		
		code_enqueue = "STANDBY LINE\n";
		code_enqueue += "Algorithm ENQUEUE(Q, ITEM)\n";
		code_enqueue += "{\n";
		code_enqueue += "    if isFull() then\n";
		code_enqueue += "        write (\"Overflow\");\n";
		code_enqueue += "    else\n";
		code_enqueue += "        r = (f + sz) mod N;\n";
		code_enqueue += "        Q[r] = ITEM\n";
		code_enqueue += "        sz = sz + 1\n";
		code_enqueue += "}\n";
		
		code_dequeue = "STANDBY LINE\n";
		code_dequeue += "Algorithm DEQUE(Q, ref ITEM)\n";
		code_dequeue += "{\n";
		code_dequeue += "    if isEmpty() then\n";
		code_dequeue += "        write (\"Underflow\")\n";
		code_dequeue += "    else\n";
		code_dequeue += "        ITEM = Q[f]\n";
		code_dequeue += "        f = (f + 1) mod N\n";
		code_dequeue += "        sz = sz - 1\n";
		code_dequeue += "}\n";
		
		frontTxt = "f";
		rearTxt = "r";
		pointer = ">>";
		
		// get placeable locations
		while(x <= nXMax){
			locations.add(new Point(x, middleY));
			x += HORIZONTAL_SPACE;
		}
	}
	
	public Script initQueue(int capacity){
		
		Script script = new Script();

		if(capacity > 10){
			script.addScene(generatePopup("Data Structure Visualizer only supports queues with a maximum size of 10. Please reduce the size of the initial queue and try again."));
			return script;
		}
		
		circles = new CircularQueue(capacity);
		squares = new CircularQueue(capacity);

		// add code and pointer to animation panel
		script.addScene(generateAddScene(codeID, EShape.TEXT, codeArea.x, codeArea.y));
		script.addScene(generateTextScene(codeID, EShape.TEXT, code_enqueue));
		script.addScene(generateAddScene(pointerID, EShape.TEXT, pointer_x, pointer_y));
		script.addScene(generateTextScene(pointerID, EShape.TEXT, pointer));
		script.addScene(generateColorScene(pointerID, EShape.TEXT, Color.RED));
		script.addScene(generateMoveScene(pointerID, EShape.TEXT, pointer_x, pointer_y));


		// add squares to animation panel
		for(int i = 0; i < capacity; i++){
			squares.enqueue(generateUniqueID());
			script.addScene(generateAddScene(squares.peekRear(), EShape.SQUARE, 0, 0));
			script.addScene(generateMoveScene(squares.peekRear(), EShape.SQUARE, locations.get(i)));
		}

		// add front, rear to animation panel
		script.addScene(generateAddScene(frontID, EShape.TEXT, locations.get(circles.frontIndex()).x, locations.get(circles.frontIndex()).y + VERTICAL_SPACE));
		script.addScene(generateTextScene(frontID, EShape.TEXT, frontTxt));
		script.addScene(generateAddScene(rearID, EShape.TEXT, locations.get(circles.frontIndex()).x, locations.get(circles.frontIndex()).y + VERTICAL_SPACE + 30));
		script.addScene(generateTextScene(rearID, EShape.TEXT, rearTxt));

		return script;
	}

	public Script enqueue(int number){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_enqueue));

		script.addScene(generateMoveCodePointerScene(3));	// code: if isFull() then
		script.addScene(generateWaitScene(1000));

		if(circles.isFull()){
			script.addScene(generateMoveCodePointerScene(4));	// code: write ("Overflow");
			script.addScene(generateWaitScene(1000));
			script.addScene(generatePopup("Overflow"));
			script.addScene(generateMoveCodePointerScene(0));	// code: standby line
			return script;
		}

		circles.enqueue(generateUniqueID());

		// animaion
		script.addScene(generateMoveCodePointerScene(5));		// code: else
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(6));		// code: r = (f+sz) mod N;
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveRearScene(circles.rearIndex()));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(7));		// code: Q[r] = ITEM
		script.addScene(generateWaitScene(1000));
		script.addScene(generateAddScene(circles.peekRear(), EShape.CIRCLE, 0, 0));
		script.addScene(generateMoveScene(circles.peekRear(), EShape.CIRCLE, locations.get(circles.rearIndex())));
		script.addScene(generateTextScene(circles.peekRear(), EShape.CIRCLE, Integer.toString(number)));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(8));		// code: sz = sz + 1
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(0));		// code: standby line

		return script;
	}

	public Script dequeue(){
		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_dequeue));

		script.addScene(generateMoveCodePointerScene(3));		// code: if isEmpty() then
		script.addScene(generateWaitScene(1000));

		if(circles.isEmpty()){
			script.addScene(generateMoveCodePointerScene(4));	// code: write ("Underflow");
			script.addScene(generateWaitScene(1000));
			script.addScene(generatePopup("Underflow"));
			script.addScene(generateMoveCodePointerScene(0));	// code: standby line
			return script;
		}

		int dequeuedID = circles.dequeue();

		script.addScene(generateMoveCodePointerScene(5));		// code: else
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(6));		// code: item = q[f]
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveScene(dequeuedID, EShape.CIRCLE, disappearPoint));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateDeleteScene(dequeuedID, EShape.CIRCLE));
		script.addScene(generateMoveCodePointerScene(7));		// code: f = (f+1) mod N
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveFrontScene(circles.frontIndex()));
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(8));		// code: sz = sz - 1
		script.addScene(generateWaitScene(1000));
		script.addScene(generateMoveCodePointerScene(0));		// code: standby line

		return script;
	}

	private Scene generateMoveCodePointerScene(int line){
		Motion motion = new Motion();
		motion.moveto = new Point(pointer_x, pointer_y + line * wordHeight);
		return generateScene(pointerID, EShape.TEXT, EAction.MOVE, motion);
	}

	private Scene generateMoveFrontScene(int index){
		Motion motion = new Motion();
		motion.moveto = new Point(locations.get(index).x, locations.get(index).y + VERTICAL_SPACE);
		return generateScene(frontID, EShape.TEXT, EAction.MOVE, motion);
	}

	private Scene generateMoveRearScene(int index){
		Motion motion = new Motion();
		motion.moveto = new Point(locations.get(index).x, locations.get(index).y + VERTICAL_SPACE + 30);
		return generateScene(rearID, EShape.TEXT, EAction.MOVE, motion);
	}

	private Scene generateChangeCodeScene(String code) {
		Motion motion = new Motion();
		motion.showtext = code;
		return generateScene(codeID, EShape.TEXT, EAction.TEXT, motion);
	}
}
