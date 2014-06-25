package redgame.obj;

import java.awt.*;
import redgame.scripts.*;
import redgame.engine.*;
import redgame.anim.*;

public class Bonus extends StaticObject {
	final static public int HEAL    = 0;
    final static public int HEAL2   = 1;
    final static public int HEAL4   = 2;
    final static public int CORDIAL = 3;
    final static public int NOHARM  = 4;
    final static public int RING    = 5;
    public int kind;
    /*
     * 死时动画的粒子系统
     */
    protected SparkParticleSystem m_spark;    
    static public Bonus getStandardBonus(GameWorld game, int x, int y, int kind) {
        Bonus b;
        int w, h;
        int rb, re;
        Rectangle rect;
        Image img;
        switch( kind ) {
            case HEAL:
                img = game.loadImage("image/fire1.png");
                w = 32;
                h = 64;
                rb = 0;
                re = 3;
                rect = new Rectangle(5, 40, 22, 19);
                break;
            case HEAL2:
                img = game.loadImage("image/b1.png");
                w = 48;
                h = 48;
                rb = 0;
                re = 29;
                rect = new Rectangle(10, 10, 28, 28);
                break;
            case HEAL4:
                img = game.loadImage("image/stone1.png");
                w = 32;
                h = 64;
                rb = 0;
                re = 3;
                rect = new Rectangle(3, 14, 26, 32);
                break;
            case RING:
                img = game.loadImage("image/b2.png");
                w = 48;
                h = 48;
                rb = 0;
                re = 29;
                rect = new Rectangle(10, 10, 28, 28);
                break;
            default:
                return null;
        }        
        b = new Bonus(game, img, x, y, w, h, rb, re);
        b.setBlockRect (rect);
        b.kind = kind;
        return b;
    }
    
	public Bonus(GameWorld game, Image img, int x, int y, int w, int h,
			int begin, int end) {
		super(game, img, x, y, w, h, begin, end);
	}
	
	public void effect(Actor a){
	   switch(kind){
	       case HEAL: 
	            a.addHP(1);
	            if (AbstractScript.getIntVar("MeetBonusHeal") == 0){
                   say("你吃到了一个小回复剂, \n它能回复你四分之一心的生命");
                   AbstractScript.setVar("MeetBonusHeal", 1);
	            } 
	            break;
           case HEAL2: a.addHP(2); 
                if (AbstractScript.getIntVar("MeetBonusHeal2") == 0){
                   say("你吃到了一个回复剂, \n它能回复二分之一心的生命");
                   AbstractScript.setVar("MeetBonusHeal2", 1);
                } 
                break;
           case HEAL4:
                a.addHP(4);  
                if (AbstractScript.getIntVar("MeetBonusHeal4") == 0){
                   say("你吃到了一个大回复剂, \n它能回复你一颗心的生命");
                   AbstractScript.setVar("MeetBonusHeal4", 1);
                } 
                break;
           case CORDIAL: a.startCordial(10000); 
                if (AbstractScript.getIntVar("MeetBonusCordial") == 0){
                   say("你吃到了一个兴奋剂, \n它能最大限度刺激你的运动神经\n"+
                       "但兴奋剂对身体很不好,会使你生命力降低");
                   AbstractScript.setVar("MeetBonusCordial", 1);
                } 
                break;
           case RING: 
                    if (a instanceof Player){ 
                        Player p = (Player) a;
                        p.addBullet();
                    }
                    if (AbstractScript.getIntVar("MeetBonusRing") == 0){
                       say("你吃到了一个面包圈");
                       AbstractScript.setVar("MeetBonusRing", 1);
                    } 
                    break;
        }
    }

	/**
	 * 更新动画
	 */
	public void move(long passedTime){
		super.move(passedTime);
        if (m_spark != null)
        {
        	m_spark.move(passedTime);
        	if (m_spark.done()) m_spark = null;
        }
		
	}
    public void paint(Graphics g){
    	super.paint(g);
        if (m_spark != null) m_spark.paint(g); 
    }
    
    public void die(){
        super.die();
        m_spark = new SparkParticleSystem(m_game, m_game.loadImage("image/star2.png"), 
                  (int)(getX() + getW() / 2) - 32, (int)getY() - 10, 10, 64, 64, 0, 0, 100,
                  2, 5, 0.5f);
    } 

    

}