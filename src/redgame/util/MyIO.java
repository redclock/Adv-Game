package redgame.util;
import java.io.*;
import java.awt.*;

public interface MyIO {
    public boolean isApplet();
    public void toggleFullScreen();
    public InputStream getInput(String filename);
    public Image loadImage(String filname);
    public void playMusic(String filname, boolean looped);
    public void stopMusic();
    public void playSound(String filname);
}
