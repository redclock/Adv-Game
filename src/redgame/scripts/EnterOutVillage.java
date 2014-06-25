package redgame.scripts;

import redgame.obj.*;

public class EnterOutVillage extends SimpleScript {
    
    public void start() {
        super.start();
        Actor a = (Actor) game.getMap().findActor("girl");
        int x = AbstractScript.getIntVar("meettask");
        if (x >= 10 && a != null)
        {
            a.setVisible(false);
            a.setBlocked(false);
        }
    }
    
    protected void runScript() {
        stop();
    }

}
