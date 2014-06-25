package redgame.status;
/*
 * ShowMenuStatus.java 作者：姚春晖
 */
import java.awt.*;

import redgame.engine.*;
import redgame.ui.*;
/**
 * ShowMenuStatus类是游戏中显示菜单的游戏状态.
 * 在这时会显示一个菜单,供玩家选择
 * @see AbstractStatus
 * @author 姚春晖
 */
public class ShowMenuStatus extends AbstractStatus{
    //菜单
    private AbstractMenu m_menu;
    //菜单位置
    private int mx = 200, my = 200;
    /**
     * 创建一个ShowMenuStatus
     * @param game 游戏引用
     */
    public ShowMenuStatus(GameWorld game, int x, int y, AbstractMenu menu) {
        super(game);
        m_menu = menu;
        mx = x;
        my = y;
    }
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        m_menu.paint((Graphics2D)g, mx, my);
        return 0;
    }

    /**
     * 更新状态, 输入上下键选择,空格键确定
     * @param passedTime 从上一次调用到现在的时间
     */
    
    public int update(long passedTime){
        m_menu.update(passedTime);
        if (m_menu.done()) return 1;
        return 0;
    }
    /**
     * 
     */
    public int move(long passedTime){
        m_menu.move(passedTime);

        return 0;
    }
}
