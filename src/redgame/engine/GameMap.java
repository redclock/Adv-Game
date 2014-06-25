package redgame.engine;
/*
 * Player.java 作者：姚春晖
 *
 */
import java.awt.*;
import java.util.*;
import java.awt.event.*;

import redgame.util.*;
import redgame.obj.*;
import redgame.status.*;


/**
 * GameMap类是游戏的地图，每一关都是一个新的地图。
 * @author 姚春晖
 * @see MapFileReader
 * @see GameWorld
 */
public class GameMap {
    private int m_w;
    private int m_h;
    private int m_picw;
    private int m_pich;
    private int m_needPrize;
    private long m_counter;
    private String[] m_intro;
    private AbstractObject m_lastObject = null;
    
    private String m_name;
    
   
    private Vector m_actors = new Vector(10); 
    
    private Vector m_objs = new Vector(10); 

    private Vector m_statics = new Vector(10); 
    
    private Vector m_bullets = new Vector(10); 
    private GameWorld m_game;
    private Image m_bkImg;
    private Dimension m_bkSize;
    
    private String startScript;
    
    /**
     * 构造地图
     * @param game 游戏类的引用
     * @see MapFileReader
     * @see GameWorld
     */
        
    public GameMap(GameWorld game) {
        m_game = game;
    }

    /**
     * 得到地图宽度
     */
    public int getWidth() {
        return m_w;
    }

    /**
     * 得到地图高度
     */
    public int getHeight() {
        return m_h;
    }
    
    /**
     * 设置地图大小
     */
    public void setSize(int w, int h){
        m_w = w; m_h = h;
    }
    
    /**
     * 得到地图名
     */

    public String getName(){
        return m_name;
    }
    
    /**
     * 设置地图名
     */
    public void setName(String name){
        m_name = name;
    }
   
    /**
     * 设置地图背景
     */
    public void setBkImg(Image img) {
        m_bkImg = img;
        m_picw = m_bkImg.getWidth(m_game.getPanel());
        m_pich = m_bkImg.getHeight(m_game.getPanel());
        m_bkSize = new Dimension(m_picw, m_pich);
        if (m_picw <=0 || m_pich <=0) m_bkImg = null;
    }

    /**
     * 设置地图背景, 如果为"<none>"则表示背景为空
     */
    public void setBkImg(String filename) {
        if (filename.equals("<none>")) {
            m_bkImg = null;
            return;
        }
        m_bkImg = m_game.getImageManager().loadAutomaticImage(
            filename, Transparency.OPAQUE, m_bkSize);
        setBkImg(m_bkImg);
        m_game.deleteImage(m_bkImg);        
    }

    /**
     * 设置地图背景大小
     */
    public void setBkSize(int w, int h){
        m_bkSize = new Dimension(w, h);
    }
    /**
     * 取得开始时的脚本 
     * @return 开始时的脚本名
     */
    public String getStartScript() {
    	return startScript;
    }
    /**
     * 设置开始时的脚本 
     * @param name 脚本名
     */
    public void setStartScript(String name) {
    	startScript = name;
    }
    /**
     * 设置关卡简介
     */
    public void setIntro(String[] intro){
        m_intro = intro;
    }
    /**
     * 得到关卡简介
     */
    public String[] getIntro(){
        if (m_intro == null){
            m_intro = new String[1];
            m_intro[0] = "本关没有介绍";
        }
        return m_intro;
    }
    
    /**
     * 取得计时器
     */
    public long getCounter(){
        return m_counter;
    }
    /**
     * 检测是否接触到了静态物体
     * @param obj 被检测的物体
     * @return 如果接触到了静态物体，返回静态物体实例，否则返回null
     */
    public StaticObject reachStatic(AbstractObject obj){
        for (int i = 0; i < m_statics.size(); i ++){
            StaticObject s = (StaticObject)m_statics.get(i);
            if (s.getVisible() && obj.checkCollision(s))
                return s;
        }
        return null;
    }
    
    /**
     * 添加人物
     */
    public void addActor(Actor act){
        m_actors.add(act);
    }

    /**
     * 添加玩家人物
     */
    public void addPlayer(Player player){
        m_actors.insertElementAt(player, 0);
    }

    /**
     * 添加静态物体
     */
    public void addStatic(StaticObject s){
        m_statics.add(s);
        if (s instanceof Bonus) m_needPrize++;
    }

    /**
     * 添加地图元素，如墙，梯子
     */
    public void addMapObject(MapObject mobj){
        m_objs.add(mobj);
    }
    
    /**
     * 添加子弹
     */
    public void addBullet(MovableObject s){
        m_bullets.add(s);
    }
   /**
     * 删除指定的一颗子弹
     */
    public void removeBullet(AbstractObject b){
        for (int i = 0; i < m_bullets.size(); i++)
        {
            if (b == m_bullets.get(i)){
                m_bullets.remove(i);
            }
        }
    }

