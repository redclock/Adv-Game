package redgame.status;
/*
 * MenuStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.util.*;
import redgame.ui.*;

/**
 * MenuStatus类是游戏中显示菜单的游戏状态.
 * 在这时会显示一个菜单,供玩家选择
 * @see AbstractStatus
 * @author 姚春晖
 */
public class MenuStatus extends AbstractStatus{
    //选项列表
    private String[] m_items = 
        {"继续游戏", 
         "玩家状态",
         "选择武器", 
         "载入进度", 
         "回到主菜单"
         };
    
    //菜单大小
    private int mw = 200, mh = 200;
    
    //菜单对象
    private TextMenuEx m_menu;
    /**
     * 创建一个MenuStatus
     * @param game 游戏引用
     */
    public MenuStatus(GameWorld game) {
        super(game);
        m_menu = new TextMenuEx(game, "菜单", m_items, mw, mh);
    }
    
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        m_menu.paint(g2d, x + (sc.width - mw)/2, 
                y + (sc.height - mh)/2);
        return 0;
    }

    /**
     * 更新状态, 输入上下键选择,空格键确定
     * @param passedTime 从上一次调用到现在的时间
     */
     public int update(long passedTime){
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)) return -1;
        m_menu.update(passedTime);
        if ( m_menu.done() ){
            switch(m_menu.getIndex()){
                case 0: return -1;                   //"继续游戏"
//                case 1: m_game.reset(); break;       // "重新开始本关"
                case 1: 
                    m_game.popStatus(); 
                    m_game.pushStatus(new ShowStateStatus(m_game));//"玩家状态"
                    return 0;
                case 2:  
                    m_game.popStatus(); 
                    m_game.pushStatus(new SelectWeaponStatus(m_game));//"选择武器"
                    return 0;       
//                case 2: m_game.goNextLevel();break;  //"跳至下一关"
                case 3:
                    m_game.popStatus(); 
                    m_game.pushStatus(new LoadGameStatus(m_game));//"载入进度"
                    return 0;                      
                case 4: m_game.backToTitle();break;  //"回到主菜单"
            }
            
            
        }
        return 0;
    }
    
	public int move(long passedTime) {
		m_menu.move( passedTime );
		return 0;
	}
    
}
