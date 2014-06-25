package redgame.obj;
/*
 * Wall.java 作者：姚春晖
 */
 import java.awt.*;
 import redgame.engine.*;
/**
 * Wall类是地图元素墙，它可以碰撞，不能爬
 * @author 姚春晖
 */
 public class Wall extends MapObject {
    private boolean m_bad;
    /**
     * 构造墙
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
     * @see MapObject#MapObject
     */
    public Wall(GameWorld game, Image img, 
					int x, int y, int w, int h,
	 				int tilew, int tileh, boolean bad) {
		super(game, img, x, y, w, h, tilew, tileh);
		m_bad = bad;
		m_blocked = true;
	}
    /**
     * 返回假
     */
	public boolean isClimbable(){
		return false;
	}

    public boolean collision(AbstractObject obj, int direction){
        if (m_bad){
            if ( obj instanceof Player ){
                ((Player)obj).hurt(1, 1000);
                return true;
            }
        }
        //执行脚本
        if (m_script != null && obj instanceof Player){
            m_game.getScript().add(m_script, 
                                    new ScriptSource(this, obj, direction));
        }

        return false;
    }

}
