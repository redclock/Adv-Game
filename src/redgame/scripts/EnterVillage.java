package redgame.scripts;

import redgame.obj.*;
import redgame.engine.*;

public class EnterVillage extends SimpleScript {

	public void start() {
		super.start();
        boolean x = game.getPlayer().items[Player.ITEM_SWORD] != null &&
                    game.getPlayer().items[Player.ITEM_RING] != null;
		Actor a = (Actor) game.getMap().findActor("block");
		if (a!= null && x){
			a.setVisible(false);
			a.setBlocked(false);
		}
	}

	protected void runScript() {
		stop();

	}

}
