package User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is used to add the new userDetails to the Database.
 */
public interface RegistrationInterface {

    /**
     * Add the user details to the Database
     * @param connection connection to the database
     * @return true if user was added, false if not added
     */
     boolean registerUser(Connection connection, String userName, String password, String userEmail) throws SQLException ;


        /**
         * Used to get the username
         * @return The username
         */
    String getUserName();

    /**
     * Gets the email id of the user and checks if it is valid, else asks to enter again
     * @return The validated user email id
     */
    String getuserEmail();

    /**
     * Gets the password of the user and validates it.
     * @return The validated user password
     */
    String getUserPassWord();


    /**
     *  Manages the registration process
     * @param connection Connects to database
     * @return true if registered successfully else false
     */
    UserDetails registrationManager(Connection connection);
}
