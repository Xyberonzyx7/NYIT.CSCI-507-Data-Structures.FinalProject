package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import lib.datastructure.SinglyLinkedList;
import lib.script.*;

public class APLinkedList extends AnimationPlanner {
	
	private SinglyLinkedList sll_node;
	private SinglyLinkedList sll_arrow;
	private List<Point> locations;
	private final int MARGIN = 100;
	private final int VERTICAL_SPACE = 50;
	private final int HORIZONTAL_SPACE = 100;
	private int spawn_x;
	private int spawn_y;
	private Rectangle codeArea;
	private int codeID;
	private String code_insertAt;
	private String code_removeAt;
	private int pointerID;
	private String pointer;
	private int pointer_x;
	private int pointer_y;
	private int nullTxtID;
	private String nullTxt;
	private int headTxtID;
	private String headTxt;
	private int head_y;

	public APLinkedList(Rectangle rectAnimationArea){
		int nXMin = (int) (rectAnimationArea.getX() + MARGIN);
		int nXMax = (int) (rectAnimationArea.getX() + rectAnimationArea.getWidth() - MARGIN);
		int nYMax = (int) (rectAnimationArea.getY() + rectAnimationArea.getHeight() - MARGIN);

		// init variable
		sll_node = new SinglyLinkedList();
		sll_arrow = new SinglyLinkedList();
		locations = new ArrayList<>();

		codeArea = new Rectangle(rectAnimationArea.width - 410, 20, 410, 450);
		codeID = generateUniqueID();
		code_insertAt = "STANDBY LINE\n";
		code_insertAt += "Algorithm INSERT(L, INDEX, ITEM)\n";
		code_insertAt += "{\n";
		code_insertAt += "    if (INDEX > L.size) then\n";
		code_insertAt += "        write (\"index out of range\");\n";
		code_insertAt += "        return;\n";
		code_insertAt += "    newest = Node(ITEM);\n";
		code_insertAt += "    if (INDEX == 0) then\n";
		code_insertAt += "        newest.next = L.head;\n";
		code_insertAt += "        L.head = newest;\n";
		code_insertAt += "    else\n";
		code_insertAt += "        current = L.head;\n";
		code_insertAt += "        for i in (INDEX - 1) and current != null\n";
		code_insertAt += "            current = current.next;\n";
		code_insertAt += "        if (current != null) then\n";
		code_insertAt += "            newest.next = current.next;\n";
		code_insertAt += "            current.next = newest;\n";
		code_insertAt += "}\n";

		code_removeAt = "STANDBY LINE\n";
		code_removeAt += "Algorithm DELETE(L, INDEX)\n";
		code_removeAt += "{\n";
		code_removeAt += "    if (INDEX >= L.size) then\n";
		code_removeAt += "        write (\"index out of range\");\n";
		code_removeAt += "        return;\n";
		code_removeAt += "    if (INDEX == 0) then\n";
		code_removeAt += "        L.head = L.head.next;\n";
		code_removeAt += "    else\n";
		code_removeAt += "        current = L.head;\n";
		code_removeAt += "        for i in (INDEX - 1) and current != null\n";
		code_removeAt += "            current = current.next;\n";
		code_removeAt += "        if (current != null and current.next != null) then\n";
		code_removeAt += "            current.next = current.next.next;\n";
		code_removeAt += "}\n";

		pointerID = generateUniqueID();
		pointer = ">>";
		pointer_x = codeArea.x - 40;
		pointer_y = codeArea.y;

		nullTxtID = generateNullID();
		nullTxt = "NULL";

		headTxtID = generateUniqueID();
		headTxt = "head";
		head_y = nYMax - 3 * VERTICAL_SPACE;

		// get placeable locations
		locations = new ArrayList<>();
		int x = nXMin;
		int y = nYMax - 2 * VERTICAL_SPACE;
		while (x < nXMax){
			locations.add(new Point(x, y));
			x += HORIZONTAL_SPACE;
		}

		spawn_x = nXMin;
		spawn_y = nYMax;
	}

