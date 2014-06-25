package redgame.scripts;
import redgame.obj.*;
import redgame.engine.*;
import redgame.status.*;
import redgame.util.*;

public class TaskGirl extends SimpleScript {
    
    protected void runScript(){
        if (source.direction == Actor.G_DOWN || source.direction == Actor.G_UP) {
            stop();
            return;
        }    
        Player b = (Player) source.cause;
        NPC a = (NPC) source.owner;
        int x = getIntVar("meettask");
        b.canControl = false;
        switch(m_counter){
            case 1:
                a.say("......");
                break;
            case 2:
                a.say("怎么办呢？");
                break;
            case 3:
                a.say("\te4");
                break;
            case 4:
                b.say("喂，你是害怕怪物不敢回去了吧。");
                break;
            case 5:
                a.setFace(Actor.G_LEFT);
                a.say("啊！\n你...你怎么在这儿？");
                break;
            case 6:
                b.say("你妈妈担心你回不去，叫我来接你");
                break;
            case 7:
                a.say("接我？难道我自己不会回去吗？");
                break;
            case 8:
                b.say("\te2\n什么态度嘛。");
                break;
            case 9:
                b.say("好了，你妈妈还在等你呢。\n赶快回家吧。");
                break;
            case 10:
                a.say("凭什么你说走就走？\n我还要先玩一会儿呢");
                break;
            case 11:
                b.say("\te2");
                break;
            case 12:
                b.say("那好，我先回去了。");
                b.setDest(2750, 10);
                break;
            case 13:
                a.say("啊\te5......");
                break;
            case 14:
                a.setFace(Actor.G_DOWN);
                if (b.getX() > 2830) m_counter --;
                break;
            case 15:
                a.setFace(Actor.G_LEFT);
                a.say("等等我......");
                game.playSound("sound/voice/wei.wav");
                a.setDest(2750, 10);
                break;
            case 16:
                if (a.getX() > 2860) m_counter --;
                break;
            case 17: 
                b.noDest();
                game.gotoMap("map/room5.txt", 50, 70);
                break;
            default:
                AbstractScript.setVar("meettask", 10);
                b.canControl = true;
                stop();    
        }
    }
    
}


