package redgame.engine;
/*
 * GameWorld.java 作者：姚春晖
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;

import redgame.obj.*;
import redgame.util.*;

import redgame.status.*;
/**
 * GameWorld类是游戏总体的管理类，提供各个子功能之间的联系，同时它也是渲染的线程。
 * @author 姚春晖
 */

public class GameWorld implements Runnable{
    //游戏线程
    private Thread  gameThread;            
    //线程停止标志
    private boolean isStopped = true;
    //是否暂停 
    private boolean isPaused = false;     
    //图像缓存
    private BufferedImage backBuffer;  
    //当前时间      
    private long currTime = 0; 
    //间隔时间         
    public long passedTime = 0;
    //玩的时间
    private long m_playTime;
    //图形面板的引用      
    private JPanel m_panel = null;
    //IO接口的引用      
    private MyIO m_io = null;
    //游戏地图或画面的大小
    private Dimension m_size;
    //游戏屏幕在地图上的区域
    private Rectangle m_screenArea;
    //状态栈
    private StatusStack m_sstack = new StatusStack(this);
    //图像管理器
    private ImageManager m_imgMgr = new ImageManager(this);
    //当前地图
    private GameMap m_map;
    //地图列表
    private String[] m_maps;
    //脚本管理器
    private ScriptManager m_scripts = new ScriptManager(this);
    //当前关号
    private int m_currLevel;
    //随机数,使用时间做种子
    private Random m_rand = new Random(new Date().getTime());
    //是否需要更新屏幕
    private boolean m_needRefresh;
    //是否需要更新屏幕
    private GameMouse m_mouse;
    
    private Graphics m_bufGraph;

    private Player m_player;

    private Player createPlayer(){
        GameConfig cfg = new GameConfig();
        cfg.load("player.txt", m_io);
        int x = cfg.getInt("x", 0);
        int y = cfg.getInt("y", 0);
        int w = cfg.getInt("w", 48);
        int h = cfg.getInt("h", 64);
        Image img = loadImage(cfg.getString("image", ""));    
        Player r = new Player(this, img, x, y, w, h);
        int stop    = cfg.getInt("skip.top", 0);
        int sbottom = cfg.getInt("skip.bottom", 0);
        int sleft   = cfg.getInt("skip.left", 0);
        int sright  = cfg.getInt("skip.right", 0);
        
        r.setBlockRect(new Rectangle(sleft, stop, w - sleft - sright, 
                                             h - sbottom - stop));
        return r;                                             
    }

    //画一帧
    private void drawFrame(){
        Graphics g = m_panel.getGraphics();
        //有时g为空
        if (g!=null && m_size.width > 0 && m_size.height > 0)
        {
            if (m_needRefresh){
                m_panel.repaint();
                m_needRefresh = false;
            }    
            if (backBuffer == null ||
                backBuffer.getWidth(m_panel) != m_size.width ||
                backBuffer.getHeight(m_panel) != m_size.height){
                backBuffer = (BufferedImage)m_panel.createImage(m_size.width, m_size.height);
            }
            if (backBuffer != null) {
                calcScreenArea();
                Graphics g2 = backBuffer.getGraphics();
                m_bufGraph = g2;
                //先用黑色清屏幕
                g2.setColor(Color.BLACK);
//              g2.fillRect(0, 0, m_size.width, m_size.height);
                //激活顶端状态
                m_sstack.updateStatus(passedTime, g2);
                //写FPS
                String FPS;
                try{
                    FPS = "FPS:"+Float.toString(1000/(float)passedTime); 
                }catch(ArithmeticException e){
                    FPS = "0";
                }
                int x = m_screenArea.x;
                if (x < 0) x = 0;
                int y = m_screenArea.y;
                if (y < 0) y = 0;
                y += m_screenArea.height - 15;
                g2.setFont(new Font("Default", 0, 10));
                g2.setColor(Color.BLACK);
                g2.drawString(FPS, x, y);
                g2.setColor(Color.WHITE);
                g2.drawString(FPS, x, y - 1);
                m_mouse.update(passedTime);
                //m_mouse.paint(g2);
                m_bufGraph = null;
                g2.dispose();
                //将缓存画到屏幕上
//                g.drawImage(backBuffer, 0, 0, 
//                            m_screenArea.width,
//                            m_screenArea.height,
//                            m_screenArea.x, 
//                            m_screenArea.y, 
//                            m_screenArea.x+m_screenArea.width,
//                            m_screenArea.y+m_screenArea.height,
//                            null);
                g.drawImage(backBuffer, 
                            -m_screenArea.x, 
                            -m_screenArea.y, 
                            m_panel);
                
            }
        }else{
//          if (m_size.width == 0)
//              System.out.println("size is 0");
//          else
//              System.out.println("panel is null");
        }
    }
    
    /**
     * @param   panel   要向那里画图引用。
     * @see     AppFrame
     */
    public GameWorld(JPanel panel, MyIO MyIO){
            m_panel = panel;
            m_io = MyIO;
            m_maps = MapFileReader.readMapList("map/maplist.txt", this);
            m_mouse = new ImageMouse(this, loadImage("image/Ocarina.png"), 10, 10);
            m_panel.addMouseListener(m_mouse);
            m_panel.addMouseMotionListener(m_mouse);
    }

