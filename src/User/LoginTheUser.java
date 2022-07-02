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

public class LoginTheUser implements LoginInterface {
    Scanner scan = new Scanner(System.in);
    Predicate<String> testEmail = e -> {
        Pattern p = Pattern.compile("[a-zA-Z0-9_]+[@][a-z]+[.][a-z]+");
        Matcher m = p.matcher(e);
        return m.matches();
    };


    @Override
    public boolean login(Connection connection, String emailId, String password) throws SQLException
    {
        String checkLogin = "select * from login where customerEmail = ? and customerPassword = ?";
        PreparedStatement checker = connection.prepareStatement(checkLogin);
        checker.setString(1,emailId);
        checker.setString(2,password);
        ResultSet isThereData = checker.executeQuery();
        if (isThereData.isBeforeFirst()) {
            while (isThereData.next()) {
                if (isThereData.isLast() && isThereData.isFirst()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public UserDetails loginManager(Connection connection) {
        String toGoBack = null;
        while(true) {
            if (Optional.ofNullable(toGoBack).isPresent() && !toGoBack.equalsIgnoreCase(""))
                return null;
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" Enter your Login details");
            System.out.println("-------------------------------------------------------------------------------------");

            try {
                String userEmail = getUserEmail();
                if (Optional.ofNullable(userEmail).isPresent()) {
                    String password = getUserPassWord();
                    if (Optional.ofNullable(password).isPresent()) {
                        if (login(connection, userEmail, password)) {
                            System.out.println("-------------------------------------------------------------------------------------");
                            System.out.println("Logged In");
                            System.out.println("-------------------------------------------------------------------------------------");

                            return  new Register().getUserDetailsAfterRegis(connection,userEmail);
                        } else {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            System.out.println("Invalid Email or password");
                            System.out.println("Enter to try again or enter 'back' to go back ");
                            System.out.println();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            toGoBack = scan.nextLine();
                        }
                    }
                    else toGoBack="back";

                }
                else toGoBack="back";
            }
            catch(SQLException s)
            {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Error in accessing database. Please try again later");
                System.out.println();
                System.out.println(s.getMessage());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }

    }

    private String getUserEmail() {
        while (true) {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Enter your emailId(abc@def.com)");
            System.out.println("-------------------------------------------------------------------------------------");

            String email = scan.nextLine();
            if (email.equalsIgnoreCase("back")||email.equalsIgnoreCase(""))
                return null;


            if (testEmail.test(email))
                return email;
            else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Invalid email, try Again or enter 'back to go back");
                System.out.println();
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }

        }
    }

    private String getUserPassWord() {

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Enter your password or 'back' to go back");
        System.out.println("-------------------------------------------------------------------------------------");

        String password = scan.nextLine();
        if (password.equalsIgnoreCase("back")||password.equalsIgnoreCase(""))
            return null;

        return password;


    }
}
