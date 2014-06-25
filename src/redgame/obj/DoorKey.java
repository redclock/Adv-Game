package redgame.obj;
import java.awt.*;
import redgame.engine.*;

public class DoorKey extends StaticObject {
    /**
     * 钥匙的类型
     */
    public int id;
    /**
     * 构造DoorKey
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 元素宽度
     * @param h 元素高度
     * @param begin 图像开始格索引
     * @param end 图像结束格索引
     */

    public DoorKey(GameWorld game, Image img, 
                    int x, int y, int w, int h, int begin, int end, int id
                    ) {
        super(game, img, x, y, w, h, begin, end);
        this.id = id;
		Player p = game.getPlayer();
		if (p != null){
			//如果已经拿到了,就隐藏
			if (p.hasKey[id])
			{
				m_dead = true;
				m_visible = false;
			}
		}
        
    }    
}
