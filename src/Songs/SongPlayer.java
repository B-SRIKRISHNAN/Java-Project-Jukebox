package Songs;

import AudioPlayer.AudioPlayer;
import AudioPlayer.AudioTrack;
import PlayLlists.PlaylistUser;
import User.UserDetails;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Scanner;

public class SongPlayer implements SongInterface{
    Scanner scan = new Scanner(System.in);
    boolean isLoggedIn = false;
    UserDetails user = null;


    public SongPlayer(boolean login, UserDetails user){
        this.isLoggedIn=login;
        this.user = user;
    }
    public SongPlayer()
    {

    }



    @Override
    public void displayAllSongs(Connection connection) throws SQLException {

        System.out.println("=====================================================================================");
        System.out.println(" All the available songs");
        System.out.println("=====================================================================================");
        String displayQuery = "select songName, songGenre, songAuthor, songAlbum " +
                " from songs join songDetails using (songDetailsId) order by songName";
        Statement display = connection.createStatement();
        ResultSet allSongs = display.executeQuery(displayQuery);
        while (allSongs.next())
        {
            String album = allSongs.getString(4);
            if (Optional.ofNullable(album).isEmpty())
                album = "Not Available";
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.format(" Song Name: %s     Genre: %s     Artist: %s     Album:%s\n",allSongs.getString(1),allSongs.getString(2),
                    allSongs.getString(3),album);

        }
        System.out.println("=====================================================================================");
        System.out.println();
    }

    @Override
    public ResultSet search(String songDetail, Connection connection) throws SQLException{
        String searchSongs = "select songId,songGenre, songName, songAlbum , songAuthor, songFile " +
                "from songs join songDetails using(songDetailsId) " +
                "where songName like concat('%', ?, '%')" +
                "or songAlbum like concat('%', ?, '%') " +
                "or songAuthor like concat('%', ?, '%') order by songId ";
        PreparedStatement songSearcher = connection.prepareStatement
                (searchSongs, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        songSearcher.setString(1,songDetail);
        songSearcher.setString(2,songDetail);
        songSearcher.setString(3,songDetail);
        ResultSet searchResults = songSearcher.executeQuery();
        displaySearchResults(searchResults);
        return searchResults;
    }

    @Override
    public AudioTrack selectTheTrack(ResultSet resultsOfSearch) throws SQLException {
        int listIndex = 1;
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(" Enter the number of the song to be played, 0 if exit search");
        System.out.println("-------------------------------------------------------------------------------------");
        int songChoice = scan.nextInt();
        AudioTrack chosenSong;
        while(resultsOfSearch.next())
        {
            if (listIndex==songChoice)
            {
                chosenSong = new Song(resultsOfSearch.getInt(1),resultsOfSearch.getString(2),
                        resultsOfSearch.getString(3), resultsOfSearch.getString(4),
                        resultsOfSearch.getString(5), resultsOfSearch.getNString(6));
                resultsOfSearch.close();
                return chosenSong;
            }
            listIndex++;

        }
        return null;
    }

    void displaySearchResults(ResultSet resultsOfSearch) throws SQLException
    {
        int listIndex = 0;
        if (!resultsOfSearch.isBeforeFirst()) {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" No matching results found!!");
            System.out.println("-------------------------------------------------------------------------------------");
            return;
        }
        while (resultsOfSearch.next())
        {
            listIndex++;
            String album = resultsOfSearch.getString(4);
            if (Optional.ofNullable(album).isEmpty())
                album = "Not Available";
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.format("%d.        SongName : %s        Genre:%s        Artist:%s        Album:%s \n",listIndex, resultsOfSearch.getString(3),
                    resultsOfSearch.getString(2), album,
                    resultsOfSearch.getString(5));
        }
        resultsOfSearch.beforeFirst();
    }

    @Override
    public void songsManager(Connection connection) {
        int userChoice;
        AudioTrack songToBePlayed = null;
        try {
            outer:
            while (true) {
                System.out.println("  SONGS ");
                System.out.println("=====================================================================================");
                System.out.println(" 1.Display all songs");
                System.out.println("-------------------------------------------------------------------------------------");

                System.out.println(" 2.Search a song");
                System.out.println("-------------------------------------------------------------------------------------");

                System.out.println(" 3. Play the selected Track");
                System.out.println("-------------------------------------------------------------------------------------");

                if (!isLoggedIn)
                {System.out.println(" 4. Add the song to playlist (Login to get this feature)");
                System.out.println("-------------------------------------------------------------------------------------");
                }

                else {
                    System.out.println(" 4. Add the song to the playlist (Duplicate entry not allowed)");
                    System.out.println("-------------------------------------------------------------------------------------");
                }
                System.out.println(" 5. Go Back");
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println("=====================================================================================");

                userChoice = scan.nextInt();
                scan.nextLine();
                switch (userChoice)
                {
                    case 1:
                    {
                        displayAllSongs(connection);break;
                    }
                    case 2:

                    {
                        System.out.println("-------------------------------------------------------------------------------------");
                        System.out.println(" Enter the songName or ArtistName or Album of the song to be searched");
                        System.out.println("-------------------------------------------------------------------------------------");
                        String songTobeSearched = scan.nextLine();
                        songToBePlayed = selectTheTrack(search(songTobeSearched,connection));
                        break;
                    }
                    case 3: {
                        if (Optional.ofNullable(songToBePlayed).isPresent())
                        {
                            try {

                                new AudioPlayer().audioManager(songToBePlayed);

                            } catch (UnsupportedAudioFileException us) {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println(" The file format is Not supported. Please use only .wav");
                                System.out.println();
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                us.printStackTrace();
                            } catch (IOException e) {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println(" Error with audioFile");
                                System.out.println();
                                e.printStackTrace();
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            } catch (LineUnavailableException l) {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println("Error in playing the file");
                                System.out.println();
                                l.printStackTrace();
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }
                        }
                        break;
                    }
                    case 4:
                    {
                        if (isLoggedIn)
                        {
                            new PlaylistUser(user,(Song) songToBePlayed).playlistManager(connection);
                        }
                        else {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            System.out.println(" Please login to avail this feature");
                            System.out.println();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                        break;
                    }
                    case 5:
                    {
                        break outer;
                    }


                    default:
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(" Enter a valid choice");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("Error in SongPlayer");
            System.out.println();
            System.out.println(e.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

    }
}
