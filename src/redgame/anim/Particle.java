package redgame.anim;
/*
 * Particle.java 作者：姚春晖
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import redgame.engine.GameWorld;
/**
 * Particle类是游戏中粒子系统的粒子类
 * @author 姚春晖
 */

public class Particle {
	private Animation m_anim;
	//位置
	public float x, y;
	//速度
	public float vx, vy;
	//加速度
	public float accx, accy;
	//透明
	public float alpha;
    //透明消减速度
	public float alphasp; 
	//生命力	
    public float life;
	//旋转角
	public float rotate_angle;
    //旋转角速度
    public float rotate_angle_sp;
    
    public boolean active = true;
    public AffineTransform at = new AffineTransform();
    public Particle(GameWorld game, Image img, int w, int h){
		m_anim = new Animation(game, img, w, h);
		
	}
	
	public void paint(Graphics g){
	    if (!active) return;
	    Graphics2D g2 = (Graphics2D) g;
	    AffineTransform t = new AffineTransform();
	    t.translate(x, y);
        t.rotate(rotate_angle);
	    t.concatenate(at);
	    g2.setTransform(t);
		if (alpha >= 1.0f)
           m_anim.paint(g, 0, 0);
		else
           m_anim.paint_alpha(g, 0, 0, alpha);
        at.setToIdentity();
        g2.setTransform(at);
	}
	
	public void move(long passedTime){
	    if (!active) return;
		float t = 0.02f * passedTime;
		
		x += vx * t;
		y += vy * t;
		
		vx += accx * t;
		vy += accy * t;
		rotate_angle += rotate_angle_sp * t; 
		alpha -=  Math.sqrt(alpha) * alphasp * t;
		if (alpha <= 0) alpha = 0;
		life -= passedTime;
		m_anim.update(passedTime);

	}
	
	public void setRange(int begin, int end, int delay){
		m_anim.setRange(begin, end, delay);
	}

	public void start(){
		m_anim.start();
		active = true;
	}
	
}
