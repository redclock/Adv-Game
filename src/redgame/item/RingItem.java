package redgame.item;
import java.awt.*;
import redgame.engine.*;
import redgame.obj.*;
import redgame.anim.*;

public class RingItem extends AbstractItem {
    Animation m_anim;
    public RingItem(GameWorld game){
        super(game, "面包圈", null );
        m_sicon = game.loadImage("image/icon1.png");
        m_anim = new Animation(m_game, m_game.loadImage("image/icon2.png"), 32 , 29);
        m_anim.setRange(0, 3, 300);  
        m_anim.start();                      
    }
    
    protected void use(Actor a){
        
    }

    public String getDescription() {
        return "面包圈！\te3";
    }
    
    public void drawStateIcon( Graphics2D g2d, int x, int y )
    {
        Composite oldac = g2d.getComposite();
        AlphaComposite ac = AlphaComposite.getInstance(
                       AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(ac);
        m_anim.paint(g2d, x, y);
        g2d.setFont(new Font("Default", Font.PLAIN, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString(Integer.toString(count), 
                             x + 15 +1, 
                             y + 28 + 1);
        g2d.setColor(Color.YELLOW);
        g2d.drawString(Integer.toString(count), 
                             x + 15, 
                             y + 28);
        g2d.setComposite(oldac);

        m_anim.update(m_game.passedTime);
    }
    
    public void setCount(int c){
        if (c <= 10)  super.setCount(c);
    }

}
