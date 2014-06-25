package redgame.applet;

import java.awt.*;
import java.applet.*;
import java.io.*;
import redgame.engine.*;
import redgame.util.*;

public class GApplet extends Applet implements MyIO{
    private GamePanel  picpane = new GamePanel();
    private AudioClip m_music = null;
    public void init() {
       GameConfig.defaultConfig.load("config.txt", this);
       this.setLayout(new BorderLayout());
       this.add(picpane, BorderLayout.CENTER);
       picpane.startGame(this);
       this.addKeyListener(KeyManager.getDefault());
	}

    public void paint(Graphics g) {
	    picpane.repaint();
	}
	

    public boolean isApplet(){
        return true;
    }
    
    public InputStream getInput(String filename){
        return getClass().getResourceAsStream(filename);
    }
    
    public Image loadImage(String filename){
        return getImage(getDocumentBase(), filename);
    }
    
    public void playMusic(String filename, boolean looped){
        stopMusic();
        m_music = getAudioClip(getDocumentBase(), filename);
        if (looped)
            m_music.loop();
        else
            m_music.play();    
    }
    public void stopMusic(){
        if (m_music != null){
            m_music.stop();
            m_music = null;
        }
    }
    public void playSound(String filename){
        AudioClip m = getAudioClip(getDocumentBase(), filename);
        m.play();    
    }
    public void toggleFullScreen() {
        
    }
}
