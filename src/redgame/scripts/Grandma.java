package redgame.scripts;

import redgame.obj.*;


public class Grandma extends SimpleScript {
    protected void runScript(){
        NPC a = (NPC) source.owner;
        Player b = (Player) source.cause;

        if (source.direction == Actor.G_DOWN){
            game.playSound("sound/voice/wei.wav");
            a.say("小兔崽子, 你干什么呢\te2");
            b.setVY(-0.4f);
            stop();
            return;
        }
        
        if (getIntVar("open") == 0){
            int m = getIntVar("meetgrandma");
            if (m == 0){
                switch(m_counter){
                    case 1: 
                            b.say("奶奶,您最近气色是越来越好了."); break;
                    case 2: a.say("难得你这样夸人\n是不是有什么事情?"); break;
                    case 3: b.say("嗯...今天天气很不错."); break;
                    case 4: a.say("是吗?\n但是你今天不许出去了."); break;
                    case 5: b.say("\te5\ts20\tcffff00ff为什么?"); break;
                    case 6: a.say("你也知道现在外面有好多怪物。"); break;
                    case 7: a.say("你一个人出去，太危险了。"); break;
                    case 8: b.say("\te2"); 
                            b.setDest(110, 10);
                            b.canControl = false;
                            break;
                    case 9: if (b.isMoving()){ m_counter --; break; }
                            b.say("看来得想个办法"); 
                            b.noDest();
                            b.canControl = true;
                            break;
                    default: setVar("meetgrandma", m+1); stop();
                }
            }else if (m == 1){
                b.canControl = false;
                switch(m_counter){
                    case 1: b.say("\ts20\tb对了!"); break;
                    case 2: a.say("什么事?"); break;
                    case 3: b.say("啊..那个..."); break;
                    case 4: b.say("啊,刚才村东头马大婶叫你打麻将去!"); break;
                    case 5: game.playSound("sound/voice/wa.wav");
                            a.say("\ts26\tcffff0000真的呀!!\n\ts16\tcff000000我就爱打麻将");
                            
                            a.setVY(-0.3f);
                            a.setFace(3 - source.direction);                            
                            break;
                    case 6: if (a.getVY() != 0) m_counter --;  break;      
                    case 7: b.say("是呀是呀快去吧!"); break;
                    case 8: a.say("那我走了!"); 
                            a.setVY(-0.3f);
                            a.setDest(155, 10);
                            break;
                    case 9: if (a.isMoving()) 
                            {
                                m_counter --;
                                if (a.getVY() == 0) a.setVY(-0.4f);
                                break;
                            }else {
                            	if (a.getVY() != 0) m_counter --;
                            }
                            break;
                            
                    case 10: a.say("\ts20等等..."); b.setFace(Actor.G_RIGHT); break; 
                    case 11: a.setFace(Actor.G_LEFT);
                             a.say("马大婶不是上个月去逝了吗.");
                             break; 
 
                    case 12: b.say("啊...这个...\tcff009900-_-!");
                             break;
                    case 13: b.say("好像不是马大婶...是赵大婶!");
                             break; 
                    case 14: game.playSound("sound/voice/heng.wav");
                             a.say("你少骗我,\te2你别想出去了!");
                             
                             break; 
                    case 15: b.say("又失败了...");
                             break; 
                    default: b.canControl = true;
                             setVar("meetgrandma", m+1); stop();
                }
            }else if (m == 2){
                switch(m_counter){
                    case 1: b.say("奶奶~~~"); break;
                    case 2: a.say("你求情也没用.我是为你好。\n我已经把开门的钥匙放在你\n拿不到的地方了."); break;
                    case 3: b.say("......"); 
                            b.setDest(50, 10);
                            b.canControl = false;
                            break;
                    case 4: if (b.isMoving()){ m_counter --; break; }
                            b.say("看来得自己找到钥匙开门了\n\tb\tc00990000可是放钥匙的地方太高了");
                            b.noDest();
                            b.canControl = true;
                            break;
                    case 5: b.say("怎么办?"); break;
                    
                    default: setVar("meetgrandma", m+1); stop();
                }
            }else if (m == 3){
                switch(m_counter){
                    case 1: b.say("奶奶, 门口有东西!"); break;
                    case 2: a.say("是吗, 我去看看"); 
                            a.setDest(375, 5);
                            break;
                    case 3: if (a.isMoving()){ m_counter --; break; }
                            a.say("什么也没有呀"); 
                            a.noDest();
                            break;
                    case 4: b.say("时机到了!"); break;
                    default: setVar("meetgrandma", m+1); stop();
                }
            }else if (m == 4){
                switch(m_counter){
                    case 1: a.setFace(3 - source.direction);
                            a.say("你说有什么东西?"); break;
                    case 2: b.say("您看不到吗?就在这里!"); break;
                    default: stop();
                }
            }else if (m == 5){
                switch(m_counter){
                    case 1: a.setFace(3 - source.direction);
                            a.say("竟然上你的当了。"); break;
                    case 2: b.say("对不起奶奶，我出去一会儿就回来。"); break;
                    case 3: a.say("真拿你没办法，\n看来我是管不了你了"); break;
                    case 4: a.say("那好吧。但你可不能离开村子。"); break;
                    default: setVar("meetgrandma", m+1); stop();
                }
            }else if (m >= 6){
                switch(m_counter){
                    case 1: a.setFace(3 - source.direction);
                            a.say("一定要小心。"); break;
                    default:stop();
                }
            }    
        }    
    }
}
