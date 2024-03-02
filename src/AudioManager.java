import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AudioManager {
    AudioInputStream audioStream;
    Clip clip;
    FloatControl volumeControl;
    ArrayList<File> files = new ArrayList<>();
    AudioManager() throws Exception {
        files.add(new File(".\\res\\SoundFX\\shoot.wav"));
        files.add(new File(".\\res\\SoundFX\\slimeDeath2.wav"));
        files.add(new File(".\\res\\SoundFX\\hit.wav"));
        files.add(new File(".\\res\\SoundFX\\error.wav"));
        files.add(new File(".\\res\\SoundFX\\rizz.wav"));
    }

    public void playAudio(int index) throws Exception {
        audioStream = AudioSystem.getAudioInputStream(files.get(index));
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (index == 3){
            volumeControl.setValue(-20);
        }else if (index == 4){
            volumeControl.setValue(-5);
        }else {
            volumeControl.setValue(-15);
        }
        clip.start();
        System.out.println("Started");
    }
}
