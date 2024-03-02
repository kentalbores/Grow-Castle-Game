import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bullet> bulletCopy;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Enemy> enemiesCopy;
    ArrayList<GameObject> buttons = new ArrayList<>();
    ArrayList<Boss> bosses = new ArrayList<>();
    final int PANEL_HEIGHT = 600, PANEL_WIDTH = 1000;
    boolean game_running, collided, gamePaused, can_fire = true, upgradeMenuVisible = false, firing, incremented = false;
    Random random = new Random();
    int mousePosX, mousePosY;
    int timesFired = 0, projectileAmount = 0;
    double angle = 0;
    int upgrade1Cost = 5, upgrade2Cost = 5, upgrade3Cost = 5, upgrade4Cost = 5, upgrade5Cost = 5;

    AudioManager audioManager = new AudioManager();
    AffineTransform originalTransform = new AffineTransform();
    ImageIcon slime = new ImageIcon(".\\res\\EnemyAnimation\\SlimeNormal.gif");
    Image slimeImage = slime.getImage();
    ImageIcon backgroundIcon = new ImageIcon(".\\res\\backgroundVF.jpg");
    Image backgroundImage = backgroundIcon.getImage();
    ImageIcon bulletIcon = new ImageIcon(".\\res\\cannonBall.gif");
    Image bullet = bulletIcon.getImage();
    ImageIcon upgradeIcon = new ImageIcon(".\\res\\UpgradeIcon.png");
    Image upgradeImage = upgradeIcon.getImage();
    ImageIcon moneyIcon = new ImageIcon(".\\res\\Money.png");
    Image kenniesImage = moneyIcon.getImage();
    ImageIcon heart = new ImageIcon(".\\res\\Heart.png");
    Image heartImage = heart.getImage();
    ImageIcon bossIcon = new ImageIcon(".\\res\\EnemyAnimation\\IdleBoss.gif");
    Image boss = bossIcon.getImage();
    int animIMG = 0;
    String animLoc = ".\\res\\Appear\\" + animIMG + ".png";
    ImageIcon upgradeMenuIcon = new ImageIcon(animLoc);
    Image upgradeMenu = upgradeMenuIcon.getImage();
    ImageIcon cannonIcon = new ImageIcon(".\\res\\soldier.gif");
    Image cannon = cannonIcon.getImage();
    ImageIcon playerIcon = new ImageIcon(".\\res\\PlayerAnimated.gif");
    Image player = playerIcon.getImage();

    Rectangle safeZone = new Rectangle(0,110,130,400);
    GameObject openUpgradeMenu, upgradeButton, upgradeButton1, upgradeButton2, upgradeButton3, upgradeButton4;
    long lastTime, currentTime, timeDiff, timeCap = 1000000000/60, spawnerTimer, timer2, reloadTime = 1500000000, lastTimeFired,
            spawnRate = 2000000000, projectileInterval = 50000000, animTimer, timeStarted, timeElapsed;
    Thread gameThread = new Thread(()->{
        lastTime = System.nanoTime();
        spawnerTimer = System.nanoTime();
        timer2 = System.nanoTime();
        animTimer = System.nanoTime();
        timeStarted = System.nanoTime();
        while (game_running){
            currentTime = System.nanoTime();
            timeDiff = currentTime - lastTime;
            timeElapsed = (currentTime - timeStarted)/1000000000;
            if (timeDiff > timeCap){
                if (!gamePaused){
                    gameTick();
                }
                lastTime = System.nanoTime();
            }
            if ((currentTime-spawnerTimer) > spawnRate && !gamePaused){
                enemies.add(new Enemy(1000,random.nextInt(450),50,50,10,slimeImage));//50
                spawnerTimer = System.nanoTime();
            }
            if (timeElapsed % 5 == 0 && bosses.isEmpty() && !gamePaused && BuildSettings.currentBuild != 0){
                bosses.add(new Boss(1000, PANEL_HEIGHT/2, 150,100,boss));
            }
//            if (!bosses.isEmpty()){
//                if (timeElapsed % 10 == 0 && !gamePaused && BuildSettings.currentBuild != 0){
//                    bosses.get(0).sprite = new ImageIcon(".\\res\\EnemyAnimation\\SummonBoss.gif").getImage();
//                    System.out.println("change");
//                }else {
//                    bosses.get(0).sprite = boss;
//                }
//            }
            if (firing){
                if ((currentTime-timer2) > projectileInterval){
                    bullets.add(new Bullet(5,290,25,25,PlayerStats.bulletDamage, mousePosX,mousePosY,bullet));
                    //0,290,25,25
                    timesFired++;
                    playSoundFX(0);
                    timer2 = System.nanoTime();
                }else if (timesFired > projectileAmount){
                    firing = false;
                    timesFired = 0;
                }
            }
            if (upgradeMenuVisible){
                if ((currentTime-animTimer) > 30000000 && animIMG < 10){
                    animIMG++;
                    System.out.println(animIMG);
                    animLoc = ".\\res\\Appear\\" + animIMG + ".png";
                    upgradeMenuIcon = new ImageIcon(animLoc);
                    upgradeMenu = upgradeMenuIcon.getImage();
                    animTimer = System.nanoTime();
                    repaint();
                }
            }else {
                if ((currentTime-animTimer) > 30000000 && animIMG > 0){
                    animIMG--;
                    System.out.println(animIMG);
                    animLoc = ".\\res\\Appear\\" + animIMG + ".png";
                    upgradeMenuIcon = new ImageIcon(animLoc);
                    upgradeMenu = upgradeMenuIcon.getImage();
                    animTimer = System.nanoTime();
                    repaint();
                }
            }
//            if ((timeElapsed % 5) == 0 && !incremented){
//                System.out.println("increment");
//                incremented = true;
//                spawnRate -= 1000000000;
//
//            }else if ((timeElapsed % 5)!= 0) {
//                incremented = false;
//            }
            can_fire = (System.nanoTime() - lastTimeFired) > reloadTime;
        }
    });

    GamePanel() throws Exception {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.DARK_GRAY);
        gameThread.start();
        game_running = true;
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseMotionAdapter);
        addKeyListener(this);
        setFocusable(true);
        openUpgradeMenu = new GameObject(900,500,upgradeImage.getWidth(null)/6,upgradeImage.getHeight(null)/6,upgradeImage);
        upgradeButton = new GameObject(330,135,105,130,null);
        upgradeButton1 = new GameObject(450,135,105,130,null);
        upgradeButton2 = new GameObject(570,135,105,130,null);
        upgradeButton3 = new GameObject(390,280,105,130,null);
        upgradeButton4 = new GameObject(510,280,105,130,null);
        //buttons.add(openUpgradeMenu);
        buttons.add(upgradeButton);
        buttons.add(upgradeButton1);
        buttons.add(upgradeButton2);
        buttons.add(upgradeButton3);
        buttons.add(upgradeButton4);
    }
    void gameTick(){
        PlayerStats.updateStats();
        for (Enemy enemy : enemies){
            if (enemy.health <= 0){
                enemies.remove(enemy);
                playSoundFX(1);
                PlayerStats.money++;
                break;
            }
            if (enemy.x <= 130){
                enemies.remove(enemy);
                PlayerStats.currentHealth -= enemy.health;
                break;
            }
            enemy.setPos();
        }
        for (Boss boss1 : bosses){
            boss1.setPos();
        }
        if (bullets.size() != 0){
            for (Bullet bullet : bullets){
                bullet.setPos();
                for (Enemy enemy : enemies){
                    if (bullet.hitBox.intersects(enemy.hitBox)){
                        collided = true;
                        enemy.health -= bullet.damage;
                        playSoundFX(2);
                        break;
                    }
                }
                if (bullet.x>1000 || collided || bullet.y < -100 || bullet.y > 1000){
                    bullets.remove(bullet);
                    collided = false;
                    return;
                }
            }
        }
        //buttonShowing = can_fire;


        repaint();
        revalidate();
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage,0,0, 1000, 600,null);

        g2d.drawImage(player,-10,250,60,100,null);
        g2d.setPaint(Color.BLACK);

        Font font = new Font("Chaparral Pro", Font.BOLD, 60);
        g2d.setFont(font);
        g2d.drawString(String.valueOf(PlayerStats.money),90,570);
        g2d.drawImage(kenniesImage, 10,520,kenniesImage.getWidth(null)/6,kenniesImage.getHeight(null)/6,null);
        g2d.drawImage(heartImage, 10,10, heartImage.getWidth(null)/4, heartImage.getHeight(null)/4,null);
        g2d.drawRect(89,29, 101, 21);
        g2d.setPaint(Color.RED);
        g2d.fillRect(90,30, (int) PlayerStats.currentHealthPercentage, 20);
        enemiesCopy = new ArrayList<>(enemies);
        for (Enemy enemy : enemiesCopy){
            g2d.drawImage(enemy.sprite,enemy.x,enemy.y,enemy.width,enemy.height,null);
        }
        for (Boss boss1 : bosses){
            g2d.drawImage(boss1.sprite, boss1.x, boss1.y, boss1.width, boss1.height, null);
        }
        g2d.setPaint(Color.BLUE);
        bulletCopy = new ArrayList<>(bullets);
        for (Bullet bullet1 : bulletCopy) {
            g2d.drawImage(bullet1.sprite,bullet1.x,bullet1.y,bullet1.width,bullet1.height,null);
        }
        if (upgradeMenuVisible || animIMG != 0){
            g2d.drawImage(upgradeMenu, 110,-90, upgradeMenu.getWidth(null), upgradeMenu.getHeight(null),null);
        }
        g2d.rotate(Math.toRadians(angle), 15,320);//315
        g2d.drawImage(cannon,-10,230,100,100,null);//0,285,100,65
        g2d.setTransform(originalTransform);
        g2d.drawImage(openUpgradeMenu.sprite, openUpgradeMenu.x,openUpgradeMenu.y,openUpgradeMenu.width,openUpgradeMenu.height,null);
