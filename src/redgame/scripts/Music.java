package redgame.scripts;
import redgame.obj.*;
import redgame.ui.*;
import redgame.engine.*;
import redgame.status.*;


public class Music extends SimpleScript {
    private AbstractMenu m;
    private String[] s = { "1943", "Mario", "»ê¶·ÂŞ", "ÈÈÑªÎïÓï"};
    public void start(){
       	super.start();
        m = new TextMenu(game, s);
    }
    protected void runScript(){
        Player b = (Player) source.cause;
        AbstractObject a = (AbstractObject) source.owner;
        
        switch(m_counter){
            case 1: a.say("ÏëÌıÌıÎÒµÄÑİ×àÂğ?"); break;
            case 2:   
                game.pushStatus(new ShowMenuStatus(game, 100, 30, m));break;
            case 3: 
                    if (m.getIndex() < s.length){
                        game.playMusic("music/"+s[m.getIndex()]+".mid", true);
                    }
                    break;
            default: stop();        
        }
    }
}