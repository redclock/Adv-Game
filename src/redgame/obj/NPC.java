package redgame.obj;
/*
 * NPC.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;
/**
 * NPC类是非玩家角色,实际上玩家角色也是其子类
 * 每次更新,由update方法设置new_face值,再有move方法进行移动
 * @author 姚春晖
 */

public class NPC extends Actor{
    /**是否在爬梯子。*/
    protected boolean m_isClimbing = false;  
    /**是否在挥剑*/
    protected boolean m_isSwording = false; 
    //要走到的目标位置的横坐标
    protected int m_destRangeStart;
    protected int m_destRangeEnd;
    /**
     *剑光
     */
    protected Sword m_sword;

    /**
     * 将new_face设为0~3的数,使其可以朝某一方向走,
     * 设为-1则静止.
     * @see Actor#m_face
     */
	protected int new_face = -1;
    /**
     * 构造NPC
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @see Actor
     */

	public NPC(GameWorld game, Image img, int x, int y, int w, int h) {
		super(game, img, x, y, w, h);
		m_delay = 100;
		updateAnimation();
		noDest();
        HP = 3;
	}
    /**
     * 覆盖Actor.move
     * @see Actor#move
     */
	public void move(long passedTime){
        //死了就不动了
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

        if (m_dead) return;

        float t = (passedTime * 10.0f/ m_delay);
		float dt;
        //昏迷中
        if (m_lull) {
            waitLull(passedTime);
            return;
        }
        //兴奋剂中
        if (m_cordial) {
            m_delay = 50;
            waitCordial(passedTime);
        }
        //挥剑中
        if (m_isSwording){
            if (m_sword.finished()){
                m_isSwording = false;  
                m_game.getMap().removeBullet(m_sword);
                m_sword = null;
                updateAnimation();
            }
            moveDown(passedTime);
            return;
        }else if (m_isClimbing){
            //更新是否在爬梯子的状态
            m_isClimbing = m_game.getMap().canClimb(this);
            if (!m_isClimbing){
                //如果不再爬了,随机选择左右
                m_face = (m_game.getRandom(2) == 0) ? G_LEFT: G_RIGHT;
                updateAnimation();
            }else{
                //脸朝上
                if (m_face != G_UP){
                    m_face = G_UP;
                    updateAnimation();
                }
                //不动时
                if (new_face == -1) {
                    m_anim.resetToStart();
                }
                //移动
                else if (new_face == G_UP){
                    dt = m_game.getMap().gotoUp(this, t);
                    y -= t;
                }
                else if (new_face == G_DOWN){
                    dt = m_game.getMap().gotoDown(this, t);
                    y += t;
                }
                else if (new_face == G_LEFT){
                    dt = m_game.getMap().gotoLeft(this, t);
                    x -= dt;
                }
                else if (new_face == G_RIGHT){
                    dt = m_game.getMap().gotoRight(this, t);
                    x += dt;
                }               
                m_anim.update(passedTime);
            }
        }else{
            //正常移动                
            //new_face == -1表示不动
            if (new_face == -1) {
    			m_anim.resetToStart();
    		}else {
    		//按new_face移动  
    			if (m_face != new_face){
    				m_face = new_face;
    				updateAnimation();
    			}
    			else if (m_face == G_LEFT){
    				dt = m_game.getMap().gotoLeft(this, t);
    				if (dt < t) cannotGo();
    				x -= dt;
    			}
    			else if (m_face == G_RIGHT){
    				dt = m_game.getMap().gotoRight(this, t);
    				if (dt < t) cannotGo();
    				x += dt;
    			}
    			m_anim.update(passedTime);	
    			//new_face = -1;
    		}
    		moveDown(passedTime);
        }		
	}
    /**
     * 覆盖collision
     * @see MovableObject#collision
     */
    
