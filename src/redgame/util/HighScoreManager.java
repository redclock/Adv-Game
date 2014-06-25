package redgame.util;
/*
 * HighScoreManager.java 作者：姚春晖
 */
import java.io.*;
/**
 * HighScoreManager类是高分榜管理器
 * @author 姚春晖
 */

public class HighScoreManager {
    
    private String m_filename;
    //最大7条
    final private int MAX = 7;
    /**
     * 每项的姓名
     */
    public String[] names = new String[MAX + 1];
    /**
     * 每项分数
     */
    public int[] scores = new int[MAX + 1];
    /**
     * 高分榜中共有几个
     */
    public int count;
    //从文件读入
    private void readData(){
        count = 0;
        String s;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(m_filename));
            while (count < MAX && (s = reader.readLine()) != null){
                names[count] = new String(s);
                scores[count] = Integer.parseInt(reader.readLine());
                count++;
            } 
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }    
    }
    //写入到高分榜
    private void writeData(){
        try{
            PrintWriter writer = new PrintWriter(new FileWriter(m_filename));
            
            for (int i = 0; i < count; i++){
                writer.println(names[i]);
                writer.println(scores[i]);
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }    
    }
    /**
     * 构造高分榜
     * @param filename 高分榜文件名
     */
    public HighScoreManager(String filename) {
        m_filename = filename;
        readData();
    }
    /**
     * 判断score是否够的上高分
     */
    public boolean isHighScore(int score){
        //如果还没到最大数, 当然可以
        if (count < MAX) return true;
        //否则只看最后一个
        return scores[count - 1] < score;
    }

    /**
     * 向高分榜添加入当前分数
     * 并排序
     * @param name 玩家姓名
     */
    
    public void addHighScore(int score, String name){
        int i;
        //找一个合适的地方插进去
        for (i = count - 1; i >=0; i--){
            if (scores[i] < score) {
                scores[i+1] = scores[i];
                names[i+1] = names[i];
            }else{
                break;
            }    
        }
        if (count < MAX) count ++;
        scores[i+1] = score;
        names[i+1] = name; 
        writeData();
    }    
}
