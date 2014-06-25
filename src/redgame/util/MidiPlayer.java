package redgame.util;
/*
 * MidiPlayer.java
 * 此文件的核心算法摘自: http://www.gameres.com/bbs/showthread.asp?threadid=19304
 */
import java.io.*;
import javax.sound.midi.*;

/**
 * MidiPlayer类是播放MIDI音乐的类
 */

public class MidiPlayer implements MetaEventListener{

    //序列器
    private Sequence sequence;
    private Sequencer sequencer;

    private boolean isLoop   = false;

    private File MidiFile;

    private static final int END_OF_TRACK_MESSAGE = 47; //磁道信息尾

    /**
     * 打开序列
     * @param filename
     */
    public void InitSequence( String filename )
    {
        try{
            MidiFile = new File( filename );
            sequence = MidiSystem.getSequence( MidiFile ); //从MidiStream流取得序列
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addMetaEventListener( this );
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * 播放序列
     * @param isLoop 是否循环播放
     */
    public void play( boolean isLoop )
    {
        if( sequencer != null && sequence != null && sequencer.isOpen() )
        {
            try {
                sequencer.setSequence( sequence );
                sequencer.start();
                this.isLoop = isLoop;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 实现MetaEventListener接口
     */
    public void meta( MetaMessage metaMessage ) {
        //循环播放
        if( metaMessage.getType() == END_OF_TRACK_MESSAGE )
        {
//            System.out.println("loop midi");
            if( sequencer != null && sequencer.isOpen() && isLoop )
            {
                sequencer.stop();
                try{
                    sequence = MidiSystem.getSequence( MidiFile ); //从MidiStream流取得序列
                    sequencer = MidiSystem.getSequencer();
                    sequencer.open();
                    sequencer.addMetaEventListener( this );
                    play(true);
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 停止音乐
     */
    public void stop()
    {
        if( sequencer != null && sequencer.isOpen() )
        {
//            System.out.println("stop midi");
            sequencer.stop();
            isLoop = false;
        }
    }
    /**
     * 关闭序列
     */
    public void close()
    {
        if( sequencer != null && sequencer.isOpen() )
        {
            sequencer.close();
        }
    }

}

