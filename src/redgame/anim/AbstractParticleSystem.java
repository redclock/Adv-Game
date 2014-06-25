package redgame.anim;
/*
 * AbstractParticleSystem.java 作者：姚春晖
 */
import java.util.Vector;
import java.awt.*;


import redgame.engine.*;

/**
 * AbstractParticleSystem类是游戏中粒子系统的父类，定义基本功能。
 * 这是一个抽象类，要使用它的派生类。
 * @author 姚春晖
 */
abstract public class AbstractParticleSystem{
	//粒子列表
	protected Vector<Particle> m_p;
	//游戏引用
	protected GameWorld m_game;
    /**
     * 构造粒子系统
     * @param game 游戏类的引用
     * @param image 物体图像
     * @param count 粒子个数
     * @param pw 图像一格的宽度
     * @param ph 图像一格的高度
     * @param begin 图片分隔开始的索引
     * @param end   图片分隔结束的索引
     * @param delay 每一帧的延迟
     */

	public AbstractParticleSystem(GameWorld game, Image image, 
					int count, int pw, int ph, int begin, 
					int end, int delay){
		m_p = new Vector<Particle>(count);
		for (int i = 0; i < count; i++){
			Particle p = new Particle(game, image, pw, ph);
			m_p.add(p);
			p.setRange(begin, end, delay);
		}
		m_game = game;
	}
	/**
	 * 重置所有粒子
	 */
	public void resetall(){
		for (int i = 0; i < m_p.size(); i++){
			reset(i);
		}
	}
	/**
	 * 重置第i号粒子
	 */
	abstract protected void reset(int i);
	/**
	 * 画粒子
	 */
	public void paint(Graphics g){
		
		for (int i = 0; i < m_p.size(); i++){
			m_p.get(i).paint(g);
		}
	}
	/**
	 * 更新粒子
	 */
	public void move(long passedTime){
		for (int i = 0; i < m_p.size(); i++){
			Particle p = m_p.get(i);
			p.move(passedTime);
			if (p.life <= 0) reset(i);
		}
	}
	
}

