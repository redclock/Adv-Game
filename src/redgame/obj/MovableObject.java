package redgame.obj;
/*
 * MovableObject.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
/**
 * MovableObject类是可以移动的物体，它具有重力
 * 这是一个抽象类，要使用它的派生类。
 * @author 姚春晖
 */
abstract public class MovableObject extends AbstractObject {
    /**
     *y方向的分速度
     */
	protected float m_vy = 0.0f;
    /**
     *x方向的分速度
     */
    protected float m_vx = 0.0f;
    /**
     * 是否具有重力
     */
    protected boolean m_gravity = true; 
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
    
	public MovableObject(GameWorld game, Image img, int x, int y, int w, int h) {
		super(game, img, x, y, w, h);
		m_anim.start();
	}
    /**
     * 由于重力而下落
     */
    public void moveDown(long passedTime){
		if (! m_gravity ) return;
		float dy = 0;
		if (m_vy < 0){
			dy = - m_game.getMap().gotoUp(this, -passedTime*m_vy);
			//如果碰到东西，并且不是弹簧，就让m_vy=0
			if (m_vy < 0 && dy > passedTime*m_vy) m_vy = 0;
		}else{
            if (m_vy == 0) m_game.getMap().gotoDown(this, 0.01f); //如果在地上,探测一下
			else dy = m_game.getMap().gotoDown(this, passedTime*m_vy);
		}
		y += dy;
		if ( passedTime >0 && m_vy > 0 && dy == 0 ){
			m_vy = 0.0f;
		}else{
			m_vy += 0.001*passedTime;
		}
	}
	/*
	 * 得到X方向分速度
	 */
	public float getVX(){
	   return m_vx;
	}
    /*
     * 得到Y方向分速度
     */
    public float getVY(){
       return m_vy;
    }
    /*
     * 设置Y方向分速度
     */
    public void setVY(float vy){
       m_vy = vy;
    }
    /*
     * 设置X方向分速度
     */
    public void setVX(float vx){
       m_vx = vx;
    }
    public void update(long passedTime){
    }
    public void move(long passedTime){
    }
    public void meetSpring(){
    }
    public boolean getGravity(){
        return m_gravity;
    }
    public void setGravity(boolean g){
        m_gravity = g;
    }
}
