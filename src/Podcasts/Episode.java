package Podcasts;

import AudioPlayer.AudioTrack;

import java.sql.Date;

public class Episode extends AudioTrack {
    private int episodeId;
    private String episodeName;
    private Date episodeDate;
    private String podcastName;
    public Episode(int episodeId, String episodeName, Date episodeDate,String podcastName, String episodeFile)
    {
        super(episodeFile);
        this.episodeId=episodeId;
        this.episodeName=episodeName;
        this.podcastName=podcastName;
        this.episodeDate=episodeDate;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public Date getEpisodeDate() {
        return episodeDate;
    }

    public void setEpisodeDate(Date episodeDate) {
        this.episodeDate = episodeDate;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    @Override
    public String toString() {
        System.out.println("____________________________________________________________________________________________________________________________");
        return " Episode:::" +
                "  Episode Name: '" + episodeName + '\'' +
                "  Episode Date: " + episodeDate +
                "   Podcast Name: '" + podcastName + '\'';
    }
}
