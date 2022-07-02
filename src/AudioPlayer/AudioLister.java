package AudioPlayer;


/**
 * Audio is used to list the audio List and play the tracks in the order given.
 * Can play the next or previous track.
 * Displays remaining tracks
 */
public interface AudioLister {

    /**
     * This method is used to manage the list and select what to play.
     */
    void manageList();

    /**
     * Goes to the previous track. If shuffle is on, then randomly goes to a track
     */
    void goToPrevious();

    /**
     * Goes to the next track. If shuffle is on, then randomly goes to a track
     */
    void goToNext();

    /**
     * Plays the given track by passing it to the Player.
     * @param trackToPlay : It is the AudioTrack(Song/Episode) to be played.
     */
    void playTheTrack(AudioTrack trackToPlay);

    /**
     * Switches looping between true or false
     */
    void setLoopPlay();

    /**
     * Switches shuffle between true and false
     */
    void setShuffle();

}
