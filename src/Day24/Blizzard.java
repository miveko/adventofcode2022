package Day24;

import java.awt.*;

public class Blizzard {
    Point location;
    Point direction;


    public int getX() {
        return location.x;
    }
    public void setX(int x) {
        location.x = x;
    }
    public int getY() {
        return location.y;
    }
    public void setY(int y) {
        location.y = y;
    }

    public Blizzard(Blizzard blizzard) {
        location = new Point(blizzard.getX(), blizzard.getY());
        direction = new Point(blizzard.direction.x, blizzard.direction.y);
    }
    public Blizzard(Point pos, char dir) {
        location = pos;
        switch (dir) {
            case '>' : direction = new Point(0, 1);
            break;
            case '<' : direction = new Point(0, -1);
            break;
            case '^' : direction = new Point(-1, 0);
            break;
            case 'v' : direction = new Point(1, 0);
            break;
            default: direction = new Point();
        }
    }

    public Point update(int width, int height) {
        location.x += direction.x;
        location.y += direction.y;
        if(location.x == 0)
            location.x = height - 2;
        else if(location.y == 0)
            location.y = width - 2;
        else if(location.x == height - 1)
            location.x = 1;
        else if(location.y == width - 1)
            location.y = 1;
        return location;
    }

    public void update(char dir) {
        switch (dir) {
            case '>' : direction = new Point(0, 1);
                break;
            case '<' : direction = new Point(0, -1);
                break;
            case '^' : direction = new Point(-1, 0);
                break;
            case 'v' : direction = new Point(1, 0);
                break;
            default: direction = new Point();
        }

        location.x += direction.x;
        location.y += direction.y;
    }   //used for object "me"

}
