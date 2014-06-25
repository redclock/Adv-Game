package redgame.engine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import redgame.util.*;

//画图的panel

public class GamePanel extends JPanel {
    protected GameWorld m_game;
    private String m_prompt = "LOADING...";
    //键盘监听器
    public GameWorld getGame(){
        return m_game;
    }
    
    public GamePanel(){
        setBackground(new Color(0, 0, 0));
        setFocusable(true);
//        setIgnoreRepaint(true);
        addKeyListener(KeyManager.getDefault());  
        //因为游戏图画根据窗体大小变化
        //当PANEL大小变化时需要重画.否则由小变大不会调用paint
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                if (m_game != null)
                    m_game.refreshScreen();
            }
        });
        
        //当失去焦点,要清空键盘,否则可能导致只有按键无抬键
        addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e){
//                System.out.println("lost Focus");
                KeyManager.clearKeyState();
                m_prompt = "PAUSED";
                repaint();
                if (m_game != null) m_game.pause();
            }
            public void focusGained(FocusEvent e){
//                System.out.println("lost Focus");
                if (m_game != null) m_game.resume();
                m_prompt = "LOADING...";
            }
        });
    }

    /**
     * 开启游戏
     */
    public void startGame(MyIO io){
        if (io.isApplet() == false){
            try{
                setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                    Toolkit.getDefaultToolkit().getImage(""),
                    new Point(0,0),
                    "invisible")
                 );
            }catch(Exception e){
            }
        }
        m_game = new GameWorld(this, io);
        m_game.start();
    }
    /**
     * 停止游戏
     */
    
    public void stopGame(){
        m_game.stop();
        m_game = null;
        repaint();
    }

    public Dimension getPreferredSize(){
        return new Dimension(640, 480);
    }

    //重画时清除屏幕
    public void paint(Graphics g){
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D)g; 
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Default", 40, 40));
        g.drawString(m_prompt, 220, 250);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}

