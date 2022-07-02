package JukeBox;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manages the Jukebox options of songs, podcasts , playlists, register, login based on
 * user input
 */
public interface JukeboxInterface {
    /**
     * This manages the user choices and selects the required class to access
     */
    void jukeboxManager();

    /**
     * Gets the connection tot the database
     * @return connection: to database
     * @throws ClassNotFoundException: Thrown if the driver class is not found
     * @throws SQLException: If there is error in using databaser
     */
     Connection getConnectionToDB() throws ClassNotFoundException, SQLException;

}
