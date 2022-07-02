package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register implements RegistrationInterface{
    Scanner scan = new Scanner(System.in);
    Predicate<String> testEmail = e->{
        Pattern p = Pattern.compile("[a-zA-Z0-9_]+[@][a-z]+[.][a-z]+");
        Matcher m = p.matcher(e);
        return m.matches();
    };
    Predicate<String> testPassword = pass->{
        Pattern p = Pattern.compile("[a-zA-Z0-9!@#$%^&*]*[!@#$%^&*][a-zA-Z0-9!@#$%^&*]*");
        Matcher m = p.matcher(pass);
        return m.matches()&&pass.length()>=8;
    };

    @Override
    public boolean registerUser(Connection connection, String userName, String password, String userEmail) throws SQLException {

        String addUser = "insert into customer(customerName, customerEmail) values(?,?)";
        PreparedStatement addUserInUserList = connection.prepareStatement(addUser);
        addUserInUserList.setString(1,userName);
        addUserInUserList.setString(2,userEmail);
        if (addUserInUserList.executeUpdate()==1)
        {
            String addToLogin = "insert into login values(?,?)";
            PreparedStatement loginToAcc = connection.prepareStatement(addToLogin);
            loginToAcc.setString(1, userEmail);
            loginToAcc.setString(2,password);
            if (loginToAcc.executeUpdate()==1)
            {
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println(" Successfully registered user");
                System.out.println("-------------------------------------------------------------------------------------");
                return true;
            }

        }

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(" User not registered");
        System.out.println();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return false;

    }

    @Override
    public String getUserName() {
        System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" Enter your name or 'back' to go back");
        System.out.println("-------------------------------------------------------------------------------------");
            String userName = scan.nextLine();
            if (userName.equalsIgnoreCase("back") || userName.equalsIgnoreCase(""))
                return null;
            else return userName;

    }

    @Override
    public String getuserEmail() {

        while(true){
            System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(" Enter your emailId(abc@def.com) or 'back' to go back");
            System.out.println("-------------------------------------------------------------------------------------");
        String email = scan.nextLine();
        if (email.equalsIgnoreCase("back")||email.equalsIgnoreCase(""))
            return null;

        if (testEmail.test(email))
            return email;
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Invalid email");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        }
//        return null;
    }

    @Override
    public String getUserPassWord() {

        while(true)
        {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" Enter your password(min 8  characters, having at least 1 special character or 'back' to go back)");
            System.out.println("-------------------------------------------------------------------------------------");
            String password = scan.nextLine();
            if (password.equalsIgnoreCase("back")||password.equalsIgnoreCase(""))
                return null;

            if (testPassword.test(password))
                return password;
            else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(" Invalid password");
                System.out.println();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

        }

    }

    @Override
    public UserDetails registrationManager(Connection connection) {
        String userName = getUserName();

        if (Optional.ofNullable(userName).isPresent()) {

            String userEmail = getuserEmail();

            if (Optional.ofNullable(userEmail).isPresent()) {

                String password = getUserPassWord();

                if (Optional.ofNullable(password).isPresent() )
                {
                    try {
                        if (registerUser(connection, userName, password, userEmail))
                        {
                            return getUserDetailsAfterRegis(connection,userEmail);
                        }
                        else return null;
                    } catch (SQLException s) {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(" Error in registration process");
                        System.out.println();
                        System.out.println(s.getMessage());
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        s.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    UserDetails getUserDetailsAfterRegis(Connection connection, String userEmail) throws SQLException
    {
        String getUserDetails = "select * from customer where customerEmail = ?";
        PreparedStatement toGetUser = connection.prepareStatement(getUserDetails);
        toGetUser.setString(1, userEmail);
        ResultSet userDetails = toGetUser.executeQuery();
        if (userDetails.next()) {
            return new UserDetails(userDetails.getInt(1), userDetails.getString(2)
                    , userDetails.getString(3));
        }
        return null;
    }
}
