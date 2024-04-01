package lib.animation;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

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

	public List<Scene> generateFlashingScene(int id, EShape shape, EAction action, Color defaultColor, Color flashColor){
		List<Scene> scenes = new ArrayList<>();

		// highlight
		Motion highlight = new Motion();
		highlight.colorto = flashColor;
		scenes.add(generateScene(id, shape, action, highlight));
		scenes.add(generateWaitScene(500));

		// unhighlight
		Motion unhighlight = new Motion();
		unhighlight.colorto = defaultColor;
		scenes.add(generateScene(id, shape, action, unhighlight));
		scenes.add(generateWaitScene(500));

		// highlight
		scenes.add(generateScene(id, shape, action, highlight));
		scenes.add(generateWaitScene(500));

		// unhighlight
		scenes.add(generateScene(id, shape, action, unhighlight));
		scenes.add(generateWaitScene(500));

		// highlight
		scenes.add(generateScene(id, shape, action, highlight));
		scenes.add(generateWaitScene(500));

		// unhighlight
		scenes.add(generateScene(id, shape, action, unhighlight));

		return scenes;
	}

	public Scene generateScene(int id, EShape shape, EAction action, Motion motion){
		Scene scene = new Scene();
		scene.id = id;
		scene.shape = shape;
		scene.action = action;
		scene.movefrom = motion.movefrom;
		scene.moveto = motion.moveto;
		scene.lengthto = motion.lengthto;
		scene.rotateto = motion.rotateto;
		scene.colorto = motion.colorto;
		scene.start = motion.start;
		scene.end = motion.end;
		scene.angle = motion.angle;
		scene.showtext = motion.showtext;
		scene.delaystart = motion.delaystart;
		return scene;
	}

	public Point getMiddlePoint(Point p1, Point p2){
		return new Point((int)((p1.getX() + p2.getX()) / 2), (int)((p1.getY() + p2.getY())/ 2));
	}

	public double getAngle(Point start, Point end){

		double deltaX = end.x - start.x;
        double deltaY = end.y - start.y;
		deltaY *= -1;	// for graphical components, positive y is pointing downward

        // Calculate the angle in radians using Math.atan2()
        double angleRadians = Math.atan2(deltaY, deltaX);

        // Convert radians to degrees
        double angleDegrees = Math.toDegrees(angleRadians);

        // Ensure the angle is positive (between 0 and 360 degrees)
        angleDegrees = (angleDegrees + 360 + 180) % 360;	// for arrow, 0 degree is <--, 180 degree is -->

        return angleDegrees;
	}

	public int getLength(Point p1, Point p2){
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return (int) Math.sqrt(dx*dx + dy*dy);
	}
}
