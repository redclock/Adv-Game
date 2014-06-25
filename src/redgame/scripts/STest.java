package redgame.scripts;
import redgame.obj.*;

public class STest extends SimpleScript {
    protected void runScript(){
        Actor a = (Actor) source.owner;
        if (source.direction == Actor.G_LEFT 
            || source.direction == Actor.G_RIGHT ){
            game.playSound("sound/voice/hello.wav");
            a.say("\tcff00ff00Hello");
        }    
        stop();
    }
}