    public boolean collision(AbstractObject obj, int direction){
        if(!m_isSwording && !m_lull && new_face != -1 && direction == G_DOWN && obj instanceof MovableObject)
        //顶着从上边来的物体
        {
            float t = (m_game.passedTime * 10.0f/ m_delay);
            float dt;
            if (m_face == G_LEFT){
                dt = m_game.getMap().gotoLeft(obj, t);
                obj.setPosition(obj.getX() - dt, obj.getY());
            }
            else if (m_face == G_RIGHT){
                dt = m_game.getMap().gotoRight(obj, t);
                obj.setPosition(obj.getX() + dt, obj.getY());
            }
        }
        //执行脚本
        if (m_script != null && obj instanceof Player){
            m_game.getScript().add(m_script, 
                                    new ScriptSource(this, obj, direction));
        }

        return false;
    } 
    /**
     * 当遇到障碍时调用
     */
	public void cannotGo(){
	}
	
	public void update(long passedTime){
        if (getX() < m_destRangeStart) new_face = G_RIGHT;
        else if (getX() > m_destRangeEnd) new_face = G_LEFT;
        else new_face = -1;
	}
	/**
	 * 挥剑
	 */
	public void doSword(int delay, boolean special){
       if (! m_isSwording ){
            m_isSwording = true;
            int x, y, b, e, w;
            Image img;
            if (special){
                if (m_face == G_LEFT){
                    x = (int)getX() - 35;
                    y = (int)getY();
                    b = 0;
                    e = 4;
                    w = 18;
                    img = m_game.loadImage("image/sword2_l.png");
                }else{
                    x = (int)(getX() + getW()) + 15;
                    y = (int)getY();
                    b = 0;
                    e = 4;
                    w = 24;
                    img = m_game.loadImage("image/sword2_r.png");
                }
            }else {
                if (m_face == G_LEFT){
                    x = (int)getX() - 15;
                    y = (int)getY();
                    b = 0;
                    e = 4;
                    w = 18;
                    img = m_game.loadImage("image/sword.png");
                }else{
                    x = (int)(getX() + getW()) + 15;
                    y = (int)getY();
                    b = 5;
                    e = 9;
                    w = 24;
                    img = m_game.loadImage("image/sword.png");
                }
            }
            if (special) {
                m_sword = new Sword(m_game, this, img, x, y, 64, 64, b, e, 3);
                m_sword.setBlockRect(new Rectangle(w, 15, 16, 48));            
            } else {
                m_sword = new Sword(m_game, this, img, x, y, 48, 64, b, e, 1);
                m_sword.setBlockRect(new Rectangle(w, 15, 6, 34));            
            }
            m_sword.setAnimDelay(delay);            
            m_sword.makeRect(); 
            m_game.getMap().addBullet(m_sword);
        }
	}
	/**
	 * 弹射
	 */
	public void doShoot(int ox, int oy, float vx, float vy){
        Bounce bullet = new Bounce(m_game, m_game.loadImage("image/icon1.png"),
                   ox, oy, 24, 24);
        bullet.setBlockRect(new Rectangle(0, 0, 24,24));                                    
        bullet.setSpeed(vx, vy);                        
        m_game.getMap().addBullet(bullet);                        
	}	
    /**
     * 弹射
     */
    public void doShoot(float angle, float r, float v){
        int ox = (int)(getX()+ getW()/2 + r*Math.cos(angle)-12);
        int oy = (int)(getY()+10 + r*Math.sin(angle));
        float vx = (float)(v*Math.cos(angle));
        float vy = (float)(v*Math.sin(angle));
        doShoot(ox, oy, vx, vy);       
    }
    /**
     * 扔炸弹
     */
    public void doBomb(int x, int y){
        Bomb bomb = new Bomb(m_game, m_game.loadImage("image/bomb.png"),
                   x, y, 32, 48, 0, 8);
        m_game.getMap().addBullet(bomb);                        
    } 
    /**
     * 令其死亡
     */    
    public void die(){
        super.die();
        if (m_isSwording){
            m_game.getMap().removeBullet(m_sword);
            m_sword = null;
        }
    }
    
    public void setDest(int x, int width){
        m_destRangeStart = x - width / 2;
        m_destRangeEnd   = x + width / 2;
    }
    
    public boolean isMoving(){
        return (getX() < m_destRangeStart) || (getX() > m_destRangeEnd);
    }
    
    
    public void noDest(){
        m_destRangeStart = 0;
        m_destRangeEnd = Integer.MAX_VALUE;   
    }
}