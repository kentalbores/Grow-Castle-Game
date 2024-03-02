import java.awt.*;

public class Enemy extends GameObject{
    int health;
    Enemy(int x, int y, int width, int height, int health, Image sprite){
        super(x,y,width,height,sprite);
        this.health = health;
    }

    @Override
    void setPos() {

        super.setPos();
        x--;
    }
}
