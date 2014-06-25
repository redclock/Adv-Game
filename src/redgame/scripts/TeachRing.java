package redgame.scripts;
import redgame.obj.*;
import redgame.ui.*;
import redgame.status.*;
import redgame.item.*;

public class TeachRing extends SimpleScript {
    private AbstractMenu m;

    public void start(){
       	 super.start();
    }
    protected void runScript(){
        if (source.direction == Actor.G_DOWN){
            stop();
            return;
        }    
        Player b = (Player) source.cause;
        Actor a = (Actor) game.getMap().findActor("teacher");
        b.canControl = false;

        int x = getIntVar("meetteacher_ring");
        if ( x == 0){
            switch(m_counter){
                case 1: a.say("年轻人, 你能到这里真是不容易."); break;
                case 2: a.say("我想测测你的功夫如何."); break;
                case 3: if (b.canSword() == false){
                            a.say("什么?你还没找到一件武器吗?");
                            m_counter = 99; //jump to case 100
                        }else{
                            a.say("你愿意接受我的测试吗?");
                        }
                        break;
                case 4: b.say("听你说的那么神秘...\n我得考虑考虑"); 
                    break;
                case 5: b.say("愿不愿意呢?"); 
                    break;
                case 6:   
                    m = new TextMenu(game, new String[]{ "同意测试", "不愿意"});
                    game.pushStatus(new ShowMenuStatus(game, 895, 32, m));
                    break;
                case 7: 
                        if (m.getIndex() == 1){
                            a.say("不愿意吗?\n我还以为你是个有胆量的人呢.");
                            b.canControl = true;
                            stop();
                        }else{
                            a.say("那好, 我们开始");
                        }
                        break;
                case 8: game.gotoMap("map/trains2.txt", 560, 370);
                        break;
                case 9: a.say("你的挑战是打败这几个紫衣人"); break;
                case 10: a.say("小心为妙呀!呵呵"); 
                        setVar("meetteacher_ring", x+1);
                        b.canControl = true; 
                        stop();
                        break;
                
                //if no sword
                case 100: a.say("那太可惜了...\n你找到合适的武器之后再来找我吧"); 
                        break;
                default: b.canControl = true; 
                        stop();        
            }
            //in training
        }else if ( x == 1){
            if (game.getMap().findActor("e1") != null &&
                (game.getMap().findActor("e1").getVisible() ||
                game.getMap().findActor("e2").getVisible() ||
                game.getMap().findActor("e3").getVisible() ||
                game.getMap().findActor("e4").getVisible() ||
                game.getMap().findActor("e5").getVisible())){
                   a.say("要把紫衣人都消灭掉\n加油呀!"); 
                   b.canControl = true; 
                   stop();
            }else{
                //succeed
                switch(m_counter){
                    case 1: a.say("年轻人, 干的不错!"); break;
                    case 2: b.say("这太简单了!"); break;
                    case 3: game.gotoMap("map/room4.txt", 806, 20); break;
                    case 4: a.say("你通过了测试, 给你一个奖励!"); break;
                    case 5: a.say("\tcffff0000\tb\ts20超级魔力面包圈发射器!"); 
                        b.curItem = b.items[Player.ITEM_RING] = new RingItem(game);
                        b.items[Player.ITEM_RING].setCount(3);
                        break;
                    case 6: a.say("并附送三个面包圈!\n将它砸到敌人头上\n能使其昏迷一段时间"); 
                        break;
                    case 7: a.say("你想听更详细的使用方法吗？");
                        break;    
                    case 8:   
                        m = new TextMenu(game, new String[]{ "我想听听", "不必了"});
                        game.pushStatus(new ShowMenuStatus(game, 895, 40, m));
                        break;
                    case 9:
                        if (m.getIndex() == 1){
                            b.canControl = true;
                            setVar("meetteacher_ring", x+1);
                            stop();
                        }else{
                            m_counter = 0;
                            setVar("meetteacher_ring", 3);
                        }
                        break;
                            
                    default: b.canControl = true; 
                             setVar("meetteacher_ring", x+1);
                            stop();        
                }
            }
        }else if ( x == 2){
            switch(m_counter){
                case 1: a.say("你想听面包圈的使用方法吗？");
                    break;    
                case 2:   
                    m = new TextMenu(game, new String[]{ "我想听听", "不必了"});
                    game.pushStatus(new ShowMenuStatus(game, 895, 32, m));
                    break;
                case 3:
                    if (m.getIndex() == 1){
                        b.canControl = true;
                        stop();
                    }else{
                        m_counter = 0;
                        setVar("meetteacher_ring", 3);
                    }
                    break;
                default: b.canControl = true; 
                        stop();        
            }
        }else if ( x == 3){
            switch(m_counter){
                case 1: a.say("我来教你面包圈的使用方法:"); break;
                case 2:
                    a.say("你可以通过菜单中[选择武器]装配\n" +
                          "面包圈，也可以按数字键2快速装配"); 
                    break;      
                case 3:
                    a.say("当你装配好后, 右下角会有图标指示"); 
                    break;
                case 4: a.say("按住X键不放，出现绿色准星后按←→键\n" +
                              "调整到适当位置，放开X键，一个充满弹\n" +
                              "性的面包圈就发射出去了"); 
                        break;
                case 5: a.say("将它砸到敌人头上\n能使其昏迷一段时间"); 
                case 6: a.say("你只有有限个面包圈, \n右下角的图标显示你的面包圈数"); 
                        break;
                case 7: a.say("但在面包圈靠近时按C键, 可以将它回收。"); 
                        break;
                default: b.canControl = true; 
                         setVar("meetteacher_ring", 2);
                         stop();        
            }
        }  
    }
}