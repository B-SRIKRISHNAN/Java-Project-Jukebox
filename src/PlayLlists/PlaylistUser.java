package PlayLlists;

import AudioPlayer.AudioListManager;
import AudioPlayer.AudioTrack;
import Podcasts.Episode;
import Songs.Song;
import User.UserDetails;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PlaylistUser implements PlaylistInterface{
    Scanner scan = new Scanner(System.in);
    UserDetails user;
    Song songToAdd = null;
    Integer podcastToAdd = null;

    public PlaylistUser(UserDetails user){

        this.user=user;

    }

    // to add from songs
    public PlaylistUser(UserDetails user, Song songToAdd)
    {
        this.user=user;
        this.songToAdd =  songToAdd;
    }

    // To add from podcasts
    public PlaylistUser(UserDetails user, int podcastToAdd)
    {
        this.user = user;
        this.podcastToAdd = podcastToAdd;
    }
    @Override
    public void displayAllPlayLists(ResultSet allPlaylists) throws SQLException {
        System.out.println("=====================================================================================");
        System.out.println(" All the available Playlists");
        System.out.println("=====================================================================================");
        int index = 1;
        if (!allPlaylists.isBeforeFirst())
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" You have no playlists");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        while (allPlaylists.next())
        {
            System.out.println("____________________________________________________________________________________________________________________________");
            System.out.println(" " + index + ". " + allPlaylists.getString(2));
            index++;
        }
        // get cursor back to zero
        allPlaylists.beforeFirst();
        System.out.println("=====================================================================================");
        System.out.println();

    }

    @Override
    public boolean createNewPlaylist(Connection connection) throws SQLException {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(" Enter name of playlist");
        System.out.println("-------------------------------------------------------------------------------------");

        String addPlaylist = "insert into playlists(playlistName, customerId) values(?,?)";
        PreparedStatement toPlaylist = connection.prepareStatement(addPlaylist);
        toPlaylist.setString(1,scan.nextLine());
        toPlaylist.setInt(2,user.getCustomerId());
        if (toPlaylist.executeUpdate()==1)
        {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" playlist has been created");
            System.out.println("-------------------------------------------------------------------------------------");

            return true;
        }
        return false;
    }

    public ResultSet getPlaylists(Connection connection) throws SQLException
    {

        String getPlaylists = "select playlistId, playlistName from playlists where customerId = ? order by playlistId";
        PreparedStatement toGetPlaylist = connection.prepareStatement(getPlaylists,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        toGetPlaylist.setInt(1,user.getCustomerId());
        return toGetPlaylist.executeQuery();

    }

    @Override
    public List<AudioTrack> getTracksFromAPlaylist(Connection connection, int playListId) throws SQLException {
        List<AudioTrack> allTracks = new ArrayList<>() ;


        String songsInPlayList = " select s.songId, s.songGenre, s.songName, sd.songAlbum , sd.songAuthor, s.songFile from " +
                "playlists pl join songsInPlaylist spl join songs s join songDetails sd " +
                "on(pl.playlistId = spl.playlistId and spl.songId = s.songId and s.songDetailsId = sd.songDetailsId) where " +
                "pl.playlistId = ? order by s.songName";
        PreparedStatement getSongs = connection.prepareStatement(songsInPlayList);
        getSongs.setInt(1, playListId);
        ResultSet songs = getSongs.executeQuery();
        while (songs.next())
        {
            allTracks.add(new Song( songs.getInt(1),songs.getString(2),songs.getString(3),
                    songs.getString(4), songs.getString(5),songs.getString(6)));
        }


        String episodeInPlaylist = "select e.episodeId, e.episodeName, e.episodeDate, p.podcastName, e.episodeFile " +
                "from playlists pl join podcastsInPlaylist ppl join podcasts p join episodes e " +
                " on(pl.playlistId = ppl.playlistId and  ppl.podcastId = p.podcastId and p.podcastId = e.podcastId) " +
                "where pl.playlistId = ? order by p.podcastName, e.episodeDate, e.episodeId";
        //p.podcastName,
        PreparedStatement getPodcasts = connection.prepareStatement(episodeInPlaylist);
        getPodcasts.setInt(1,playListId);
        ResultSet episodesInPodcast = getPodcasts.executeQuery();
        while(episodesInPodcast.next())
        {
            allTracks.add(new Episode(episodesInPodcast.getInt(1),episodesInPodcast.getString(2),
                    episodesInPodcast.getDate(3),episodesInPodcast.getString(4),
                    episodesInPodcast.getString(5)));

        }

        return allTracks;
    }

    @Override
    public void displayTracksInPlayList(List<AudioTrack> tracks) throws SQLException  {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(" Tracks in the playlist are :");
        System.out.println("-------------------------------------------------------------------------------------");

        for (int i=0; i< tracks.size();i++)
        {
            int number = i+1;
            System.out.println("-------------------------------------------------------------------------------------");
            AudioTrack track = tracks.get(i);
            if (track.getClass()==Song.class)
                System.out.println(" " + number + ". "+ track);
            else if(track.getClass()==Episode.class)
                System.out.println(" " + number + ". "+ track);


        }

    }

    @Override
    public void playlistManager(Connection connection) {

        ResultSet playlists;
        List<AudioTrack> tracksInPlaylist;
        int selectedPlaylist = 0;
        int choice;
        try {
            outer:
            while (true) {

                playlists = getPlaylists(connection);
                System.out.println(" PLAYLISTS");
                System.out.println("=====================================================================================");
                System.out.println(" 1. Select a playlist ");
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println(" 2. create a new playlist");
                System.out.println("-------------------------------------------------------------------------------------");
                if (Optional.ofNullable(songToAdd).isEmpty()&&Optional.ofNullable(podcastToAdd).isEmpty())
                {
                    System.out.println(" 3. Choose a track to play from the available tracks / autoplay");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 4. Delete the selected playlist");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 5. Go Back");
                    System.out.println("-------------------------------------------------------------------------------------");
                }
                else if (Optional.ofNullable(songToAdd).isEmpty())
                {
                    System.out.println(" 3. Add the podcast to the playlist");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 4. Go Back");
                    System.out.println("-------------------------------------------------------------------------------------");
                }
                else
                {
                    System.out.println(" 3. Add the song to the playlist");
                    System.out.println("-------------------------------------------------------------------------------------");
                    System.out.println(" 4. Go Back");
                    System.out.println("-------------------------------------------------------------------------------------");
                }



                System.out.println("=====================================================================================");

                choice= scan.nextInt();
                scan.nextLine();

                switch (choice)
                {
                    case 1:
                    {
                        displayAllPlayLists(playlists);
                        System.out.println("-------------------------------------------------------------------------------------");
                        System.out.println(" Enter a playlist choice");
                        System.out.println("-------------------------------------------------------------------------------------");
                        int playlistChoice = scan.nextInt();
                        selectedPlaylist = getPlaylistId(playlists,playlistChoice);
                        break;
                    }

                    case 2 :{
                        if (createNewPlaylist(connection))
                            System.out.println("-------------------------------------------------------------------------------------");
                            System.out.println(" Created a playlist");
                        System.out.println("-------------------------------------------------------------------------------------");

                             break;
                    }

                    case 3: {


                         if (Optional.ofNullable(songToAdd).isEmpty()&&Optional.ofNullable(podcastToAdd).isEmpty())
                         {
                             tracksInPlaylist = getTracksFromAPlaylist(connection, selectedPlaylist);

                             System.out.println("-------------------------------------------------------------------------------------");
                             System.out.println("Do you wish to autoplay(1 - yes, 2- no)?");
                             System.out.println("-------------------------------------------------------------------------------------");
                             int autoplay = scan.nextInt();
                             if (autoplay==1)
                             {
                                 displayTracksInPlayList(tracksInPlaylist);
                                 new AudioListManager(tracksInPlaylist,true).manageList();
                             }
                             else
                             {

                                 displayTracksInPlayList(tracksInPlaylist);
                                 System.out.println("-------------------------------------------------------------------------------------");
                                 System.out.println(" Choose the track to start from");
                                 System.out.println("-------------------------------------------------------------------------------------");
                                 int trackToStart = scan.nextInt();
                                 if (trackToStart == 0)
                                     trackToStart = 1;
                                 System.out.println("-------------------------------------------------------------------------------------");
                                 System.out.println(" Do you wish to loop the playlist(y/n)");
                                 System.out.println("-------------------------------------------------------------------------------------");
                                 scan.nextLine();
                                 String toLoop = scan.nextLine();
                                 if (toLoop.equalsIgnoreCase("y"))
                                     new AudioListManager(tracksInPlaylist, true, false, trackToStart).manageList();
                                 else {
                                     System.out.println("-------------------------------------------------------------------------------------");
                                     System.out.println(" Do you wish to shuffle(y/n)");
                                     System.out.println("-------------------------------------------------------------------------------------");
                                     String toShuffle = scan.nextLine();
                                     if (toShuffle.equalsIgnoreCase("y"))
                                         new AudioListManager(tracksInPlaylist, false, true, trackToStart).manageList();
                                     else
                                         new AudioListManager(tracksInPlaylist, trackToStart).manageList();


                                 }
                             }
                        }
                        else if (Optional.ofNullable(songToAdd).isEmpty())
                        {
                            if (addThePodcast(connection, podcastToAdd ,selectedPlaylist ))
                            {
                                System.out.println("-------------------------------------------------------------------------------------");
                                System.out.println(" Podcast has been added ");
                                System.out.println("-------------------------------------------------------------------------------------");
                            }
                            else {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println(" Podcast cannot be added. Possible duplicate Podcast");
                                System.out.println();
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }

                        }
                        else {
                            if (addTheSong(connection, songToAdd.getSongId(),selectedPlaylist ))
                            {
                                System.out.println("-------------------------------------------------------------------------------------");
                                System.out.println(" Song has been added");
                                System.out.println("-------------------------------------------------------------------------------------");
                            }
                            else {
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                System.out.println(" Song cannot be added. Possible duplicate song");
                                System.out.println();
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            }

                        }
                        break outer;
                    }

                    case 4:
                    {

                        if (Optional.ofNullable(songToAdd).isEmpty()&&Optional.ofNullable(podcastToAdd).isEmpty())
                        {
                            deleteThePlayList(connection,selectedPlaylist);
                        }
                        else
                            break outer;
                        break;
                    }
                    case 5:
                    {
                        if (Optional.ofNullable(songToAdd).isEmpty()&&Optional.ofNullable(podcastToAdd).isEmpty())
                            break outer;

                    }
                        default: {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(" Enter a valid choice");
                        System.out.println();
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }

                }


            }
        }
        catch(SQLException s)
        {

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Error in using playlists");
            System.out.println();
            System.out.println(s.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

    }

    @Override
    public void deleteThePlayList(Connection connection, int playlistId) throws SQLException {
        String delete = "delete from playlists where playlistId = ?";
        PreparedStatement toDelete = connection.prepareStatement(delete);
        toDelete.setInt(1,playlistId);
        if (toDelete.executeUpdate()==1)
        {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println(" Playlist has been deleted");
            System.out.println("-------------------------------------------------------------------------------------");

        }
        else
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Playlist could not be deleted");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }

    }

    int getPlaylistId(ResultSet playlists, int choiceOfPlaylist) throws SQLException
    {
        int index = 1;
        while (playlists.next())
        {
            if (index==choiceOfPlaylist)
                return playlists.getInt(1);
            index ++;
        }
        return 0;
    }

    @Override
    public boolean addTheSong(Connection connection, int songId, int playlistId) throws SQLException
    {
        if (checkIfSongNotPresent(connection, songId, playlistId))
        {
            String addSong = "insert into songsInPlaylist values(?,?)";
            PreparedStatement add = connection.prepareStatement(addSong);
            add.setInt(1, playlistId);
            add.setInt(2, songId);
            return add.executeUpdate() == 1;
        }
        else
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Song already exists in playlist");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }

    }

    boolean checkIfSongNotPresent(Connection connection, int songId, int playlistId) throws SQLException
    {
        String checkMatch = "select * from songsInPlaylist where songId = ? and playlistId = ?";
        PreparedStatement checkExists = connection.prepareStatement(checkMatch);
        checkExists.setInt(1,songId);
        checkExists.setInt(2, playlistId);
        return !checkExists.executeQuery().isBeforeFirst();
    }

    @Override
    public boolean addThePodcast(Connection connection, int podcastId, int playlistId) throws SQLException
    {
        if(checkIfPodcastNotPresent(connection, podcastId,playlistId))
        {
            String addPodcast = "insert into podcastsInPlaylist values(?,?)";
            PreparedStatement add = connection.prepareStatement(addPodcast);
            add.setInt(1, playlistId);
            add.setInt(2, podcastId);
            return add.executeUpdate() == 1;
        }
        else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Podcast already present in playlist");
            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
    }

    boolean checkIfPodcastNotPresent(Connection connection, int podcastId, int playlistId) throws SQLException
    {
        String checkMatch = "select * from podcastsInPlayList where podcastId = ? and playlistId = ?";
        PreparedStatement checkExists = connection.prepareStatement(checkMatch);
        checkExists.setInt(1,podcastId);
        checkExists.setInt(2, playlistId);
        return !checkExists.executeQuery().isBeforeFirst();
    }
}
