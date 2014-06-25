package redgame.obj;
/*
 * Bounce.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
/**
 * Bounce类是可以弹跳的物体，它碰到墙壁可以弹回
 * 
 * @author 姚春晖
 */
public class Bounce extends MovableObject {
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
    
    public Bounce(GameWorld game, Image img, int x, int y, int w, int h) {
        super(game, img, x, y, w, h);
        m_blocked = true;
    }
    /**
     * 移动
     */   
    public void move(long passedTime){
        m_anim.update(passedTime);
        moveDown(passedTime);
    }
    /**
     * 设置初速度
     */   
    public void setSpeed(float vx, float vy){
        m_vx = vx;
        m_vy = vy;
    }
    
    /**
     * 由于重力而下落
     */
    public void moveDown(long passedTime){
        float dy;
        float dx;
        boolean b = false;
        if (m_vy < 0){
            dy = m_game.getMap().gotoUp(this, -passedTime*m_vy);
            y -= dy;       
            if (dy < - passedTime*m_vy){
                m_vy = - m_vy*0.99f;
                b = true;
            }   
        }else{
            dy = m_game.getMap().gotoDown(this, passedTime*m_vy);
            y += dy;
            if (dy < passedTime*m_vy){
                m_vy = - m_vy*0.99f;
                b = true;
            }    
        }
        if (m_vx < 0){
            dx = m_game.getMap().gotoLeft(this, -passedTime*m_vx);
            x -= dx;
            if (dx < -passedTime*m_vx){
                m_vx = - m_vx*0.99f;
            }       
        }else{
            dx = m_game.getMap().gotoRight(this, passedTime*m_vx);
            x += dx;
            if (dx < passedTime*m_vx){
                m_vx = - m_vx*0.99f;
             }    
        }
        if (!b) m_vy += 0.001*passedTime;
    }   
}
