package AudioPlayer;

public class AudioTrack {
    private String filePath;
    public AudioTrack(String filePath)
    {
        this.filePath=filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
