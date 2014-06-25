package redgame.engine;
/*
 * StatusStack.java 作者：姚春晖
 */

import java.awt.*;
import java.util.*;
import redgame.status.*;
/**
 * AbstractStatus类是盛放游戏状态的栈.
 * 所有状态都在游戏状态栈中,栈顶状态为当前活动状态.
 * @see AbstractStatus
 * @author 姚春晖
 */

public class StatusStack{
	//游戏引用
	private GameWorld m_game = null;
	//系统栈
    private Stack<AbstractStatus> m_stack = new Stack<AbstractStatus>();
	
    /**
     * 创建一个状态栈
     * @param game 游戏引用
     */
    public StatusStack(GameWorld game){
		m_game = game;
	}
	/**
	 * 得到当前状态
	 */
    public AbstractStatus getCurrStatus(){
		if (m_stack.isEmpty())
			return null;
		else
			return m_stack.peek();
	}
	/**
	 * 清空栈
	 */
	public void clear(){
		m_stack.clear();
	}
    /**
     * 压入一个状态
     */
	public int push(AbstractStatus s){
		s.setPrior(getCurrStatus());
		m_stack.push(s);
        System.out.println("push "+s.getClass()+" "+m_stack.size());
		return m_stack.size() - 1;
	} 
	/**
	 * 弹出状态
	 */
	public AbstractStatus pop(){
        System.out.println("pop "+m_stack.size());
        if (m_stack.isEmpty())
			return null;
		else
			return m_stack.pop();
	}
	
	/**
	 * 更新状态, 如果栈顶的draw, move, update返回值的和不为0,则弹出该状态
	 */
	public void updateStatus(long passedTime, Graphics g){
		AbstractStatus s = getCurrStatus();
		if (s == null) return;
		s.updatePrior(passedTime, g);
		int r = s.draw(passedTime, g);
        r += s.move(passedTime);
        r += s.update(passedTime);
		if (r != 0){
            System.out.println("pop "+m_stack.size());
            m_stack.pop();
		}
	}
	/**
	 * 只保留最顶端n个,其余删除
	 */
	public void deleteUntil(int n){
	   while ( m_stack.size() > n ){
	       m_stack.remove(0);
	   }
	}
}