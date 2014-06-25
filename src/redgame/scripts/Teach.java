package redgame.scripts;
import redgame.obj.*;
import redgame.status.*;

public class Teach extends SimpleScript {
    NPC a; 
    protected void runScript(){
        Player b = (Player)source.cause; 
        
        if (a == null){
            a = (NPC) game.getMap().findActor("mf");
            if (a == null){
                stop();
                return;
            }    
        }

        int x = getIntVar("meetteacher_save");
        if (x == 0){
            switch(m_counter){
                case 1: a.say("\te0"); 
                        break;
                case 2: a.say("这位先生, 看你印堂锃亮, \n看来要走好运"); break;
                case 3: a.say("等会儿让老夫给你算上一卦..."); break;
                case 4: 
                        b.say("\te6"); 
                        break;
                case 5: b.say("大伯, 你看清楚了, 是我!"); break;
                case 6: a.say("啊?"); break;
                case 7: a.say("原来是"+b.getName()+"呀!\n我还当有客人呢\n最近眼神不太好..."); break;
                case 8: b.say("大伯, 你还在干这骗人的活呀!"); break;
                case 9: a.say("\te2"); break;
                case 10: a.say("你这小子, 说话怎么不小心!\n我这是...预测人生!靠手艺吃饭!\n怎么能说我骗人?"); break;
                case 11: b.say("^_^哈哈, 您别生气"); break;
                case 12: a.say("不过这些日子外面出来好些怪物,\n" + 
                               "搞的我连一个生意也没做成。" ); break;
                default: 
                    setVar("meetteacher_save", x+1);
                    stop();
            }   
        }else if ( x == 1){
            switch(m_counter){
                case 1: a.say("去，去，别打搅我做生意。"); 
                        break;
                default: 
                    stop();
            }    
        }else{
            stop();
        }
    }     
}