package redgame.item;

import java.awt.*;

import redgame.obj.*;
import redgame.engine.*;

public abstract class AbstractItem {
    protected int count = 0;
    private boolean m_active = false;
    protected Image m_sicon;
    protected Image m_uicon;
    protected String m_name;
    protected GameWorld m_game;
    
    public AbstractItem(GameWorld game, String name, Image selectIcon){
        m_game = game;
        m_name = name;
        m_sicon = selectIcon;
    } 
    
    public String getName(){
        return m_name;
    }
    
    public void setName(String name){
        m_name = name;
    }
    
    public boolean getActive(){
        return m_active;
    }
    
    public void setActive(boolean active){
        m_active = active;
    }
    
    public void drawSelectIcon( Graphics2D g2d, int x, int y, boolean selected ){
        if (m_sicon == null){
            g2d.setColor(Color.blue);
            g2d.drawRect(x, y, 32, 32);
            g2d.setColor(Color.yellow);
            if (selected) {
            	g2d.fillRect(x + 1, y + 1, 30, 30);	
            } else {
            	g2d.fillRect(x, y, 32, 32);	
            }
        }else{
            if (! selected) {
                Composite oldac = g2d.getComposite();
                AlphaComposite ac = AlphaComposite.getInstance(
                               AlphaComposite.SRC_OVER, 0.5f);
                g2d.setComposite(ac);
                g2d.drawImage(m_sicon, x, y, m_game.getPanel());
                g2d.setComposite(oldac);
            } else {
                g2d.drawImage(m_sicon, x, y, m_game.getPanel());
            }
        }
    }
        
    public void drawStateIcon( Graphics2D g2d, int x, int y )
    {
    	drawSelectIcon(g2d, x, y, true);
    }

    protected abstract void use(Actor a);
    
    public boolean available(){
        return count > 0;
    }
    
    public boolean availableSpecial(){
        return false;
    }

    public int getCount(){
        return count;
    }
    
    public void setCount(int c){
        if (c >= 0)  count = c;
    }
    public abstract String getDescription();
    
}
