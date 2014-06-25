package redgame.scripts;

import redgame.scripts.*;

import redgame.engine.*;
import redgame.ui.*;
import redgame.obj.*;
import redgame.status.*;
import redgame.util.*;

public class BlockOut extends SimpleScript {
    
    protected void runScript(){
        NPC    a = (NPC)source.owner;
        Player b = (Player)source.cause; 
        switch(m_counter){
            case 1:  
            	a.say("咦,你要到外面去吗?");   
        		break;	
        	case 2:
            	a.say("但看你的装备,好像不全呀.");   
        		break;	
        	case 3:
            	a.say("外面很危险,你没有足够的装备是不能出去的.");   
        		break;	
        	case 4:
            	a.say("多在村子里转一转吧.");   
        		break;	
            default: 
                stop();
        }
    }
}
