package Day22;

import Base.Puzzle;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day22_MonkeyMap extends Puzzle {


    char[][] COLORS = new char[][] {{' ', 'G', 'W', 'B', ' '}, {'O', 'G', 'R', 'B', 'O'},
                                    {' ', 'G', 'Y', 'B', ' '}, {' ', 'O', 'O', 'O', ' '}};
    Map<Character, Character> OPP_SIDE = new HashMap<Character, Character>(){{put('W', 'Y');put('R', 'O');put('G', 'B');
                                                                             put('Y', 'W');put('O', 'R');put('B', 'G');}};

    char[][] map;
    String path;
    Point direction, lastDir, position;
    int cubeSide, horizCorrection;
    Map<Character, Point> sidePosition;
    Map<Point, Character> positionSide;
    TelePoint teleFrom, teleTo;     //!!!!!probably can be removed!!!!!!//


    public static void main(String[] args) {
        Puzzle puzzle = new Day22_MonkeyMap();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        List<String> mapList = new ArrayList<>();
        int maxWidth = 0;
        while (inputReader.hasNextLine()) {
            String line = inputReader.nextLine();
            if(line.length() == 0)
                break;
            else if(line.length() > maxWidth)
                maxWidth = line.length();
            mapList.add(line);
        }
        //Initializing map and path
        path = inputReader.nextLine();
        map = new char[mapList.size()][maxWidth];
        for(int x = 0; x < map.length; x++)
            for(int y = 0; y < maxWidth; y++)
                map[x][y] = (mapList.get(x).length() > y &&
                        (mapList.get(x).charAt(y) == '.' || mapList.get(x).charAt(y) == '#'))
                        ?   mapList.get(x).charAt(y)    :   ' ';

        //Start PartOne
        position = new Point(0, mapList.get(0).indexOf("."));   //start from the most top left available space
        direction = new Point(0, 1);            //heading right at the start according to assignment
        navigate(true);
        setAnswerPartOne(String.valueOf(getPassword()));

        //Start PartTwo
        position = new Point(0, mapList.get(0).indexOf("."));   //start from the most top left available space
        direction = new Point(0, 1);            //heading right at the start according to assignment
        cubeSide = Math.max(map.length, maxWidth) / 4;
        //introducing horizontal correction to align sidePosition and COLORS
        horizCorrection = 2 - (position.y / cubeSide);   //Always starting from top left of White, where Red is downside, Green is leftside, Orange is upperside
        sidePosition = new HashMap<>();
        positionSide = new HashMap<>();
        for(int x = 0; x < map.length; x+=cubeSide)
            for(int y = 0; y < maxWidth; y+=cubeSide)
                if(map[x][y] != ' ') {
                    int xOnCOLORS = x / cubeSide;
                    int yOnCOLORS = y / cubeSide + horizCorrection;
                    sidePosition.put(COLORS[xOnCOLORS][yOnCOLORS], new Point(xOnCOLORS, yOnCOLORS));
                    positionSide.put(new Point(xOnCOLORS, yOnCOLORS), COLORS[xOnCOLORS][yOnCOLORS]);
                }
        navigate(false);
        setAnswerPartTwo(String.valueOf(getPassword()));
    }

    private void navigate(boolean isPartOne) {
        StringBuilder num = new StringBuilder();
        for(char ch : path.toCharArray()) {
            if(ch == 'R') {
                int steps = Integer.parseInt(num.toString());
                move(steps, isPartOne);
                num = new StringBuilder();
                turnRight();
            } else if(ch == 'L') {
                int steps = Integer.parseInt(num.toString());
                move(steps, isPartOne);
                num = new StringBuilder();
                turnLeft();
            } else {
                num.append(ch);
            }
        }
        move(Integer.parseInt(num.toString()), isPartOne);
    }

    private void move(int steps, boolean partOne) {
        for(int i = 0; i < steps; i++) {
            Point nextPos = (partOne)   ? getNextPosition(position) :  getNextPosition();
            if(map[nextPos.x][nextPos.y] == '.') {
                position = nextPos;
                lastDir = null;          //!!!!!!!UGLY!!!!!!!//
                System.out.println(position.x + " " + position.y);
            } else if(map[nextPos.x][nextPos.y] == '#') {
                if(lastDir != null) {            //!!!!!!!UGLY!!!!!!!//
                    direction = lastDir;         //!!!!!!!UGLY!!!!!!!//
                    lastDir = null;                 //!!!!!!!UGLY!!!!!!!//
                }
                break;
            }
        }
    }

    private Point getNextPosition(Point p) {       //Used int PartOne
        Point nextPos = new Point(p.x + direction.x, p.y + direction.y);
        if(nextPos.x < 0)   nextPos.x = map.length - 1;
        else if(nextPos.x == map.length) nextPos.x = 0;
        else if(nextPos.y < 0) nextPos.y = map[0].length - 1;
        else if(nextPos.y == map[0].length) nextPos.y = 0;
        while (map[nextPos.x][nextPos.y] == ' ') {
            nextPos = getNextPosition(nextPos);
        }

        return nextPos;
    }                     //Used int PartOne

    private void turnRight() {          //clockwise
        if(direction.y == 1) {          //turn from right to bottom
            direction.y = 0;
            direction.x = 1;
        } else if(direction.y == -1) {  //turn from left to top
            direction.y = 0;
            direction.x = -1;
        } else if(direction.x == 1) {   //turn from bottom to left
            direction.y = -1;
            direction.x = 0;
        } else {                        //turn from up to right
            direction.y = 1;
            direction.x = 0;
        }
    }                                   //clockwise

    private void turnLeft() {           //counterclockwise
        if(direction.y == 1) {          //turn from right to top
            direction.y = 0;
            direction.x = -1;
        } else if(direction.y == -1) {  //turn from left to bottom
            direction.y = 0;
            direction.x = 1;
        } else if(direction.x == 1) {   //turn from bottom to right
            direction.y = 1;
            direction.x = 0;
        } else {                        //turn from up to left
            direction.y = -1;
            direction.x = 0;
        }
    }                                   //counterclockwise

    private int getPassword() {
        int result = (position.x + 1) * 1000 + (position.y + 1) * 4;
        if(direction.x == -1) {
            result += 3;
        } else if(direction.x == 1) {
            result += 1;
        } else if(direction.y == -1)  {
            result += 2;
        }

        return result;
    }

    private Point getNextPosition() {                               //Used in PartTwo
        Point nextPos = new Point(position.x + direction.x, position.y + direction.y);
        Point currPosRelative = new Point(position.x % cubeSide, position.y % cubeSide);
        if(!inMapBounds(nextPos) || map[nextPos.x][nextPos.y] == ' ') {
            Point currentSide = new Point(position.x / cubeSide, position.y / cubeSide + horizCorrection);  //coordinates on COLORS
            teleFrom = new TelePoint(direction, currentSide, currPosRelative);
            teleTo = new TelePoint();
            char nextColor = getNextColor(currentSide, direction);
            Point nextSide = sidePosition.get(nextColor);
            teleTo.setSideCell(nextSide);
            char currColor = COLORS[currentSide.x][currentSide.y];
            if(getNextColor(nextSide, new Point(0, 1)) == currColor) {
                teleTo.setDirection(new Point(0, 1));
            } else if(getNextColor(nextSide, new Point(0, -1)) == currColor) {
                teleTo.setDirection(new Point(0, -1));
            } else if(getNextColor(nextSide, new Point(1, 0)) == currColor) {
                teleTo.setDirection(new Point(1, 0));
            } else if(getNextColor(nextSide, new Point(-1, 0)) == currColor) {
                teleTo.setDirection(new Point(-1, 0));
            }

            calcCoordsAfterRotation();
            return  new Point(teleTo.getSideCell().x * cubeSide + teleTo.getRelCoordinates().x,
                    (teleTo.getSideCell().y - horizCorrection) * cubeSide + teleTo.getRelCoordinates().y);
        }                                                          //Used in PartTwo

        return nextPos;
    }
    private char getNextColor(Point currentSide, Point dir ) {
        Point nextSide = new Point(currentSide.x + dir.x, currentSide.y + dir.y);           //coordinates on COLORS
        Point nextOppSide = new Point(currentSide.x - dir.x, currentSide.y - dir.y);
        if(positionSide.get(nextSide) != null)
            return positionSide.get(nextSide);
        if(positionSide.get(nextOppSide) != null)
            return OPP_SIDE.get((positionSide.get(nextOppSide)));
        char nextColor = ' ';
        if(dir.y == 0) {  //direction is UP or DOWN
            if(nextSide.x == -1) return 'O';
            else if(positionSide.get(new Point(nextSide.x, nextSide.y - 1)) != null){
                return positionSide.get(new Point(nextSide.x, nextSide.y - 1));
            } else if(positionSide.get(new Point(nextSide.x, nextSide.y + 1)) != null){
                return positionSide.get(new Point(nextSide.x, nextSide.y + 1));
            } return COLORS[nextSide.x][2];
        } else {
            if(nextSide.x == 1) return COLORS[nextSide.x] [nextSide.y];
            else if(positionSide.get(new Point(nextSide.x - 1, nextSide.y)) != null){
                return positionSide.get(new Point(nextSide.x - 1, nextSide.y));
            } else if(positionSide.get(new Point(nextSide.x + 1, nextSide.y)) != null){
                return positionSide.get(new Point(nextSide.x + 1, nextSide.y));
            } return 'W';
        }
    }

    private void calcCoordsAfterRotation() {
        Point nextDir = new Point(teleTo.getDirection().x * -1, teleTo.getDirection().y * -1);
        lastDir = new Point(direction.x, direction.y);
        teleTo.setRelCoordinates(new Point(teleFrom.getRelCoordinates().x, teleFrom.getRelCoordinates().y));
        while (!direction.equals(nextDir)) {
            Point newPos = new Point();
            if(direction.y == -1 || direction.y == 1) {
                newPos.x = cubeSide - 1 - teleTo.getRelCoordinates().y;
            } else {
                newPos.x = teleTo.getRelCoordinates().y;
            }

            if(direction.x == -1) {
                newPos.y = teleTo.getRelCoordinates().x;
            } else {
                newPos.y = cubeSide - 1 - teleTo.getRelCoordinates().x;
            }
            teleTo.setRelCoordinates(newPos);
            turnRight();
        }
    }

    private boolean inMapBounds(Point pos) {
        return (pos.x >= 0 && pos.y >= 0 && pos.x < map.length && pos.y < map[0].length);
    }
}
