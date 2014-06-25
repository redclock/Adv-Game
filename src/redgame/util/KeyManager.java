package redgame.util;
/*
 * KeyManager.java 作者：姚春晖
 */

import java.awt.event.*;

/**
 * KeyManager类是图像管理器
 * 它管理按键信息, 其来源是窗体的键盘事件
 * @author 姚春晖
 */

public class KeyManager implements KeyListener{
    private static KeyManager m_default = new KeyManager();
    
    public static KeyManager getDefault() {
    	return m_default;
    }
    
	public void keyPressed(KeyEvent evt){
       setKeyState(evt.getKeyCode(), true);
    }
    public void keyReleased(KeyEvent evt){
        setKeyState(evt.getKeyCode(), false);
    }
    public void keyTyped(KeyEvent evt){
        addText(evt.getKeyChar());
    }

    //上一次的信息
	private static boolean m_lastKeys[] = new boolean[256];
    //这一次的信息
    private static boolean m_currKeys[] = new boolean[256];
	
	/**
	 * 设置某个键的状态
	 * @param key 键ID
	 * @param down 是按下还是抬起
	 */
	public synchronized static void setKeyState(int key, boolean down) {
		if (key < 256){
			m_lastKeys[key] = m_currKeys[key];
			m_currKeys[key] = down;
		}
	}
    /**
     * 清空所有键的状态
     */
    public synchronized static void clearKeyState() {
		for (int key = 0; key < 256; key ++){
			m_lastKeys[key] = false;
			m_currKeys[key] = false;
		}
	}
	/**
	 * 是否上一次未抬起而这一次为按下
	 */
	public synchronized static boolean isKeyJustDown(int key) {
		if (key < 256){
			boolean r = m_lastKeys[key] == false &&
					m_currKeys[key] == true;
			if (r) m_lastKeys[key] = m_currKeys[key];
			return r;
		}
		return false;
	}
    /**
     * 是否这一次为按下
     */
    public synchronized static boolean isKeyDown(int key) {
		if (key < 256){
			return m_currKeys[key];
		}
		return false;
	}	
    /**
     * 是否上一次按下未而这一次为抬起
     */
    public synchronized static boolean isKeyJustUp(int key) {
		if (key < 256){
			boolean r = m_lastKeys[key] == true &&
					m_currKeys[key] == false;
			if (r) m_lastKeys[key] = m_currKeys[key];
			return r;
		}
		return false;
	}
	
	private static String received = "";
    private static boolean receiving = false;
    /**
	 * 开始接受键盘输入字符
	 */
	public static void startReceive(){
	   received = "";
	   receiving = true;
	}
    /**
     * 接受键盘输入字符
     */
    public static void addText(char c){
       if ( receiving ) received += c;
    }
    /**
     * 结束键盘输入字符
     */
    public static void endReceive(){
       receiving = false;
    }
    /**
     * 取得键盘输入字符,并清空缓冲
     */
    public static String getReceived(){
       String r = received;
       received = "";
       return r;
    }
}
