package redgame.engine;
/*
 * MapFileReader.java 作者：姚春晖
 */
 
import java.io.*;
import java.awt.*;
import redgame.obj.*;

/**
 * MapFileReader类是读地图文件生成地图的类
 * @author 姚春晖
 */

public class MapFileReader {
    String name;
	Image img = null;
	int w = 0, h = 0;
	int x = 0, y = 0;
	int sleft = 5, sright = 5, sup = 2, sdown = 2;
	int tilew = 0, tileh = 0;
	int begin = 0, end = 0;
	int id = 0;
	int block;
	int face;
	int HP;
	int delay;
	int kind;
	String destMap;
	int destX, destY;
	boolean bad;
	boolean open;
    boolean canDamage;
    String script;
    String ability;
    
	private BufferedReader m_reader;
	private GameWorld m_game;
	public MapFileReader(){
		
	}
	//工具函数: 从文件中读出用空格隔开的一段文字
	private String getItem() throws IOException{
		String r = "";
		int c;
		do{
			c = m_reader.read();
		}while(c <= ' ' && c >=0);
		
		while (c > ' '){
			r += String.valueOf((char)c);
			c = m_reader.read();
		}
		return r;
		
	}
	//读入一个段
	private void readSection() throws IOException{
	
		String s;
		bad = false;
		open = false;
		block = 0;
		name = null;
		script = null;
		face = -1;
		HP = -1;
		delay = -1;
		kind = 0;
		ability = "";
		canDamage = false;
		do{
			s = getItem().toUpperCase();
			if (s.equals("POSITION")){
				x = Integer.parseInt(getItem());
				y = Integer.parseInt(getItem());
            }else if (s.equals("NAME")){
                name = getItem();
            }else if (s.equals("SIZE")){
				w = Integer.parseInt(getItem());
				h =	Integer.parseInt(getItem());
            }else if (s.equals("HP")){
                HP = Integer.parseInt(getItem());
            }else if (s.equals("ID")){
                id = Integer.parseInt(getItem());
            }else if (s.equals("FACE")){
                face = Integer.parseInt(getItem());
            }else if (s.equals("DELAY")){
                delay = Integer.parseInt(getItem());
            }else if (s.equals("IMAGE")){
				img = m_game.loadImage(getItem());
			}else if (s.equals("TILESIZE")){
				tilew = Integer.parseInt(getItem());
				tileh =	Integer.parseInt(getItem());
			}else if (s.equals("SKIP")){
				sleft = Integer.parseInt(getItem());
				sright = Integer.parseInt(getItem());
				sup = Integer.parseInt(getItem());
				sdown =	Integer.parseInt(getItem());
            }else if (s.equals("BAD")){
                bad = true;
            }else if (s.equals("DAMAGE")){
                canDamage = true;
            }else if (s.equals("OPEN")){
                open = true;
            }else if (s.equals("BLOCK")){
                block = 1;
            }else if (s.equals("UNBLOCK")){
                block = 2;
            }else if (s.equals("DESTINATION")){
                destMap = getItem();
                destX = Integer.parseInt(getItem());
                destY = Integer.parseInt(getItem());
            }else if (s.equals("KIND")){
                String k = getItem();
                if (k.equalsIgnoreCase("HEAL"))
                    kind = Bonus.HEAL;
                else if (k.equalsIgnoreCase("HEAL2"))
                    kind = Bonus.HEAL2;   
                else if (k.equalsIgnoreCase("HEAL4"))
                    kind = Bonus.HEAL4;   
                else if (k.equalsIgnoreCase("CORDIAL"))
                    kind = Bonus.CORDIAL;   
                else if (k.equalsIgnoreCase("NOHARM"))
                    kind = Bonus.NOHARM;   
                else if (k.equalsIgnoreCase("RING"))
                    kind = Bonus.RING;   
            }else if (s.equals("RANGE")){
                begin = Integer.parseInt(getItem());
				end =   Integer.parseInt(getItem());
            }else if (s.equals("SCRIPT")){
                script = getItem();
            }else if (s.equals("ABILITY")){
                ability = getItem();
            }else if (s.equals("END")){
				s = "";
			}
        }while (!s.equals(""));
	}
	//设置公共的参数
	private void setParams(AbstractObject r){
        r.setBlockRect(new Rectangle(sleft, sup, 
                            w - sleft - sright, h - sdown - sup));
        if (block == 1){
            r.setBlocked(true);            
        }else if (block == 2){
            r.setBlocked(false);
        }
        if (delay > 0) r.setDelay(delay);
        r.setName(name);   
        r.setScript(script);                        
    }
	
