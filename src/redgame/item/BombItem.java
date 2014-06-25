package redgame.item;
import java.awt.*;
import redgame.item.*;
import redgame.obj.*;
import redgame.engine.*;
import redgame.anim.*;

public class BombItem extends AbstractItem {
    private Animation m_anim;
    public BombItem(GameWorld game){
        super(game, "炸弹", null );
        m_sicon = game.loadImage("image/icon5.png");
        m_anim = new Animation(game, game.loadImage("image/bomb.png"), 32, 48);
        m_anim.setRange(0, 8, 300);
        m_anim.start();
    }

    protected void use(Actor a) {
    }

    public String getDescription() {
        return "\ts30\tb嘭!";
    }
    
    public void drawStateIcon( Graphics2D g2d, int x, int y ){
        Composite oldac = g2d.getComposite();
        AlphaComposite ac = AlphaComposite.getInstance(
                       AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(ac);
        m_anim.paint(g2d, x, y - 5);
        g2d.setFont(new Font("Default", Font.PLAIN, 20));
        g2d.setColor(Color.BLACK);
        g2d.drawString(Integer.toString(count), 
                             x + 15 + 1, 
                             y + 28 + 1);
        g2d.setColor(Color.YELLOW);
        g2d.drawString(Integer.toString(count), 
                             x+ 15, 
                             y+ 28);
        g2d.setComposite(oldac);
        m_anim.update(m_game.passedTime);
    }


}
