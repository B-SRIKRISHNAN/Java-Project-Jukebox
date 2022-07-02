package AudioPlayer;


import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Player is used to play a particular audio track. It can also check if you want to play next or previous track.
 * Returns the required Enum type for the direction of next song.
 */
public interface Player {

    /**
     * This method is used to determine how the audio will be played with the options and also to create a clip from
     * given track path.
     * @param trackToPlay: The input AudioTrack to be played.
     * @throws UnsupportedAudioFileException : If file format not supported
     * @throws IOException: Error reading file
     * @throws LineUnavailableException: Line to play the song unavailable
     */
    Clip audioManager(AudioTrack trackToPlay) throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    /**
     * This method is used to run a track from its current position.
     */
    void play();

    /**
     * This method is used to stop the track at its current position.
     * The current position is to be stored globally and used for continuing the track.
     */
    void pause();

    /**
     * This method is used to stop the track and close the Clip.
     * Can be used to move to the next or previous track by closing the current Clip.
     */
    void stop();

    /**
     * The method is used to get the time remaining in the audio track
     * @return returns a String with the Time in 'min:sec' format.
     */
    String getRemainingTime();

    /**
     * Forwards the song from the current position to another position
     */
    void forward();

    /**
     * Moves the track from current position to another position  backwards
     */
    void backward();

    /**
     * Sets the looping of the track to true if it is not looping. If looping, it will set looping to false.
     */
    void setLoop();

}
