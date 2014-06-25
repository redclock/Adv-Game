package redgame.status;
/*
 * MapIntroStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import redgame.engine.*;
import redgame.util.*;
/**
 * MapIntroStatus类是关卡开始前简介的游戏状态.
 * @see AbstractStatus
 * @author 姚春晖
 */
public class MapIntroStatus extends AbstractStatus{
    private String[] m_intro;
    private BufferedImage bi;
    /**
     * 创建一个MapIntroStatus
     * @param game 游戏引用
     */
    public MapIntroStatus(GameWorld game) {
        super(game);

        m_intro = m_game.getMap().getIntro();

        Rectangle sc = m_game.getScreenArea();
        bi = new BufferedImage(sc.width, sc.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, sc.width, sc.height);
        g2.setColor(Color.WHITE);
        g2.setColor(Color.WHITE);
        Font f = g2.getFont();
        Font msgf = new Font("宋体", 0, 14);
        g2.setFont(msgf);
        for (int i = 0; i < m_intro.length; i++){
           g2.drawString(m_intro[i], 50, 50+i*20);
        }
        g2.setFont(f);
        g2.dispose();
    }
    
    /**
     * 按键退出
     */
    public int update(long passedTime){
        if ( KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)
            ||KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)){
//            System.out.println("Intro exited");
            return -1;
        }
        return 0;
    }

    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        g2.drawImage(bi, x, y, m_game.getPanel());
        return 0;
    }

    /**
     * 什么也不做
     */
    public int move(long passedTime) {
        return 0;
    }   
}