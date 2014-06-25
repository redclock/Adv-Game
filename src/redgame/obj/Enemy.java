package redgame.obj;
/*
 * Enmey.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;

/**
 * Enemy类是敌人角色
 * 
 * @author 姚春晖
 */

public class Enemy extends NPC{
    private boolean m_harmful = true;
    private Bonus m_bonus;

    /**
     * 构造Enemy
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @see Actor
     */

    public Enemy(GameWorld game, Image img, int x, int y, int w, int h) {
        super(game, img, x, y, w, h);
    }
    /**
     * 覆盖collision
     * @see NPC#collision
     */

    public boolean collision(AbstractObject obj, int direction){
        if (obj instanceof Bounce){
            if (m_harmable){
                //if (!m_lull){
                    if (!m_dead) startLull(5000);
                // }else{
                //    stopLull();
                // }     
            }
            m_game.getMap().removeBullet(obj);
            return true;
        }else {
            return super.collision(obj, direction);
        }
    } 
    
    public void die() {
        super.die();
        if (m_bonus != null) {
            m_bonus.setPosition (getX(), getY());
            m_game.getMap().addStatic(m_bonus);
        }        
    }
    
    public boolean getHarmful(){
        return m_harmful;
    }

    public void setHarmful(boolean h){
        m_harmful = h;
    }
    
    public Bonus getBonus() {
        return m_bonus;
    }
    
    public void setBonus(Bonus b) {
        m_bonus = b;
    }
}
