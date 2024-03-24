package lib.script;

import java.util.List;
import java.util.ArrayList;

public class Script {
	List<Scene> cmds;	

	public Script(){
		cmds = new ArrayList<>();
	}

	public void add(Scene cmd){
		cmds.add(cmd);
	}

	public Scene get(int index){
		return cmds.get(index);
	}

	public int size(){
		return cmds.size();
	}
}

