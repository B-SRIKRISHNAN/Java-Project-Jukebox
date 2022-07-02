package PlayLlists;

import AudioPlayer.AudioTrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface is used to do operations using playlists, associated with the registered user.
 * You can add new playLists, display associated playlists , select a playlist , and delete a playlist
 */
public interface PlaylistInterface {

    /**
     * This method is used to display al the playlists associate with the user.
     * @param allPlaylists the resultSet to display
     */
    void displayAllPlayLists(ResultSet allPlaylists) throws SQLException;

    /** Creates a new playList with the name given by user
     * and returns its playlistId
     * @param connection Connection to database
     * @return true if playlist has been created, else false
     */
    boolean createNewPlaylist(Connection connection) throws SQLException ;

    /**
     * Gets an existing playlist and returns the tracks in the playlist.
     * @param connection Connection to database.
     * @param playListId id of the playlist to get tracks from.
     * @return List of tracks in the playlist
     */
    List<AudioTrack> getTracksFromAPlaylist(Connection connection, int playListId) throws SQLException ;

    /**
     * Displays all the songs and podcast episode in the playlist
     * @param tracks The list of tracks to be displayed
     */
    void displayTracksInPlayList(List<AudioTrack> tracks) throws SQLException ;

    /**
     * Manage the playlist options like creating a new playlist, viewing an old playList
     * @param connection connection to the database
     */
    void playlistManager(Connection connection);

    /**
     * Deletes a playlist from the user's playlists
     * @param connection The connection to the database
     * @param playlistId id of the playlist to be deleted
     * @throws SQLException If there is an error in SQL operations
     */
    void deleteThePlayList(Connection connection, int playlistId) throws SQLException;

    /**
     * This is used to add a selected song to the selected playlist
     * @param connection Connection to the database
     * @param songId id of the song to be added
     * @param playlistId id of the playlist to add the song to
     * @return true, if song has been added, else false
     * @throws SQLException If there is error in using the SQL queries
     */
    boolean addTheSong(Connection connection, int songId, int playlistId) throws SQLException;

    /**
     * This is used to add a selected podcast to the selected playlist
     * @param connection Connection to the database
     * @param podcastId id of the podcast to be added
     * @param playlistId id of the playlist to add the podcast to
     * @return true, if song has been added, else false
     * @throws SQLException If there is error in using the SQL queries
     */
    boolean addThePodcast(Connection connection, int podcastId, int playlistId) throws SQLException;


}
