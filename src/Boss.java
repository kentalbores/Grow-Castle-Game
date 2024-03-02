import java.awt.*;

public class Boss extends GameObject{
    double xPos;
    Boss(int x, int y, int width, int height, Image sprite) {
        super(x, y, width, height, sprite);
    }
    @Override
    void setPos() {

        super.setPos();
        xPos += 0.2;
        if (xPos >= 1){
            x--;
            xPos = 0;
        }
        System.out.println(x + " : " + xPos);
    }
}
