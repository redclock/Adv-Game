package redgame.status;
/*
 * AddHighScoreStatus.java 作者：姚春晖
 */
import redgame.engine.*;

import java.awt.*;
import javax.swing.*;

//显示对话框放在另一个线程里.(但实际上好像并不起作用?)
class DialogThread extends Thread{
    private boolean m_done = false;
    public String result;
    private String m_text;
    DialogThread(String text){
        m_text = text;
    }
    public synchronized void run(){
        result = JOptionPane.showInputDialog(null, m_text);
        m_done = true;
    }
    public boolean isDone(){
        return m_done;
    }
}

/**
 * AddHighScoreStatus类是添加高分榜的游戏状态.
 * 在这时会显示一个对话框,要求玩家输入姓名
 * @see AbstractStatus
 * @author 姚春晖
 */

public class AddHighScoreStatus extends AbstractStatus {
    //对话框线程
    private DialogThread m_dlg;
    //要添加的高分
    private int m_score;

    /**
     * 创建一个AddHighScoreStatus
     * @param game 游戏引用
     * @param score 得分
     */

    public AddHighScoreStatus(GameWorld game, int score) {
        super(game);
        m_score = score;
        m_dlg = new DialogThread("你得了"+score+"分,可以进入高分榜.\n请留下大名:");
        m_dlg.start();
    }
    /**
     * 更新状态, 输入完毕则停止
     * @param passedTime 从上一次调用到现在的时间
     */

    public int update(long passedTime){
        if (m_dlg.isDone()){
            if (m_dlg.result != null && !m_dlg.result.trim().equals("")){
            //    m_game.getHighScore().addHighScore(m_score, m_dlg.result);
            }
            return -1;
        }
        return 0;
    }
    /**
     * 画图代码:什么也不画
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    
    public  int draw(long passedTime, Graphics g){
        return 0;
    }
}