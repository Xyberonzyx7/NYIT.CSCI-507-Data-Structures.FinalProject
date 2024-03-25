package lib.animation;

public class AnimationPlanner {
	protected int objCount;	

	public AnimationPlanner(){
		objCount = 0;
	}

	public int generateUniqueID(){
		objCount++;
		return objCount;
	}
}
