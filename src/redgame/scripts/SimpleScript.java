package redgame.scripts;

public abstract class SimpleScript extends AbstractScript {
    protected int m_counter = 0;
    public void start(){
        m_running = true;
    }
    public void stop(){
        m_running = false;
    }
    public int update(){
        m_counter ++;
        runScript();
        return 0;
    }
    protected abstract void runScript();

}
