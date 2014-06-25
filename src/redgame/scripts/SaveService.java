package redgame.scripts;
import redgame.*;
import redgame.obj.*;
import redgame.ui.*;
import redgame.engine.*;
import redgame.status.*;
import redgame.util.*;

public class SaveService extends SimpleScript {
    private AbstractMenu m;
    private String[] s = { "我要保存", "不保存"};
    NPC a; 

    public void start(){
       	 super.start();
         m = new TextMenu(game, s);
    }
    
    protected void runScript(){
        Player b = (Player)source.cause; 
        if (a == null){
            a = (NPC) game.getMap().findActor("mf");
            if (a == null){
                stop();
                return;
            }    
        }
        switch(m_counter){
            case 1: a.say("你要在记录你当前的旅途吗?"); break;
            case 2: game.pushStatus(new ShowMenuStatus(game, 100, 130, m)); break;
            case 3:if (m.getIndex() == 0) m_counter = 99; 
                   else a.say("这么说..."); 
                   break;
            case 4: a.say("你要我来算一卦?\n\ts20太好了!"); break;
            case 5: b.say("不, 不是...\n我随便转转"); break;
            //if select yes        
            case 100: game.pushStatus(new InputStatus(game, 100, 130, "请输入要存盘的名称:"));
                    break;
            case 101: 
                if (InputStatus.result.equals("")){
                    a.say("你不输入名字是不能保存的");
                }else{
                    GameSaveLoad gsl = new GameSaveLoad(game, InputStatus.result);
                    if ( gsl.saveGame("map/saveroom.txt") ){
                    	a.say("哈哈,完成了!");	
                    }else{
                    	a.say("保存失败了。\n\tb你究竟输入了什么？");
                    }
                    break;
                }
            default: 
                    stop();
        }
    }
}        
