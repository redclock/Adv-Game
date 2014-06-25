package redgame.obj;
import java.util.*;
/*
 * Sword.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
/**
 * Sword类是武器剑
 * 
 * @author 姚春晖
 */
public class Sword extends MovableObject {
    public Rectangle rect;
    private Actor m_owner;
    private int m_damage;
    /**
     * 构造物体，并开启动画
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @see Animation
     * @see GameWorld
     */
     
    public Sword(GameWorld game, Actor owner, Image img, int x, int y, int w, int h,
                    int begin, int end, int damage) {
        super(game, img, x, y, w, h);
        m_owner = owner;
        m_anim.setRange(begin, end, m_delay);
        m_anim.setLooped(false);
        m_anim.start();
        m_blocked = false;
        m_damage = damage;
    }
    /**
     * 移动
     */   
    public void move(long passedTime){
        m_anim.update(passedTime);
    }
    
    public void update(long passedTime){

        Vector actors = m_game.getMap().getActors();
        for (int i = 0; i < actors.size(); i ++){
            Actor a = (Actor)actors.get(i);
            if (!a.isdead() && a.checkCollision(rect))
                a.hurt(m_damage, 1000);
        }
    }
    public boolean finished(){
        return m_anim.end == m_anim.currIndex;
    }
    
    public void makeRect(){
        rect = new Rectangle((int)x, (int)y, m_block.width, m_block.height);
    }
    
}
