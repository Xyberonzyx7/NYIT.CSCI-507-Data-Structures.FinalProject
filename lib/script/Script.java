package lib.script;

import java.util.List;
import java.util.ArrayList;

public class Script {
	List<Scene> scenes;	

	public Script(){
		scenes = new ArrayList<>();
	}

	public void addScene(Scene scene){
		scenes.add(scene);
	}

	public Scene getScene(int index){
		return scenes.get(index);
	}

	public int SceneSize(){
		return scenes.size();
	}
}

