package redgame.item;
import java.awt.*;
import redgame.item.*;
import redgame.obj.*;
import redgame.engine.*;

public class SwordItem extends AbstractItem {
    private Image m_state;
    public float power;
    public SwordItem(GameWorld game){
        super(game, "铁剑", null );
        m_sicon = game.loadImage("image/icon3.png");
        m_state = game.loadImage("image/icon4.png");
        count = 1;
    }

    /**
     * Method use
     *
     *
     * @param a
     *
     */
    protected void use(Actor a) {
    }

    /**
     * Method getDescription
     *
     *
     * @return
     *
     */
    public String getDescription() {
        return "一剑在手\n\ts26走遍天下!";
    }
    
    public void drawStateIcon( Graphics2D g2d, int x, int y ){
        Composite oldac = g2d.getComposite();
        AlphaComposite ac = AlphaComposite.getInstance(
                       AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(ac);
        g2d.setColor(Color.green);
        g2d.fillRect(x - 30, y + 15, (int)(power * 0.6f), 10);
        g2d.setColor(Color.ORANGE);
        g2d.drawRect(x - 30, y + 15, 60, 10);
        g2d.drawImage(m_state, x - 5 , y - 5, m_game.getPanel());
        g2d.setComposite(oldac);
    }

}
