package redgame.scripts;

import redgame.obj.*;

public class EnterRoom5 extends SimpleScript {
    
    public void start() {
        super.start();
        Actor a = (Actor) game.getMap().findActor("girl");
        if (a != null)
        {
            a.setVisible(false);
            a.setBlocked(false);
        }
    }
    
    protected void runScript() {
        stop();
    }

}
