package redgame.util;

/**
 * BoxImg.java
 */

import java.awt.*;
import java.awt.image.*;

/**
 * BoxImg 对话框，面板的边框,它具有飞入效果
 * @author 姚春晖
 *
 */
public class BoxImg{
    //保存图像
	private BufferedImage img;
    /**
     * 初始位置
     */
	public int bx, by;
    /**
     * 当前位置
     */
	public int x, y;
	/**
	 * 目标位置
	 */
    public int dx, dy;
    /**
     * 大小
     */
    public int bw, bh;
    private int stop = 5, sleft = 5, sbottom = 5, sright = 5;
    
    //初始化图片
    private BufferedImage createBox(int w, int h){
        BufferedImage box = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) box.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(50, 50, 50, 150));
        g2d.fillRoundRect(sleft, stop, w - sleft - sright, h - stop - sbottom, 10, 10);
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawRoundRect(sleft, stop, w - sleft - sright, h - stop - sbottom, 10, 10);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.dispose();
        return box;
    } 
    
    /**
     * 
     * @param x 位置
     * @param y 位置
     * @param w 大小
     * @param h 大小
     * @param destx 目标位置
     * @param desty 目标位置
     */
    public BoxImg(int x, int y, int w, int h, int destx, int desty){
        bx = x;
        by = y;
        this.x = x;
        this.y = y;
        bw = w;
        bh = h;
        dx = destx;
        dy = desty;
        img = createBox(w, h);

    }
    
    /**
     * 画在屏幕上
     * @param g 图形设备
     * @param obs ImageObserver 
     */
    public void paint(Graphics g, ImageObserver obs){
        g.drawImage(img, (int)x, (int)y, obs);
    }
    
    /**
     * 飞入 移动
     * @param delta 增量(0~1 1为直接从初始到目标)
     * @return 是否移动完成
     */
    public boolean move(float delta){
        x += (dx - bx) * delta;
        y += (dy - by) * delta;
        boolean finished = true;
        //正向移动
        if (delta > 0){
            if ((dx - bx) * (dx - x) <= 0 ) x = dx;
            else finished = false;
            if ((dy - by) * (dy  - y) <= 0 ) y = dy;
            else finished = false;
        }else {
        	//反向移动
            if ((bx - dx) * (bx - x) <= 0 ) x = bx;
            else finished = false;
            if ((by - dy) * (by - y) <= 0 ) y = by;
            else finished = false;
        }
        return finished;
    }
    
    
} 

