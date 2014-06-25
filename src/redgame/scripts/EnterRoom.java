package redgame.scripts;

import redgame.obj.*;

public class EnterRoom extends SimpleScript {
	
	public void start() {
		super.start();
		//Actor a = (Actor) game.getMap().findActor("grandma");
		//if (a != null) a.setVisible(false);
	}
	
	protected void runScript() {
		stop();
	}

}
