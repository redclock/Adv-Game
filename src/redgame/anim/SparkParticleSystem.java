package redgame.anim;
import redgame.engine.*;
import java.awt.*;

public class SparkParticleSystem extends AbstractParticleSystem {
    private int m_w;
    private int m_h; 
    private int m_x;
    private int m_y; 
    private int left;
    private float m_vx_range;
    private float m_vy_range;
    private float m_accy;
    public SparkParticleSystem(GameWorld game, Image image, 
                    int x, int y,
                    int count, int pw, int ph,
                    int begin, int end, int delay,
                    float vx_range,
                    float vy_range,
                    float accy
                    ){
        super(game, image, count, pw, ph, begin, end, delay);
        m_w = pw;
        m_h = ph;
        m_x = x;
        m_y = y;
        m_vx_range = vx_range;
        m_vy_range = vy_range;
        m_accy = accy;
        resetall();
        left = count;
    }
	/**
	 * 重置第i号粒子
	 */
	protected void reset(int i)
	{
		Particle p = m_p.get(i);
		if (p.active)
		{
			p.x = m_x + m_game.getRandom(20) - 10;
			p.y = m_y + m_game.getRandom(20) - 10;
			p.vx = m_game.getRandom(100) / 100.0f * m_vx_range + 1.0f;
			if (m_game.getRandom(2) == 1) p.vx = -p.vx;
            p.vy = - m_game.getRandom(100) / 100.0f * m_vy_range + 1.0f;
			p.accx = 0;
			p.accy = m_accy;
			p.alpha = 1.0f;
			p.alphasp = 0.1f;
			p.life = m_game.getRandom(1500) + 1500;
			p.at.setToIdentity();
			p.at.rotate(-p.life*p.vx / 1000.0, m_w / 2, m_h / 2);
			p.at.scale(p.life/3000.0, p.life/3000.0);
		}
	}
	/**
	 * 更新粒子
	 */
	public void move(long passedTime){
		for (int i = 0; i < m_p.size(); i++){
			Particle p = m_p.get(i);
			p.move(passedTime);
			p.at.setToIdentity();
			p.at.rotate(-p.life*p.vx / 1000.0, m_w / 2, m_h / 2);
			p.at.scale(p.life /3000.0, p.life/3000.0);
			if (p.life <= 0)
			{
				p.active = false;
				left--;
			}
		}
	}
	
	public boolean done()
	{
		return left <= 0;
	}

}
