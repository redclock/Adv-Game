package redgame.scripts;

public class ToSaveRoom extends SimpleScript {
    protected void runScript(){
        setVar("_save_out_map", args[0]);
        setVar("_save_out_x", args[1]);
        setVar("_save_out_y", args[2]);
        stop();
    }
}        
