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

	public Scene generateScene(int id, EShape shape, EAction action, Motion motion){
		Scene scene = new Scene();
		scene.id = id;
		scene.shape = shape;
		scene.action = action;
		scene.movefrom = motion.movefrom;
		scene.moveto = motion.moveto;
		scene.extendto = motion.extendto;
		scene.start = motion.start;
		scene.end = motion.end;
		scene.angle = motion.angle;
		scene.showtext = motion.showtext;
		scene.delaystart = motion.delaystart;
		return scene;
	}
}
