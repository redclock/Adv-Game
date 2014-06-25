package redgame.scripts;

import redgame.obj.*;
import redgame.status.*;
import redgame.util.*;

public class Beginning extends SimpleScript {
   Player b;

    NPC c;
    NPC a;

    public void start() {
        super.start();
        b = game.getPlayer();
        c = (NPC) game.getMap().findActor("mf");
        a = (NPC) game.getMap().findActor("grandma");
        int x = getIntVar("process");
        if (x != 3) {
            if (c != null) {
                c.setVisible(false);
                c.setBlocked(false);
            }
        }else{
            if (a != null) {
                a.setVisible(false);
                a.setBlocked(false);
            }
        }
    }
    
    protected void runScript(){
        int x = getIntVar("process");
        
        if (x == 0){
            switch(m_counter){
                case 1: 
                    b.canControl = false;
                    game.playSound("sound/voice/nihao.wav");
                    b.say("\ts25大家好!第一次和大家见面"); 
                    b.setFace(Actor.G_DOWN);
                    break;
                case 2: 
                    b.say("先自我介绍, 我的名字是..."); 
                    break;
                case 3: 
                    b.say("呀, 忘了, 我还没有名字呢!"); 
                    b.setFace(Actor.G_DOWN);
                    break;
                case 4:
                    game.pushStatus(new InputStatus(game, 220, 170, "请替我取一个名字吧:"));
                    break;
                case 5:
                    if (InputStatus.result.trim().equals("")){
                        b.setName("无名");
                    }else{
                        b.setName(InputStatus.result.trim());
                    }
                    KeyManager.clearKeyState();
                    break;
               case 6:     
                    b.say("哈哈, 我叫"+b.getName()+".\te3");
                    break;
                case 7:     
                    b.say("那么我接下来介绍一下基本操作吧!"); 
                    break;
                case 8: 
                    b.say("\tb\tc00990000按Z键是跳跃!");
                    break;
                case 9: game.playSound("sound/voice/wa.wav");
                        b.setVY(-0.3f);
                        break;  
                case 10: if(b.getVY() != 0) {m_counter --; break;  } 
                    b.say("\tb\tc00990000跳到梯子时按上键,就可以爬梯子了");
                    break;
                case 11: 
                    b.setFace(Actor.G_RIGHT);
                    b.say("不过今天真是出去玩的好天气呢");        
                    break;
                case 12: 
                    b.say("啊,门还锁着呢,得向奶奶要钥匙了");
                    break;
                default:
                    b.canControl = true; 
                    setVar("process", 1);
                    stop();        
            }
        }else {
            stop();
        }
            
    }
}
