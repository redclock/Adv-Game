package redgame.scripts;
import redgame.obj.*;

public class TestScript extends ThreadScript{
    protected void runScript() throws ThreadTerminated{
        Actor  a = (Actor) source.owner;
        Actor  b = (Actor) source.cause;
        
        if (source.direction == Actor.G_DOWN){
            game.playSound("sound/voice/wei.wav");
            a.say("\ts20\tb喂！！\n\ts14\tB你不要踩我的头！");
            unlock();
            b.setVY(-0.4f);
        }else{
            game.playSound("sound/voice/nihao.wav");
            a.say("你好。");
            unlock();
        }
        waitTime(1500);
    }
}
