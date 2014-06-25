package redgame.appframe;

import redgame.scripts.*;

import redgame.util.*;
import redgame.status.*;

import java.io.*;

////////////////////////////////////////////
//冒险游戏
//作者:姚春晖
////////////////////////////////////////////
/***
 * 程序入口
 */
public class GApp{
//    private static AppFrame mainFrame;
//    
//	private static void Debugloop(){
//        BufferedReader reader = 
//                    new BufferedReader(new InputStreamReader(System.in));    
//	    String s, s1;
//        while (true){
//            try{
//                s = reader.readLine();
//                if (s.equals("showvars")){
//                    AbstractScript.showVars();
//                }else if (s.equals("setvar")){
//                    System.out.print("Var Name:");
//                    s = reader.readLine();
//                    System.out.print("Value:");
//                    s1 = reader.readLine();
//                    AbstractScript.setVar(s, s1);
//                }else if (s.equals("gotomap")){
//                    System.out.print("Map Path:");
//                    s = reader.readLine();
//                    mainFrame.getGame().gotoMap(s, 0, 0);
//                }else if (s.equals("playanim")){
//                    System.out.print("Anim Path:");
//                    s = reader.readLine();
//                    mainFrame.getGame().pushStatus(new 
//                        AnimStatus(mainFrame.getGame(), 
//                            (int) mainFrame.getGame().getPlayer().getX() - 100,
//                            (int) mainFrame.getGame().getPlayer().getY() - 100,
//                            s)
//                        );
//                }else if (s.equals("addHP")){   
//                    mainFrame.getGame().getPlayer().addHP(4);
//                }else if (s.equals("addring")){   
//                    mainFrame.getGame().getPlayer().addBullet();
//                }else if (s.equals("exit")){
//                    System.exit(0);
//                }else{
//                    System.out.println("Bad command");
//                }     
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//	    
//	}
	public static void main(String args[]){
        AppFrame mainFrame = new AppFrame("Game Frame");
        //GameLib.waitTime(5000);
		mainFrame.startGame();
        GameLib.waitTime(5000);
        //Debugloop();
	}
}