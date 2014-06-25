package redgame.status;
/*
 * MsgStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.util.*;

/**
 * MsgStatus类是显示消息的游戏状态.
 * @see AbstractStatus
 * @author 姚春晖
 */

public class MsgStatus extends AbstractStatus{
	private String m_text;
	private int m_x, m_y;
    /**
     * 创建一个MsgStatus
     * @param game 游戏引用
     * @param x
     * @param y 显示的位置
     * @param text 消息文字
     */
    public MsgStatus(GameWorld game, int x, int y, String text) {
		super(game);
		m_text = text;
		m_x = x;
		m_y = y;
	}	
	/**
     * 按键退出
	 */
	public int update(long passedTime){
		if ( KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)
			||KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)){
			return -1;
		}
		return 0;
	}
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */

	public int draw(long passedTime, Graphics g){
		Font f = g.getFont();
		Font msgf = new Font("黑体", 0, 18);
		g.setFont(msgf);
		g.setColor(Color.BLACK);
		g.drawString(m_text, m_x + 1, m_y + 1);
		g.setColor(Color.WHITE);
		g.drawString(m_text, m_x, m_y);
		g.setFont(f);
		return 0;
	}
}
