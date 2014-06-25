package redgame.util;
import java.awt.*;
public class GameLib {
    private static GraphicsDevice gd;
    private static Frame m_win;
    private static Robot m_robot;
    
    private static void createRobot() {
    	try{
	    	if (gd == null) {
	    		m_robot = new Robot();
	    	}else {
	    		m_robot = new Robot(gd);
	    	}
    	}catch (AWTException ex) {
    		ex.printStackTrace();
    	}
    }
    
    public static GraphicsDevice getGraphicsDevice() {
    	return gd;
    }
    /**
     * 初始化
     * @param win 游戏窗口
     */
    public static void Init(Frame win){
        if (win == null) return;
        GraphicsConfiguration gc = win.getGraphicsConfiguration();
        gd = gc.getDevice();
        m_win = win;
    }
    /**
     * 切换窗口与全屏幕
     */
    public static void setFullScreen(boolean full){
        if (gd == null) return;
        System.out.println("Set Full Screen: " + full);
        if (full){
//            m_win.setVisible(false);
//            m_win.setdvalidate();
//            m_win.setUndecorated(true);
//            m_win.setVisible(true);
            gd.setFullScreenWindow( m_win );
            m_win.validate();
            m_win.repaint();
        }else{
//            m_win.setVisible(false);
//            m_win.setUndecorated(false);
//            m_win.setVisible(true);
            gd.setFullScreenWindow( null );
            m_win.validate();
            m_win.repaint();
        }
    }
    /**
     * 返回当前是否是全屏幕
     */
    public static boolean isFullScreen(Window win){
        if (gd == null) return false;
        return gd.getFullScreenWindow() == win;
    }
    /**
     * 确定显示模式是否被支持
     */
    public static boolean displayModeSupported(int width, int height, int bitDepth){
        if (gd == null) return false;
        DisplayMode[] modes = gd.getDisplayModes();
        for (int i = 0; i < modes.length; i++){
            if (width == modes[i].getWidth() &&
                height == modes[i].getHeight() &&
                bitDepth == modes[i].getBitDepth()){
                    return true;
                }
        }
        return false;        
    } 
    /**
     * 设置显示模式
     */
    public static boolean setMode(int width, int height, int bitDepth){
        if (gd == null) return false;
        gd.setDisplayMode(new DisplayMode(width, height, bitDepth, 0));
        return true;
    }

    public static DisplayMode getDisplayMode() {
        if (gd == null) return null;
        return gd.getDisplayMode();
    }    

    public static void waitTime(long time){
        long ot = System.currentTimeMillis();
        long t;
        do{
            t = System.currentTimeMillis();
            try{
            	Thread.sleep(10);
            }catch(Exception ex){
            	ex.printStackTrace();
            }
        }while ( t < time + ot );
    }
    
    public static void setMousePos(int x, int y) {
    	if (m_robot == null) createRobot();
    	if (m_robot != null) m_robot.mouseMove(x, y); 
    }
}
