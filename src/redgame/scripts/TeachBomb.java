package redgame.scripts;
import redgame.obj.*;
import redgame.ui.*;
import redgame.status.*;
import redgame.item.*;

public class TeachBomb extends SimpleScript {
    private AbstractMenu m;

    public void start(){
         super.start();
         if (source.direction == Actor.G_UP || source.direction == Actor.G_DOWN){
            stop();
            return;
        }
    }

    protected void runScript(){
        Player b = (Player) source.cause;
        Actor a = (Actor) source.owner;
        
        b.canControl = false;

        int x = getIntVar("meetteacherbomb");
        a.setFace(Actor.G_RIGHT);
        //first meet
        if ( x == 0){
            switch(m_counter){
                case 1: a.say("\te0"); break;
                case 2: a.say("你是怎么进来的？"); break;
                case 3: a.say("我被关在这里很久了，\n从没有见一个人来过"); break;
                case 4: b.say("我找到了把钥匙，把门打开了。"); break;
                case 5: a.say("真的吗?\n\ts30\tcffff0088那太好了!\n"); break;
                case 6: a.say("为了表达我的谢意，"); break;
                case 7: a.say("我送给你这个游戏中最厉害的武器:"); break;
                case 8: a.say("\ts40\tcff880088炸弹!");
                        game.playSound("sound/explode.wav");
                        b.curItem = b.items[Player.ITEM_BOMB] = new BombItem(game); 
                        b.curItem.setCount(3);
                            
                        setVar("meetteacherbomb", 2);
                        m_counter = 0;
                        break;
                case 9: a.say("我来告诉你使用的方法:"); break;
                default: b.canControl = true; 
                        setVar("meetteacherbomb", 2);
                        stop();        
            }
        }else if (x == 10){
            switch(m_counter){
                case 1:
                    a.say("你可以通过菜单中[选择武器]装配\n" +
                          "炸弹，也可以按数字键3快速装配"); 
                    break;      
                case 2:
                    a.say("当你装配好后, 右下角会有图标指示"); 
                    break;
                case 3:
                    a.say("你可以按X在你想要的地方放炸弹。\n");
                    break;
                case 4:
                    a.say("过一段时间后，炸弹就会爆炸。\n");
                    break;
                case 5:
                    a.say("炸弹也可能伤害你自己，\n所以要小心使用"); 
                    break;
                case 6:
                    a.say("提示一点，炸弹的威力可是很大的\n不仅能炸伤敌人，有时连石头都能炸碎。"); 
                    break;
                case 7:
                    a.say("但你一次最多能拿3个炸弹。"); 
                    break;
                case 8:
                    a.say("炸弹不够时可以再来找我。"); 
                    break;
                default:
                    b.canControl = true; 
                    stop();    
                    setVar("meetteacherbomb", 2);
            }
        }else { 
            switch(m_counter){
                case 1:
                    if (b.items[Player.ITEM_BOMB] != null 
                        && b.items[Player.ITEM_BOMB].getCount() < 3) {

                        a.say("炸弹不够了吗？"); 
                    }else {
                        m_counter = 99;
                    }
                    break;
                case 2:
                    b.items[Player.ITEM_BOMB].setCount(3);
                    a.say("好了，我给你装满了");        
                    break;
                case 3:
                    a.say("记住，你一次只能装3个炸弹");
                    break;    
                case 100: a.say("你要听炸弹的使用方法吗？");
                        break;
                case 101:
                    m = new TextMenu(game, new String[]{ "我听一听", "不必了"});
                    game.pushStatus(new ShowMenuStatus(game, 100, 60, m));
                    break;
                case 102:    
                    if (m.getIndex() == 1){
                        b.canControl = true;
                        stop();
                    }else{
                        m_counter = 0;
                        setVar("meetteacherbomb", 10);
                    }
                    break;
                default:
                    b.canControl = true; 
                    stop(); 
            }           
        } 
    }
}            