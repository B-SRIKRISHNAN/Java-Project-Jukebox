package User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {
    Register register;
    Connection connection;

    @BeforeEach
    void setUp() {
        register = new Register();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox", "root", "root@123");
        }
        catch (ClassNotFoundException | SQLException c)
        {
            c.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() throws SQLException{
        connection.createStatement().executeUpdate("delete from customer where customerName = 'testUser'");
        connection.close();

    }

    @Test
    void registerCheck() throws SQLException
    {
        String userName = "testUser";
        String userEmail = "test@email.com";
        String password = "test@123";
        assertTrue(register.registerUser(connection,userName,password,userEmail));
    }

    @Test
    void checkEmailFormat()
    {
        assertFalse(register.testEmail.test("abcd"));
        assertFalse(register.testEmail.test("abcd@123"));
        assertTrue(register.testEmail.test("abcd@efgh.ijk"));
    }

    @Test
    void checkPasswordFormat() {
        assertFalse(register.testPassword.test("abc$d"));
        assertFalse(register.testPassword.test("abcdefgh"));
        assertTrue(register.testPassword.test("abcdefgh$"));

    }
}