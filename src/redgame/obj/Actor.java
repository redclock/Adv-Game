package redgame.obj;
/*
 * Actor.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
import redgame.anim.*;

/**
 * Actor类是游戏中的人物,包括玩家和敌人
 * 
 * @author 姚春晖
 */

public class Actor extends MovableObject{
    /**
     * 是否麻痹
     */
    protected  boolean m_lull;    
    /*
     * 是否已死
     */
    protected boolean   m_dead;
    /*
     * 是否可被伤害
     */

    protected boolean   m_harmable;
    /*
     * 是否有兴奋剂
     */
    protected boolean   m_cordial;
    /*
     * 死时动画的计时器
     */
    protected long   m_deadCounter;
    /*
     * 死时动画的粒子系统
     */
    protected SparkParticleSystem m_spark1;
    protected SparkParticleSystem m_spark2;
    /*
     * 无敌动画的计时器
     */
    protected long   m_noHarmCounter;
    /*
     * 兴奋剂的计时器
     */
    protected long   m_cordialCounter;
    /**
     * 朝向 0~3
     * @see AbstractObject#G_UP
     * @see AbstractObject#G_DOWN
     * @see AbstractObject#G_LEFT
     * @see AbstractObject#G_RIGHT
     */
    protected int m_face;        
    private  long m_lullCounter;  //麻痹计时器
    private  long m_lullTime;     //麻痹时间
    /**
     * 麻痹时动画
     */
    protected  Animation m_lullAnim = null; 

    /**
     * Hit Point
     */
    public int HP;

    /**
     * 由属性更新动画
     */
    protected void updateAnimation(){
        m_anim.setRange(m_face * 4, m_face * 4 + 3, m_delay);
    }
    /**
     * 动作,更新动画
     */
    public void move(long passedTime){
        m_anim.update(passedTime);
        if (m_spark1 != null)
        {
        	m_spark1.move(passedTime);
        	if (m_spark1.done()) m_spark1 = null;
        }
        if (m_spark2 != null)
        {
            m_spark2.move(passedTime);
            if (m_spark2.done()) m_spark2 = null;
        }
    }
    /**
     * 更新状态
     */
    public void update(long passedTime){
    }
    /**
     * 取得朝向
     * @see #m_face
     */
    public int getFace(){
        return m_face;
    }
    /**
     * 设置朝向
     * @see #m_face
     */
    public void setFace(int face){
        if (face >= 0 && face <= 3 && m_face != face){
            m_face = face;
            updateAnimation();
        } 
    }
    /**
     * 构造物体
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @see Animation
     * @see GameWorld
     * @see MovableObject
     */

    public Actor(GameWorld game, Image img, int x, int y, int w, int h){
        super(game, img, x, y, w, h);
        m_block = new Rectangle(5, 2, w-10, h-4);
        m_face = G_LEFT;
        m_dead = false;
        m_harmable = true;
        m_blocked = true;
        updateAnimation();
        m_lullAnim = new Animation(game, game.loadImage("image/light1.png"), 64, 64);
        m_lullAnim.setRange(0, 3, m_delay);
    }

    public void paint(Graphics g){
        if (!m_visible) return;
        super.paint(g);
        if (! inScreen()) return;
        if (!m_harmable){
            m_anim.paint_alpha_mask(g, 
                (int)x - m_block.x, 
                (int)y - m_block.y, 
                0xff0000 | (m_game.getRandom(100))<<8 | (m_game.getRandom(100)), 
                0.5f);
            m_noHarmCounter -= m_game.passedTime;
            if (m_noHarmCounter <= 0) m_harmable = true;        
                
        }else if (m_cordial){
            m_anim.paint_alpha_mask(g, 
                (int)x - m_block.x, 
                (int)y - m_block.y, 
                0x009900 | (m_game.getRandom(100))<<16 | (m_game.getRandom(100)), 
                0.4f);
            m_noHarmCounter -= m_game.passedTime;
            if (m_noHarmCounter <= 0) m_harmable = true;        
                
        }
        if (m_lull){
            m_lullAnim.update(m_game.passedTime);  
            m_lullAnim.paint(g, (int)(getX() + getW()/2 - 32), 
                                (int)(getY() + getH() - 64)
                            );
        }
        //死亡的动画
        if (m_dead){
            int yy = (int)( m_deadCounter * 50 / 1000 );
            m_anim.paint_alpha(g, (int)x - m_block.x, (int)y - m_block.y - yy, 
                    1.0f - (float)m_deadCounter / 1000.0f);
            m_deadCounter += m_game.passedTime;
            if (m_spark1 != null) m_spark1.paint(g); 
            if (m_spark2 != null) m_spark2.paint(g); 
            if (m_deadCounter >= 900) m_visible = false;        
        }
    }
    /**
     * 返回true: 可以碰撞
     */
    public boolean isBlocked(){
        return m_blocked && (!m_dead);
    }
    /**
     * 开始被麻痹
     * @param lullTime 昏迷时间
     */
    public void startLull(long lullTime){
        m_game.playSound("sound/hit.wav");
        m_lullTime = lullTime;
        m_lullCounter = 0;
        m_lull = true;
        m_lullAnim.start();
    }
    /**
     * 开始服用兴奋剂
     * @param lullTime 作用时间
     */
    public void startCordial(long time){
        m_cordialCounter = time;
        m_cordial = true;
        hurt(1, 100);
    }
    /**
     * 停止麻痹
     */
    protected void stopLull(){
        m_lull = false;
    }
    /**
     * 等待并更新麻痹计时器
     */
    protected void waitLull(long passedTime){
        m_lullCounter += passedTime;
        if (m_lullCounter > m_lullTime){
            m_lull = false;
        }
    }
    /**
     * 等待并更新兴奋剂计时器
     */
    protected void waitCordial(long passedTime){
        m_cordialCounter -= passedTime;
        if (m_cordialCounter <= 0){
            m_cordial = false;
            m_delay = 100;
        }
    }
    /**
     * 令其死亡
     */    
    public void die(){
        m_game.playSound("sound/si.wav");
        m_dead = true;
        m_deadCounter = 0;
        m_lull = false;
        m_anim.resetToStart();
        m_spark1 = new SparkParticleSystem(m_game, m_game.loadImage("image/flare3.png"), 
                  (int)(getX() + getW() / 2) - 16, (int)getY() - 10, 10, 32, 32, 0, 0, 100,
                  5, 5, 0.1f);
        m_spark2 = new SparkParticleSystem(m_game, m_game.loadImage("image/flare3_y.png"), 
                  (int)(getX() + getW() / 2) - 16, (int)getY() - 10, 10, 32, 32, 0, 0, 100,
                  5, 5, 0.1f);
    }
    /**
     * 是否已死
     */
    public boolean isdead(){
        return m_dead;
    }
    
    public void hurt(int p, long noHarmTime){
        if (! m_harmable ) return;
        HP -= p;
        if (HP < 0) HP = 0;
        if (HP <= 0){
            die();
        }else{
            m_harmable = false;
            m_noHarmCounter = noHarmTime;
        }
    }
    
    public void addHP(int p){
        HP +=p;
    }
}