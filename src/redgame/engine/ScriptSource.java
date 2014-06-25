package redgame.engine;
import redgame.obj.*;

public class ScriptSource {
    public AbstractObject owner = null;
    public AbstractObject cause = null;
    public int direction;
    
    public ScriptSource(AbstractObject owner){
        this.owner = owner;
    }
    public ScriptSource(AbstractObject owner, AbstractObject cause, int direction){
        this.owner = owner;
        this.cause = cause;
        this.direction = direction;
    }
}
