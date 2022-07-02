package AudioPlayer;

import Songs.Song;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AudioPlayerTest {

    AudioPlayer player;
    String filePath;
    Song song;
    @BeforeEach
    void setUp() {
        filePath ="TestResources/sample-12s.wav";
        player = new AudioPlayer();
        song= new Song(1,"pop","abc","def","ghi",filePath);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void audioManagerTest() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        player.audioManager(song);
    }
}