package redgame.obj;
/*
 * Friend.java 作者：姚春晖
 *
 */
import java.awt.*;
import redgame.engine.*;

/**
 * Friend类是非敌人角色
 * 
 * @author 姚春晖
 */

public class Friend extends NPC{
    /**
     * 构造Friend
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 图像一格的宽度
     * @param h 图像一格的高度
     * @see Actor
     */
    
    public Friend(GameWorld game, Image img, int x, int y, int w, int h) {
        super(game, img, x, y, w, h);
        setFace(G_DOWN);
        HP = 10000000;
    }
    /**
     * 覆盖collision
     * @see NPC#collision
     */
    
    public boolean collision(AbstractObject obj, int direction){
        boolean b = super.collision(obj, direction);
        return b;
    } 
    
}
