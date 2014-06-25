package redgame.scripts;

import redgame.obj.*;

public class TeachSpring extends SimpleScript {
    static private int c = 0;
    protected void runScript(){
        Actor a = (Actor) source.owner;
        if (source.direction == Actor.G_DOWN){
            stop();
            return;
        }    
        switch(c % 3){
            case 0: a.say("你试过跳弹簧垫吗?\n好玩极了"); break;
            case 1: a.say("在弹簧垫你会跳的很高\n有可能到达平常跳不到的地方"); break;
            case 2: a.say("在弹簧垫上不停的跳, 你会越跳越高!"); break;
        }
        c++;
        stop();
    }   

}
