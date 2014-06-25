package redgame.util;

import redgame.scripts.AbstractScript;
import redgame.engine.*;
import redgame.obj.*;

public class GameSaveLoad {
    final private static String SAVEDIR = "save/";
    final private static String FILEDIR = ".sav";
    final private static String FILEVARSDIR = ".var";

    private GameWorld m_game;
    private String m_name;
    
    public boolean loadGame(MyIO io, GameConfig cfg){
        if ( cfg.load(m_name + FILEDIR, io) == false ) {
        	return false;
        }
        GameConfig vcfg = new GameConfig();
        if ( vcfg.load(m_name + FILEVARSDIR, io) == false ) {
        	return false;
        }
        AbstractScript.clearVars();
        String[] keys = vcfg.getKeys();
        for (int i = 0; i < keys.length; i++){
            AbstractScript.setVar(keys[i], vcfg.getString(keys[i]));
        }
        
        return true;
    }
    
    public boolean saveGame(String map){
        Player p = m_game.getPlayer();
        GameConfig cfg = new GameConfig();
        cfg.put("tag", "GameSaveFile");
        cfg.put("x", (int)p.getX());
        cfg.put("y", (int)p.getY());
        cfg.put("name", p.getName());
        cfg.put("map", map);
        cfg.put("hp", p.HP);
        cfg.put("face", p.getFace());
        cfg.put("playtime", (int)m_game.getPlayTime());
        cfg.put("cansword", p.items[Player.ITEM_SWORD] != null);
        cfg.put("canshoot", p.items[Player.ITEM_RING] != null);
        cfg.put("canbomb", p.items[Player.ITEM_BOMB] != null);
        if (p.canShoot())
            cfg.put("ring.num", p.items[Player.ITEM_RING].getCount());
        if (p.canBomb())
            cfg.put("bomb.num", p.items[Player.ITEM_BOMB].getCount());
        if (p.curItem == p.items[Player.ITEM_SWORD])
            cfg.put("curitem", "sword");    
        else if (p.curItem == p.items[Player.ITEM_RING])
            cfg.put("curitem", "ring");    
        else if (p.curItem == p.items[Player.ITEM_BOMB])
            cfg.put("curitem", "bomb");    
        
        for (int i = 0; i < 256; i++){
           	if (p.hasKey[i])
                cfg.put("key" + i, p.hasKey[i]);
            if (p.openedDoor[i])    
                cfg.put("door" + i, p.openedDoor[i]);
        }
        if (cfg.save(m_name + FILEDIR) == false) return false;
        cfg.reset();
        java.util.Map<String, String> vars = AbstractScript.getVars();
        Object[] keys = ( vars.keySet().toArray());
        for (int i = 0; i < keys.length; i++){
            cfg.put((String)keys[i], vars.get(keys[i]));
        }
        return cfg.save(m_name + FILEVARSDIR);

    }
    public GameSaveLoad(GameWorld game, String name){
        m_game = game;
        m_name = SAVEDIR + name;
    }
    
    
}
