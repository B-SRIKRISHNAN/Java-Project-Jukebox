package Podcasts;

import AudioPlayer.AudioTrack;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface is a common interface for podcasts used by both Logged In and general users. It contains the features to be
 * implemented by both.
 */
public interface PodcastInterface {
    /**
     * Get all the podcasts from the given database and display them
     * @param connection: The database connection from which you get the required data.
     */
    void displayAllPodcasts(Connection connection) throws SQLException;
    /**
     * Return all the podcast results matching the given input,
     * which matches either podcastName or celebrityName
     * @param podcastDetail The songName or the ArtistName or the AlbumName
     * @return Returns a ResultSet of all the podcasts matching the given details.
     */
    ResultSet search(String podcastDetail, Connection connection) throws SQLException;

    /**
     * Selects a podcast from the search results
     * @return The podcastId of the selected podcast.
     */
    int selectAPodcast(ResultSet searchResultOfPodcasts)  throws SQLException;

    /** This method gets all the episodes in the podcast and returns them as a list.
     *
     * @param podCastId The podcast whose episodes are to be taken
     * @param connection the database connection
     * @return the List of episodes in the podcast, null if no episodes
     */
    List<AudioTrack> getAllEpisodesInThePodcast(int podCastId, Connection connection)  throws SQLException;

    /**
     * This is used to manage a podcast
     * @param connection The connection to the database
     */
    void podcastManager(Connection connection);


}
