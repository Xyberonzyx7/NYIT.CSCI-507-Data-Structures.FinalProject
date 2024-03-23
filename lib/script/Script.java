package lib.script;

import java.util.List;
import java.util.ArrayList;

public class Script {
	List<Command> cmds;	

	public Script(){
		cmds = new ArrayList<>();
	}

	public void add(Command cmd){
		cmds.add(cmd);
	}

	public Command get(int index){
		return cmds.get(index);
	}

	public int size(){
		return cmds.size();
	}
}

