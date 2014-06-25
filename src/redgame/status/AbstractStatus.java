package redgame.status;
/*
 * AbstractStatus.java 作者：姚春晖
 */
import redgame.engine.*;

/**
 * AbstractStatus类是"游戏状态"的基类."游戏状态"为游戏当前在做什么.
 * 所有状态都在游戏状态栈中,栈顶状态为当前活动状态.
 * 这是一个抽象类，要使用它的派生类
 * @see StatusStack
 * @author 姚春晖
 */
abstract public class AbstractStatus {
	//游戏引用
	GameWorld m_game = null;
	//计时器
	long m_counter = 0;
	//状态栈中的前一个状态
	protected AbstractStatus m_prior;
	/**
     * 创建一个AbstractStatus
     * @param game 游戏引用
     */
	public AbstractStatus(GameWorld game) {
		m_game = game;
	}
	/**
	 * 设置前一个状态
	 */
	public void setPrior(AbstractStatus prior){
		m_prior = prior;
	}
	/**
	 * 更新前一个状态,默认行为为调用draw,move,而不调用update
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
	 */
	public void updatePrior(long passedTime, java.awt.Graphics g){
		if (m_prior != null) {
			m_prior.updatePrior(passedTime, g);
			m_prior.draw(passedTime, g);
			m_prior.move(passedTime);
//			m_prior.update(passedTime);
		}
	}
	public int start(){
	   return 0;
	}
	/**
	 * 覆盖这个方法, 加入更新状态代码
     * @param passedTime 从上一次调用到现在的时间
     */
	public abstract int update(long passedTime);


    /**
     * 覆盖这个方法, 加入画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public abstract int draw(long passedTime, java.awt.Graphics g);

    /**
     * 更新计时器
     * @param passedTime 从上一次调用到现在的时间
     */
    public int move(long passedTime) {
		m_counter += passedTime;
		return 0;
	}	
}
