package redgame.util;
import java.util.*;
import java.io.*;

public class GameConfig {
    
    //数据结构
    private Map<String, String> m_map;
    
    public static GameConfig defaultConfig = new GameConfig();
    /**
     * Method GameConfig
     *
     *
     */
    public GameConfig() {
        m_map = new HashMap<String, String>();
        reset();
    }

    /**
     * Method load
     * 装载配置文件
     *
     * @param filename 要保存的文件名
     */
    public boolean load(String filename, MyIO io) {
        try{
            BufferedReader m_reader = new BufferedReader(
                                    new InputStreamReader(io.getInput(filename)));
            m_map.clear();
            String s = m_reader.readLine();
            
            while (s != null){
                int p = s.indexOf('=');
                if (p > 0){
                    m_map.put(s.substring(0, p).trim(), s.substring(p+1).trim());
                }
                s = m_reader.readLine();
            }
            m_reader.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method save
     *
     * @param filename
     *
     */
    public boolean save(String filename) {
        try{
            PrintWriter p = new PrintWriter(filename);
            for (String key: m_map.keySet()){
                p.println(key+"="+m_map.get(key));
            }
            p.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false; 
        }    
    }
    
    public String[] getKeys(){
        String[] r = new String[m_map.size()];
        Object[] key = m_map.keySet().toArray(); 
        for(int i =0; i < r.length; i++){
            r[i] = (String) key[i];
        }
        return r;    
    }
    
    public String getString(String Key){
        return m_map.get(Key);
    }
    
    public String getString(String Key, String Default){
        String s = m_map.get(Key);
        if (s == null){
            return Default;
        }
        return s;
    }

    public int getInt(String Key, int Default){
        String s = m_map.get(Key);
        if (s == null){
            return Default;
        }
        return Integer.parseInt(s);
    }

    public boolean getBoolean(String Key, boolean Default){
        String s = m_map.get(Key);
        if (s == null){
            return Default;
        }
        return (s.equals("1")) || (s.compareToIgnoreCase("true") == 0);
    }

    public void put(String Key, String Value){
        m_map.put(Key, Value);
    }

    public void put(String Key, int Value){
        m_map.put(Key, String.valueOf(Value));
    }

    public void put(String Key, boolean Value){
        m_map.put(Key, String.valueOf(Value));
    }
    /**
     * Method reset
     *
     *
     */
    public void reset() {
        m_map.clear();
    }

}
