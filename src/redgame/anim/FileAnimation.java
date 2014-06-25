package redgame.anim;
/*
 * AnimStatus.java 作者：姚春晖
 */
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import redgame.engine.*;
public class FileAnimation{

    final public static int NORMAL = 0;
    final public static int MIDDLE = 1;
    final public static int BOTTOM = 2;
    final public static int TOP = 3;

    private BufferedReader m_reader = null;

    private class ItemInfo{
        Image img = null;
        Point pos;
        Dimension size;
        float alpha  = 1;
        int delay = 100;
        public ItemInfo(String filename, GameWorld game){
            img = game.loadImage(filename);
            size = new Dimension(img.getWidth(game.getPanel()),
                                img.getHeight(game.getPanel()));
//            System.out.println("Anim load: " + filename);
        }
        
        public void load(FileAnimation owner) throws IOException{
            String s;
            do{
                s = owner.getItem().toUpperCase();
                if (s.equals("ALPHA")) {
                    alpha = owner.getItemFloat();
                }else if (s.equals("DELAY")) {
                    delay = owner.getItemInt();
                }else if (s.equals("SCALE")) {
                    size.width = (int) (size.width * owner.getItemFloat());
                    size.height = (int) (size.height * owner.getItemFloat());
                }else if (s.equals("SUB")){
                    int x = owner.getItemInt();
                    int y = owner.getItemInt();
                    size.width = owner.getItemInt(); 
                    size.height = owner.getItemInt();

                    img = ((BufferedImage) img).getSubimage(
                           x, y, size.width, size.height );
                }else if (s.equals("END")){
                    s = "";
                }
            }while(!s.equals(""));
        }
    }

    private int m_loop = -1;
    private int m_count = 0;
    private int m_maxw = 0, m_maxh = 0;
    private int m_x, m_y;
    private ItemInfo[] infos;
    private int m_style;
    private GameWorld m_game;
    private long m_counter;
    private int curIndex;
    private boolean m_stop;
    
    String getItem() throws IOException{
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
    
    int getItemInt() throws IOException{
         return Integer.parseInt(getItem());   
    }
    
    float getItemFloat() throws IOException{
         return Float.parseFloat(getItem());   
    }

    private void loadFile(String filename){
        int index = 0;
        String s;
        m_maxw = 0; m_maxh = 0;
        m_style = NORMAL;
        try{
            InputStream input = m_game.getIO().getInput(filename);
            if (input == null) return;
            m_reader = new BufferedReader(new InputStreamReader(input));
            infos = new ItemInfo[100];
            m_count = 0;
            do{
                s = getItem().toUpperCase();
                if (s.equals("LOOP")) {
                    m_loop = getItemInt();
                }else if (s.equals("STYLE")) {
                    s = getItem().toUpperCase();
                    if (s.equals("MIDDLE")){
                        m_style = MIDDLE;
                    }else if (s.equals("TOP")){
                        m_style = TOP;
                    }else if (s.equals("BOTTOM")){
                        m_style = BOTTOM;
                    } 
                }else if (s.equals("PIC")){
                    infos[index] = new ItemInfo(getItem(), m_game);
                    infos[index].load(this);
                    if (infos[index].size.width > m_maxw) 
                        m_maxw = infos[index].size.width;  
                    if (infos[index].size.height > m_maxh) 
                        m_maxh = infos[index].size.height;                          
                    index ++;
                }
            }while(!s.equals(""));
            if (m_reader != null){
               m_reader.close();
               m_reader = null;
            }
            m_count = index;
            ItemInfo[] temp = new ItemInfo[m_count];
            System.arraycopy(infos, 0, temp, 0, m_count);
            infos = temp;
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        m_reader = null;
    }
    
    private void calcPositions(){
        for (int i = 0; i < m_count; i++){
            switch(m_style){
                case MIDDLE: 
                    infos[i].pos = new Point(m_x + (m_maxw - infos[i].size.width) /2 - m_maxw / 2,
                                         m_y + (m_maxh - infos[i].size.height) /2 - m_maxh / 2);
                    break;          
                case BOTTOM: 
                    infos[i].pos = new Point(m_x + (m_maxw - infos[i].size.width) /2 - m_maxw / 2,
                                         m_y + (m_maxh - infos[i].size.height) - m_maxh);
                    break;          
                case TOP: 
                    infos[i].pos = new Point(m_x + (m_maxw - infos[i].size.width) /2,
                                         m_y);
                    break;
                default:               
                    infos[i].pos = new Point(m_x, m_y);
            }
        }
    }
    /**
     * 创建一个FileAnimation
     * @param game 游戏引用
     * @param x    位置
     * @param y    位置
     */
    public FileAnimation(GameWorld game, int x, int y, String filename) {
        m_game = game;
        m_x = x; m_y = y;
        m_stop = false;
        loadFile(filename);
        if (m_count <= 0) m_stop = true;
        else calcPositions();
  //      System.out.println("stop = " + m_stop);
        curIndex = 0;
        m_counter = 0;
    }
    /**
     * 画图代码
     * @param passedTime 从上一次调用到现在的时间
     * @param g          用来画图的引用 
     */
    public void paint(Graphics g){
        if (m_stop) return;
        Graphics2D g2 = (Graphics2D)g;
        
        Composite oldac = g2.getComposite();
        if (infos[curIndex].alpha < 1){
            AlphaComposite ac = AlphaComposite.getInstance(
                       AlphaComposite.SRC_OVER, infos[curIndex].alpha);
            g2.setComposite(ac);
        }
//        System.out.println("draw: "+index+", "+ m_pos[index].x +", " + m_pos[index].y);
        g2.drawImage( infos[curIndex].img, 
                      infos[curIndex].pos.x,
                      infos[curIndex].pos.y,
                      infos[curIndex].size.width,
                      infos[curIndex].size.height,
                      m_game.getPanel()
                    );
        g2.setComposite(oldac);
    }

    public void update(long passedTime){
        if (m_stop) return;
        m_counter += passedTime;
        if (m_counter >= infos[curIndex].delay){
            
            curIndex ++;
            m_counter = 0;
            if (curIndex >= m_count){
                curIndex = 0;
                m_loop --;
                if (m_loop == 0) m_stop = true;
            }    
        }
    }
    
    public boolean isStop(){
        return m_stop;
    }
}
