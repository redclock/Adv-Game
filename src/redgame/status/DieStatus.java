package redgame.status;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import redgame.engine.*;
import redgame.anim.*;
import redgame.util.*;
/*
 * DieStatus.java 作者：姚春晖
 */
//下落的星星粒子系统
class MoveParticleSystem extends AbstractParticleSystem{
	private int line;
	private boolean isfirst;
	private int maph;
	private int x, y;
	MoveParticleSystem(GameWorld game, Image image, 
					int count, int pw, int ph,
					int begin, int end, int delay){
		super(game, image, count, pw, ph, begin, end, delay);
		
        Rectangle sc = game.getScreenArea();
        x = sc.x;
        y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        line = sc.width / pw + 1; 
        maph = ((sc.height - 1) / 64 + 1)* 64;
        isfirst = true;
		resetall();
		isfirst = false;
	}
	protected void reset(int i){
		Particle p = m_p.get(i);
		if (isfirst){
			p.x = (i % line)*64+x;
			p.y = (i / line)*64+y;
		}else{
			p.x = (i % line)*64+x;
			p.y = - 64 - p.life * 0.02f *p.vy+y;
		}
		p.vx = -0.0f;
		p.vy = 5.0f;
		p.alpha = 1.0f;
		p.life = p.life + (int)((maph - p.y + y)/p.vy*50);
		p.accx = 0;
		p.accy = 0;
		p.alphasp = 0.01f;
		p.start();
	}

}
/**
 * DieStatus类是游戏中GAME OVER的游戏状态.
 * 
 * @see AbstractStatus
 * @author 姚春晖
 */ 
public class DieStatus extends AbstractStatus {
	//圆锥裁减区
	private Area m_clip;
    //去除圆锥的裁减区
    private Area m_nclip;
	private Image m_img;
	private MoveParticleSystem m_mps;
    /**
     * 创建一个DieStatus
     * @param game 游戏引用
     */
    public DieStatus(GameWorld game) {
		super(game);
		Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
		m_clip = new Area();
        int[] px = {x+sc.width/2, x+sc.width/2 - 100, x+sc.width/2 + 100};
        int[] py = {y, y+sc.height-40, y+sc.height-40};
		m_clip.add(new Area(new Polygon(px, py, 3)));
        m_clip.add(new Area(new Ellipse2D.Float(x+sc.width/2 - 100, 
                                                y+sc.height-80,
                                                200, 80)));
		m_nclip = new Area(new Rectangle(x,y,sc.width,sc.height));
		m_nclip.subtract(m_clip);
		m_img = m_game.loadImage("image/gameover.png");
		
		m_mps = new MoveParticleSystem(m_game, 
				m_game.loadImage("image/stars.png"),
				((sc.height - 1) / 64 + 2) * (sc.width / 64 + 1),
				64, 64, 0, 29, 5);
		m_game.playMusic("sound/die.wav", false);
	}
	
    /**
     * 对于前个状态:只允许在圆锥裁减区显示
     */
    public void updatePrior(long passedTime, Graphics g){
		
		if (m_prior != null) {
	//		g.setClip(m_clip);
			m_prior.updatePrior(passedTime, g);
			m_prior.draw(passedTime, g);
			m_prior.move(passedTime);
			g.setClip(0, 0, m_game.getSize().width, m_game.getSize().height);
//			m_prior.update(passedTime);
		}
		
	}
    /**
     * 按键退出
     */
    
	public int update(long passedTime){
        m_counter += passedTime;
        
        if (m_counter > 3000 &&( KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)
			||KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE))){
			m_game.backToTitle();
		}
		return 0;
	}

    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public int draw(long passedTime, Graphics g){
        g.setClip(m_nclip);
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
		int h = sc.height;
        
        if (m_counter > 3000) 
            g.setColor(new Color(0, 0, 0));
        else    
            g.setColor(new Color(0, 0, 0, (float)m_counter / 3000));
		g.fillRect(x, y, w, h);
		m_mps.paint(g);

        g.setClip(0, 0, m_game.getSize().width,m_game.getSize().height);
		Font f = g.getFont();
		Font bigf = new Font("Arial Black", Font.BOLD, 60);
		Rectangle2D rt = bigf.getStringBounds("Game   Over", ((Graphics2D)g).getFontRenderContext());
		g.setColor(Color.GREEN);
		g.setFont(bigf);
		g.drawString("Game   Over", x+(int)( w - rt.getWidth())/2, y+100);
		g.setFont(f);
		g.drawImage(m_img, x+w/2 - 70, y+h-170, m_game.getPanel());
		return 0;
	}
	public int start(){
	    m_counter = 0;
	    return 0;
	}
    /**
     * 更新计时器
     * @param passedTime 从上一次调用到现在的时间
     */
	public int move(long passedTime) {
		m_mps.move(passedTime);
		return 0;
	}	
}