    public void gotoMap(String filename, int x, int y){
        SwitchStatus s = new SwitchStatus(this, new StageStatus(this, filename, x, y), 1);
        s.needRepaint = true;
        m_sstack.push(s);
    }
    /**
     * 装载地图
     * @see     GameMap 
     */
    public void loadMap(String filename){
        currTime = System.currentTimeMillis();
        backBuffer = null;
        MapFileReader mfr = new MapFileReader();
        m_map = mfr.readMap(this, filename);
        m_player.setVisible(true);
        m_map.addPlayer(m_player);
        m_size = new Dimension(m_map.getWidth(), m_map.getHeight());
        //将地图名称显示出来
//        m_frame.leftpane.txtName.setText(m_map.getName());
        //初始化
        //将其他状态删除
        m_sstack.deleteUntil(2);
        KeyManager.clearKeyState();
        calcScreenArea();
        //m_sstack.push(new MapIntroStatus(this));
    }
    /**
     * 重新开始当前地图
     * @see     GameMap 
     */
    public void reset(){
        gotoMap(m_maps[m_currLevel], (int)m_player.getX(), (int)m_player.getY());
        //String s = GameConfig.defaultConfig.getString("StartScript", "");
        //if (!s.equals("")){
        //    m_scripts.add(s, null);
        //}
    }

    /**
     * 进入下一关，如果当前为最后一关，就进入第一关
     */
    public void goNextLevel(){
        m_currLevel++;              //关号递增
        //如果大于最大关,从第一关开始
        if (m_currLevel >= m_maps.length){
            pushStatus(new SwitchStatus(this, new CompleteAllStatus(this), 1));
        }else{
            gotoMap(m_maps[m_currLevel], (int)m_player.getX(), (int)m_player.getY());
        }
    }

   /**
     * 开启游戏线程
     * @see     #stop
     */
    public void start(){
        isStopped = false;
        KeyManager.clearKeyState();
        gameThread = new Thread(this);
        gameThread.start();
    }
    /**
     * 停止游戏线程
     * @see     #start
     */
    public void stop(){
        isStopped = true;
        gameThread.interrupt();
        gameThread = null;
        stopMusic();
    }
    /**
     * 暂停游戏线程
     * @see     #start
     */
    public void pause(){
        isPaused = true;
    }
    /**
     * 回复游戏线程
     * @see     #start
     */
    public void resume(){
        isPaused = false;
        currTime = System.currentTimeMillis();
    }
    
    /**
     * 使游戏状态回到标题 
     */
     
    public void backToTitle(){
        m_size = new Dimension(640, 480);
        m_sstack.clear();
        m_scripts.clear();
        KeyManager.clearKeyState();
        m_currLevel = 0;
        pushStatus(new TitleStatus(this));
        m_needRefresh = true;
        m_player = createPlayer();
    }
    
    public void startLogo(){
        m_size = new Dimension(640, 480);
        m_sstack.clear();
        KeyManager.clearKeyState();
        m_currLevel = 0;
        KeyManager.clearKeyState();
        pushStatus(new SwitchStatus(this, new LogoStatus(this), 1));
        m_needRefresh = true;
        m_player = createPlayer();
    }
    /**
     * 运行游戏线程
     * @see     #start
     */ 
    public void run(){
        long framerate = 10;
        m_screenArea = new Rectangle();
        //backToTitle();
        startLogo();
        System.out.println("Game begin");
        passedTime = 0;
        //游戏循环
        while( !isStopped ){
//            if (KeyManager.isKeyDown(KeyEvent.VK_ALT) 
//                        && KeyManager.isKeyJustDown(KeyEvent.VK_F)) {
//                m_io.toggleFullScreen();
//            }
//            if (KeyManager.isKeyDown(KeyEvent.VK_ALT) 
//                        && KeyManager.isKeyJustDown(KeyEvent.VK_D)) {
//                GameLib.setFullScreen(false);
//            }
            currTime = System.currentTimeMillis(); 
            if (!isPaused){
            	drawFrame();
                m_playTime += passedTime;
            }
            try{
                if (passedTime < framerate){
                    Thread.sleep(framerate - passedTime);
                }else{
                    Thread.sleep(5);
                }
            } catch( InterruptedException ex ) {
                break;
            }
            passedTime = System.currentTimeMillis() - currTime;
            //防止动作过大
            if (passedTime > 100) passedTime = 100;
            
        }
        m_scripts.clear();
        System.out.println("Game end");
    }

    /**
     * 取得当前地图大小
     */
    public Dimension getSize(){
        return m_size;
    }
    
    /**
     * 取得屏幕区域
     */
    public Rectangle getScreenArea(){
        return m_screenArea;
    }
    
