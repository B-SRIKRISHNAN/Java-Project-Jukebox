package User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This interface is used to log in an already registered user
 */
public interface LoginInterface {
    /**
     * This method is used to get the user details and check if they are available in database
     * @param connection : Used to connect to database
     * @return true, if the details were found, else false.
     */
     boolean login(Connection connection, String emailId, String password) throws SQLException;

    /**
     * Manages the login process
     * @param connection connection to the database
     * @return UserDetails after checking in database , if not registered then null
     */
    UserDetails loginManager(Connection connection);

}
