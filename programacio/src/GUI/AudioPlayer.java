package GUI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private FloatControl volumeControl;
    private boolean isMuted;

    public AudioPlayer(String clipPath, int volumeValue, boolean continuous) {
        new Thread(() -> {
            File audioFile = new File(clipPath);
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(-30 + volumeValue);
                clip.start();
                if (continuous) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void buttonSound() {
        new AudioPlayer("./programacio/src/GUI/sfx/button_sound.wav", 10, false);
    }

    public static void doAlarm() {
        new AudioPlayer("./programacio/src/GUI/sfx/alarm.wav", 20, false);
    }

    public static AudioPlayer doBGM() {
        return new AudioPlayer("./programacio/src/GUI/sfx/bgm.wav", 0, true);
    }

    public static void doError() {
        new AudioPlayer("./programacio/src/GUI/sfx/error.wav", 20, false);
    }

    public static void doExplosion() {
        new AudioPlayer("./programacio/src/GUI/sfx/explosion-8bit.wav", 20, false);
    }

    public void changeVolume(int newVolume) {
        volumeControl.setValue(-30 + newVolume);
    }

    public boolean isMuted() { return isMuted; }
    public void setMuted(boolean isMuted) { this.isMuted = isMuted; }
}