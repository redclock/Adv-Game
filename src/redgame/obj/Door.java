package redgame.obj;
/*
 * Door.java 作者：姚春晖
 */
import java.awt.*;
import redgame.scripts.AbstractScript;
import redgame.engine.*;
/**
 * Door类是静止物体门.
 * @author 姚春晖
 */
public class Door extends StaticObject {
	//是否打开
	private boolean m_open;
	public String destMap;
	public int destX, destY;
	/**
	 * 对应钥匙的id
	 */
	public int id; 
    /**
     * 构造Door
     * @param game 游戏类的引用
     * @param img 物体图像
     * @param x 物体位置横坐标
     * @param y 物体位置纵坐标
     * @param w 元素宽度
     * @param h 元素高度
     */
    public Door(GameWorld game, Image img, 
					int x, int y, int w, int h, int id) {
		super(game, img, x, y, w, h, 0, 0);
		//静止
		m_anim.setLooped(false);
		m_open = false;
		this.id = id;
		Player p = game.getPlayer();
		//如果以前就开过门
		if (p != null){
			if (p.openedDoor[id]) open();
		}
	}
	/**
	 * 开门
	 */
	public void open(){
		if (!m_open){
			m_anim.setRange(0, 3, 100);
			m_anim.start();
			m_open = true;
		}
	}

    /**
     * 关门
     */
    public void close(){
        if (m_open){
            m_anim.setRange(0, 3, 100);
            m_anim.resetToStart();
            m_anim.stop();
            m_open = false;
        }
    }

	/**
	 * 是否打开了
	 */
	public boolean isOpen(){
		return m_open;
	}
	/**
	 * 进入门
	 */
	public void enter(Player p){
        p.setVisible(false);
        if ("_save_door".equals(m_name)){
            m_game.gotoMap(AbstractScript.getStrVar("_save_out_map"),
                           AbstractScript.getIntVar("_save_out_x"),
                           AbstractScript.getIntVar("_save_out_y"));
            
        }else{
            m_game.gotoMap(destMap, destX, destY);
        }
        if ( m_script != null  ){
            m_game.getScript().add(m_script, 
                        new ScriptSource(this, p, G_UP));
        }
        
	}	
}
