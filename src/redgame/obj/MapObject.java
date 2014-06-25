package redgame.obj;
/*
 * MapObject.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
import redgame.anim.*;
/**
 * MapObject类是地图元素，它的图像是小图片平铺成大块
 * 这是一个抽象类，要使用它的派生类。
 * @author 姚春晖
 */
abstract public class MapObject extends AbstractObject{
    protected boolean m_damagable = false;
    /**
     * 构造地图元素
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 元素宽度
     * @param h 元素高度
     * @param tilew 图像一格的宽度
     * @param tileh 图像一格的高度
     * @see Animation
     * @see GameWorld
     * @see AbstractObject#AbstractObject
     */
    public MapObject(GameWorld game, Image img, 
					int x, int y, int w, int h,
	 				int tilew, int tileh) {
		super(game, null, x, y, w, h);
        //不使用默认的动画，而使用TiledAnimation
		m_anim = new TiledAnimation(game, img, w, h, tilew, tileh);
	}	
	/**
	 * 默认处理碰撞：什么也不做
	 */
	public boolean collision(AbstractObject obj, int direction){
		
		return false;
	} 
	/**
	 * 这个对象是否可爬
	 */
	abstract public boolean isClimbable();
	
	public void move(long passedTime){
	    m_anim.update(passedTime);
	}
    
    public boolean getDamagable(){
        return m_damagable;
    }

    public void setDamagable(boolean d){
        m_damagable = d;
    }
    
    public void destory(){
        m_visible = false;
        m_blocked = false;
    }
}
