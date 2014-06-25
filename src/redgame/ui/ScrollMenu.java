package redgame.ui;

import java.awt.*;
import redgame.engine.*;

public class ScrollMenu extends AbstractMenu {
    private int top = 0;
    private int count;
    private int w, h;
    private int lines;

    public ScrollMenu(GameWorld game, String [] items, int showlines, int linewidth, int lineheight){
        super(game, items);
        w = linewidth;
        h = lineheight;
        lines = showlines;
        count = items.length;
    }
    
    public boolean canScrollUp(){
        return top > 0;
    }

    public boolean canScrollDown(){
        return count - top > lines;
    }
    
    public void goLeft(){
        goUp();
    }
    
    public void goRight(){
        goDown();
    }

    public void goUp(){
        if (m_index <= 0) return;
        if (m_index == top) top --;
        m_index --;
    }

    public void goDown(){
        if (m_index >= count - 1) return;
        if (m_index == top + lines - 1) top ++;
        m_index ++;
    }

    public void move(long passedTime){
               
    }
    
    public void paint(Graphics2D g2d, int x, int y){
        Font f = g2d.getFont();
        if (item_bounds == null){
            item_bounds = new Rectangle[m_items.length];
            for (int i = 0; i < m_items.length; i++){
                item_bounds[i] = f.getStringBounds(m_items[i], 
                                        g2d.getFontRenderContext()).getBounds();
                item_bounds[i].move( -item_bounds[i].x, 
                                     - item_bounds[i].y 
                                     + (h - item_bounds[i].height) / 2
                                   );
            }
        }
        
        int i = top;
        while (i < count && i < top + lines){
            if (i == m_index){
                g2d.setColor(new Color(100, 200, 100, 100));
                g2d.fill3DRect(x, y + h * (i - top), w, h, true);
                g2d.setColor(Color.WHITE);
                g2d.drawString(m_items[i], x + item_bounds[i].x + 10, y 
                               + h * (i - top) + item_bounds[i].y);
            }else{
                g2d.setColor(Color.WHITE);
                g2d.drawString(m_items[i], x + item_bounds[i].x + 10, y 
                               + h * (i - top) + item_bounds[i].y);
            }
            i++;
        }
    }

}
