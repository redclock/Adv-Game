package redgame.obj;
/*
 * Player.java 作者：姚春晖
 *
 */
import java.awt.*;
import java.awt.event.KeyEvent;
import redgame.engine.*;
import redgame.util.*;
import redgame.status.*;
import redgame.item.*;
import redgame.anim.*;

/**
 * Player类是玩家的角色，可以跳，可以用键盘控制。游戏中一般只有一个实例
 * @author 姚春晖
 */
final public class Player extends NPC{
    final public static int ITEM_SWORD = 0;
    final public static int ITEM_RING = 1;
    final public static int ITEM_BOMB = 2;
     
    private float m_shootAngle = 0.0f;   //瞄准时的角度
    //是否在瞄准
    private boolean m_isShooting = false;  

    private Animation m_power;
    private boolean m_powering;
    public AbstractItem curItem;
    
    public AbstractItem[] items = new AbstractItem[4];
     
    /**
     * 拥有id的钥匙
     */
    public boolean hasKey[] = new boolean[256];
    /**
     * 已经开的门id
     */
    public boolean openedDoor[] = new boolean[256];
    /**
     * 是否可控制
     */
    public boolean canControl = true;
    
    public boolean canShoot(){
        if (items[ITEM_RING] == null) return false;
        return items[ITEM_RING].available();
    }
    
    public boolean canSword() {
        if (items[ITEM_SWORD] == null) return false;
        return items[ITEM_SWORD].available();
    }

    public boolean canBomb() {
        if (items[ITEM_BOMB] == null) return false;
        return items[ITEM_BOMB].available();
    }

    public Player(GameWorld game, Image img, int x, int y, int w, int h) {
        super(game, img, x, y, w, h);
        m_delay = 100;
        HP = 12;
        m_power = new Animation(game, game.loadImage("image/dd.png"), 100, 100);
        m_power.setRange(0, 6, 100);
        m_powering = false;
      //  items[ITEM_SWORD] = new SwordItem(game);
      //  items[ITEM_RING] = new RingItem(game);
      //  items[ITEM_BOMB] = new BombItem(game);
      //  items[ITEM_BOMB].setCount(30);
    }
    
    //处理键盘输入
    private void doInput(long passedTime){
        if (KeyManager.isKeyJustDown(KeyEvent.VK_1)) {
            curItem = items[ITEM_SWORD];
        }else if (KeyManager.isKeyJustDown(KeyEvent.VK_2)) {
            curItem = items[ITEM_RING];
        }else if (KeyManager.isKeyJustDown(KeyEvent.VK_3)) {
            curItem = items[ITEM_BOMB];
        }
        
        
        if (curItem == null){
            
        }else if (curItem == items[ITEM_SWORD]){
            //X键挥剑
            if (canSword() && !m_isClimbing && 
                !m_isShooting && 
                !m_isSwording ){
                SwordItem s = (SwordItem) curItem;
                if (s.power >= 10 && KeyManager.isKeyJustDown(KeyEvent.VK_X)){
                    m_game.playSound("sound/shout1.wav");
                    doSword(100, false);
                    s.power -= 10;
                }else if (s.power >= 100 && KeyManager.isKeyJustDown(KeyEvent.VK_C)){
                    m_game.playSound("sound/shout1.wav");
                    doSword(100, true);
                    m_power.resetToStart();
                    m_power.setLooped(false);
                    m_power.start();
                    m_powering = true;
                    s.power -= 100;
                }   
            } 
            m_isShooting = false;
        }else if (curItem == items[ITEM_RING]){
            //按下X键，显示瞄准镜
            if (canShoot() &&
                !m_isShooting && !m_isSwording && 
                KeyManager.isKeyJustDown(KeyEvent.VK_X)){
                    switch(m_face){
                        case G_RIGHT: m_shootAngle = 0.0f; break;
                        case G_UP: m_shootAngle = -1.57f; break;
                        case G_LEFT: m_shootAngle = 3.14f; break;
                        case G_DOWN: m_shootAngle = 1.57f; break;
                    }    
                    m_isShooting = true;
            }else if (m_isShooting && ! KeyManager.isKeyDown(KeyEvent.VK_X)){
                //抬起X键，放出面包圈
                doShoot(m_shootAngle, 10, 0.3f);
                if (items[ITEM_RING] != null){
                    items[ITEM_RING].setCount(items[ITEM_RING].getCount() - 1);
                }
                m_game.playSound("sound/pop.wav");
                m_isShooting = false;
            }
            if (m_isShooting){
                //瞄准时按左右键
                if (KeyManager.isKeyDown(KeyEvent.VK_LEFT)){
                    m_shootAngle -= 0.001 * passedTime;
                }if (KeyManager.isKeyDown(KeyEvent.VK_RIGHT)){
                    m_shootAngle += 0.001 * passedTime;
                }
            }    
        } else if (curItem == items[ITEM_BOMB]){
            //X键扔炸弹
            if (canBomb() && !m_isClimbing && 
                !m_isShooting && 
                !m_isSwording && 
                KeyManager.isKeyJustDown(KeyEvent.VK_X)){
                    //m_game.playSound("sound/shout1.wav");
                    doBomb((int)x, (int)y + m_block.height - 30);
                items[ITEM_BOMB].setCount(items[ITEM_BOMB].getCount() - 1);
            } 
            m_isShooting = false;
        }
        
        if (! m_isShooting){
            //移动
            if (KeyManager.isKeyDown(KeyEvent.VK_LEFT)){
                new_face = G_LEFT;
            }if (KeyManager.isKeyDown(KeyEvent.VK_RIGHT)){
                new_face = G_RIGHT;
            }if (KeyManager.isKeyDown(KeyEvent.VK_UP)){
                //爬梯子
                if (m_isClimbing || m_game.getMap().canClimb(this)){
                    new_face = G_UP;
                    m_isClimbing = true;
                    m_vy = 0.0f;
                }
            }if (KeyManager.isKeyDown(KeyEvent.VK_DOWN)&&
                (m_isClimbing || m_game.getMap().canClimb(this))){
                new_face = G_DOWN;
                m_isClimbing = true;
                m_vy = 0.0f;
            }
            //跳跃
            if (KeyManager.isKeyDown(KeyEvent.VK_Z)){
                if (m_vy == 0.0f && !m_isClimbing){
                    m_vy = - 0.4f;
                    m_game.playSound("sound/jump1.wav");
                }
            }   
        }    
    }
    
