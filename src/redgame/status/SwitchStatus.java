/////////////////////////////////////////
//SwitchStatus extends AbstractStatus
//从"当前状态"切换到"其他状态"的状态
////////////////////////////////////////
package redgame.status;
/*
 * SwitchStatus.java 作者：姚春晖
 */

import java.awt.*;
import redgame.engine.*;
/**
 * SwitchStatus类是从"当前状态"切换到"其他状态"的状态.
 * 它可以使两个状态的过渡更平滑.它是这样工作的:
 * 
 * 要从状态A过渡到B,此时状态栈为:<br>
 * ..., A <br>
 * 设S为SwitchStatus,开始过渡时,进入消隐阶段: <br>
 * ..., A, S <br>
 * 消隐结束后, 将S和A弹出, 在压入B和S, 进入渐入阶段: <br>
 * ..., B, S <br>
 * 最后: <br>
 * ..., B <br>
 * 
 * @see AbstractStatus
 * @author 姚春晖
 */
public class SwitchStatus extends AbstractStatus {
	private AbstractStatus m_next;   //切换之后的状态
	private boolean isBack = false;	 //是否已经完成一半，正在退回到下一状态
	public long MaxCounter;        //半个过程进行时间的毫秒
	private int m_index;			//切换形式 0 - 直接
									//1-淡入淡出 2-方框
    public boolean needRepaint = false;
    /**
     * 创建一个SwitchStatus
     * @param game 游戏引用
     * @param nextStatus 下一个状态
     * @param index 切换方式: 0 - 直接 1-淡入淡出 2-方框
     */
	public SwitchStatus(GameWorld game, AbstractStatus nextStatus, int index) {
		super(game);
		m_next = nextStatus;
		m_index = index;
		MaxCounter = 0;
                                
        switch(m_index){
			case 1: 
			    MaxCounter = 1000; 
			    break;
			case 2: MaxCounter = 2000; break;
		}
	}
    /**
     * 更新状态, 什么也不作
     * @param passedTime 从上一次调用到现在的时间
     */
    
	public int update(long passedTime){
	
		return 0;
	}

	//画切换形式:淡入淡出
	private void draw_fade(long passedTime, Graphics g){
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
		int h = sc.height;
		float t = (float) m_counter / (float) MaxCounter;
		int alpha = (int)(255 * t);
		if (alpha < 0 ) alpha = 0;
		else if(alpha > 255) alpha = 255; 
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(x, y, w, h);
	}
	
    //画切换形式:方框
	private void draw_rect(long passedTime, Graphics g){
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
        int h = sc.height;
		float t = (float) m_counter / (float) MaxCounter;
		g.setColor(Color.BLACK);
		g.fillRect(x, y, (int)(t*w/2), h);
		g.fillRect(x, y, w, (int)(t*h/2));
		g.fillRect(x+w - (int)(t*w/2), y, (int)(t*w/2), h);
		g.fillRect(x, y+h - (int)(t*h/2), w, (int)(t*h/2));
	}

    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
		switch(m_index){
			case 1: draw_fade(passedTime, g); break;
			case 2: draw_rect(passedTime, g); break;
		}
		return 0;
	}

    /**
     * 更新计时器
     * @param passedTime 从上一次调用到现在的时间
     */

    public int move(long passedTime) {
        if (isBack){
			m_counter -= passedTime;
			if (m_counter <= 0) return -1;
		}else{
			m_counter += passedTime;
			if (m_counter >= MaxCounter){
                try{
                    m_game.getPanel().repaint();
                }catch(Exception e){
                    e.printStackTrace();
                }
                isBack = true;
				m_game.popStatus();
				m_game.pushStatus(m_next);
				m_game.pushStatus(this);
				m_next.start();
			}
		}
		return 0;
	}	
}