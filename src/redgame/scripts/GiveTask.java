package redgame.scripts;

import redgame.obj.*;

public class GiveTask extends SimpleScript {
    protected void runScript(){
        if (source.direction == Actor.G_DOWN || source.direction == Actor.G_UP){
            stop();
            return;
        }    
        Player b = (Player) source.cause;
        Actor a = (Actor) source.owner;
        Actor c = (Actor) game.getMap().findActor("girl");
        Door d = (Door) game.getMap().findStatic("door");
        int x = getIntVar("meettask");
        b.canControl = false;

        if ( x == 0){
            switch(m_counter){
                case 1: b.say("阿姨, 您这里有什么好吃的?"); break;
                case 2: a.say("原来是"+b.getName()+"啊"); break;
                case 3: a.say("......");  
                        break;
                case 4: a.say("你别来烦我了...");  
                        break;
                case 5: b.say("\te1"); break;
                default: b.canControl = true; 
                         setVar("meettask", x+1);
                         stop();
            }             
        }else if ( x == 1){
            switch(m_counter){
                case 1: a.say("我正为我女儿的事发愁呢。"); break;
                case 2: b.say("哦？"); 
                		game.playSound("sound/voice/o.wav");
                		break;
                case 3: b.say("怎么了啊?"); break;
                case 4: a.say("我女儿阿澄说今天要从外地回来.\n可到现在还没有到."); break;
                case 5: a.say("而且最近村子外面突然来了许多怪物."); break;
                case 6: a.say("我怕..."); break;
                case 7: a.say("万一..."); break;
                case 8: a.say("出什么事..."); break;
                case 9: a.say("那......"); break;
                case 10: a.say("呜~~~"); break;
                case 11: b.say("\te6"); break;
                case 12: b.say("阿姨您别哭了。我出去接阿澄吧"); break;
                case 13: a.say("啊，太好了\n^_^"); break;
                case 14: a.say("你找到她一定尽快把她带回来呀。\te3"); break;
                case 15: b.say("......"); break;
                default: b.canControl = true; 
                         setVar("meettask", x+1);
                         stop();
            }
        }else if ( x == 2){
            switch(m_counter){
                case 1: a.say("怎么样，见到我女儿了吗？"); break;
                case 2: b.say("还没有......"); 
                        break;
                default: b.canControl = true; 
                         stop();
            }            
        }else if ( x ==10 ){    //已经找回来了
            switch(m_counter){
                case 1: a.say("多谢你把我女儿接回来。\te3"); 
                        break;
                case 2: b.say("哈哈，没什么啦\n^,^");
                        break;
                case 3: a.say("没有你的帮助她可是不敢回来的。\n呵呵");
                        break;
                case 4: b.say("呵呵...");
                        if (d != null){
                            d.open();    
                            game.playSound("sound/dooropen.wav");
                        }
                        break;
                case 5: 
                        c.setBlocked(true);
                        c.setVisible(true);
                        c.setPosition(460, 65);
                        c.say("我才不是不敢呢！");
                        game.playSound("sound/voice/wei.wav");
                        break;
                case 6: 
                        b.setFace(Actor.G_RIGHT);
                        b.say("\te0");
                        break;
                case 7: 
                        c.say("我是不会感谢你的！\n\te2");
                        break;
                case 8: 
                        c.setFace(Actor.G_UP);
                        a.say("阿澄, 你怎么能这么说！");
                        break;
                case 9: c.setVisible(false);
                        c.setBlocked(false);
                        if (d != null){
                            d.close();    
                            game.playSound("sound/dooropen.wav");
                        }
                        break;
                case 10: b.say("为什么发这么大脾气呢？"); break;
                case 11:
                      a.say("不好意思啊。\n是我女儿不懂礼貌。"); 
                      break; 
                case 12:
                      b.setFace(Actor.G_LEFT);
                      b.say("哈哈，没关系的。"); 
                      break; 
                case 13:
                      a.say("对了，" + b.getName() +"，你出来好长时间了。\n你奶奶不担心吗？"); 
                      break; 
                case 14:
                      b.say("说的也是。那我现在回家了。"); 
                      break; 
               
                default: b.canControl = true; 
                         setVar("meettask", x+1);
                         setVar("process", 3);
                         stop();
            }            
        }else {
            switch(m_counter){
                case 1:
                      a.say("以后常来玩～"); 
                      
                      break; 
                default: b.canControl = true; 
                         stop();
            }            
        }        
                
    }    
}