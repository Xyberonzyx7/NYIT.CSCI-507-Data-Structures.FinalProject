package lib.script;

import java.util.List;
import java.util.ArrayList;

public class Script {
	List<Scene> scenes;	

	public Script(){
		this.scenes = new ArrayList<>();
	}

	public void removeAllScene(){
		this.scenes.clear();
	}

	public void addScene(Scene scene){
		this.scenes.add(scene);
	}

	public void addScene(List<Scene> scenes){
		this.scenes.addAll(scenes);
	}

	public Scene getScene(int index){
		return this.scenes.get(index);
	}

	public int SceneSize(){
		return this.scenes.size();
	}
}

