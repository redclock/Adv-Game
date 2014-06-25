package redgame.status;
/*
 * ShowStateStatus.java 作者：姚春晖
 */
import redgame.scripts.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import redgame.engine.*;
import redgame.ui.*;
import redgame.util.*;
import redgame.obj.*;
import redgame.anim.*;
import redgame.item.*;

/**
 * ShowStateStatus类是游戏中显示玩家状态
 * @see AbstractStatus
 * @author 姚春晖
 */
public class ShowStateStatus extends AbstractStatus{

    private final int FLYIN = 0;
    private final int SHOW = 1;
    private final int FLYOUT = 2;

    private int state;

    private BoxImg m_box; 
    private BoxImg m_box_top; 

    private Font font = new Font("宋体", Font.PLAIN, 16);
    private Animation m_heart;
    
    private String weaponStr;
    
    private int keyCount = 0;
    
    public ShowStateStatus(GameWorld game){
        super(game);
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
        int h = sc.height;
        
        state = FLYIN;

        int destx = x + (w - 300) / 2;
        int desty = y + (h - 200) / 2;
        m_box = new BoxImg(x-300, desty, 300, 200, destx, desty);
        m_box_top = new BoxImg(destx, -50, 300, 50, destx, desty - 50);

        m_heart = new Animation(game, game.loadImage("image/heart.png"), 20, 20);
        Player p = m_game.getPlayer();
        weaponStr = "";
        if (p.items[Player.ITEM_SWORD] != null) weaponStr += "剑  ";
        if (p.items[Player.ITEM_RING] != null) weaponStr += "面包圈  ";
        if (p.items[Player.ITEM_BOMB] != null) weaponStr += "炸弹";
        if (weaponStr.equals("")) weaponStr = "无";
        for (int i = 0; i < 256; i++) {
            if (p.hasKey[i] && !p.openedDoor[i]) keyCount ++; 
        }
        m_game.playSound("sound/interface.wav");
    }

    public int update(long passedTime){
        m_counter ++;
        switch(state){
            case FLYIN:
                if (m_box.move((float)passedTime * 0.004f))
                    state = SHOW;
                m_box_top.move((float)passedTime * 0.004f);
                 
                break;
            case SHOW:    
                if ( KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE) || 
                    KeyManager.isKeyJustDown(KeyEvent.VK_ENTER) ||
                    KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)){
                      state = FLYOUT;
                      m_game.playSound("sound/interface.wav");
                }
                break;
            case FLYOUT:    
                
                if ( m_box.move(-(float)passedTime * 0.004f) )
                    return -1;
                m_box_top.move(-(float)passedTime * 0.004f);
                break;
        }
        return 0;
    }
 
     public int draw(long passedTime, Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        m_box.paint(g, m_game.getPanel());
        m_box_top.paint(g, m_game.getPanel());
        g2d.setFont(font);
        g2d.setColor(Color.YELLOW);                 
        g2d.drawString("玩家状态", (int)m_box_top.x + 120, (int)m_box_top.y + 30);
        g2d.setColor(Color.white);
        g2d.drawString("名字:", (int)m_box.x + 50, (int)m_box.y + 50);
        g2d.drawString("生命:", (int)m_box.x + 50, (int)m_box.y + 90);
        g2d.drawString("武器:", (int)m_box.x + 50, (int)m_box.y + 130);
        g2d.drawString("钥匙:", (int)m_box.x + 50, (int)m_box.y + 170);
        
        g2d.setColor(Color.CYAN);
        g2d.drawString(m_game.getPlayer().getName(), (int)m_box.x + 150, (int)m_box.y + 50);
        int p = m_game.getPlayer().HP;
        int n = (int)(p / 4);
        if (p == n * 4){
            m_heart.setRange(0, 0, 100);
            for (int i = 0; i < n; i++){
                m_heart.paint(g, (int)m_box.x+150 + i*20, (int)m_box.y + 75);
            }
        }else{
            m_heart.setRange(0, 0, 100);
            for (int i = 0; i < n; i++){
                m_heart.paint(g, (int)m_box.x+150 + i*20, (int)m_box.y + 75);
            }
            m_heart.setRange(4-p%4, 4-p%4, 100);
            m_heart.paint(g, (int)m_box.x+150 + n*20, (int)m_box.y + 75);
        } 
        g2d.drawString(weaponStr, (int)m_box.x + 150, (int)m_box.y + 130);
        g2d.drawString(keyCount + "", (int)m_box.x + 150, (int)m_box.y + 170);
        return 0;
    }    
}