    //生成Patrol的信息
    private Patrol readPatrol() throws IOException{
		readSection();
		Patrol r = new Patrol(m_game, img, x, y, w, h, begin, end);
        setParams(r);
        
        r.setFace(face);
        if ( ability.indexOf('f') >=0 ) r.setFollow(true);
        if ( ability.indexOf('s') >=0 ) r.setCanSword(true);
        if ( ability.indexOf('j') >=0 ) r.setCanJump(true);
        if ( ability.indexOf('g') >=0 ) r.setGravity(false);
        if ( ability.indexOf('k') >=0 ) r.setHarmful(false);
        if ( ability.indexOf('m') >=0 ) r.setStill(true);
        if (HP > 0) r.HP = HP;
        if (r.HP > 10) {
            r.setBonus(Bonus.getStandardBonus(m_game, 0, 0, Bonus.HEAL4));
        }else if (r.HP > 4) {
            if (m_game.getRandom (3) == 0){
                r.setBonus(Bonus.getStandardBonus(m_game, 0, 0, Bonus.HEAL2));
            }else {
                r.setBonus(Bonus.getStandardBonus(m_game, 0, 0, Bonus.HEAL));
            }
        }else if (r.HP > 2) {
            if (r.getCanSword() || m_game.getRandom (3) == 0){
                r.setBonus(Bonus.getStandardBonus(m_game, 0, 0, Bonus.HEAL));
            }else {
                r.setBonus(null);
            }
            
        }
        return r;
	}

    //生成Friend的信息
    private Friend readFriend() throws IOException{
        readSection();
        Friend r = new Friend(m_game, img, x, y, w, h);
        setParams(r);
        r.setFace(face);
        if (HP > 0) r.HP = HP;
        return r;
    }

    //生成Wall的信息
    private MapObject readWall() throws IOException{
		readSection();
		Wall r = new Wall(m_game, img, x, y, w, h, tilew, tileh, bad);
        r.setDamagable(canDamage);
        setParams(r);
        return r;
	}

    //生成Spring的信息

    private MapObject readSpring() throws IOException{
        readSection();
        Spring r = new Spring(m_game, img, x, y, w, h, tilew, tileh);
        r.setDamagable(canDamage);
        setParams(r);
        return r;
    }
    //生成Ladder的信息

	private MapObject readLadder() throws IOException{
		readSection();
		Ladder r = new Ladder(m_game, img, x, y, w, h, tilew, tileh);
        r.setDamagable(canDamage);
        setParams(r);
        return r;
	}
    //生成Static的信息

    private StaticObject readStatic() throws IOException{
        readSection();
        StaticObject r = new StaticObject(m_game, img, x, y, w, h, begin, end);
        setParams(r);
        return r;
    }
    //生成Bonus的信息

    private Bonus readBonus() throws IOException{
		readSection();
        Bonus r = new Bonus(m_game, img, x, y, w, h, begin, end);
        setParams(r);
        r.kind = kind;
        return r;
	}
    //生成Key的信息

    private DoorKey readKey() throws IOException{
        readSection();
        DoorKey r = new DoorKey(m_game, img, x, y, w, h, begin, end, id);
        setParams(r);
        return r;
    }
    //生成Door的信息
    
	private Door readDoor() throws IOException{
		readSection();
		Door r = new Door(m_game, img, x, y, w, h, id);
        setParams(r);
        r.destMap = destMap;
        r.destX = destX;
        r.destY = destY;
        if (open) r.open();									
        return r;
	}
    //生成Intro的信息

