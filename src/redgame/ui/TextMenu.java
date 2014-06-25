package redgame.ui;

import java.awt.*;
import redgame.engine.*;

public class TextMenu extends AbstractMenu {
    protected int stop = 10, sbottom = 10, sleft = 10, sright = 20;
    protected int linesp = 2;

    protected int w = 0, h = 0;
    public TextMenu(GameWorld game, String [] items){
        super(game, items);
    }
    
    public void paint(Graphics2D g2d, int x, int y){
            Font f = g2d.getFont();
            
            if (item_bounds == null){
                item_bounds = new Rectangle[m_items.length];
                for (int i = 0; i < m_items.length; i++){
                    item_bounds[i] = f.getStringBounds(m_items[i], 
                                            g2d.getFontRenderContext()).getBounds();
                    item_bounds[i].move(-item_bounds[i].x + x, h-item_bounds[i].y + y);
                    h += item_bounds[i].height + linesp;
                    if (w < item_bounds[i].width) w = item_bounds[i].width;
                }
                for (int i = 0; i < m_items.length; i++){
                	item_bounds[i].width = w;
                }
            }
            
            g2d.setPaint(new Color(100, 100, 100, 200));  
            g2d.fillRect(x - sleft, y - stop, w + sleft + sright, h + stop + sbottom) ;
            g2d.setPaint(Color.black);  
            g2d.drawRect(x - sleft + 1, y - stop + 1, w + sleft + sright, h + stop + sbottom) ;
            g2d.setPaint(Color.white);  
            g2d.drawRect(x - sleft, y - stop, w + sleft + sright, h + stop + sbottom) ;
            for (int i = 0; i < m_items.length; i++){
                g2d.setColor(Color.black);
                g2d.drawString(m_items[i], item_bounds[i].x + 10 + 1, item_bounds[i].y  + 1);
                if (i != m_index){
                    g2d.setColor(Color.white);
                }else{
                    g2d.setColor(Color.yellow);
                    g2d.fillRect( item_bounds[i].x , 
                                  item_bounds[i].y - 6,
                                  5, 5);
                }
                g2d.drawString(m_items[i], item_bounds[i].x + 10, item_bounds[i].y);
            }
    }
    
    public void move(long passedTime){
               
    }

}
