package redgame.scripts;
import redgame.engine.*;
import java.util.*;
/**
 *
 * 脚本
 * 
 */
public abstract class AbstractScript{
    private static Map<String, String> vars = new HashMap<String, String>(); 
    
    public static int getIntVar(String name){
        String s = vars.get(name);
        if (s == null) return 0;
        return Integer.parseInt(s);
    }
    
    public static float getFloatVar(String name){
        String s = vars.get(name);
        if (s == null) return 0;
        return Float.parseFloat(s);
    }
    
    public static String getStrVar(String name){
        String s = vars.get(name);
        return s;
    }
    
    public static void setVar(String name, int var){
        vars.put(name, String.valueOf(var));
    }

    public static void setVar(String name, float var){
        vars.put(name, String.valueOf(var));
    }

    public static void setVar(String name, String var){
        vars.put(name, String.valueOf(var));
    }
    public static void clearVars(){
        vars.clear();
    }
    public static void showVars(){
        Object[] keys = vars.keySet().toArray();
        for (int i = 0; i < keys.length; i++){
            System.out.println(keys[i]+" = "+vars.get(keys[i]));
        }
    }
    public static Map getVars(){
        return vars;
    }    
    //是否正在运行
    protected boolean m_running = false;
    /**
     * 游戏引用
     */
    public GameWorld game;
    /**
     * 脚本来源信息
     */
    public ScriptSource source;
    /**
     * 脚本参数
     */
    public String[] args;
    
    public AbstractScript(){
        game = null;
    }
    /**
     * 是否正在运行
     */
    public boolean isRunning(){
        return m_running;
    }
    
    /**
     * 开始脚本
     */
    public abstract void start();
    /**
     * 强制停止
     */
    public abstract void stop();
    /**
     * 执行脚本
     */
    public abstract int update();

}
