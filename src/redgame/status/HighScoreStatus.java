package redgame.status;
/*
 * HighScoreStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.util.*;

/**
 * HighScoreStatus类是显示高分榜的游戏状态.
 * @see AbstractStatus
 * @author 姚春晖
 */
public class HighScoreStatus extends AbstractStatus {
    private Image m_bk_img;
    private HighScoreManager m_highScore;
    /**
     * 创建一个AddHighScoreStatus
     * @param game 游戏引用
     */
    public HighScoreStatus(GameWorld game) {
        super(game);
        m_bk_img = m_game.loadImage("image/score.png");
        m_game.playMusic("music/score.mid", true);
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
            m_game.backToTitle();
        }
        m_counter += passedTime;
        return 0;
    }
    /**
     * 什么也不做
     */
    public  int move(long passedTime){
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
        g.drawImage(m_bk_img, x, y, m_game.getPanel());
        Font f = g.getFont();
        Font bigf = new Font("宋体", 0, 14);
        g.setColor(Color.green);
        g.setFont(bigf);
        for (int i = 0; i < m_highScore.count; i++){
            g.drawString(m_highScore.names[i], 140, 175+30*i);
            g.drawString(m_highScore.scores[i]+"", 470, 175+30*i);
        }
        g.setFont(f);
        return 0;
    }
}