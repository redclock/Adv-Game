package redgame.status;

import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.item.*;
import redgame.ui.AbstractMenu;
import redgame.util.*;
import redgame.obj.*;

class ItemMenu extends AbstractMenu {
	AbstractItem[] itemobjs; 
	private Font namefont = new Font("宋体", Font.BOLD, 14);
	int count;
	public ItemMenu(GameWorld game) {
		super(game);
		itemobjs = new AbstractItem [3];
		Player p = m_game.getPlayer();
		count = 0;
		for (int i = 0; i < 3; i++) {
		    if (p.items[i] != null){
		        itemobjs[count++] = p.items[i];       
		    }
		}
		if (count == 0) {
            m_items = new String[1];
		}else {
            m_items = new String[count];
		}
    }
	
	public void paint(Graphics2D g2d, int x, int y) {
        if (count == 0) {
            g2d.setFont(new Font("宋体", Font.BOLD, 16));
            g2d.setColor(Color.BLACK);                 
            String s = "你还没有任何武器";
            g2d.setColor(Color.BLACK);                 
            g2d.drawString(s, x + 10 + 1, y + 30 + 1); 
            g2d.setColor(Color.WHITE);
            g2d.drawString(s, x + 10, y + 30); 
        }
        
        for (int i = 0; i < count; i ++) {
            int xx = x + (i % 2) * 150; 
            int yy = y + (i / 2) * 40; 
        	itemobjs[i].drawSelectIcon(g2d, xx + 10, yy, m_index == i);
            g2d.setFont(namefont);
            g2d.setColor(Color.BLACK);                 
            g2d.drawString(itemobjs[i].getName(), xx + 50 + 1, yy + 11); 
            if (m_index == i)
            	g2d.setColor(Color.CYAN);
            else
            	g2d.setColor(Color.DARK_GRAY);
            g2d.drawString(itemobjs[i].getName(), xx + 50, yy + 10); 
        }
	}
	
	public void move(long passedTime) {
		
	}
	
    public void goUp(){
        if ( m_index >= 2) m_index -= 2;
    }

    public void goDown(){
        if (m_items.length -  m_index > 2) m_index += 2;
    }
    

}


public class SelectWeaponStatus extends AbstractStatus {
    private final int FLYIN = 0;
    private final int SHOW = 1;
    private final int FLYOUT = 2;
    
    private int state;
    
    private ItemMenu m_menu;
    private BoxImg m_box; 
    private BoxImg m_box_top; 
    private BoxImg m_box_bottom; 
    private Font font = new Font("宋体", Font.PLAIN, 16);
    Image emotion;
    private TextRenderer txtRender = new TextRenderer();

    public SelectWeaponStatus(GameWorld game) {
        super(game);
        m_menu = new ItemMenu(game);
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
        int h = sc.height;
        emotion = m_game.loadImage("image/emotion.png");
        state = FLYIN;

        int destx = x + (w - 300) / 2;
        int desty = y + (h - 200) / 2;
        m_box = new BoxImg(x-300, desty, 300, 100, destx, desty);
        m_box_top = new BoxImg(destx, -50, 300, 50, destx, desty - 50);
        m_box_bottom = new BoxImg(x+w, desty + 100, 300, 100, destx, desty +100);
        m_game.playSound("sound/interface.wav");
        
    }

    /**
     * 按键退出
     */
    
    public int update(long passedTime){
        switch(state){
            case FLYIN:
                if( m_box.move((float)passedTime * 0.004f) ) 
                	state = SHOW;
                m_box_top.move((float)passedTime * 0.004f);
                m_box_bottom.move((float)passedTime * 0.004f);
                 
                break;
            case SHOW:    
                if (KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)) {
                    state = FLYOUT;
                    m_game.playSound("sound/interface.wav");
                }
                if (m_menu.done()) {
                    if (m_menu.count > 0){
                        m_game.getPlayer().curItem = m_menu.itemobjs[m_menu.getIndex()];
                    }
                    state = FLYOUT;
                    m_game.playSound("sound/interface.wav");
                }
                break;
            case FLYOUT:    
                m_box_top.move(-(float)passedTime * 0.004f);
                m_box_bottom.move(-(float)passedTime * 0.004f);
                if ( m_box.move(-(float)passedTime * 0.004f) )
                	return -1;
                break;
        }
        m_menu.update(passedTime);
        return 0;
    }

    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        m_box.paint(g, m_game.getPanel());
        m_box_top.paint(g, m_game.getPanel());
        m_box_bottom.paint(g, m_game.getPanel());
        g2d.setFont(font);
        g2d.setColor(Color.YELLOW);                 
        g2d.drawString("可选的物品", (int)m_box_top.x + 100, (int)m_box_top.y + 30);
        m_menu.paint(g2d, (int)m_box.x + 20, (int)m_box.y + 30);
        g2d.setFont(font);
        g2d.setColor(Color.YELLOW); 
        if (m_menu.count > 0){
            txtRender.drawText((int)m_box_bottom.x + 20, 
            		(int)m_box_bottom.y + 30, 
            		g2d,
            		m_menu.itemobjs[m_menu.getIndex()].getDescription(),
            		emotion);
        }    		
        return 0;
    }
    
}
