import java.awt.*;

public class GameObject {
    int x, y, width, height;
    Image sprite;
    Rectangle hitBox;
    GameObject(int x, int y, int width, int height, Image sprite){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        hitBox = new Rectangle(x,y,width,height);
    }
    void setPos(){
        hitBox.setLocation(x,y);
    }
}
