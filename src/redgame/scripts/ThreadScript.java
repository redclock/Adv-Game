package redgame.scripts;
/**
 * 每个脚本是一个线程,它运行时主线程等待,主线程运行时脚本等待
 */
public abstract class ThreadScript extends AbstractScript implements Runnable{
    //令脚本强制结束的异常
    protected class ThreadTerminated extends Exception{}
    //是否正在等待
    private boolean m_suspended = true;
    //是否已经停止
    private boolean m_stopped = false;
    //是否在休眠
    private boolean m_sleeping = false;
    //线程
    private Thread m_thread;
    
    /**
     * 是否正在等待
     */
    public synchronized boolean isSuspended(){
        return m_suspended;
    }
    
    /**
     * 是否正在休眠
     */
    public synchronized boolean isSleeping(){
        return m_sleeping;
    }

    /**
     * 从等待中回复
     */
    public synchronized void spResume(){
        m_suspended = false;
    }
    /**
     * 挂起等待
     */
    public synchronized void spSuspend(){
        m_suspended = true;
    }
    /**
     * 开始脚本
     */
    public void start(){
        m_thread = new Thread(this);
        m_thread.start();
        //等待线程开始
        while (!m_running && m_thread.isAlive()){
        }
    }
    /**
     * 停止线程
     */
    public synchronized void stop(){
        m_stopped = true;    
    }
    
    /**
     * 执行
     */
    public int update(){
       if (! m_sleeping ){
            m_suspended = false;
            //等待
            while( !m_suspended && m_thread.isAlive()){
            }
        }
        return 0;
    } 

    protected void unlock() throws ThreadTerminated{
        spSuspend();
        while (m_suspended){
            if ( m_stopped ){
                throw new ThreadTerminated();
            }
        }
    }
    /**
     * 覆盖Runnable的run()
     */
    public void run(){
        m_running = true;
        try{
            runScript();
        }catch(ThreadTerminated e){
        }    
        m_running = false;
    }
    /**
     * 脚本内容
     */
    protected abstract void runScript() throws ThreadTerminated;
    
    protected void waitTime(long m) throws ThreadTerminated{
        m_sleeping = true;
        m_suspended = true;
        try{
            Thread.sleep(m);
        }catch(InterruptedException e){
            e.printStackTrace();
        }    
        m_sleeping = false;
        unlock();
    }
}