    private float checkLeft(Vector v, AbstractObject obj, float min){
        for (int i = 0; i < v.size(); i ++){
            AbstractObject obj2 = (AbstractObject)v.get(i);
            if (obj == obj2 || !obj2.isBlocked()) continue; 
            
            if ( obj2.getY() < obj.getY()+obj.getH()
                && obj2.getY()+obj2.getH() > obj.getY()
                ){
                float nlen = obj.getX() - obj2.getX() - obj2.getW();
                if (nlen >= 0 && nlen < min){
                    m_lastObject = obj2;
                    min = nlen;
                }
            }
        }
        return min;
    } 
    
    private float checkRight(Vector v, AbstractObject obj, float min){
        for (int i = 0; i < v.size(); i ++){
            AbstractObject obj2 = (AbstractObject)v.get(i);
            if (obj == obj2 || !obj2.isBlocked()) continue; 
            
            if (obj2.isBlocked() && obj2.getY() < obj.getY()+obj.getH()
                && obj2.getY()+obj2.getH() > obj.getY()){
                float nlen = obj2.getX() - obj.getX() - obj.getW();
                if (nlen >= 0 && nlen < min){
                    m_lastObject = obj2;
                    min = nlen;
                }
            }
        }
        return min;
    } 
    private float checkUp(Vector v, AbstractObject obj, float min){
        for (int i = 0; i < v.size(); i ++){
            AbstractObject obj2 = (AbstractObject)v.get(i);
            if (obj == obj2 || !obj2.isBlocked()) continue; 
            
            if (obj2.getX() < obj.getX()+obj.getW()
                && obj2.getX()+obj2.getW() > obj.getX()){
                float nlen = obj.getY() - obj2.getY() - obj2.getH();
                if (nlen >= 0 && nlen < min){
                    m_lastObject = obj2;
                    min = nlen;
                }
            }
        }
        return min;
    } 
    private float checkDown(Vector v, AbstractObject obj, float min){
        for (int i = 0; i < v.size(); i ++){
            AbstractObject obj2 = (AbstractObject)v.get(i);
            if (obj == obj2 || !obj2.isBlocked()) continue; 
            
            if (obj2.getX() < obj.getX()+obj.getW()
                && obj2.getX()+obj2.getW() > obj.getX()){
                float nlen = obj2.getY() - obj.getY() - obj.getH();
                if (nlen >= 0 && nlen < min){
                    m_lastObject = obj2;
                    min = nlen;
                }
            }
        }
        return min;
    } 
   /**
    * 一个物体要向左走一定距离，要检测它的碰撞，向它所碰到的物体发送被碰消息
    * @see AbstractObject#collision
    * @param obj 对象
    * @param len 对象希望走的距离
    * @return 由于碰撞等原因，对象最多能走的距离
    */
    public float gotoLeft(AbstractObject obj, float len) {
        float min = obj.getX();
        m_lastObject = null;
        if (obj.isBlocked()){
            min = checkLeft(m_actors, obj, min);
            min = checkLeft(m_objs, obj, min);
            min = checkLeft(m_bullets, obj, min);
        }
        if (min < len && m_lastObject != null){
            m_lastObject.collision(obj, AbstractObject.G_LEFT);
        }
        return (min < len)? min : len;
    }

   /**
    * 一个物体要向右走一定距离，要检测它的碰撞，向它所碰到的物体发送被碰消息
    * @see AbstractObject#collision
    * @param obj 对象
    * @param len 对象希望走的距离
    * @return 由于碰撞等原因，对象最多能走的距离
    */
    public float gotoRight(AbstractObject obj, float len) {
        float min = m_w - obj.getX() - obj.getW();
        m_lastObject = null;
        if (obj.isBlocked()){
            min = checkRight(m_actors, obj, min);
            min = checkRight(m_objs, obj, min);
            min = checkRight(m_bullets, obj, min);
        }
        if (min < len && m_lastObject != null){
            m_lastObject.collision(obj, AbstractObject.G_RIGHT);
        }
        return (min < len)? min : len;
    }

   /**
    * 一个物体要向上走一定距离，要检测它的碰撞，向它所碰到的物体发送被碰消息
    * 向上有一个特点，没有边界，即物体可以跑到屏幕之上
    * @see AbstractObject#collision
    * @param obj 对象
    * @param len 对象希望走的距离
    * @return 由于碰撞等原因，对象最多能走的距离
    */
   public float gotoUp(AbstractObject obj, float len) {
        //向上特殊处理，没有边界
        float min = len;
        //float min = obj.getY(); //这句为有边界
        m_lastObject = null;
        
        if (obj.isBlocked()){
            min = checkUp(m_actors, obj, min);
            min = checkUp(m_objs, obj, min);
            min = checkUp(m_bullets, obj, min);
        }
        
        if (min < len && m_lastObject != null){
            m_lastObject.collision(obj, AbstractObject.G_UP);
        }
        return (min < len)? min : len;
    }

