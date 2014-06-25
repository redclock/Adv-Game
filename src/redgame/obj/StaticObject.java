package redgame.obj;
/*
 * StaticObject.java 作者：姚春晖
 */
import java.awt.*;
import redgame.engine.*;
/**
 * StaticObject类是静止物体.
 * 它默认为宝物
 * @author 姚春晖
 */
public class StaticObject extends AbstractObject {
    /*
     * 是否已死
     */
    protected boolean   m_dead;
    /*
     * 死时动画的计时器
     */
    protected long   m_deadCounter;
    /**
     * 构造StaticObject
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 元素宽度
     * @param h 元素高度
     * @param begin 图像开始格索引
     * @param end 图像结束格索引
     */

	public StaticObject(GameWorld game, Image img, 
					int x, int y, int w, int h, int begin, int end
	 				) {
		super(game, img, x, y, w, h);
		m_anim.setRange(begin, end, m_delay);
		m_anim.start();
		m_blocked = false;
	}

	/**
	 * 更新动画
	 */
	public void move(long passedTime){
		m_anim.update(passedTime);
	}
    /**
     * 是否已死
     */
    public boolean isdead(){
        return m_dead;
    }
    public void die(){
        m_dead = true;
        m_deadCounter = 0;
    } 

    public void paint(Graphics g){
        if ( !m_visible ) return;
        if ( !m_dead ){
            super.paint(g);
        }else{
            int yy = (int)( m_deadCounter * 50 / 1000 );
            m_anim.paint_alpha(g, (int)x - m_block.x, (int)y - m_block.y - yy, 
                    1.0f - (float)m_deadCounter / 1000.0f);
            m_deadCounter += m_game.passedTime;
            if (m_deadCounter >= 900) m_visible = false;
        }
    }

}