//        for (GameObject object : buttons){
//            g2d.drawRect(object.x, object.y, object.width, object.height);
//        }
    }


    MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            mousePosX = e.getX();
            mousePosY = e.getY();
            angle = Math.toDegrees(Math.atan2(e.getY() - 290,e.getX() - 5));//60 100 => 326 15
        }
    };
    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(e.getPoint());
            if (can_fire && !openUpgradeMenu.hitBox.contains(e.getX(),e.getY()) && !gamePaused && !safeZone.contains(e.getX(),e.getY())){
                firing = true;
                lastTimeFired = System.nanoTime();
            }
            if (openUpgradeMenu.hitBox.contains(e.getX(),e.getY())){
                upgradeMenuVisible = !upgradeMenuVisible;
                gamePaused = !gamePaused;
            }
            // && PlayerStats.money >= upgrade1Cost
            // && PlayerStats.money >= upgrade2Cost
            // && PlayerStats.money >= upgrade3Cost
            // && PlayerStats.money >= upgrade4Cost
            // && PlayerStats.money >= upgrade5Cost
            if (upgradeButton.hitBox.contains(e.getX(),e.getY()) && upgradeMenuVisible){
                if (PlayerStats.money >= upgrade1Cost){
                    reloadTime -= 100000000;
                    System.out.println("Upgrade! 1");
                    PlayerStats.money -= upgrade1Cost;
                    playSoundFX(4);
                }else {
                    playSoundFX(3);
                }
            }else if (upgradeButton1.hitBox.contains(e.getX(),e.getY()) && upgradeMenuVisible){
                if (PlayerStats.money >= upgrade2Cost){
                    PlayerStats.bulletDamage += 5;
                    System.out.println(PlayerStats.bulletDamage);
                    System.out.println("Upgrade 2!");
                    PlayerStats.money -= upgrade2Cost;
                    playSoundFX(4);
                }
                else {
                    playSoundFX(3);
                }
            }else if (upgradeButton2.hitBox.contains(e.getX(),e.getY()) && upgradeMenuVisible){
                if (PlayerStats.money >= upgrade3Cost){
                    projectileAmount++;
                    System.out.println("Upgrade 3!");
                    PlayerStats.money -= upgrade3Cost;
                    playSoundFX(4);
                }else {
                    playSoundFX(3);
                }
            }else if (upgradeButton3.hitBox.contains(e.getX(),e.getY()) && upgradeMenuVisible){
                if (PlayerStats.money >= upgrade4Cost){
                    System.out.println("Upgrade 4!");
                    PlayerStats.money -= upgrade4Cost;
                    PlayerStats.maxHealth += 20;
                    playSoundFX(4);
                }else {
                    playSoundFX(3);
                }

            }else if (upgradeButton4.hitBox.contains(e.getX(),e.getY()) && upgradeMenuVisible){
                if (PlayerStats.money >= upgrade5Cost){
                    System.out.println("Upgrade 5!");
                    PlayerStats.money -= upgrade5Cost;
                    PlayerStats.healthRegen++;
                    BuildSettings.currentBuild = 0;
                    System.out.println(BuildSettings.currentBuild);
                    gamePaused = true;
                    playSoundFX(4);
                }else {
                    playSoundFX(3);
                }
            }

            PlayerStats.updateStats();
            repaint();

            //if ()

            //gamePaused = !gamePaused;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    };

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void playSoundFX(int index){
        try {
            audioManager.playAudio(index);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
