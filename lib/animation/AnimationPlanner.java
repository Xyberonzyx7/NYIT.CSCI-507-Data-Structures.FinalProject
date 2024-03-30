package lib.animation;

import java.awt.Point;

import lib.script.EAction;
import lib.script.EShape;
import lib.script.Scene;
import lib.script.Motion;

public class AnimationPlanner {
	protected int objCount;	

	public AnimationPlanner(){
		objCount = 0;
	}

	public int generateUniqueID(){
		objCount++;
		return objCount;
	}

	public int generateNullID(){
		return -1;
	}

	public Scene generateWaitScene(int delaystart){
		Motion motion = new Motion();
		motion.delaystart = 2000;
		return generateScene(generateNullID(), EShape.NONE, EAction.WAIT, motion);
	}

	public Scene generateDeleteScene(int id, EShape shape){
		Motion motion = new Motion();
		return generateScene(id, shape, EAction.DELETE, motion);
	}

	public Scene generateScene(int id, EShape shape, EAction action, Motion motion){
		Scene scene = new Scene();
		scene.id = id;
		scene.shape = shape;
		scene.action = action;
		scene.movefrom = motion.movefrom;
		scene.moveto = motion.moveto;
		scene.extendto = motion.extendto;
		scene.shrinkto = motion.shrinkto;
		scene.rotateto = motion.rotateto;
		scene.colorto = motion.colorto;
		scene.start = motion.start;
		scene.end = motion.end;
		scene.angle = motion.angle;
		scene.showtext = motion.showtext;
		scene.delaystart = motion.delaystart;
		return scene;
	}
}
