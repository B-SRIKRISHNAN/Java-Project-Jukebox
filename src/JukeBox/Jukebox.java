package JukeBox;


import PlayLlists.PlaylistUser;
import Podcasts.PodcastPlayer;
import Songs.SongPlayer;
import User.LoginTheUser;
import User.Register;
import User.UserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Jukebox implements JukeboxInterface{
    boolean loggedIn=false;
    UserDetails user = null;
    Scanner scan = new Scanner(System.in);
    @Override
    public void jukeboxManager() {
        Connection connection =  null;
        try
        {
             connection = getConnectionToDB();
            outer:
            while (true) {
                System.out.println( " Main Menu");
                System.out.println("*************************************************************************************");
                System.out.println(" 1.Song");
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println(" 2.Podcast");
                System.out.println("-------------------------------------------------------------------------------------");
                if (!loggedIn) {
                    System.out.println(" 3. Register User ");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 4. Login user   ");
                    System.out.println("-------------------------------------------------------------------------------------");
                } else
                {
                    System.out.println(" 3. Playlists");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 4. LogOut");
                    System.out.println("-------------------------------------------------------------------------------------");
                }

                System.out.println(" 0. Exit");
                System.out.println("-------------------------------------------------------------------------------------");
                int choice = scan.nextInt();
                scan.nextLine();
                switch (choice) {
                    case 1:{
                        if (!loggedIn)
                            new SongPlayer().songsManager(connection);
                        else
                            new SongPlayer(true,user).songsManager(connection);

                        break;
                    }
                    case 2:{
                        if (!loggedIn)
                            new PodcastPlayer().podcastManager(connection);
                        else
                            new PodcastPlayer(true,user).podcastManager(connection);
                            // After playlist
                        break;
                    }
                    case 3: {
                        if (!loggedIn)
                        {
                           user = new Register().registrationManager(connection);
                           if (Optional.ofNullable(user).isPresent())
                           {
                               loggedIn=!loggedIn;
                           }
                        }
                        else
                            new PlaylistUser(user).playlistManager(connection);
                        //Add playList manager here
                        break;
                    }
                    case 4: {
                        if (!loggedIn)
                        {
                            user = new LoginTheUser().loginManager(connection);
                            if (Optional.ofNullable(user).isPresent())
                                loggedIn=!loggedIn;
                        }
                        else {
                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println(" Logging Out");
                            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            loggedIn= false;
                            user=null;
                        }
                        break;
                    }
                    default:break outer;

                }
        }
        }
        catch (SQLException s)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Problem with accessing database");
            System.out.println();
            System.out.println(s.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }
        catch (ClassNotFoundException c)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Problem with the JDBC driver");
            System.out.println();
            System.out.println(c.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }
        catch (InputMismatchException i)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Please enter valid input values");
            System.out.println();
            System.out.println(" "+i.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        finally {

            scan.close();
            if (Optional.ofNullable(connection).isPresent())
                try {
                    connection.close();
                }
            catch (SQLException s)
            {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(" Error closing the connection");
                System.out.println();
                System.out.println(s.getMessage());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }
    }

    @Override
    public Connection getConnectionToDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","root@123");

    }


}
