package redgame.scripts;

import redgame.scripts.*;
import redgame.engine.*;
import redgame.ui.*;
import redgame.obj.*;
import redgame.status.*;
import redgame.util.*;
import redgame.anim.*;


public class SaveFairy extends SimpleScript {

    private AbstractMenu m;
    private String[] s = { "我要保存", "不保存"};
    AbstractObject a; 

    public void start(){
       	 super.start();
         m = new TextMenu(game, s);
    }
    
    protected void runScript(){
        Player b = (Player)source.cause; 
        int meet = AbstractScript.getIntVar("meetFairy");
        if (source.direction != AbstractObject.G_RIGHT){
        	stop();
        	return;
        }
        if (a == null){
            a = (AbstractObject) game.getMap().findMapObject("fairy");
            if (a == null){
                stop();
                return;
            }    
        }
        switch(m_counter){
            case 1:  
            	a.say("欢迎来到魔法屋！");   
        	case 2: if (meet == 0){
					m_counter = 199;
        		}
        		break;	
            case 3: a.say("你要在这里记录你当前的旅途吗?"); break;
            case 4: game.pushStatus(new ShowMenuStatus(game, 450, 150, m)); break;
            case 5: if (m.getIndex() == 0) m_counter = 99; 
                   else a.say("祝你一路平安了..."); 
                   if (meet == 0) m_counter = 149;
                   
                   break;
                   
            //if select yes        
            case 100: game.pushStatus(new InputStatus(game, 400, 150, "请输入要存盘的名称:"));
                    break;
            case 101: 
                if (InputStatus.result.equals("")){
                    a.say("你不输入名字是不能保存的");
                }else{
                    GameSaveLoad gsl = new GameSaveLoad(game, InputStatus.result);
                    if ( gsl.saveGame("map/magicroom.txt") ){
						m_counter = 299;
                    }else{
                    	a.say("保存失败了。\n\tb你究竟输入了什么？");
                    }
                }

                break;
            //first meet fairy    
            case 200:  
            	b.say("哇，你浮在上面不觉得头晕吗？");   
            	break;
            case 201:  
            	a.say("......");  
            	break;
            case 202:  
            	a.say("呵呵，我可是仙女");   
            	break;
            case 203:  
            	a.say("小伙子, 你做好踏入冒险征程的准备了吗?");   
            	break;
            case 204:
            	a.say("前面的路途可是充满坎坷的呦.");   
            	break;
            case 205:
            	b.say("噢，听你说的蛮有趣的。\n那我一定要出去看看了");   
            	break;
            case 206:
            	a.say("太好了，如果你的旅程有了进展，\n别忘了到我这里来，我会记录下你的故事");   
            	break;
            case 207:
                a.say("在我这里记录后,你可以在ESC\n" + 
                          "呼出的菜单中选择[载入游戏],\n" +
                          "选择记录来继续这次冒险");
                m_counter = 2;
                AbstractScript.setVar("meetFairy", 1);    
                break;
            
           	//save successfully
           	case 300:
                AnimStatus s = new AnimStatus(game, 430, 230, "image/anim/light2.txt", 
                            new SparkParticleSystem(game, game.loadImage("image/star2.png"), 
                                410, 230, 30, 
                                64, 64, 0, 0, 100,
                                12, 15, 0.1f));
                game.playSound("sound/magic.wav");
                game.pushStatus(s);                     
				break;
           	case 301:
				a.say("哈哈,完成了!");	
            default: 
                stop();
        }
    }
}
