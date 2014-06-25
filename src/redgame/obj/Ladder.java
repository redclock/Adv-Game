package redgame.obj;
/*
 * Ladder.java 作者：姚春晖
 */
import java.awt.*;
import redgame.engine.*;
/**
 * Ladder类是地图元素梯子，它可以爬，不能碰撞
 * @author 姚春晖
 */

public class Ladder extends MapObject {
    /**
     * 构造梯子
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
    public Ladder(GameWorld game, Image img, 
					int x, int y, int w, int h,
	 				int tilew, int tileh) {
		super(game, img, x, y, w, h, tilew, tileh);
		m_blocked = false;
	}
    /**
     * 返回真
     */
	public boolean isClimbable(){
		return true;
	}
}
	