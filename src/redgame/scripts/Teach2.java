package redgame.scripts;
import redgame.obj.*;
import redgame.ui.*;
import redgame.status.*;
import redgame.item.*;

public class Teach2 extends SimpleScript {
    private AbstractMenu m;

    public void start(){
         super.start();
    }
    protected void runScript(){
        Player b = (Player) source.cause;
        Actor a = (Actor) game.getMap().findActor("teacher");
        if (source.direction == Actor.G_UP || source.direction == Actor.G_DOWN){
            stop();
            return;
        }
        
        b.canControl = false;

        int x = getIntVar("meetteacher");
        //first meet
        if ( x == 0){
            switch(m_counter){
                case 1: a.say("年轻人, 你愿不愿意接受我的测试?"); break;
                case 2: a.say("这个测试可以训练你闯荡江湖的能力."); break;
                case 3: a.say("并且,通过测试后我会给你一件神秘礼物."); break;
                case 4: a.say("但是"); break;
                case 5: a.say("如果你失败了,你可能失去生命."); break;
                case 6: a.say("你有勇气接收这个挑战吗?"); break;
                case 7: b.say("听你说的那么神秘...\n我得考虑考虑"); break;
                case 8: b.say("愿不愿意呢?"); break;
                case 9:   
                    m = new TextMenu(game, new String[]{ "同意测试", "不愿意"});

                    game.pushStatus(new ShowMenuStatus(game, 70, 150, m));break;
                case 10: 
                        if (m.getIndex() == 1){
                            a.say("不愿意吗?\n我还以为你是个有胆量的人呢.");
                            b.canControl = true;
                            stop();
                        }else{
                            a.say("那好, 我们开始");
                        }
                        break;
                case 11: game.gotoMap("map/trains1.txt", 17, 150);
                        
                        break;
                case 12: a.say("你要通过这条路, 不要让紫衣人碰到你.\n但你可以踩到他们身上"); 
                        b.setFace(Actor.G_RIGHT);
                        break;
                case 13: a.say("我在下面等着你."); 
                        a.setFace(Actor.G_LEFT);
                        break;
                case 14: a.setPosition(6, 240); 
                        a.setFace(Actor.G_RIGHT);
                        break;
                default: b.canControl = true; 
                        setVar("meetteacher", x+1);
                        stop();        
            }
        //finish train    
        }else if (x == 1){
            switch(m_counter){
                case 1: a.say("年轻人, 干的不错!"); break;
                case 2: b.say("这太简单了!"); break;
                case 3: game.gotoMap("map/room3.txt", 33, 110);
                        break;
                case 4: a.say("年轻人, 做为奖励, 给你一把宝剑");
                        b.curItem = b.items[Player.ITEM_SWORD] = new SwordItem(game); 
                        break;
                case 5: a.say("你要听我给你讲剑的使用方法吗？");
                    
                        break;
                case 6:
                    m = new TextMenu(game, new String[]{ "我听一听", "不必了"});
                    game.pushStatus(new ShowMenuStatus(game, 70, 150, m));
                    break;
                case 7:    
                    if (m.getIndex() == 1){
                        setVar("meetteacher", 2);
                        a.say("好吧，我相信你的领悟能力");
                        b.canControl = true;
                        stop();
                    }else{
                        m_counter = 0;
                        setVar("meetteacher", 10);
                    }
                    break;
                default: b.canControl = true; 
                        setVar("meetteacher", 2);
                        stop();        
            }
        //teach sword  
        }else if (x == 10){
            switch(m_counter){
                case 1:
                    a.say("你可以通过菜单中[选择武器]装配\n" +
                          "你的剑，也可以按数字键1快速装配"); 
                    break;      
                case 2:
                    a.say("当你装配好后, 右下角会有图标指示"); 
                    break;
                case 3:
                    a.say("使用这把剑需要能量的积蓄。\n" +
                          "你在握着剑时，剑的能量就会满满上升"); 
                    break;
                case 4:
                    a.say("当你积蓄了一定能量时，你可以按X键挥剑。\n" +
                          "剑气会将你伤害附近的敌人。"); 
                    break;
                case 5:
                    a.say("你也可以等积蓄了满能量时，按C键挥剑。\n" +
                          "你可以获得更大的攻击力和攻击范围。"); 
                    break;
                case 6:
                    a.say("我教给你的就这么多了。\n剩下的就要靠你自己实践了。"); 
                    break;
                default:
                    b.canControl = true; 
                    stop();    
                    setVar("meetteacher", 2);
            } 
        //default        
        }else if (x == 3){
            switch(m_counter){
                case 1: a.say("年轻人, 干的不错!"); break;
                case 2: b.say("这太简单了!"); break;
                case 3: game.gotoMap("map/room3.txt", 33, 110);
                        break;
                default: b.canControl = true; 
                        setVar("meetteacher", 2);
                        stop();        
            }            
        }else {
            switch(m_counter){
                case 1:
                    a.say("你还想做什么？"); 
                    break;
                case 2:
                    m = new TextMenu(game, new String[]{ "我想学习剑的使用方法", "我想再次接受测试", "我随便转转"});
                    game.pushStatus(new ShowMenuStatus(game, 70, 150, m));
                    break;
                case 3:    
                    if (m.getIndex() == 2){
                        b.canControl = true;
                        stop();
                    }else if (m.getIndex() == 0){
                        m_counter = 0;
                        setVar("meetteacher", 10);
                    }    
                    break;
                case 4:
                    a.say("看来你还意犹未尽呢。"); 
                    break;
                case 5:
                    a.say("好的，这次你可以试试使用你的\tcffff0000\tb剑"); 
                    break;
                case 6: game.gotoMap("map/trains1.txt", 17, 150);
                        
                        break;
                case 7: a.say("和上次一样，你要通过这条路。"); 
                        b.setFace(Actor.G_RIGHT);
                        break;
                case 8: a.say("我在下面等着你."); 
                        a.setFace(Actor.G_LEFT);
                        break;
                case 9: a.setPosition(6, 240); 
                        a.setFace(Actor.G_RIGHT);
                        break;
                default: b.canControl = true; 
                        setVar("meetteacher", 3);
                        stop();        
            }    
        }    
    }
}