package AudioPlayer;

import Podcasts.Episode;
import Songs.Song;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class AudioPlayer implements Player{
    TrackStatus status ;
    boolean toLoop = false;
    Scanner scan = new Scanner(System.in);
    long currentPos = 0L;
    Clip clip;
    AudioInputStream inputAudio;
    boolean autoPlay = false;


    @Override
    public Clip audioManager(AudioTrack trackToPlay) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {

         if (trackToPlay.getClass()== Song.class)
        {
            Song trackToPlay1 = (Song)trackToPlay;
            inputAudio = AudioSystem.getAudioInputStream(new File(trackToPlay1.getFilePath()));
            System.out.println();
            System.out.println(trackToPlay1);
        }
        else if (trackToPlay.getClass()== Episode.class)
        {
            Episode trackToPlay1 = (Episode)trackToPlay;
            inputAudio = AudioSystem.getAudioInputStream(new File(trackToPlay1.getFilePath()));
            System.out.println();
            System.out.println(trackToPlay1);
        }
        else return null;


        clip = AudioSystem.getClip();
        clip.open(inputAudio);
        clip.loop(0);
        clip.start();
        status = TrackStatus.PLAYING;

        int choice;


        if(!autoPlay) {
            outer:
            while (true) {

                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.CLOSE) {
                        clip.close();
                    }
                });

                if (clip.getMicrosecondPosition() == clip.getMicrosecondLength() && !toLoop && status == TrackStatus.PLAYING) {

                    status = TrackStatus.STOPPED;
                    clip.close();

                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(status);
                    System.out.println();
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                    return null;
                }
                System.out.println("=====================================================================================");
                System.out.println("1. Play   2. Pause   3. Loop   4. Get Remaining Time    5. Forward by 10 seconds   ");
                System.out.println();
                System.out.println("6. Backward by 10 seconds    7. Stop ");
                System.out.println("=====================================================================================");
                choice = scan.nextInt();
                switch (choice) {
                    case 1: {
                        play();
                        break;
                    }

                    case 2: {

                        pause();
                        break;

                    }
                    case 3: {
                        setLoop();
                        break;
                    }

                    case 4: {
                        System.out.println("--------------------------------");
                        System.out.println(getRemainingTime());
                        System.out.println("--------------------------------");

                        break;
                    }

                    case 5:
                        forward();
                        break;
                    case 6:
                        backward();
                        break;
                    case 7: {
                        stop();
                        break outer;
                    }

                    default: {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(" Invalid choice");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
            }
            return null;
        }
        else{
            clip.setMicrosecondPosition(clip.getMicrosecondLength()-5000000);
            clip.addLineListener(e->{
                if (e.getType()== LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            return clip;

        }

    }

    @Override
    public void play()
    {
        if (!clip.isOpen())
            return;
            if (status != TrackStatus.PLAYING && status!= TrackStatus.STOPPED) {
                clip.setMicrosecondPosition(currentPos);
                clip.start();
                status = TrackStatus.PLAYING;
                System.out.println("--------------------------------");
                System.out.println(status);
                System.out.println("--------------------------------");
            }
            else if(status==TrackStatus.STOPPED)
            {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(" Audio has stopped playing");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            }
            else {
                System.out.println("--------------------------------");
                System.out.println("Already Playing");
                System.out.println("--------------------------------");
            }
    }

    @Override
    public void pause() {
        if (!clip.isOpen())
            return;
        if (status != TrackStatus.PAUSED && status!= TrackStatus.STOPPED )
        {
            currentPos= clip.getMicrosecondPosition();

            clip.stop();

            status=TrackStatus.PAUSED;
            System.out.println("--------------------------------");
            System.out.println(status);
            System.out.println("--------------------------------");
        }
        else if(status==TrackStatus.STOPPED)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Audio has stopped playing");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }
        else
        {
            System.out.println("--------------------------------");
            System.out.println("Already Paused");
            System.out.println("--------------------------------");
        }
    }

    @Override
    public void stop() {
        if (!clip.isOpen())
            return;
        clip.stop();
        clip.close();
        status=TrackStatus.STOPPED;
        System.out.println("--------------------------------");
        System.out.println(status);
        System.out.println("--------------------------------");

    }

    @Override
    public void setLoop()
    {
        if (!clip.isOpen())
            return;
        if (toLoop)
        {
            toLoop=false;
            clip.loop(0);
            System.out.println("--------------------------------");
            System.out.println("Not Looping the Track");
            System.out.println("--------------------------------");
        }
        else
        {
            toLoop= true;
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("--------------------------------");
            System.out.println("Looping the audio");
            System.out.println("--------------------------------");
        }

    }

    @Override
    public String getRemainingTime()
    {
        if (!clip.isOpen())
            return null;
        long timeRemainingInSeconds = (clip.getMicrosecondLength()- (clip.getMicrosecondPosition())%clip.getMicrosecondLength())/1000000;
        long minutes = (timeRemainingInSeconds)/60;
        long seconds = (timeRemainingInSeconds)%60;
        return minutes+":"+seconds;
    }

    @Override
    public void forward() {
        if (!clip.isOpen())
            return;

        if (clip.getMicrosecondPosition()<clip.getMicrosecondLength()-10000000)
            clip.setMicrosecondPosition(clip.getMicrosecondPosition()+10000000);
        else
            clip.setMicrosecondPosition(clip.getMicrosecondLength());
    }

    @Override
    public void backward() {
        if (!clip.isOpen())
            return;
         if (clip.getMicrosecondPosition()>10000000)
            clip.setMicrosecondPosition(clip.getMicrosecondPosition()-10000000);
         else
             clip.setMicrosecondPosition(0);
    }


}

