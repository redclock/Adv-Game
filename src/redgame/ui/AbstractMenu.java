package redgame.ui;

import java.awt.*;
import java.awt.event.*;
import redgame.engine.*;
import redgame.util.*;

public abstract class AbstractMenu {
    protected String[] m_items;
    protected int m_index;
    protected boolean m_done = false;
    protected Rectangle[] item_bounds = null;
    protected GameWorld m_game;
 
    public AbstractMenu(GameWorld game){
        m_game = game;
    }
    
    public AbstractMenu(GameWorld game, String [] items){
        m_game = game;
    	m_items = items;
    }
    
    public void setIndex(int index){
        m_index = index;
    }
    
    public int getIndex(){
        return m_index;
    }
    
    public abstract void paint(Graphics2D g2d, int x, int y);
    
    public abstract void move(long passedTime);
    
    protected void keyInput() {
        if (KeyManager.isKeyJustDown(KeyEvent.VK_UP)){
            goUp();
        }else if (KeyManager.isKeyJustDown(KeyEvent.VK_DOWN)){
            goDown();
        }else if (KeyManager.isKeyJustDown(KeyEvent.VK_LEFT)){
            goLeft();
        }else if (KeyManager.isKeyJustDown(KeyEvent.VK_RIGHT)){
            goRight();
        }else if (m_items.length > 0 &&
                  KeyManager.isKeyJustDown(KeyEvent.VK_SPACE)||
                  KeyManager.isKeyJustDown(KeyEvent.VK_ENTER)){
            m_done = true;
        }
    }

    protected void mouseInput() {
        if (item_bounds != null && m_game.getMouse().isMoved()) {
        	//System.out.println("sss");
        	for (int i = 0; i < m_items.length; i ++) {
        		
        		if (item_bounds[i].contains(
        				m_game.getMouse().getX(),
        				m_game.getMouse().getY())
        				){
        			m_index = i; 
        			break;
        		}
        	}
        }
    }

    public void update(long passedTime){
    	keyInput();
    	mouseInput();
    }

    public void goLeft(){
        m_index --;
        if (m_index < 0) m_index = m_items.length - 1;
    }
    
    public void goRight(){
        m_index ++;
        if (m_index >= m_items.length) m_index = 0;
    }

    public void goUp(){
        m_index --;
        if (m_index < 0) m_index = m_items.length - 1;
    }

    public void goDown(){
        m_index ++;
        if (m_index >= m_items.length) m_index = 0;
    }
    
    public boolean done(){
        return m_done;
    }
}
