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
		Scene scene = new Scene();
		scene.id = generateNullID();
		scene.shape = EShape.NONE;
		scene.action = EAction.WAIT;
		scene.movefrom = null;
		scene.moveto = null;
		scene.extendto = -1;
		scene.shrinkto = -1;
		scene.rotateto = -1;
		scene.start = null;
		scene.end = null;
		scene.angle = -1;
		scene.showtext = "";
		scene.delaystart = delaystart;
		return scene;
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
		scene.start = motion.start;
		scene.end = motion.end;
		scene.angle = motion.angle;
		scene.showtext = motion.showtext;
		scene.delaystart = motion.delaystart;
		return scene;
	}
}
