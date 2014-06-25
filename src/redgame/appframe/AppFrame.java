package redgame.appframe;

import redgame.engine.*;
import redgame.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class CloseAdapter extends WindowAdapter{
    public void windowClosing(WindowEvent e){
        System.exit(0);
    }
}

/**
 * AppFrame是主窗体
 * @author 姚春晖
 */
public class AppFrame extends JFrame implements MyIO{
    private GamePanel  picpane = new GamePanel();
    
    //背景音乐播放器,同一时间只有一个背景音乐
    private MusicPlayer m_music = null;
    
    private static boolean isFirst = true;

    private static DisplayMode window_mode = null;
    
    public boolean isApplet(){
        return false;
    }
    
    public InputStream getInput(String filename){ 
        try{
            return new FileInputStream(filename);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        }    
    }

    public Image loadImage(String filename){
        return Toolkit.getDefaultToolkit().getImage(filename);
    }

    public void playMusic(String filename, boolean looped){
        stopMusic();
        m_music = new MusicPlayer(filename, looped);
        m_music.start();
    }
    public void stopMusic(){
        if (m_music != null){
            m_music.stop();
            m_music = null;
        }
    }
    public void playSound(String filename){
        MusicPlayer music = new MusicPlayer(filename, false);
        music.start();
    }

    /**
     * 将窗体移动到屏幕中间
     */ 
    public void moveCenter(){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2,
              (screen.height - getHeight()) / 2);
    } 

    public void toggleFullScreen() {
        GameWorld game = picpane.getGame();
        if (game != null) {
            picpane.getGame().pause();
            
            boolean full = !GameConfig.defaultConfig.getBoolean("FullScreen", true);

            GameLib.setFullScreen(false);
            try {
                Thread.sleep(1000);
            }catch(InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Toggle full: " + full);

            GameConfig.defaultConfig.put("FullScreen", full);
            
            try {
                Thread.sleep(1000);
            }catch(InterruptedException ex) {
                ex.printStackTrace();
            }
            
            AppFrame f = new AppFrame("HELLO"+full, picpane);
            this.setVisible(false);
            this.remove(picpane);
            this.validate();
            this.dispose();
            picpane.getGame().resume();
        }
    }   
     
    /**
     * 开启游戏
     */
    public void startGame(){
        picpane.startGame(this);
    }
    /**
     * 停止游戏
     */
    
    public void stopGame(){
        picpane.stopGame();
    }
    
    public GameWorld getGame(){
        return picpane.getGame();
    }
    
    private void Init() {
        boolean full = GameConfig.defaultConfig.getBoolean("FullScreen", true);
        setUndecorated(full);

        Container cp = getContentPane();
        
        cp.setLayout(new BorderLayout());
        cp.add(picpane, BorderLayout.CENTER);
        addWindowListener(new CloseAdapter());
        setVisible(true);
        //为了去除标题栏和边框的宽度，精确定窗口大小
        Insets insets = getInsets();
        setSize(insets.left+insets.right+640,insets.top+insets.bottom+480);

        GameLib.Init(this);
        
        if (!full){
            GameLib.setFullScreen(false);
            System.out.println("IT'S not full");            
            if (window_mode != null) {
              //  GameLib.setMode(window_mode.getWidth(), window_mode.getHeight(), window_mode.getBitDepth());
            }
            moveCenter();
        }else{
            if (window_mode == null)
                window_mode = GameLib.getDisplayMode();
            GameLib.setFullScreen(true);
            System.out.println("IT'S full");            
            if (GameLib.displayModeSupported(640, 480, 32))
                GameLib.setMode(640, 480, 32);
            else if (GameLib.displayModeSupported(640, 480, 24))
                GameLib.setMode(640, 480, 24);
            else if (GameLib.displayModeSupported(640, 480, 16))
                GameLib.setMode(640, 480, 16);
        }
        setResizable(false);
        
        setVisible(true);
        
        isFirst = false;
    }
    
    /**
     * @param s 标题
     */
    public AppFrame(String s){
        super(s);
        GameConfig.defaultConfig.load("config.txt", this);
        Init();
    }

    public AppFrame(String s, GamePanel p){
        super(s);
        picpane = p;
        Init();
        System.out.println("PPP");            
        
    }    
    
}