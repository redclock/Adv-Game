package redgame.ui;

import java.awt.*;
import java.awt.image.*;

import redgame.engine.GameWorld;

public class TextMenuEx extends AbstractMenu {
    private BufferedImage bi;
    private int mw, mh;
    private long m_counter = 0;
    private Font m_font = new Font("ËÎÌå", Font.PLAIN, 18);
    private String m_title;
    
    private void createBack() {
        bi = new BufferedImage(mw+5, mh+5, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(new Color(0,0,0,150));
        g.fillRect(5,5,mw+5,mh+5);                
        g.setPaint(new GradientPaint(0, 0, new Color(0,0,0,255), 0, mh, 
                        new Color(255,255,255,100)));
        g.fillRect(0,0,mw,mh);    
        g.setFont(new Font("ºÚÌå", 0, 18));
        g.setColor(Color.GREEN);
        g.drawString(m_title, 80, 30);
        g.setColor(Color.GREEN);
        g.drawLine(10, 35, mw - 20, 35);
        g.dispose();            
    }
    
    public TextMenuEx(GameWorld game, String title, String[] items, int w, int h) {
    	super(game, items);
    	mw = w;
    	mh = h;
    	m_title = title;
    	createBack();
    }
    
	public void paint(Graphics2D g2d, int x, int y) {
        g2d.drawImage(bi, x, y, m_game.getPanel());
        
        g2d.setFont(m_font);
        for (int i = 0; i < m_items.length; i++){
            int w = m_font.getStringBounds(
            		m_items[i], g2d.getFontRenderContext()
            		).getBounds().width;
            g2d.setColor(Color.BLACK);

            g2d.drawString(m_items[i], x + (mw - w) / 2 +1, y + i*30 + 60 +1);
            
            if ( i == m_index ){
                g2d.setColor(new Color(
                        (int)(127*(1.5+ 0.5 * Math.sin((double)m_counter/200))),
                        (int)(127*(1.5+ 0.5 * Math.sin((double)m_counter/200))),
                        (int)(127*(1.5+ 0.5* Math.sin((double)m_counter/200)))
                    )
                );
            }else{
                g2d.setColor(Color.GRAY);
            }
            g2d.drawString(m_items[i], x + (mw - w) / 2 , y + i*30 + 60 );
        }                
	}
	
	public void move(long passedTime) {
		m_counter += passedTime;
	}
}

