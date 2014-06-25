package redgame.scripts;
import redgame.obj.*;


public class SayOne extends SimpleScript {
    protected void runScript(){
        AbstractObject a = source.owner;
        if (source.direction == Actor.G_LEFT || source.direction == Actor.G_RIGHT ){
            if (args.length == 0) 
                ;
            else if (args.length == 1) 
                a.say(args[0]);
            else {
                a.say(args[0]);
                game.playSound(args[1]);
            }    
        }
        stop();
    } 
}   