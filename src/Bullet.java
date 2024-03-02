import java.awt.*;

public class Bullet extends GameObject{
    int damage;
    double xDelta, yDelta, mousePosX, mousePosY, xDec,yDec, speedPercentage = 0.08;
    double angle, hypotenuse = 300,rise,run;
    Bullet(int x, int y, int width, int height, int damage, int xDir, int yDir, Image sprite) {
        super(x, y, width, height, sprite);
        hitBox = new Rectangle(x,y,width,height);
        this.damage = damage;
//        this.xDelta = (float)xDir - 15;
//        this.yDelta = (float)yDir - 15;
        this.mousePosX = xDir;
        this.mousePosY = yDir;
        angle = Math.atan2(mousePosY - 290, mousePosX - 5);
        rise = hypotenuse * Math.sin(angle);
        run = hypotenuse * Math.cos(angle);
//        System.out.println("rise: " + rise + " : " + "dagan: " + run);
//        System.out.println(angle + " angle in deg: " + Math.toDegrees(angle));
    }

    @Override
    void setPos() {
        super.setPos();
        xDec = run * speedPercentage;
        yDec = rise * speedPercentage;
        x+=(xDec);
        y+=(yDec);
//        xDec += xDelta;
//        yDec += yDelta;
//        y += Math.round(yDec/4000);
//        x += Math.round(xDec/4000);
//        System.out.println("y: " + y);
//        System.out.println("yDelta: " + yDelta);

    }
}