    /*
     * 由PANEL大小，地图大小,人物位置计算出屏幕区域
     */    
    private void calcScreenArea(){
        Dimension psize = m_panel.getSize();
        if (psize.width >= m_size.width){ 
            m_screenArea.x = -(psize.width - m_size.width)/2;
            m_screenArea.width = m_size.width;
        }else {
            m_screenArea.width = psize.width;
            if ( m_map ==null ||m_player == null ){
                m_screenArea.x = 0;
            }else{
                int px = (int)(m_player.getX()+m_player.getW()/2);
                if (px <= psize.width/2)
                    m_screenArea.x = 0;
                else if (m_size.width - px <= psize.width/2)
                    m_screenArea.x = m_size.width - psize.width;
                else
                    m_screenArea.x = px - psize.width/2;
           }
        }            
        if (psize.height >= m_size.height){ 
            m_screenArea.y = -(psize.height - m_size.height)/2;
            m_screenArea.height = m_size.height;
        }else {
            m_screenArea.height = psize.height;
            if ( m_map ==null || m_player == null ){
                m_screenArea.y = 0;
            }else{
                int py = (int)(m_player.getY()+m_player.getH()/2);
                if (py <= psize.height/2)
                    m_screenArea.y = 0;
                else if (m_size.height - py <= psize.height/2)
                    m_screenArea.y = m_size.height - psize.height;
                else
                    m_screenArea.y = py - psize.height/2;
           }
        }            
    }    
    /**
     * 取得面板
     */
    public JPanel getPanel(){
        return m_panel;
    }
    /**
     * 取得当前地图
     */
    public GameMap getMap(){
        return m_map;
    }
    /**
     * 取得离屏缓冲
     */
    public BufferedImage getBuffer(){
        return backBuffer;
    }
    /**
     * 取得脚本管理器
     */
    public ScriptManager getScript(){
        return m_scripts;
    }
    /**
     * 取得图片管理器
     */
    public ImageManager getImageManager(){
        return m_imgMgr;
    }
    /**
     * 取得随机数，范围：0~n-1
     */
    public int getRandom(int n){
        return m_rand.nextInt(n);
    }
    /**
     * 取得玩家角色
     */
    public Player getPlayer(){
        return m_player;
    }
    /**
     * 添加玩家人物
     */
    public void setPlayer(Player player){
        m_player = player;
    }
    /**
     * 更新屏幕
     */
    public synchronized void refreshScreen(){
       //把m_needRefresh设为true,下一帧时更新屏幕
       m_needRefresh = true; 
    }    
    /**
     * 向状态栈中压入一个状态
     * @param s 要压入的状态示例的引用
     * @see #popStatus
     * @see StatusStack#push
     */
    public void pushStatus(AbstractStatus s){
        m_sstack.push(s);
    }

    /**
     * 从状态栈中弹出一个状态
     * @return 状态栈被弹出的状态，如果状态栈为空，返回null
     * @see #pushStatus
     * @see StatusStack#pop
     */
    public AbstractStatus popStatus(){
        return m_sstack.pop();
    }
    /**
     * 播放指定音乐文件，并停止当前音乐。格式为au，aiff，wav
     * @param filename 要播放的音乐文件
     * @param looped 是否循环
     * @see #stopMusic
     * @see #playSound
     * @see MusicPlayer#play
     */
    
    public void playMusic(String filename, boolean looped){
        if ( GameConfig.defaultConfig.getBoolean("Music", true) ){
            m_io.playMusic(filename, looped);
        }
    }
    /**
     * 播放指定音乐文件，但不停止当前音乐。格式为au，aiff，wav
     * @param filename 要播放的音乐文件
     * @see #playMusic
     * @see MusicPlayer#play
     */
    public void playSound(String filename){
        if ( GameConfig.defaultConfig.getBoolean("Sound", true) ){
            m_io.playSound(filename);
        }
    }
    /**
     * 停止当前音乐
     * @see #playMusic
     */
    public void stopMusic(){
        m_io.stopMusic();
    }
    /**
     * 装载指定图像文件。格式为png，jpg，gif，bmp
     * @param filename 要装载的图像文件
     * @return 装载后的图像
     * @see ImageManager#loadImage
     */
    public Image loadImage(String filename){
        return m_imgMgr.loadImage(filename);
    }
    /**
     * 从内存中删除图像
     */
    public void deleteImage(Image img){
        m_imgMgr.delete(img);
    }
    public MyIO getIO(){
        return m_io;
    }
	/**
	 * @param playTime The playTime to set.
	 */
	public void setPlayTime(long playTime) {
		this.m_playTime = playTime;
	}
	/**
	 * @return Returns the playTime.
	 */
	public long getPlayTime() {
		return m_playTime;
	}

	/**
	 * @param mouse 设置鼠标.
	 */
	public void setMouse(GameMouse mouse) {
		m_mouse = mouse;
	}

	/**
	 * @return 取得鼠标.
	 */
	public GameMouse getMouse() {
		return m_mouse;
	}   
    /**
     * 取得图形环境
     */
    public Graphics getBufferGraphics() {
        return m_bufGraph;
    }
}
