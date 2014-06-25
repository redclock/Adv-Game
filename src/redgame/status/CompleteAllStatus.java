package redgame.status;
/*
 * CompleteAllStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import redgame.engine.*;
import redgame.util.*;
/**
 * CompleteAllStatus类是通关的游戏状态.
 * 
 * @see AbstractStatus
 * @author 姚春晖
 */

public class CompleteAllStatus extends AbstractStatus {
    //"congratulations"图像
    private Image m_con_img;
    //图像大小
    private int m_imgw, m_imgh;
    /**
     * 创建一个CompleteAllStatus
     * @param game 游戏引用
     */
    public CompleteAllStatus(GameWorld game) {
        super(game);
        m_con_img = m_game.loadImage("image/congratulations.png");
        m_imgw = m_con_img.getWidth(m_game.getPanel());
        m_imgh = m_con_img.getHeight(m_game.getPanel());
        m_game.playSound("sound/passall.mid");
    }
    /**
     * 对于前个状态:禁止其更新和显示
     */
    public void updatePrior(long passedTime, Graphics g){
    }
    /**
     * 按键退出
     */
    public int update(long passedTime){
        if ( KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)
            ||KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)){
            m_game.startLogo();
        }
        m_counter += passedTime;
        return 0;
    }
    /**
     * 什么也不做
     */
    public int move(long passedTime){
        return 0;
    }
    
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    
    public int draw(long passedTime, Graphics g){
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
        int h = sc.height;
        g.setColor(Color.BLACK);
        g.fillRect(x, y, w, h);
        Font f = g.getFont();
        int size = (int)(m_counter/30);
        if (size > 100) size = 100;
        int iw = m_imgw * size / 100;
        int ih = m_imgh * size / 100;
        g.drawImage(m_con_img, (w-iw)/2+x, y + 100, 
                                (w-iw)/2+x+iw, y+100+ih,
                                0, 0, 
                                m_imgw, m_imgh,
                                m_game.getPanel());
                                 
        if (size == 100){
            
            Font bigf = new Font("宋体", Font.BOLD, 32);
            Rectangle2D rt = bigf.getStringBounds("你已经成功通关", ((Graphics2D)g).getFontRenderContext());
            g.setColor(Color.YELLOW);
            g.setFont(bigf);
            //在屏幕中心画
            g.drawString("你已经成功通关", x+(int)( w - rt.getWidth())/2, y+250);
        }        
        g.setFont(f);
        return 0;
    }
}