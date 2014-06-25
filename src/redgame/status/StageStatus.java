package redgame.status;
/*
 * StageStatus.java 作者：姚春晖
 */

import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.ui.*;
import redgame.util.*;
/**
 * StageStatus类是显示和更新地图的游戏状态.
 * @see AbstractStatus
 * @author 姚春晖
 */
public class StageStatus extends AbstractStatus {
    //状态条
    private StateBar m_bar;
    private String m_mapName;
    private int m_x, m_y;

    /**
     * 创建一个StageStatus
     * @param game 游戏引用
     * @param mapName 要装载的地图
     * @param x 主人公出场位置
     * @param y 主人公出场位置
     */
    public StageStatus(GameWorld game, String mapName, int x, int y) {
        super(game);
        m_bar = new StateBar(game);
        m_mapName = mapName;
        m_x = x;
        m_y = y;
    }
    public void setPrior(AbstractStatus prior){
        m_prior = null;
    }
    public int start(){
        m_game.loadMap(m_mapName);
        m_game.getPlayer().setPosition(m_x, m_y);
        if (m_game.getMap().getStartScript() != null) {
        	m_game.getScript().add(m_game.getMap().getStartScript(), null);
        	
        }
        return 0;
    }
    /**
     * 更新状态, 输入完毕则停止
     * @param passedTime 从上一次调用到现在的时间
     */
    public int update(long passedTime){
        m_game.getScript().update();
        m_game.getMap().update(passedTime);
        m_game.getMap().moveActors(passedTime);
        if (KeyManager.isKeyDown(KeyEvent.VK_W))
            m_game.pushStatus(new SelectWeaponStatus(m_game));
        return 0;
    }

    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        m_game.getMap().paint(g);
        m_bar.draw(g);
        return 0;
    }
    /**
     * 更新计时器
     * @param passedTime 从上一次调用到现在的时间
     */

    public int move(long passedTime) {
        m_game.getMap().move(passedTime);
        m_bar.move(passedTime);
        return 0;
    }
}