package redgame.obj;
/*
 * Patrol.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
/**
 * Patrol类是可以巡逻的敌人
 * 
 * @author 姚春晖
 */
public class Patrol extends Enemy {
	//巡逻的范围
	private int m_min, m_max;
	private boolean m_follow = false;
    private boolean m_canSword = false;
    private boolean m_canJump = false;
    private boolean m_still = false;
    
    /**
     * 构造Patrol
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @param pmin 巡逻范围的最小值 
     * @param pmax 巡逻范围的最大值 
     * @see Actor
     */
     
    public Patrol(GameWorld game, Image img, 
				int x, int y, int w, int h, 
				int pmin, int pmax) {
		super(game, img, x, y, w, h);
		m_min = pmax > pmin ? pmin:pmax;
		m_max = pmax < pmin ? pmin:pmax;
	}
	/**
	 * 设置是否静止不动
	 */
	public void setStill(boolean still){
	   m_still = still;
	}
	/**
	 * 取得是否静止不动
	 */
    public boolean getStill(){
       return m_still;
    }
    /**
	 * 设置是否能跟踪
	 */
	public void setFollow(boolean follow){
	   m_follow = follow;
	}
    /**
     * 取得是否能跟踪
     */
    public boolean getFollow(){
       return m_follow;
    }
    /**
     * 设置是否能挥剑
     */
    public void setCanSword(boolean sword){
       m_canSword = sword;
    }
    /**
     * 取得是否能挥剑
     */
    public boolean getCanSword(){
       return m_canSword;
    }
    /**
     * 设置是否能跳跃
     */
    public void setCanJump(boolean jump){
       m_canJump = jump;
    }
    /**
     * 取得是否能跳跃
     */
    public boolean getCanJump(){
       return m_canJump;
    }
    /**
	 * 不能再向前走时, 就回头
	 */
	public void cannotGo(){
		m_face = 3 - m_face;
		updateAnimation();
	}
	/**
	 * 更新状态
	 */
	public void update(long passedTime){
        if (m_dead) return;
        super.update(passedTime);
        if (new_face == -1) new_face = m_face;
        if ( m_follow ) { 
            Player p = m_game.getPlayer();
            if (!m_lull && p != null && p.getVisible()){
                if ( 
                    getY() < p.getY() + p.getH() &&
                    getY() + getH() > p.getY() &&
                    p.getX() + p.getW() > m_min && p.getX() < m_max){
                        new_face = (p.getX() < getX())? G_LEFT : G_RIGHT; 
                    }
            }
        }
        if (getX() <= m_min){
            new_face = G_RIGHT;
        }else if (getX()+getW() >= m_max){
            new_face = G_LEFT;
        }
        Player p = m_game.getPlayer();
        if (m_canSword && !m_lull && p != null && p.getVisible() &&
            ! m_isSwording && 
            getY() < p.getY() + p.getH() &&
            getY() + getH() > p.getY()){
            
            int d = (int)(p.getX() - getX() - getW ());
            if (d > 0 && d < 15){
                new_face = G_RIGHT;
                m_face = G_RIGHT;
                updateAnimation();
                doSword(300, false);
                
            }else{
                d = (int)(getX() - p.getX() - p.getW ());
                if (d > 0 && d < 15){
                    new_face = G_LEFT;
                    m_face = G_LEFT;
                    updateAnimation();
                    doSword(300, false);
                }
            }        
        }
        if (m_still) new_face = -1;
	}	
}
