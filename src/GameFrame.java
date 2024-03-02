import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    GamePanel gamePanel = new GamePanel();
    MenuPanel menuPanel = new MenuPanel();
    ArrayList<JPanel> builds = new ArrayList<>();
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(".\\res\\SoundFX\\MainBG.wav"));
    Clip clip = AudioSystem.getClip();
    FloatControl volumeControl;
    int lastBuild = BuildSettings.currentBuild;
    GameFrame() throws Exception {
        clip.open(audioStream);
        clip.setMicrosecondPosition(6000000);
        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-22);
        clip.start();
        builds.add(menuPanel);
        builds.add(gamePanel);
        add(builds.get(0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        do {
            if (volumeControl.getValue() <= -10) {
                volumeControl.setValue(volumeControl.getValue() + 0.2f);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (lastBuild != BuildSettings.currentBuild) {
                switchPanel(BuildSettings.currentBuild);
                lastBuild = BuildSettings.currentBuild;
            }
        } while (BuildSettings.currentBuild <= 1);
    }
    public void switchPanel(int build) throws Exception {
        clip.close();
//        builds.set(0, new MenuPanel());
//        builds.set(1,new GamePanel());
        remove(builds.get(lastBuild));
        add(builds.get(build));
        repaint();
        revalidate();
    }
}
