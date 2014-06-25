package redgame.scripts;
import redgame.obj.*;
import redgame.ui.*;
import redgame.engine.*;
import redgame.status.*;


public class Music extends SimpleScript {
    private AbstractMenu m;
    private String[] s = { "1943", "Mario", "魂斗罗", "热血物语"};
    public void start(){
       	super.start();
        m = new TextMenu(game, s);
    }
    protected void runScript(){
        Player b = (Player) source.cause;
        AbstractObject a = (AbstractObject) source.owner;
        
        switch(m_counter){
            case 1: a.say("想听听我的演奏吗?"); break;
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