    /**
     * 更新状态
     */
    public void update(long passedTime){
        super.update(passedTime);
        //死掉就退出
        if (m_dead && !m_visible){
            m_game.pushStatus(new DieStatus(m_game));
        }
        if (m_dead || m_lull || m_isSwording) return;
        super.update(passedTime);
        //输入          
        if (canControl) doInput(passedTime);
        
        if (curItem != null && curItem instanceof SwordItem) {
            SwordItem s = (SwordItem) curItem;
            s.power += (float) ( passedTime * 0.02f );
            if (s.power > 100) s.power = 100; 
        }
        
        StaticObject obj = m_game.getMap().reachStatic(this);
        if (obj != null && !obj.isdead()){
            //取得宝物
            if(obj instanceof Bonus){
                Bonus b = (Bonus) obj;
                obj.die();
                b.effect(this);   
                if (b.getScript() != null){
                    m_game.getScript().add(b.getScript(), new ScriptSource(b, this, 0));
                }   
                m_game.playSound("sound/success.wav");
            }else if (obj instanceof DoorKey){
                //钥匙
                obj.die();
                DoorKey key = (DoorKey)obj;
                m_game.playSound("sound/key.wav");
                hasKey[key.id] = true;
                if (key.getScript() != null)
                    m_game.getScript().add(key.getScript(), new ScriptSource(key, this, 0));
            }else if (obj instanceof Door){
                //门
                Door d = (Door) obj;
                if (canControl){
                    if (KeyManager.isKeyJustDown(KeyEvent.VK_UP)){
                        if (! d.isOpen() && hasKey[d.id]){
                            d.open();
                            m_game.playSound("sound/dooropen.wav");
                            openedDoor[d.id] = true;
                        }else if (d.isOpen()){
                            d.enter(this);
                            //m_game.pushStatus(new SwitchStatus(m_game, new CompleteStatus(m_game), 0));   
                        }else{
                            m_game.pushStatus(new SayStatus(m_game, 
                                            d, "门被锁上了，\n你必须先找到钥匙\n^_^"));   
                        }
                    }
                }
            }   
        }
    }
    
    public void paint(Graphics g){
        if (getVisible() == false) return;
        super.paint(g);
        if (m_isSwording){
            if (m_face == G_LEFT){
                m_anim.setRange(16, 16, 1000);
            }else{
                m_anim.setRange(17, 17, 1000);
            }    
            //m_swordAnim.paint(g, (int)getX()-5, (int)getY());
        }else if (m_isShooting){
            //画准星
            int ox = (int)(getX()+ getW()/2 + 50 * Math.cos(m_shootAngle));
            int oy = (int)(getY()+ 10 + 50 * Math.sin(m_shootAngle));
            g.setColor(Color.GREEN);
            g.drawOval(ox-6, oy-6, 12, 12);
            g.drawLine(ox, oy-8, ox, oy+8);
            g.drawLine(ox-8, oy, ox+8, oy);
        }
        if (m_powering) {
           // m_power.paint(g, (int) getX()- (100 - getW()) /2, (int) getY()-20);
            m_power.update(m_game.passedTime);
            if (m_power.currIndex == m_power.end) m_powering = false;
        }

    }

    public void cannotGo(){
    }
 
    public void meetSpring(){
        if (KeyManager.isKeyDown(KeyEvent.VK_Z)){
            if (!m_isClimbing && m_vy > -0.8f){
                m_vy -= 0.1f;
//                m_game.playSound("sound/jump1.wav");
            }
        }   
    }
    
    /**
     * 覆盖collision
     * @see NPC#collision
     */
    public boolean collision(AbstractObject obj, int direction){
        if (obj instanceof Bounce && KeyManager.isKeyDown(KeyEvent.VK_C)){
            if (items[ITEM_RING] != null && curItem == items[ITEM_RING]){
                m_game.playSound("sound/getring.wav");
                m_game.getMap().removeBullet(obj);
                items[ITEM_RING].setCount(items[ITEM_RING].getCount() +1);
                return true;
            }else {
                return false;
            }
        }else  if (obj instanceof Enemy){
            hurt(1, 500);
            return true;
        }else{
            return super.collision(obj, direction);
        }
    } 

    /**
     * 令其死亡
     */    
    public void die(){
        super.die();
        m_game.playSound("sound/gong.wav");
    }
    public void addHP(int p){
        HP +=p;
        if (HP > 16) HP = 16;
    }
    public void addBullet(){
        if (items[ITEM_RING] != null)
            items[ITEM_RING].setCount(items[ITEM_RING].getCount() +1);
    }

}
