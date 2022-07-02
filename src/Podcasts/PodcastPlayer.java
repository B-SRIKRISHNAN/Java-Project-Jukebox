package Podcasts;

import AudioPlayer.AudioListManager;
import AudioPlayer.AudioTrack;
import PlayLlists.PlaylistUser;
import User.UserDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PodcastPlayer implements PodcastInterface{
    Scanner scan = new Scanner(System.in);
    boolean isLoggedIn = false;
    UserDetails user = null;
    public PodcastPlayer(boolean isLoggedIn, UserDetails user)
    {
        this.isLoggedIn=isLoggedIn;
        this.user=user;
    }
    public PodcastPlayer()
    {

    }

    @Override
    public void displayAllPodcasts(Connection connection) throws SQLException {
        System.out.println("=====================================================================================");
        System.out.println(" All the available Podcasts");
        System.out.println("=====================================================================================");
        String selectPodcasts = "select podcastName, celebrity from podcasts order by podcastName";
        Statement podcasts = connection.createStatement();
        ResultSet allPodcasts = podcasts.executeQuery(selectPodcasts);
        while(allPodcasts.next())
        {
            System.out.println("----------------------------------------------------------------------------------------------------------------------");
            System.out.format(" Podcast Name: %s      Celebrity: %s \n",allPodcasts.getString(1),allPodcasts.getString(2));
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
        System.out.println("=====================================================================================");
        System.out.println();
    }

    @Override
    public ResultSet search(String podcastDetail, Connection connection) throws SQLException {
        String searchPodcast = "select * from podcasts where podcastName like concat('%', ?, '%') " +
                "or celebrity like concat('%', ?, '%') order by podcastName";
        PreparedStatement getPodcasts = connection.prepareStatement(searchPodcast,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        getPodcasts.setString(1,podcastDetail);
        getPodcasts.setString(2,podcastDetail);
        ResultSet searchResults = getPodcasts.executeQuery();
        displaySearchResults(searchResults);

        return searchResults;
    }

    void displaySearchResults(ResultSet resultsOfSearch) throws SQLException
    {
        int listIndex = 1;
        if (!resultsOfSearch.isBeforeFirst()) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" No matching results found!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        while (resultsOfSearch.next())
        {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.format("%d.      Podcast Name: %s     Celebrity: %5s  \n",listIndex, resultsOfSearch.getString(2),
                    resultsOfSearch.getString(3));
            listIndex++;
        }
        resultsOfSearch.beforeFirst();

    }

    @Override
    public int selectAPodcast(ResultSet searchResultOfPodcasts)  throws SQLException
    {
        int listIndex = 1;
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println(" Enter the number of the Podcast to be played, 0 if exit search");
        System.out.println("-------------------------------------------------------------------------------------");
        int podcastChoice = scan.nextInt();
        while(searchResultOfPodcasts.next())
        {
            if (listIndex==podcastChoice)
            {
                return searchResultOfPodcasts.getInt(1);
            }
            listIndex++;

        }
        return 0;
    }

    @Override
    public List<AudioTrack> getAllEpisodesInThePodcast(int podCastId, Connection connection) throws SQLException {
        List<AudioTrack> episodesList = new ArrayList<>();
//        String getEpisodesInPodcast = "select e.episodeId, e.episodeName, e.episodeDate, p.podcastName, e.episodeFile from podcasts p join episodes e using(podcastId) where p.podcastId = ? order by e.episodeDate, e.episodeName ";
        String getEpisodesInPodcast = "select e.episodeId, e.episodeName, e.episodeDate, p.podcastName, e.episodeFile " +
                " from podcasts p join episodes e using(podcastId) " +
                " where p.podcastId = ? " +
                " order by e.episodeDate, e.episodeName";
        PreparedStatement getEpisode = connection.prepareStatement(getEpisodesInPodcast);
        getEpisode.setInt(1,podCastId);
        ResultSet episodesInPodcast = getEpisode.executeQuery();

        while (episodesInPodcast.next())
        {
            episodesList.add(new Episode(episodesInPodcast.getInt(1),episodesInPodcast.getString(2),
                    episodesInPodcast.getDate(3),episodesInPodcast.getString(4)
                    , episodesInPodcast.getString(5)));
        }
        return episodesList;
    }

    @Override
    public void podcastManager(Connection connection) {
        int choice;
        List<AudioTrack> episodesInPodcast =  null;
        int podcastId = 0;
        try {
            outer:
            while (true) {
                System.out.println("  PODCASTS ");
                System.out.println("=====================================================================================");
                System.out.println(" 1. Display all podcasts");
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println(" 2. search a podcast");
                System.out.println("-------------------------------------------------------------------------------------");
                System.out.println(" 3. play episodes in the podcast");
                System.out.println("-------------------------------------------------------------------------------------");
                if (isLoggedIn)
                {
                    System.out.println(" 4. Add podcast to Playlist( Duplicate entry not allowed )");
                    System.out.println("-------------------------------------------------------------------------------------");
                }
                else {
                    System.out.println(" 4. Add podcast to Playlist(Login to avail this feature)");
                    System.out.println("-------------------------------------------------------------------------------------");
                }
                System.out.println(" 5. Go back");
                System.out.println("-------------------------------------------------------------------------------------");
                choice = scan.nextInt();

                scan.nextLine();

                switch (choice) {
                    case 1:{

                        displayAllPodcasts(connection);
                        break;
                    }
                    case 2:{
                        System.out.println("-------------------------------------------------------------------------------------");
                        System.out.println(" Enter the podcast name or celebrity name to search");
                        System.out.println("-------------------------------------------------------------------------------------");
                        String toSearch = scan.nextLine();
                        podcastId =  selectAPodcast(search(toSearch,connection));
                        if (podcastId==0)
                            break;
                        episodesInPodcast = getAllEpisodesInThePodcast(podcastId,connection);
                        break;
                    }
                    case 3:{
                        new AudioListManager(episodesInPodcast,1).manageList();
                        break;
                    }
                    case 4:{
                        if (isLoggedIn)
                        {
                            new PlaylistUser(user,podcastId).playlistManager(connection);
                        }
                        else {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            System.out.println(" Please login to avail this feature");
                            System.out.println();
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                        break;
                    }
                    case 5: {
                        break outer;}
                    default: {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(" Enter a valid choice");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }

            }
        }catch (SQLException s)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(" Error in PodcastPlayer");
            System.out.println();
            System.out.println(s.getMessage());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }

    }
}
