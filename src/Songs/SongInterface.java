package Songs;

import AudioPlayer.AudioTrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface is a common interface for songs used by both Logged In and general users. It contains the features to be
 * implemented  by both.
 */
public interface SongInterface {
    /**
     * Get all the songs from the given database and display them
     * @param connection: The database connection from which you get the required data.
     */
    void displayAllSongs(Connection connection) throws SQLException;

    /**
     * Return all the song results matching the given input,
     * which matches either songName or ArtistName or AlbumName
     * @param songDetail The songName or the ArtistName or the AlbumName
     * @return Returns a ResultSet of all the songs matching the given details.
     */
    ResultSet search(String songDetail, Connection connection) throws SQLException;


    /**
     * select a song from the given ResultSet.
     * @param resultsOfSearch: ResultSet from the search for a song.
     * @return A Song object with the details of the matching track. Returns null if no match found.
     */
    AudioTrack selectTheTrack(ResultSet resultsOfSearch) throws SQLException;

    /**
     * This method is used to manage the songs by choice from user
     */
    void songsManager(Connection connection);

}
