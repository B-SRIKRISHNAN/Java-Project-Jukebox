package Songs;

import AudioPlayer.AudioTrack;

import java.util.Optional;

public class Song extends AudioTrack {
    private int songId;
    private String genre;
    private String songName;
    private String songAlbum;
    private String songAuthor;


    public Song(int songId, String genre, String songName, String songAlbum, String songAuthor, String songFile) {
        super(songFile);
        this.songId = songId;
        this.genre = genre;
        this.songName = songName;
        this.songAlbum = songAlbum;
        this.songAuthor = songAuthor;

    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public String getSongAuthor() {
        return songAuthor;
    }

    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    @Override
    public String toString() {
        if(Optional.ofNullable(songAlbum).isEmpty())
            songAlbum = "Not Available";
        System.out.println("____________________________________________________________________________________________________________________________");
        return " Song::::" +
                " Song Name: '" + songName + '\'' +
                " Genre: '" + genre + '\'' +
                "  Artist: '" + songAuthor + '\''+
                "  Album: '" + songAlbum + '\'';
    }
}
