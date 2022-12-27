package Day22;

import java.awt.*;

public class TelePoint {
    private Point direction;
    private Point sideCell;

    private Point relCoordinates;

    public Point getDirection() {
        return direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getSideCell() {
        return sideCell;
    }

    public void setSideCell(Point sideCell) {
        this.sideCell = sideCell;
    }

    public Point getRelCoordinates() {
        return relCoordinates;
    }

    public void setRelCoordinates(Point relCoordinates) {
        this.relCoordinates = relCoordinates;
    }
    public TelePoint() {

    }
    public TelePoint(Point dir, Point side, Point relCoords) {
        direction = dir;
        sideCell = side;
        relCoordinates = relCoords;
    }


}
