package redgame.util;

import java.awt.*;
import java.awt.event.*;

import redgame.engine.*;

public abstract class GameMouse implements MouseListener, MouseMotionListener{
	protected int x;
	protected int y;
    protected GameWorld m_game;
    final public static int MAX_BUTTON = 4;
    private boolean[] m_lastButtons = new boolean[MAX_BUTTON];
	private boolean[] m_curButtons = new boolean[MAX_BUTTON];
	private boolean m_isMoved = false;
	private int lastx, lasty;
	
	public GameMouse(GameWorld game) {
		m_game = game;
	}
	
	public void reset() {
		m_lastButtons = new boolean[MAX_BUTTON];
		m_curButtons = new boolean[MAX_BUTTON];
	}
	
	public abstract void paint(Graphics g);

	public  void mouseMoved(MouseEvent evt){
		Rectangle sc = m_game.getScreenArea();
		lastx = x;
		lasty = y;
		if (sc == null) return;
		x = evt.getX();
		y = evt.getY();
		if (x < 0) x = 0;
		if (x < - sc.x) x = -sc.x;
		if (sc.x < 0 && x >= sc.width - sc.x) x = sc.width - sc.x - 1;
		else if (sc.x >= 0 && x >= sc.width) x = sc.width - 1;
		if (y < 0) y = 0;
		if (y < - sc.y) y = -sc.y;
		if (sc.y < 0 && y >= sc.height - sc.y) y = sc.height - sc.y - 1;
		else if (sc.y >= 0 && y >= sc.height) y = sc.height - 1;
	}
	
	public void mousePressed(MouseEvent evt){
		int bt = evt.getButton();
		if (bt < MAX_BUTTON){
			m_lastButtons[bt] = m_curButtons[bt];
			m_curButtons[bt] = true;
		}
	}
	
	public  void mouseReleased(MouseEvent evt){
		int bt = evt.getButton();
		if (bt < MAX_BUTTON){
			m_lastButtons[bt] = m_curButtons[bt];
			m_curButtons[bt] = false;
		}
	}
	
	public void mouseClicked(MouseEvent evt) {
		
	}
	
	public void setPosition(int x, int y) {
		Rectangle sc = m_game.getScreenArea();
		if (sc == null) return;
		x = x - sc.x;
		y = y - sc.y;
		this.x = x;
		this.y = y;
		Point p = m_game.getPanel().getLocationOnScreen();
		GameLib.setMousePos(p.x + x, p.y + y);
	}
	
	public void update(long passedTime){ 
		if (Math.abs(lastx - x) > 0 || Math.abs(lasty - y) > 0) {
			m_isMoved = true;
		}else {
			m_isMoved = false;
		}
		lastx = x;
		lasty = y;
	}

	public void mouseEntered(MouseEvent evt) {

	}

	public void mouseExited(MouseEvent evt) {

	}

	public void mouseDragged(MouseEvent evt) {
		mouseMoved(evt);
	}
	
	public int getX() {
		Rectangle sc = m_game.getScreenArea();
		if (sc == null) {
			return x;
		}else{
			return x + sc.x;
		}
	}
	
	public int getY() {
		Rectangle sc = m_game.getScreenArea();
		if (sc == null) {
			return y;
		}else{
			return y + sc.y;
		}
	}
	
	public boolean isMoved() {
		return m_isMoved;
	}
	
	public boolean isButtonDown(int bt) {
		if (bt < MAX_BUTTON) {
			return m_curButtons[bt];
		}else {
			return false;
		}
	}

	public boolean isButtonJustDown(int bt) {
		if (bt < MAX_BUTTON) {
			boolean r = m_curButtons[bt] && !m_lastButtons[bt];
			if (r) m_lastButtons[bt] = m_curButtons[bt];
			return r;
		}else {
			return false;
		}
	}
}
