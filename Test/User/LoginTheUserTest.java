package User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginTheUserTest {

    LoginTheUser loginTheUser;
    Connection connection;
    Register register;
    boolean isRegistered;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {

        loginTheUser = new LoginTheUser();
        register = new Register();

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "root@123");
            String userName = "testUser";
            String userEmail = "test@email.com";
            String password = "test@123";
            isRegistered = register.registerUser(connection,userName,password,userEmail);

    }

    @AfterEach
    void tearDown() throws SQLException{
        connection.createStatement().executeUpdate("delete from customer where customerName = 'testUser'");
        connection.close();
    }

    @Test
    void emailCheck() {
        if (isRegistered)
        {
            String userEmail = "test@email.com";
            assertTrue(loginTheUser.testEmail.test(userEmail));

        }
    }
    @Test
    void login() throws SQLException{
        if (isRegistered) {
            String userEmail = "test@email.com";
            String password = "test@123";
            assertTrue(loginTheUser.login(connection, userEmail, password));
        }
    }
}