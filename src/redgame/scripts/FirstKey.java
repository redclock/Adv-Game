package redgame.scripts;
import redgame.obj.*;


public class FirstKey extends SimpleScript {
    protected void runScript(){
        Player b = (Player) source.cause;
        
        switch(m_counter){
            case 1: b.say("哈哈,总算拿到了!"); game.playSound("sound/voice/haha.wav"); break;
            case 2: b.say("\tb\tc00990000到门前按上键开门\n门开之后按上键进门"); 
                    break;
            default: 
                AbstractScript.setVar("meetgrandma", 5);
                stop();        
        }
    }
}