   /**
    * 一个物体要向下走一定距离，要检测它的碰撞，向它所碰到的物体发送被碰消息
    * @see AbstractObject#collision
    * @param obj 对象
    * @param len 对象希望走的距离
    * @return 由于碰撞等原因，对象最多能走的距离
    */
    public float gotoDown(AbstractObject obj, float len) {
        float min = m_h - obj.getY() - obj.getH();
        m_lastObject = null;
        if (obj.isBlocked()){
            min = checkDown(m_actors, obj, min);
            min = checkDown(m_objs, obj, min);
            min = checkDown(m_bullets, obj, min);
        }
        if (min < len && m_lastObject != null){
            m_lastObject.collision(obj, AbstractObject.G_DOWN);
        }
        
        return (min < len)? min : len;
    }
   /**
    * 物体当前所在位置是否可以爬上爬下 
    * @param obj 对象
    */
    public boolean canClimb(AbstractObject obj){
        float mid = obj.getX() + obj.getW() * 0.5f;
        float miny = obj.getY(), maxy = obj.getY() + obj.getH();
        for (int i = 0; i < m_objs.size(); i++){
            MapObject o = (MapObject)m_objs.get(i);
            if ( o.isClimbable() && 
                Math.abs(o.getX()+o.getW()*0.5f - mid)<= o.getW()*0.5f - 5
                && maxy > o.getY() && miny < o.getY() + o.getH())
                return true; 
        }
        return false;
    }

   /**
    * 画地图和上面的物体
    */
   public void paint(Graphics g) {
        if (m_bkImg == null){
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, m_w, m_h);
        }else {
            Rectangle sa = m_game.getScreenArea();
            int x, y;
            if (m_w == sa.getWidth())
                x = 0;
            else    
                x = (int) (sa.getX() * (m_w - m_bkSize.getWidth()) / (m_w - sa.getWidth()));
            if (m_h == sa.getHeight())
                y = 0;
            else
                y = (int) (sa.getY() * ( m_h - m_bkSize.getHeight()) / (m_h - sa.getHeight()));
            g.drawImage(m_bkImg, 
                        x, y,
                        x + (int) m_bkSize.getWidth(), y + (int) m_bkSize.getHeight(), 
                        0, 0, m_picw, m_pich, 
                        m_game.getPanel()
                        );
        }
        for (int i = 0; i < m_objs.size(); i++){
            ((MapObject)m_objs.get(i)).paint(g);            
        }
        for (int i = 0; i < m_statics.size(); i++){
            ((StaticObject)m_statics.get(i)).paint(g);          
        }

        for (int i = 0; i < m_actors.size(); i++){
            ((Actor)m_actors.get(i)).paint(g);          
        }
        for (int i = 0; i < m_bullets.size(); i++){
            ((MovableObject)m_bullets.get(i)).paint(g);          
        }

    }

    /**
     * 更新计时器
     * @param passedTime 上次调用到这次所用的毫秒数
     */
    public void move(long passedTime){
        for (int i = 0; i < m_statics.size(); i++){
            ((StaticObject)m_statics.get(i)).move(passedTime);          
        }
        for (int i = 0; i < m_objs.size(); i++){
            ((MapObject)m_objs.get(i)).move(passedTime);          
        }
    }
    /**
     * 更新状态
     * @param passedTime 上次调用到这次所用的毫秒数
     */

    public void update(long passedTime){
        m_counter += passedTime;
        for (int i = 0; i < m_actors.size(); i++){
            ((Actor)m_actors.get(i)).update(passedTime);            
        }
        for (int i = 0; i < m_bullets.size(); i++){
            ((MovableObject)m_bullets.get(i)).update(passedTime);            
        }
        if (KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)){
            m_game.pushStatus(new MenuStatus(m_game));
        }
    }
    /**
     * 使所有人物动起来
     * @param passedTime 上次调用到这次所用的毫秒数
     */
    public void moveActors(long passedTime){
        for (int i = 0; i < m_actors.size(); i++){
            ((Actor)m_actors.get(i)).move(passedTime);          
        }
        for (int i = 0; i < m_bullets.size(); i++){
            ((MovableObject)m_bullets.get(i)).move(passedTime);          
        }
    }
    
    public Vector getActors(){
        return m_actors;
    }
    
    public Vector getMapObjs(){
        return m_objs;
    }

    public Vector getWeapons(){
        return m_bullets;    
    }
    
    public Vector getStatics(){
        return m_statics;
    }

    private AbstractObject findObject(Vector v, String name){
        for (Object x: v){
            AbstractObject a = (AbstractObject) x;
            String n = a.getName();
            if (n != null && n.equals(name)){
                return a;
            }
        }
        return null;
    }
    
    public Actor findActor(String name){
        return (Actor) findObject(m_actors, name);
    }
    public MapObject findMapObject(String name){
        return (MapObject) findObject(m_objs, name);
    }
    public StaticObject findStatic(String name){
        return (StaticObject) findObject(m_statics, name);
    }
}
