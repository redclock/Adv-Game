package redgame.obj;

import java.awt.*;
import java.util.*;
import redgame.engine.*;
import redgame.anim.*;
public class Bomb extends MovableObject {

    public Rectangle rect;

    private int bomb_delay = 3000;
    private long m_counter = 0;
    private Animation m_exp_anim;
    private FileAnimation m_exp_anim_big;
    private boolean m_exploded = false;
    private boolean m_readyToExplode = false;
    
    public Bomb(GameWorld game, Image img, int x, int y, int w, int h,
                    int begin, int end) {
        super(game, img, x, y, w, h);

        m_exp_anim = new Animation(game, game.loadImage("image/fire3.png"), 56, 56);
        m_exp_anim.setRange(0, 10, 70);
        m_exp_anim.setLooped(false);

        m_anim.start();

        m_anim.setRange(begin, end, m_delay);
        m_anim.setLooped(true);
        m_exp_anim.start();
        m_blocked = true;
        m_block.x = 4;
        m_block.y = 10;
        m_block.width = 24;
        m_block.height = 30;
    }
    public void update(long passedTime){
        m_anim.update(passedTime);
        moveDown(passedTime);
        m_counter += passedTime;
        
        if (m_exploded == false && m_readyToExplode){
            explode();
        }
        if (m_exploded == false && m_counter > bomb_delay){
            setExploded(true);
        }
        if (m_exploded){
            Vector actors = m_game.getMap().getActors();
            for (int i = 0; i < actors.size(); i ++){
                Actor a = (Actor)actors.get(i);
                if (!a.isdead() && a.checkCollision(rect))
                    a.hurt(3, 1000);
            }
            m_exp_anim_big.update(passedTime);
            if (m_exp_anim_big.isStop()){
                m_game.getMap().removeBullet(this);
            }    
        }
        
    }
    public void move(long passedTime){
    }
    public void paint(Graphics g){
        //如果不可见就不画
        if (!m_visible || !inScreen()) return;
        super.paint(g);
        if (m_exploded){
     //       m_exp_anim_big.paint(g);
        }    
    }
    
    public void explode(){
        if (m_exploded == false){
            m_exploded = true;    
            m_game.playSound("sound/explode.wav");
            m_anim = m_exp_anim;
            m_exp_anim_big = new FileAnimation(m_game, (int)x + 17, (int)y + 10, "image/anim/exp.txt");
            m_blocked = false;
            m_gravity = false; 
            rect = new Rectangle((int)x - 10, (int)y - 10, 50, 50);
            Vector objs = m_game.getMap().getMapObjs();
            for (int i = 0; i < objs.size(); i ++){
                MapObject m = (MapObject)objs.get(i);
                if (m.getVisible() && m.getDamagable() && m.checkCollision(rect)){
                    m.destory();
                }
            }
            objs = m_game.getMap().getWeapons();
            for (int i = 0; i < objs.size(); i ++){
                MovableObject m = (MovableObject)objs.get(i);
                if (m instanceof Bomb){
                    Bomb b = (Bomb)objs.get(i);
                    if (b.getVisible() && !b.getExploded() && b.checkCollision(rect)){
                        b.setExploded(true);
                    }
                }
            }
        }
    }
    
    public void setExploded(boolean e){
        m_readyToExplode = e;
    }

    public boolean getExploded(){
        return m_exploded;
    }
}