	public Script initLinkedList(int[] nums){

		Script script = new Script();

		script.addScene(generateAddScene(codeID, EShape.TEXT, codeArea.x, codeArea.y));
		script.addScene(generateChangeCodeScene(code_insertAt));
		script.addScene(generateAddScene(pointerID, EShape.TEXT, pointer_x, pointer_y));
		script.addScene(generateTextScene(pointerID, EShape.TEXT, pointer));
		script.addScene(generateColorScene(pointerID, EShape.TEXT, Color.RED));

		// add circles and arrows
		sll_node = new SinglyLinkedList();
		sll_arrow = new SinglyLinkedList();
		for(int i = 0; i < nums.length; i++){
			int nodeID = generateUniqueID();
			int arrowID = generateUniqueID();
			sll_node.insertAt(i, nodeID);
			sll_arrow.insertAt(i, arrowID);

			// animation
			script.addScene(generateAddScene(nodeID, EShape.CIRCLE, 0, 0));
			script.addScene(generateMoveScene(nodeID, EShape.CIRCLE, locations.get(i)));
			script.addScene(generateTextScene(nodeID, EShape.CIRCLE, Integer.toString(nums[i])));
			script.addScene(generateAddScene(arrowID, EShape.ARROW, 0, 0, 180));
			script.addScene(generateMoveScene(arrowID, EShape.ARROW, getMiddlePoint(locations.get(i), locations.get(i+1))));
		}

		// add Null animation
		script.addScene(generateAddScene(nullTxtID, EShape.TEXT, 0, 0));
		script.addScene(generateTextScene(nullTxtID, EShape.TEXT, nullTxt));
		script.addScene(generateMoveScene(nullTxtID, EShape.TEXT, locations.get(nums.length).x - 20, locations.get(nums.length).y));

		// add head animation
		script.addScene(generateAddScene(headTxtID, EShape.TEXT, 0, 0));
		script.addScene(generateTextScene(headTxtID, EShape.TEXT, headTxt));
		script.addScene(generateMoveScene(headTxtID, EShape.TEXT, locations.get(0).x - 20, head_y));

		return script;
	}

