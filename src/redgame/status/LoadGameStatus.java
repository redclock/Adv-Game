package redgame.status;
/*
 * LoadGameStatus.java 作者：姚春晖
 */
import redgame.scripts.*; 
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import redgame.engine.*;
import redgame.ui.*;
import redgame.util.*;
import redgame.obj.*;
import redgame.anim.*;
import redgame.item.*;

/**
 * LoadGameStatus类是游戏中显示载入游戏的游戏状态.
 * 在这时会显示一个输入框,供玩家选择
 * @see AbstractStatus
 * @author 姚春晖
 */
public class LoadGameStatus extends AbstractStatus{

    private final int FLYIN = 0;
    private final int SHOW = 1;
    private final int FLYOUT = 2;

    private int state;
    
    private BoxImg m_box; 
    private BoxImg m_box_top; 
    
    private Animation m_arrow_down;
    private Animation m_arrow_up;

    private Font font = new Font("宋体", Font.PLAIN, 16);

    private ScrollMenu m_menu;
    
    private String []files;
    private String[] getFileList(){
        File f = new File("save");
        File[] fs = f.listFiles(
            new FileFilter(){
                public boolean accept(File pathname){
                   // System.out.println(pathname);
                    if (!pathname.isFile()) return false;
                    
                    String name = pathname.getName();
                    int i = name.lastIndexOf('.');
                    if (i < 0) return false;
                    String ext = name.substring(i + 1, name.length());
                    if (ext.equalsIgnoreCase("sav")) return true;
                    return false;
                }
            }
            );
        String[] r = new String[fs.length];
        for (int i = 0; i < fs.length; i++){
            String s = fs[i].getName();
            s = s.substring(0, s.length() - 4);
            r[fs.length - i - 1] = s;
        }
        return r;
    }
    
    public LoadGameStatus(GameWorld game){
        super(game);
        Rectangle sc = m_game.getScreenArea();
        int x = sc.x;
        int y = sc.y;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int w = sc.width;
        int h = sc.height;
        
        state = FLYIN;

        int destx = x + (w - 300) / 2;
        int desty = y + (h - 200) / 2;
        m_box = new BoxImg(x-300, desty, 300, 200, destx, desty);
        m_box_top = new BoxImg(destx, -50, 300, 50, destx, desty - 50);

        files = getFileList();
        m_menu = new ScrollMenu(game, files, 7, 240, 20);
        
        m_arrow_down = new Animation(m_game, m_game.loadImage("image/arrow.png"), 32, 32);
        m_arrow_down.setRange(0, 2, 200);
        m_arrow_down.start();
        m_arrow_up = new Animation(m_game, m_game.loadImage("image/arrow.png"), 32, 32);
        m_arrow_up.setRange(3, 5, 200);
        m_arrow_up.start();
        m_game.playSound("sound/interface.wav");
    }
    public int move(long passedTime){
        m_menu.update(passedTime);
        m_arrow_down.update(passedTime);
        m_arrow_up.update(passedTime);
        return 0;
    }
    
    public boolean load(String name){
        GameSaveLoad gsl = new GameSaveLoad(m_game, name);
        GameConfig cfg = new GameConfig();
        gsl.loadGame(m_game.getIO(), cfg);
        if (cfg.getString("tag") == null) return false;
        if (! "GameSaveFile".equals(cfg.getString("tag"))) return false;

        Player p = m_game.getPlayer();
        p.setName(cfg.getString("name"));
        p.HP = cfg.getInt("hp", 12);
        p.setFace(cfg.getInt("face", 0));
        p.canControl = true;
        m_game.setPlayTime(cfg.getInt("playtime", 0));
        if ( cfg.getBoolean("cansword", true) ){
            p.items[Player.ITEM_SWORD] = new SwordItem(m_game);
        }else{
            p.items[Player.ITEM_SWORD] = null; 
        }
        if ( cfg.getBoolean("canshoot", true) ){
            p.items[Player.ITEM_RING] = new RingItem(m_game);
            p.items[Player.ITEM_RING].setCount(cfg.getInt("ring.num", 0));
        }else {
            p.items[Player.ITEM_RING] = null;
        }
        if ( cfg.getBoolean("canbomb", true) ){
            p.items[Player.ITEM_BOMB] = new BombItem(m_game);
            p.items[Player.ITEM_BOMB].setCount(cfg.getInt("bomb.num", 0));
        }else {
            p.items[Player.ITEM_BOMB] = null;
        }
        String cur = cfg.getString("curitem", "");
        if (cur.equals("sword")) {
            p.curItem = p.items[Player.ITEM_SWORD];
        }else if (cur.equals("ring")) {
            p.curItem = p.items[Player.ITEM_RING];
        }else if (cur.equals("bomb")) {
            p.curItem = p.items[Player.ITEM_BOMB];
        }else {
            p.curItem = null;
        }

        for (int i = 0; i < 256; i++){
            p.hasKey[i] = cfg.getBoolean("key"+i, false);
            p.openedDoor[i] = cfg.getBoolean("door"+i, false);
        }
        
        m_game.gotoMap( cfg.getString("map"), 
                        cfg.getInt("x", 0), 
                        cfg.getInt("y", 0)
                      );
        return true;
    }    
     
    public int update(long passedTime){
        m_counter ++;
        switch(state){
            case FLYIN:
                if (m_box.move((float)passedTime * 0.004f))
                	state = SHOW;
                m_box_top.move((float)passedTime * 0.004f);
                 
                break;
            case SHOW:    
                if (m_menu.done()){
                    state = FLYOUT;
                    m_game.playSound("sound/interface.wav");
                    if (files.length > 0)
                        load(files[m_menu.getIndex()]);
                }
                if ( KeyManager.isKeyJustDown(KeyEvent.VK_ESCAPE)){
                      state = FLYOUT;
                      m_game.playSound("sound/interface.wav");
                }
                break;
            case FLYOUT:    
                
            	if ( m_box.move(-(float)passedTime * 0.004f) )
            		return -1;
                m_box_top.move(-(float)passedTime * 0.004f);
                break;
        }

//        switch((int)m_counter){
//        case 1: 
//            m_game.pushStatus(new InputStatus(m_game, m_x, m_y, "输入要装载的名称"));
//            break;
//        case 2:
//            if (! load(InputStatus.result)) return -1;
//            break;
//        }
        return 0;
    }
    
    public int draw(long passedTime, Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        m_box.paint(g, m_game.getPanel());
        m_box_top.paint(g, m_game.getPanel());
        g2d.setFont(font);
        g2d.setColor(Color.YELLOW);                 
        g2d.drawString("请选择文件名", (int)m_box_top.x + 100, (int)m_box_top.y + 30);
        m_menu.paint(g2d, (int)m_box.x + 20, (int)m_box.y + 30);
        if (m_menu.canScrollDown()){
        	m_arrow_down.paint(g, (int) m_box.x + m_box.bw - 40, 
        					(int) m_box.y + m_box.bh - 50);  
        } 
        if ( m_menu.canScrollUp() ) {	
        	m_arrow_up.paint(g, (int) m_box.x + m_box.bw - 40, 
					(int) m_box.y + m_box.bh - 80);   
        }

        return 0;
    }    
        
}