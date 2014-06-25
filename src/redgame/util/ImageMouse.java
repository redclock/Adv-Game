package redgame.util;

import java.awt.*;
import redgame.engine.*;

public class ImageMouse extends GameMouse {
	private Image m_cursor; 
	protected int m_w, m_h;
	protected int hotx, hoty;
	public ImageMouse(GameWorld game, Image cursor, int x, int y) {
		super(game);
		m_cursor = cursor;
		hotx = x;
		hoty = y;
		if (cursor != null) {
			m_w = cursor.getWidth(game.getPanel());
			m_h = cursor.getHeight(game.getPanel());
		}
	}

	public void paint(Graphics g) {
		if (m_cursor != null) {
			g.drawImage(m_cursor, getX() - hotx, getY() - hoty, m_game.getPanel());
		}
	}

}
