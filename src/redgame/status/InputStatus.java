package redgame.status;
/*
 * InputStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import redgame.engine.*;
import redgame.anim.*;
import redgame.util.*;
/**
 * InputStatus类
 * 在这时会显示一个对话框,要求玩家输入信息
 * @see AbstractStatus
 * @author 姚春晖
 */
public class InputStatus extends AbstractStatus {

    private int m_x, m_y;
    private int m_w;
    
    private String m_prompt;    
    private Font m_font = new Font("宋体", 0, 16);    
    private Animation m_anim;
    /**结果*/
    public static String result;
    /**
     * 创建一个AddHighScoreStatus
     * @param game 游戏引用
     * @param x 位置X坐标
     * @param y 位置Y坐标
     * @param prompt 提示
     */

    public InputStatus(GameWorld game, int x, int y, String prompt) {
        super(game);
        m_anim = new Animation(game, game.loadImage("image/pen.png"), 18, 24);
        m_anim.setRange(0, 1, 300);
        m_anim.start();
        m_x = x; m_y = y;
        m_w = 200;
        m_prompt = prompt;
        result = "";
        KeyManager.startReceive();
    }

    /**
     * 更新状态, 输入完毕则停止
     * @param passedTime 从上一次调用到现在的时间
     */

    public int update(long passedTime){
        
        String s = KeyManager.getReceived();
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if (c == '\n'){
                KeyManager.endReceive();
                KeyManager.clearKeyState();
                return 1;
            }else if ( c == 8){
                if (!result.equals("")) 
                    result = result.substring(0, result.length() - 1);
            }else if ( c > 32){
                if (result.getBytes().length < 22) result += c;
            }
        }
        return 0; 
    }
    public int move(long passedTime){
        m_anim.update(passedTime);
        return 0;
        
    }    
    /**
     * 画图代码:什么也不画
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    
    public int draw(long passedTime, Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        //draw Edit area
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(m_font);
        g2d.setColor(Color.black);
        g2d.drawRoundRect(m_x-8+1, m_y+1, m_w, 30, 5, 5);
        g2d.setPaint(new Color(100, 100, 200, 100));
        g2d.fillRoundRect(m_x-8, m_y, m_w, 30, 5, 5);
        g2d.setColor(Color.yellow);
        g2d.drawRoundRect(m_x-8, m_y, m_w, 30, 5, 5);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_OFF);


        g2d.setColor(Color.black);
        g2d.drawString(m_prompt, m_x+1, m_y-5+1);
        g2d.setColor(Color.white);
        g2d.drawString(m_prompt, m_x, m_y-5);

        FontRenderContext frc = g2d.getFontRenderContext();
        Rectangle2D rect = m_font.getStringBounds(result, frc);
        
        g2d.setColor(Color.black);
        g2d.drawString(result, m_x , m_y + 22);
        g2d.setColor(Color.white);
        g2d.drawString(result, m_x - 1 , m_y + 22 - 1);
        m_anim.paint(g, m_x + rect.getBounds().width, m_y+2);
        return 0;
    }
}

