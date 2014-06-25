package redgame.util;
/*
 * MusicPlayer.java
 * 此文件的核心算法摘自: http://www.gameres.com/bbs/showthread.asp?threadid=19304
 */

import java.io.*;
import javax.sound.sampled.*;


/**
 * MusicPlayer类是播放声音的类.
 * 能自动有文件扩展名判断是wav还是midi
 * @see MidiPlayer
 */

public class MusicPlayer implements Runnable {
    //音乐线程
    private Thread musicThread;
    //midi播放器
    private MidiPlayer m_midi = null;           
    //是否循环
    private boolean m_looped = false;   
    //是否已经停止        
    private boolean m_stopped = false; 
    //文件名         
    private String m_filename;
    //工具函数:获得扩展名
    private String getExt(String filename){
        int i = filename.lastIndexOf('.');
        if (i<0) return "";
        else return filename.substring(i+1);
    }
    //取得音频样本
    private byte[] getAudioSamples( AudioInputStream MusicStream, AudioFormat format )
    {
        int AudioSampleLengh = ( int )( MusicStream.getFrameLength() *
                                      format.getFrameSize() );
        byte aAudioSamples[] = new byte[ AudioSampleLengh ];
        DataInputStream dataInputStream = new DataInputStream( MusicStream );
  
        try{
            dataInputStream.readFully( aAudioSamples );
        }catch( Exception e ){
            e.printStackTrace();
        }
  
        return aAudioSamples;
    }
  
    //播放au,aiff,wav音乐流, 这个函数基本完全为帖子上的代码
    private synchronized void play()
    {
          ByteArrayInputStream aMusicInputStream;
          AudioFormat format;
          AudioInputStream musicInputStream;
          byte[] audioSamples;
          SourceDataLine line;
          try {
              File MusicFile = new File(m_filename);
      
              musicInputStream
                 = AudioSystem.getAudioInputStream( MusicFile ); //取得文件的音频输入流
              format = musicInputStream.getFormat(); //取得音频输入流的格式
              audioSamples = getAudioSamples( musicInputStream, format );//取得音频样本
      
              aMusicInputStream
                       = new ByteArrayInputStream( audioSamples );
              int bufferSize = format.getFrameSize() *
                      Math.round(format.getSampleRate() / 10);
              byte[] buffer = new byte[bufferSize];
              try {
                  DataLine.Info info =
                      new DataLine.Info(SourceDataLine.class, format);
                  line = (SourceDataLine)AudioSystem.getLine(info);
                  line.open(format, bufferSize);
              }
              catch ( LineUnavailableException e ) {
                  e.printStackTrace();
                  return;
              }
      
              if( !line.isRunning() )
              {
                  line.start();
              }
      
             int numBytesRead = 0;
             while (numBytesRead != -1 && !m_stopped) {
                 numBytesRead =
                     aMusicInputStream.read(buffer, 0, buffer.length);
                 if (numBytesRead != -1) {
                    line.write(buffer, 0, numBytesRead);
                 }
             }
             line.drain();
             line.close();
          }catch(Exception e){
             e.printStackTrace();
          }
    }

    /**
     * 构造一个MusicPlayer
     * @param filename 文件名
     * @param looped 是否循环
     */
    public MusicPlayer(String filename, boolean looped){  
          m_looped = looped;                          
          m_stopped = false;  
          m_filename = filename;  
          //如果扩展名为mid的话...                      
          if (getExt(filename).equalsIgnoreCase("mid")){
              m_midi = new MidiPlayer();
          }
    }
    /**
     * 开始音乐线程
     */
    public void start(){
        //midi和wav需要不同的方法
        if (m_midi == null){
            musicThread = new Thread(this);
            musicThread.start();
            m_stopped = false;
        }else{
            m_midi.InitSequence(m_filename);
            m_midi.play(m_looped);
        }    
    }
    /**
     * 结束音乐线程
     */
    public void stop(){
        if (m_midi == null){
            musicThread.interrupt();
            musicThread = null;
            m_stopped = true; 
        }else{
            m_midi.stop();
            m_midi.close();
        }    
    }
    /**
     * 运行音乐线程
     */
    public void run(){
        //不停播放
        do{
            play();
        }while(m_looped && !m_stopped);       
    }
}

