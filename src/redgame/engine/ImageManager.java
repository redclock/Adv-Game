package redgame.engine;
/*
 * ImageManager.java 作者：姚春晖
 */
import java.awt.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 * ImageManager类是图像管理器
 * 它读入图像文件, 并可以防止重复读入
 * @author 姚春晖
 */

public class ImageManager{
    private Vector<ImageItem> m_images = new Vector<ImageItem>();
	private GameWorld m_game;
	
	private Image find(String filename){
		for(int i = 0; i < m_images.size(); i++)
			if (m_images.get(i).equalName(filename)){
				return m_images.get(i).getImage();
			}
		return null;	
	}
	/**
	 * 从内存中删除图像
	 */
	public void delete(Image img){
        for(int i = 0; i < m_images.size(); i++)
            if (m_images.get(i).getImage() == img){
                m_images.remove(i);
                return;
            }
	}
	
	public ImageManager(GameWorld game){
		m_game = game;
	}
	
	/**
	 * 装载图像, 如果已经装载过, 只将引用传回
     * 使用Toolkit.getImage()
	 * @param filename 文件名
	 * @return 读到的图像
	 */
	public Image loadImage(String filename){
		Image img = find(filename);
		//如果已经装载过
		if (img != null) return img;
		img = m_game.getIO().loadImage(filename);
		MediaTracker tracker= new MediaTracker(m_game.getPanel());
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		}
		catch (InterruptedException e){
			e.printStackTrace();
			return null;
		}
		m_images.add(new ImageItem(filename, img));
		return img;
	}

    /**
     * 装载图像, 如果已经装载过, 只将引用传回
     * 返回BufferedImag
     * 使用ImageIO
     * @param filename 文件名
     * @return 读到的图像
     */
    public BufferedImage loadBufferedImage(String filename){
        Image img = find(filename);
        //如果已经装载过
        if (img != null && img instanceof BufferedImage) 
            return (BufferedImage)img;
        try{
            
            BufferedInputStream stream = 
                    new BufferedInputStream(m_game.getIO().getInput(filename));
            BufferedImage bi = ImageIO.read(stream);
            stream.close();
            m_images.add(new ImageItem(filename, bi));
            return bi;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 装载图像,创建优化的兼容图像
     * 
     * 返回BufferedImag
     * 使用ImageIO
     * @param filename 文件名
     * @param transparency 值为
     *        Transparency.OPAQUE, Transparency.BITMASK, Transparency.TRANSLUCENT
     * @param size 若不为null,则强制缩放为size
     * @return 读到的图像
     */
    public Image loadAutomaticImage(
        String filename, 
        int transparency, 
        Dimension size){
            BufferedImage bi = loadBufferedImage(filename);
            if (bi == null) return null;
            GraphicsConfiguration gc = m_game.getPanel().getGraphicsConfiguration();
            if (gc == null) return bi;
            int w, h;
            if ( size == null ){
                w = bi.getWidth();
                h = bi.getHeight();
            }else{
                w = size.width;
                h = size.height;
            }
            BufferedImage abi = gc.createCompatibleImage(w, h, transparency);
            Graphics g = abi.getGraphics();
            if ( size == null ){
                g.drawImage(bi, 0, 0, m_game.getPanel());
            }else{
                g.drawImage(bi, 0, 0, w, h, m_game.getPanel());
            }
            g.dispose();
            bi.flush();
            return abi;
    }

    public Image createImage(ImageProducer ip){
        Image img = Toolkit.getDefaultToolkit().createImage(ip);
        
        MediaTracker tracker= new MediaTracker(m_game.getPanel());
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        }
        catch (InterruptedException e){
            e.printStackTrace();
            return null;
        }
        return img;
    }
   
    //图像列表的单位
	private class ImageItem{
		//图像
		private Image m_image;
		//文件名
		private String m_name;
		public ImageItem(String name, Image img){
			m_image = img;
			m_name = name;
		}
		public Image getImage(){
			return m_image;
		}
		public boolean equalName(String s){
			return m_name.equals(s);
		}
	}
}