    private String[] readIntro() throws IOException{
        String rr[] = null, r[] = new String[100];
        int n = 0;
        try{
            String s;
            do{
                s = m_reader.readLine();
                if (s == null) break;
                s = s.trim();
                if (!s.equalsIgnoreCase("end")){
                    n++;
                    r[n-1] = new String(s);
                }else break;
            }while (s!=null);
            rr = new String[n];
            for (int i = 0; i < n; i++){
                rr[i] = r[i];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return rr;
    }
    /**
     * 由指定文件生成地图
     */
	public GameMap readMap(GameWorld game, String filename){
		try{
//			System.out.println("Open Map:"+filename);
            InputStream input = game.getIO().getInput(filename);
            if (input == null) return createEmptyMap(game);
			m_reader = new BufferedReader(new InputStreamReader(input));
			GameMap map = createEmptyMap(game);
			m_game = game;
			String s;
			do{
				s = getItem().toUpperCase();
				if (s.equals("NAME")){
					map.setName(getItem());
				}else if (s.equals("SIZE")){
					map.setSize(Integer.parseInt(getItem()), 
								Integer.parseInt(getItem()));
				}else if (s.equals("MUSIC")){
					game.playMusic(getItem(), true);
				}else if (s.equals("BACKGROUND")){
					map.setBkImg(getItem());
                }else if (s.equals("BACKGROUNDSIZE")){
                    map.setBkSize(Integer.parseInt(getItem()), 
                                Integer.parseInt(getItem()));
                }else if (s.equals("INTRODUCTION")){
                    map.setIntro(readIntro());
                }else if (s.equals("MAXSCORE")){
                    Integer.parseInt(getItem());
                }else if (s.equals("ONSTART")){
                    map.setStartScript(getItem());
				}else if (s.equals("PATROL")){
					map.addActor(readPatrol());
                }else if (s.equals("PLAYER")){
                    readSection();
                }else if (s.equals("FRIEND")){
                    map.addActor(readFriend());
                }else if (s.equals("SPRING")){
					map.addMapObject(readSpring());
                }else if (s.equals("WALL")){
                    map.addMapObject(readWall());
                }else if (s.equals("LADDER")){
					map.addMapObject(readLadder());
                }else if (s.equals("STATIC")){
                    map.addStatic(readStatic());
                }else if (s.equals("BONUS")){
                    map.addStatic(readBonus());
				}else if (s.equals("DOOR")){
					map.addStatic(readDoor());
                }else if (s.equals("KEY")){
                    map.addStatic(readKey());
                }else if (s.equals("END")){
					s = "";
				}
			}while (!s.equals(""));
			m_reader.close();
			return map;
		}catch(Exception e){
			e.printStackTrace();
			return createEmptyMap(game);
		}
	}
    /**
     * 读入地图列表
     */
	public static String[] readMapList(String filename, GameWorld game){
		String rr[] = null, r[] = new String[100];
		int n = 0;
		try{
            InputStream input = game.getIO().getInput(filename);
            if (input == null) return null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String s;
			do{
				s = reader.readLine();
				if (s == null) break;
				s = s.trim();
				if (!s.equals("")){
					n++;
					r[n-1] = new String(s);
				}
			}while (s!=null);
			rr = new String[n];
			for (int i = 0; i < n; i++){
				rr[i] = r[i];
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return rr;
		
	}
	/**
	 * 建立空地图
	 */
	public GameMap createEmptyMap(GameWorld game){
		GameMap map = new GameMap(game);
		map.setSize(640, 480);
		return map;
	}	
	/**
	 * 建立默认地图
	 */
	public GameMap createDefaultMap(GameWorld game){
		GameMap map = createEmptyMap(game);

		map.addPlayer( new Player(game, 
						game.loadImage("image/char4.png"), 
						100, 100, 32, 48
						));
		
		map.setName("默认地图");
		map.setBkImg(game.loadImage("image/bk1.jpg"));				
		return map;
	}	
}
