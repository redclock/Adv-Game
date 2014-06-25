package redgame.obj;
/*
 * AbstractObject.java 作者：姚春晖
 *
 */
 import java.awt.*;
 import redgame.engine.*;
 import redgame.anim.*;
 import redgame.status.*;
 
/**
 * AbstractObject类是游戏中所有物体的父类，定义基本功能。
 * 这是一个抽象类，要使用它的派生类。
 * @author 姚春晖
 */

abstract public class AbstractObject{
    /**
     * 朝向下
     */	
	final static public int G_DOWN	= 0;
    /**
     * 朝向左
     */ 
    final static public int G_LEFT  = 1;
    /**
     * 朝向右
     */ 
    final static public int G_RIGHT = 2;
    /**
     * 朝向上
     */ 
    final static public int G_UP    = 3;

    /**
     * 图片x坐标
     */ 
    protected float x;
    /**
     * 图片y坐标
     */ 
    protected float y;
    /**
     * 图片一帧宽度
     */ 
    protected int w;
    /**
     * 图片一帧高度
     */ 
    protected int h;
    /**
     * 游戏的引用
     */ 
    protected GameWorld m_game;
    /**
     * 动画类
     */ 
    protected Animation m_anim;
	/*
	 * 碰撞矩形
	 */
	protected Rectangle m_block;
    /*
     * 图片每一帧的延迟
     */
    protected int       m_delay; 
    /*
     * 是否可见
     */
    protected boolean   m_visible;
    
    /*
     * 是否可碰撞
     */
    protected boolean   m_blocked = true; 
    /**
     *  名字
     */
    protected String m_name; 
    /**
     *  对应的脚本
     */
    protected String m_script;
      
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
     */

	public AbstractObject(GameWorld game, Image img, int x, int y, int w, int h){
		m_game = game;
		this.x = x;  this.y = y;
		this.w = w;  this.h = h;
		m_block = new Rectangle(0, 0, w, h);
		m_delay = 100;
		m_visible = true;
		if (img != null)
			m_anim = new Animation(game, img, w, h);
	}
	/**
	 * 得到X位置
	 */
	public float getX(){
		return x;
	} 
    /**
     * 得到Y位置
     */
    public float getY(){
		return y;
	} 
    /**
     * 得到碰撞宽度
     */
    public int getW(){
		return m_block.width;
	}
	/**
	 * 得到图像宽度
	 */ 
	public int getImgW(){
	   return m_anim.getWidth();
	} 
    /**
     * 得到图像高度
     */ 
    public int getImgH(){
       return m_anim.getHeight();
    } 
    /**
     * 得到碰撞高度
     */
    public int getH(){
		return m_block.height;
	} 
    /**
     * 设置位置
     */
    public void setPosition(float ax, float ay){
		x = ax; y = ay;
	} 
    /**
     * 设置大小
     */
    public void setSize(int aw, int ah){
		w = aw; y = ah;
	}
    /**
     * 检测是否与其他对象碰撞
     * @param obj 要检测的另一个对象
     */
    
	public boolean checkCollision(AbstractObject obj){
		return (obj.getX() + obj.getW() > getX() && 
		        obj.getX() < getX() + getW() )&&
		       (obj.getY() + obj.getH() > getY() && 
		        obj.getY() < getY() + getH());
	}
    /**
     * 检测是否与矩形碰撞
     * @param rect 要检测的矩形
     */
    
    public boolean checkCollision(Rectangle rect){
        return (rect.x + rect.width > getX() && 
                rect.x < getX() + getW() )&&
               (rect.y + rect.height > getY() && 
                rect.y < getY() + getH());
    }
    /**
	 * 是否在屏幕中
	 *
	 */
    public boolean inScreen(){
        Rectangle sa = m_game.getScreenArea();
        return (
                (sa.width >= m_game.getSize().width || 
                  ( x - m_block.x + m_anim.getWidth() >= sa.x && 
                    x - m_block.x <= sa.x + sa.width
                  )
                )&&
                (sa.height >= m_game.getSize().height ||
                  ( y - m_block.y + m_anim.getHeight() >= sa.y && 
                    y - m_block.y <= sa.y + sa.height
                  )
                )
               );
    }

    /**
     * 通知某个对象撞到自己，各个子类可以覆盖这个方法，进行碰撞处理。
     * @param obj 撞来的对象
     * @param direction 方向 
     * @return 是否已处理
     */
    public boolean collision(AbstractObject obj, int direction){
		//System.out.println(this.getClass()+" meets "+obj.getClass()
		//					+" dir = "+direction);
		return false;
	} 
    /**
     * 设置碰撞矩形区，即图像中能进行碰撞检测的那一个部分。
     * @param block 新的碰撞矩形
     */
    
	public void setBlockRect(Rectangle block){
		m_block = block;
	}
	
    /**
     * 画物体。如果图像可用则画出当前帧，否则画一个方框
     * @param g 要画到的地方
     */
    public void paint(Graphics g){
        //如果不可见就不画
		if (!m_visible || !inScreen()) return;
		//如果动画为空,简单画一个矩形
		if (m_anim == null){
			g.setColor(new Color(100,100,100,100));
            g.fillRect((int)x - m_block.x,(int)y  - m_block.y, m_block.width, m_block.height);
		}else{
		  //否则画动画图形
            m_anim.paint(g, (int)x - m_block.x, (int)y - m_block.y);
        }
	}
    /**
     * 返回是否可见
     */
    public boolean getVisible(){
		return m_visible;
	}
    /**
     * 设置是否可见
     */
    public void setVisible(boolean visible){
		m_visible = visible;
	}

    /**
     * 设置是否会进行碰撞
     */
    public void setBlocked(boolean b){
        m_blocked = b;
    }
    /**
     * 是否会进行碰撞
     */
    public boolean isBlocked(){
	   return m_blocked && m_visible;
	};
	/**
	 * 说话
	 */
	public void say(String text){
        m_game.pushStatus(new SayStatus(m_game, this, text));
	}
	/**
	 * 得到名字
	 */
	public String getName(){
	   return m_name;
	}
	/**
	 * 设置名字
	 */
	public void setName(String name){
	   m_name = name;
	}  
    /**
     * 得到脚本
     */
    public String getScript(){
       return m_script;
    }
    /**
     * 设置脚本
     */
    public void setScript(String script){
       m_script = script;
    }
    /**
     * 设置延时
     */
    public void setDelay(int delay){
        m_delay = delay;
        setAnimDelay(delay);
    }  
    /**
     * 设置动画每帧延时
     */
    public void setAnimDelay(int delay){
        m_anim.setDelay(delay);
    }  
    
    public Animation getAnim() {
        return m_anim;
    }
}