package AudioPlayer;

import Podcasts.Episode;
import Songs.Song;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import  java.util.Scanner;

public class AudioListManager implements AudioLister {
    boolean looping = false;
    boolean shuffle = false;
    int currentTrack; // from 1-n
    boolean autoplay = false;
    List<AudioTrack> allTracks;
    Supplier<Integer> supplyRandomNumber = () -> new Random().nextInt(1, allTracks.size()+1);

    public AudioListManager(List<AudioTrack> allTracks, boolean looping, boolean shuffle, int currentTrack)
    {
        this.allTracks = allTracks;
        this.looping = looping;
        this.shuffle = shuffle;
        this.currentTrack = currentTrack;

    }

    public AudioListManager(List<AudioTrack> allTracks, int currentTrack)
    {
        this.allTracks = allTracks;
        this.currentTrack = currentTrack;
    }

    public AudioListManager(List<AudioTrack> allTracks, boolean autoplay)
    {
        this.allTracks = allTracks;
        this.autoplay = autoplay;
        currentTrack=1;
    }


    @Override
    public void manageList()
    {
        Scanner scan = new Scanner(System.in);
        int choice;
        if (Optional.ofNullable(allTracks).isEmpty())
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" No Tracks Found to Play");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        if (currentTrack > allTracks.size()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Invalid choice of track");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
//
        if (autoplay) {
            playTheTrack(allTracks.get(currentTrack-1), true);
        }
        else
            playTheTrack(allTracks.get(currentTrack-1));
        outer:
        while (!autoplay)
        {



            System.out.println("=====================================================================================");
            System.out.println(" 1. Show All Tracks In List      2. Previous Track      3. Next Track    ");
            System.out.println();
            System.out.println(" 4. Loop     5. Shuffle    6. Go Back");
            System.out.println("=====================================================================================");
            choice = scan.nextInt();
            switch (choice)
            {
                case 1:displayAllTracks();break;

                case 2: goToPrevious();
                    playTheTrack(allTracks.get(currentTrack-1));
                    break;

                case 3: goToNext();
                    playTheTrack(allTracks.get(currentTrack-1));
                break;

                case 4: setLoopPlay();break;

                case 5: setShuffle();break;

                case 6: break outer;

                default: {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("Enter a valid choice");
                    System.out.println();
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }

        }

    }

    public void displayAllTracks()
    {
        System.out.println("____________________________________________________________________________________________________________________________");
        System.out.println(" List of all the tracks ");
        System.out.println("____________________________________________________________________________________________________________________________");

        allTracks.stream().filter(t->t.getClass()==Song.class).forEach(t->{Song thisSong = (Song)t;
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println(thisSong);
            System.out.println("____________________________________________________________________________________________________________________________");

        });
        allTracks.stream().filter(t->t.getClass()==Episode.class).forEach( t->
        {

            Episode thisEpisode = (Episode) t;
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println(thisEpisode);
            System.out.println("____________________________________________________________________________________________________________________________");


        });
    }

    public void playTheTrack(AudioTrack trackToPlay, boolean autoPlay)
    {
        AudioPlayer play = new AudioPlayer();
        try {

                play.autoPlay = autoPlay;
                Clip clip = play.audioManager(trackToPlay);
                AudioTrack playTrack;
                while (currentTrack != allTracks.size()) {

                    if (!clip.isOpen()) {

                        System.out.println("Playing next track");
                        goToNext();
                        playTrack = allTracks.get(currentTrack - 1);
                        playTheTrack(playTrack, autoPlay);
                        return;
                    }


                }

                System.out.println("playing last track");
                while (clip.isOpen());


        }
        catch(UnsupportedAudioFileException us)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("The file format is Not supported. Please use only .wav");
            System.out.println();
            System.out.println(us.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }
        catch (IOException e)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Error with audioFile");
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        catch(LineUnavailableException l)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Error in playing the file");
            System.out.println();
            System.out.println(l.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
    @Override
    public void playTheTrack(AudioTrack trackToPlay)
    {
        Player play = new AudioPlayer();
        try{
        play.audioManager(trackToPlay);}
        catch(UnsupportedAudioFileException us)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("The file format is Not supported. Please use only .wav");
            System.out.println();
            System.out.println(us.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }
        catch (IOException e)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Error with audioFile");
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        catch(LineUnavailableException l)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Error in playing the file");
            System.out.println();
            System.out.println(l.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    @Override
    public void goToPrevious() {
        if (currentTrack!=1)
        {
            if (!shuffle)
                currentTrack = currentTrack - 1;
            else {
                int randomTrack = supplyRandomNumber.get();
                if (randomTrack==currentTrack)
                    currentTrack = currentTrack-1;
                else
                    currentTrack=randomTrack;
            }
        }
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("No tracks before this");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }


    }

    @Override
    public void goToNext() {
        if (currentTrack!= allTracks.size() && !looping)
        {
           if (!shuffle)
                currentTrack = currentTrack + 1;
           else
           {
               int randomTrack = supplyRandomNumber.get();
               if (randomTrack==currentTrack)
                   currentTrack = currentTrack+1;
               else
                   currentTrack=randomTrack;
           }


        }
        else if (looping)
        {
            if (currentTrack == allTracks.size())
                currentTrack=1;
            else
                currentTrack = currentTrack+1;
        }
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("No tracks after this");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }



    }

    @Override
    public void setLoopPlay() {
        looping = !looping;
        if (looping) {
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println("Looping the tracks");
            System.out.println("____________________________________________________________________________________________________________________________");
        }
        else
        {
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println("Not Looping the tracks");
            System.out.println("____________________________________________________________________________________________________________________________");

        }


    }

    @Override
    public void setShuffle()
    {
        shuffle=!shuffle;
        if (shuffle) {
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println("Shuffling the tracks");
            System.out.println("____________________________________________________________________________________________________________________________");
        }
        else
        {
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println("Not shuffling the tracks");
            System.out.println("____________________________________________________________________________________________________________________________");

        }
    }
}


