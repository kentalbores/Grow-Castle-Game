import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {
    final int PANEL_HEIGHT = 600, PANEL_WIDTH = 1000;
    boolean main_menu_scene = true, exitSelected, playSelected;
    ImageIcon bgIcon = new ImageIcon(".\\res\\MainMenuRes\\bghd2.gif");
    Image bgImage = bgIcon.getImage();
    ImageIcon PlayButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\PlayButton.png");
    Image PlayButton = PlayButtonIcon.getImage();
    ImageIcon ExitButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\ExitButton.png");
    Image ExitButton = ExitButtonIcon.getImage();
    Rectangle playButtonHitBox, exitButtonHitBox;
    long lastTime, currentTime, timeDiff, timeCap = 1000000000/30;
    Thread gameThread = new Thread(()-> {
        lastTime = System.nanoTime();
        while (main_menu_scene) {
            currentTime = System.nanoTime();
            timeDiff = currentTime - lastTime;
            if (timeDiff > timeCap) {
                if (playSelected){
                    PlayButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\PlayButtonSelected.png");
                    PlayButton = PlayButtonIcon.getImage();
                } else if (exitSelected){
                    ExitButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\ExitButtonSelected.png");
                    ExitButton = ExitButtonIcon.getImage();
                } else {
                    PlayButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\PlayButton.png");
                    PlayButton = PlayButtonIcon.getImage();
                    ExitButtonIcon = new ImageIcon(".\\res\\MainMenuRes\\ExitButton.png");
                    ExitButton = ExitButtonIcon.getImage();
                }

                repaint();
                lastTime = System.nanoTime();
            }
        }
    });

    MenuPanel(){
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.DARK_GRAY);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseMotionAdapter);
        addKeyListener(keyAdapter);
        setFocusable(true);
        gameThread.start();
        playButtonHitBox = new Rectangle(PANEL_WIDTH/2- PlayButton.getWidth(null)/6,(int)(PANEL_HEIGHT/2.6),PlayButton.getWidth(null)/3,PlayButton.getHeight(null)/3);
        exitButtonHitBox = new Rectangle(PANEL_WIDTH/2 - ExitButton.getWidth(null)/6,(int)(PANEL_HEIGHT/1.8),ExitButton.getWidth(null)/3, ExitButton.getHeight(null)/3);
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bgImage,0,0,PANEL_WIDTH + 10,PANEL_HEIGHT + 10,null);
        g2d.drawImage(PlayButton, PANEL_WIDTH/2 - PlayButton.getWidth(null)/6,(int)(PANEL_HEIGHT/2.6),PlayButton.getWidth(null)/3, PlayButton.getHeight(null)/3,null);
        g2d.drawImage(ExitButton, PANEL_WIDTH/2 - ExitButton.getWidth(null)/6,(int)(PANEL_HEIGHT/1.8),ExitButton.getWidth(null)/3, ExitButton.getHeight(null)/3,null);
    }
    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (playSelected){
                System.out.println("Playing");
                BuildSettings.currentBuild++;
                //gameFrame.switchPanel();
            }else if (exitSelected){
                System.exit(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }
    };
    MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            if (exitButtonHitBox.contains(e.getX(),e.getY())){
                exitSelected = true;
            } else if (playButtonHitBox.contains(e.getX(),e.getY())){
                playSelected = true;
            } else {
                exitSelected = false;
                playSelected = false;
            }
        }
    };
    KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            System.out.println("pressed");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
        }
    };
}