	public Script insertAt(int index, int data){

		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_insertAt));

		script.addScene(generateMoveCodePointerScene(3));	// code: if (index > l.size) then

		// protection
		if(index > sll_node.getSize()){
			script.addScene(generateMoveCodePointerScene(4));	// code: write ("index out of range");
			script.addScene(generateMoveCodePointerScene(5));	// code: return
			script.addScene(generateMoveCodePointerScene(0));	// code: standby line
			return script;
		}

		// modify the list before animation
		sll_node.insertAt(index, generateUniqueID());
		sll_arrow.insertAt(index, generateUniqueID());

		script.addScene(generateMoveCodePointerScene(6));		// code: newest = Node(item);
		script.addScene(generateAddScene(sll_node.getAt(index), EShape.CIRCLE, 0, 0));
		script.addScene(generateMoveScene(sll_node.getAt(index), EShape.CIRCLE, spawn_x, spawn_y));
		script.addScene(generateTextScene(sll_node.getAt(index), EShape.CIRCLE, Integer.toString(data)));
		script.addScene(generateWaitScene(1000));

		script.addScene(generateMoveCodePointerScene(7));		// code: if (index == 0) then
		if(index == 0){
			script.addScene(generateMoveCodePointerScene(8));	// code: newest.next = L.head
			script.addScene(generateAddScene(sll_arrow.getAt(index), EShape.ARROW, 0, 0));
			script.addScene(generateMoveArrowScene(sll_arrow.getAt(index), EShape.ARROW, new Point(spawn_x, spawn_y), locations.get(index)));
			script.addScene(generateWaitScene(1000));

			script.addScene(generateMoveCodePointerScene(9));	// code: L.head = newest;
			script.addScene(generateMoveScene(sll_node.getAt(index), EShape.CIRCLE, locations.get(index)));
			script.addScene(generateMoveArrowScene(sll_arrow.getAt(index), EShape.ARROW, locations.get(index), locations.get(index+1)));
			for(int i = index + 1; i < sll_node.getSize(); i++){
				script.addScene(generateMoveScene(sll_node.getAt(i), EShape.CIRCLE, locations.get(i)));
				script.addScene(generateMoveScene(sll_arrow.getAt(i), EShape.CIRCLE, getMiddlePoint(locations.get(i), locations.get(i+1))));
			}
			script.addScene(generateMoveScene(nullTxtID, EShape.TEXT, locations.get(sll_node.getSize()).x - 20, locations.get(sll_node.getSize()).y));
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMoveCodePointerScene(0));	// code: standby line
			return script;
		}

		script.addScene(generateMoveCodePointerScene(10)); // code: else
		script.addScene(generateMoveCodePointerScene(11)); // code: current = l.head;
		script.addScene(generateHighlightScene(sll_node.getAt(0), EShape.CIRCLE, Color.BLUE, Color.RED));

		script.addScene(generateMoveCodePointerScene(12)); // code: for i in (index - 1) and current != null

		int currentIndex = 0;
		for(int i = 1; i < index; i++){
			script.addScene(generateMoveCodePointerScene(13)); // code: current = current.next;
			script.addScene(generateHighlightScene(sll_node.getAt(i), EShape.CIRCLE, Color.BLUE, Color.RED));
			if(i == index - 1){
				currentIndex = i;
			}
			script.addScene(generateMoveCodePointerScene(12)); // code: for i in (index - 1) and current != null
		}
		script.addScene(generateFlashingScene(sll_node.getAt(currentIndex), EShape.CIRCLE, Color.BLUE, Color.YELLOW));
		script.addScene(generateMoveCodePointerScene(14)); // code: if (current != null) then

		script.addScene(generateMoveCodePointerScene(15)); // code: newest.next = current.next
		script.addScene(generateAddScene(sll_arrow.getAt(index), EShape.ARROW, 0, 0, getAngle(new Point(spawn_x, spawn_y), locations.get(index))));
		script.addScene(generateMoveArrowScene(sll_arrow.getAt(index), EShape.ARROW, new Point(spawn_x, spawn_y), locations.get(index)));
		script.addScene(generateWaitScene(1000));

		script.addScene(generateMoveCodePointerScene(16)); // code: current.next = newest
		script.addScene(generateMoveArrowScene(sll_arrow.getAt(currentIndex), EShape.ARROW, locations.get(currentIndex), new Point(spawn_x, spawn_y)));
		script.addScene(generateWaitScene(1000));

		// move the rest of the node backward including NULL
		for(int i = index + 1; i < sll_node.getSize(); i++){
			script.addScene(generateMoveScene(sll_node.getAt(i), EShape.CIRCLE, locations.get(i)));
			script.addScene(generateMoveScene(sll_arrow.getAt(i), EShape.ARROW, getMiddlePoint(locations.get(i), locations.get(i+1))));
		}
		script.addScene(generateMoveScene(nullTxtID, EShape.TEXT, locations.get(sll_node.getSize()).x - 20, locations.get(sll_node.getSize()).y));

		// move the newest node to the vacancy
		script.addScene(generateMoveScene(sll_node.getAt(index), EShape.CIRCLE, locations.get(index)));
		script.addScene(generateMoveArrowScene(sll_arrow.getAt(index), EShape.ARROW, locations.get(index), locations.get(index+1)));
		script.addScene(generateMoveArrowScene(sll_arrow.getAt(currentIndex), EShape.ARROW, locations.get(currentIndex), locations.get(currentIndex+1)));

		script.addScene(generateMoveCodePointerScene(0)); // code: if (current != null) then
		
		return script;
	}

	public Script removeAt(int index){

		Script script = new Script();

		script.addScene(generateChangeCodeScene(code_removeAt));

		script.addScene(generateMoveCodePointerScene(3)); // code: if (index >= l.size) then

		if(index >= sll_node.getSize()){
			script.addScene(generateMoveCodePointerScene(4)); // code: write ("index out of range");
			script.addScene(generateMoveCodePointerScene(5)); // code: return;
			script.addScene(generateMoveCodePointerScene(0)); // code: standby line
			return script;
		}

		script.addScene(generateMoveCodePointerScene(6)); // code: if (index == 0) then
		if(index == 0){
			script.addScene(generateMoveCodePointerScene(7)); // code: l.head = l.head.next;
			script.addScene(generateMoveScene(headTxtID, EShape.TEXT, locations.get(1).x - 20, head_y));
			script.addScene(generateWaitScene(1000));
			
			// delete index 0 node
			script.addScene(generateDeleteScene(sll_node.getAt(index), EShape.CIRCLE));
			script.addScene(generateDeleteScene(sll_arrow.getAt(index), EShape.ARROW));

			sll_node.removeAt(index);
			sll_arrow.removeAt(index);

			// move whole list backward including null text
			script.addScene(generateMoveScene(headTxtID, EShape.TEXT, locations.get(0).x - 20, head_y));
			for(int i = 0; i < sll_node.getSize(); i++){
				script.addScene(generateMoveScene(sll_node.getAt(i), EShape.CIRCLE, locations.get(i)));	
				script.addScene(generateMoveScene(sll_arrow.getAt(i), EShape.ARROW, getMiddlePoint(locations.get(i), locations.get(i+1))));	
			}
			script.addScene(generateMoveScene(nullTxtID, EShape.TEXT, locations.get(sll_node.getSize()).x - 20, locations.get(sll_node.getSize()).y));
			script.addScene(generateWaitScene(1000));
			script.addScene(generateMoveCodePointerScene(0)); 	// code: standby line
			return script;
		}else{
			script.addScene(generateMoveCodePointerScene(8));	// code: else
			script.addScene(generateMoveCodePointerScene(9));	// code: current = current.next
			script.addScene(generateHighlightScene(sll_node.getAt(0), EShape.CIRCLE, Color.BLUE, Color.RED));
			script.addScene(generateMoveCodePointerScene(10));	// code: for i in (index-1) and current != null
			
			int currentIndex = 0;

			for(int i = 1; i < index; i++){
				script.addScene(generateMoveCodePointerScene(11));	// code: current = current.next
				script.addScene(generateHighlightScene(sll_node.getAt(i), EShape.CIRCLE, Color.BLUE, Color.RED));
				if(i == index-1){
					currentIndex = i;
				}
				script.addScene(generateMoveCodePointerScene(10)); // code: for i in (index-1) and current != null
			}
			script.addScene(generateFlashingScene(sll_node.getAt(currentIndex), EShape.CIRCLE, Color.BLUE, Color.YELLOW));
			script.addScene(generateMoveCodePointerScene(12)); // code: if (current != null and current.next != null) then

			script.addScene(generateMoveCodePointerScene(13)); // code: current.next = current.next.next;
			script.addScene(generateDeleteScene(sll_node.getAt(index), EShape.CIRCLE));
			script.addScene(generateDeleteScene(sll_arrow.getAt(index), EShape.ARROW));
			script.addScene(generateMoveArrowScene(sll_arrow.getAt(currentIndex), EShape.ARROW, locations.get(currentIndex), locations.get(index+1)));
			script.addScene(generateWaitScene(1000));

			// delete node
			sll_node.removeAt(index);
			sll_arrow.removeAt(index);

			// rearrange linked list
			script.addScene(generateMoveArrowScene(sll_arrow.getAt(currentIndex), EShape.ARROW, locations.get(currentIndex), locations.get(index)));
			for(int i = index; i < sll_node.getSize(); i++){
				script.addScene(generateMoveScene(sll_node.getAt(i), EShape.CIRCLE, locations.get(i)));
				script.addScene(generateMoveArrowScene(sll_arrow.getAt(i), EShape.CIRCLE, locations.get(i), locations.get(i+1)));
			}
			script.addScene(generateMoveScene(nullTxtID, EShape.TEXT, locations.get(sll_node.getSize()).x - 20, locations.get(sll_node.getSize()).y));
			script.addScene(generateMoveCodePointerScene(0)); 	// code: standby line
			return script;
		}
	}
	
	private Scene generateChangeCodeScene(String code){
		Motion motion = new Motion();
		motion.showtext = code;
		return generateScene(codeID, EShape.TEXT, EAction.TEXT, motion);
	}

	private List<Scene> generateMoveCodePointerScene(int line){
		List<Scene> scenes = new ArrayList<>();
		Motion motion = new Motion();
		motion.moveto = new Point(pointer_x, pointer_y + wordHeight * line);
		scenes.add(generateScene(pointerID, EShape.TEXT, EAction.MOVE, motion));
		scenes.add(generateWaitScene(1000));
		return scenes;
	}

	private List<Scene> generateMoveArrowScene(int id, EShape shape, Point start, Point end){
		List<Scene> scenes = new ArrayList<>();
		scenes.add(generateMoveScene(id, EShape.ARROW, getMiddlePoint(start, end)));
		scenes.add(generateRotateScene(id, EShape.ARROW, getAngle(start, end)));
		scenes.add(generateLengthScene(id, EShape.ARROW, getLength(start, end) - 50));
		return scenes;
	}
}
