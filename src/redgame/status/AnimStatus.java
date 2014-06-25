package redgame.status;
/*
 * AnimStatus.java 作者：姚春晖
 */
import java.awt.*;
import redgame.engine.*;
import redgame.anim.*;
public class AnimStatus extends AbstractStatus{
    FileAnimation m_anim;
    AbstractParticleSystem m_p;

    /**
     * 创建一个AnimStatus
     * @param game 游戏引用
     * @param x    位置
     * @param y    位置
     */
    public AnimStatus(GameWorld game, int x, int y, String name, AbstractParticleSystem p){
        super(game);
        m_p = p;
        m_anim = new FileAnimation(game, x, y, name);
    }
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        m_anim.paint(g);
        if (m_p != null) m_p.paint(g);
        return 0;
    }

    
    public int update(long passedTime){
        if (m_p != null) m_p.move(passedTime);
        m_anim.update(passedTime);
        if (m_anim.isStop())
            return 1;
        else    
            return 0;
    }


}
