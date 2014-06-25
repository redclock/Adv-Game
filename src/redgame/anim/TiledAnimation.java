package redgame.anim;
/*
 * TiledAnimtion.java 作者：姚春晖
 */
import java.awt.*;
import java.awt.image.*;

import redgame.engine.*;
/**
 * TiledAnimation类是管理和显示平铺动画的类.
 * 当物体比图片大时, 画面平铺显示
 * @author 姚春晖
 */

public class TiledAnimation extends Animation {
    private int m_frame;
    /**
     * 构造TiledAnimtion
     * @param game 游戏类的引用
     * @param img 图像
     * @param w 物体宽
     * @param h 物体高
     * @param tilew 图像一格的宽度
     * @param tileh 图像一格的高度
     */
    public TiledAnimation(GameWorld game, Image img, int w, 
							int h, int tilew, int tileh){
		super(game, null, w, h);
        int imgw = img.getWidth(m_game.getPanel());
        int imgh = img.getHeight(m_game.getPanel());
      
        m_frame = imgw / tilew;
		BufferedImage bi = new BufferedImage(w, h * m_frame, BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();
        
		for (int f = 0; f < m_frame; f++){
    		big.setClip(0, f * h, w, f * h + h);
    		for (int i = 0; i < w + tilew; i += tilew){
    			for (int j = 0; j< h + tileh; j += tileh){
    			 
    				if (j == 0 || (imgh <= tileh))
                        big.drawImage(img, i, h * f + j, i + tilew, h * f + j + tileh, 
                                    tilew * f , 0, tilew * f + tilew, tileh, m_game.getPanel());
    				else				
                        big.drawImage(img, i, h * f + j, i + tilew, h * f + j + tileh, 
                                    tilew * f , tileh, tilew * f + tilew, 2 * tileh, m_game.getPanel());
    			}
    		}
    	}	
		m_img = bi;
		setClips();
        setRange(0, m_frame - 1, 100);
		if (m_frame > 1){
		    //System.out.println("frame = "+m_frame);
		    start();
		}  
//		m_nx = 1;
//		m_ny = 1;
	}
	
//	public void paint(Graphics g, int x, int y){
//		
//		Graphics2D g2 = (Graphics2D)g; 
//		g2.drawImage(m_img, x, y, m_game.getPanel());
//	}

}
