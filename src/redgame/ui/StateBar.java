package redgame.ui;
/*
 * StateBar.java 作者：姚春晖
 */
 
import java.awt.*;
import java.awt.image.*;
import redgame.engine.*;
import redgame.anim.*;

/**
 * StateBar类是状态条
 * 
 * @author 姚春晖
 */

public class StateBar{
    private GameWorld m_game;
    private int m_counter;
    private BufferedImage bi;
    private Animation m_heart;
    private int m_w;
    private void createBar(){
        int barHeight = 25;
        m_w = m_game.getSize().width;
        Paint barPaint = new GradientPaint(
                                0, 0, new Color(0,0,100,255), 
                                640, 0, new Color(100,0,100,0));
        bi = new BufferedImage(m_w, barHeight,
                                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setPaint(barPaint);
        g2.fillRect(0, 0, m_w, barHeight);
        g2.setPaint(null);
        g2.dispose();
    }
    public StateBar(GameWorld game){
        m_game = game;
        createBar();
        m_heart = new Animation(game, game.loadImage("image/heart.png"), 20, 20);
    }

    /**
     * 更新计时器
     * @param passedTime 从上一次调用到现在的时间
     */
    public void move(long passedTime){
        m_counter += passedTime;
    }
    //工具函数:将毫秒转换为字符串
    private String timeToStr(long time){
        String s = "";
        long part;
        time /= 1000;
        part = time % 60;
        if (part < 10)
            s = ":0"+part;
        else
            s = ":"+part;    
        time/=60;
        part = time % 60;
        if (part < 10)
            s = ":0"+part+s;
        else
            s = ":"+part+s;
        time/=60;
        s = time+s;
        return s;
    }
    /**
     * 画图代码
     * @param g          用来画图的引用 
     */
    public void draw(Graphics g){
        if (m_w != m_game.getSize().width)
            createBar();
        Graphics2D g2 = (Graphics2D)g;
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        g2.drawImage(bi, x, y, m_game.getPanel());
        g2.setColor(Color.WHITE);
        Font f = g2.getFont();
        Font bigf = new Font("宋体", 0, 12);
        g2.setFont(bigf);
        
        g2.drawString("Map:"+ m_game.getMap().getName(), 25+x, 18+y);

        g2.drawString("Time:"+ timeToStr(m_game.getPlayTime()), 150+x, 18+y);
        int p = m_game.getPlayer().HP;
        int n = (int)(p / 4);
        if (p == n * 4){
            m_heart.setRange(0, 0, 100);
            for (int i = 0; i < n; i++){
                m_heart.paint(g, x+m_game.getScreenArea().width -20 - i*20, y+5);
            }
        }else{
            m_heart.setRange(0, 0, 100);
            for (int i = 0; i < n; i++){
                m_heart.paint(g, x+m_game.getScreenArea().width - 20 - i*20, y+5);
            }
            m_heart.setRange(4-p%4, 4-p%4, 100);
            m_heart.paint(g, x+m_game.getScreenArea().width - 20 - n*20, y+5);
        } 
        if (m_game.getPlayer().curItem != null){
            m_game.getPlayer().curItem.drawStateIcon(g2,  
                                 x+m_game.getScreenArea().width - 40, 
                                 y+m_game.getScreenArea().height - 40);
        }
        g2.setFont(f);
    }
}