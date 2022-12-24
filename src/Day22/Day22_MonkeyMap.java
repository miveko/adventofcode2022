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
    Point direction, position;
    int cubeSide;
    int horizCorrection;
    Map<Character, Point> sidePosition;


    public static void main(String[] args) {
        Puzzle puzzle = new Day22_MonkeyMap();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        List<String> mapList = new ArrayList<>();
        int maxWidth = 0;
        int i = 0;
        while (inputReader.hasNextLine()) {
            mapList.add(inputReader.nextLine());
            if(mapList.get(i).length() > maxWidth)
                maxWidth = mapList.get(i).length();
        }

        map = new char[mapList.size() - 2][maxWidth];
        for(int x = 0; x < map.length; x++)
            for(int y = 0; y < maxWidth; y++)
                map[x][y] = (mapList.get(x).length() > y &&
                        (mapList.get(x).charAt(y) == '.' || mapList.get(x).charAt(y) == '#'))
                        ?   mapList.get(x).charAt(y)    :   ' ';
        path = mapList.get(mapList.size() - 1);
        position = new Point(0, mapList.get(0).indexOf("."));   //start from the most top left available space
        direction = new Point(0, 1);            //heading right at the start according to assignment
        navigate(true);
        setAnswerPartOne(String.valueOf(getPassword()));

        //Start PartTwo
        position = new Point(0, mapList.get(0).indexOf("."));   //start from the most top left available space
        direction = new Point(0, 1);            //heading right at the start according to assignment
        cubeSide = Math.max(map.length, map[0].length) / 4;
        //introducing horizontal correction to align sidePosition and COLORS
        horizCorrection = 2 - (position.x / cubeSide);   //Always starting from top left of White, where Red is downside, Green is leftside, Orange is upperside
        sidePosition = new HashMap<Character, Point>();                 ///////////////       W : white
                                  //| |G|W|B| |//       G : green
        for(int x = 0; x < map.length; x+=cubeSide)                         //|O|G|R|B|O|//       B : blue
            for(int y = 0; y < maxWidth; y+=cubeSide)                       //| |G|Y|B| |//       R : red
                if(map[x][y] != ' ') {                                      //| |O|O|O| |//       Y : yellow
                                                        ///////////////       O : orange
                    sidePosition.put(COLORS[x + horizCorrection][y], new Point(x, y));
                }

        position = new Point(0, mapList.get(0).indexOf("."));   //start from the most top left available space
        direction = new Point(0, 1);            //heading right at the start according to assignment
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
    }

    private void move(int steps, boolean partOne) {
        for(int i = 0; i < steps; i++) {
            Point nextPos = (partOne)   ? getNextPosition(position) :   getNextPosition();
            if(map[nextPos.x][nextPos.y] == '.') {
                position = nextPos;
            } else if(map[nextPos.x][nextPos.y] == '#') {
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
    }

    private Point getNextPosition() {       //Used in PartTwo
        Point nextPos = new Point(position.x + direction.x, position.y + direction.y);
        Point currPosRelative = new Point(position.x % cubeSide, position.y % cubeSide);
        if(map[nextPos.x][nextPos.y] == ' ') {
            Point currentSide = new Point(position.x / cubeSide + horizCorrection, position.y / cubeSide);  //coordinates on COLORS
            char currColor = COLORS[currentSide.x][currentSide.y];
            char nextColor = getNextColor(currColor, currentSide);
            Point nextSide = sidePosition.get(nextColor);
            int rotations = calcRotations(currentSide, nextSide);
            Point nextPosRelative = new Point();
            switch (rotations) {
                case 1: turnRight();
                        break;
                case 2: turnRight();
                        turnRight();
                        break;
                case 3: turnLeft();
                        break;
            }

        }


        return nextPos;
    }
    private void turnRight() {
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
    }
    private void turnLeft() {
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
    }

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


    private char getNextColor(char currColor, Point currentSide) {
        Point nextSide = new Point(currentSide.x + direction.x, currentSide.y + direction.y);           //coordinates on COLORS
        ////!!!!NOT GENERALIZED!!!!////
        char nextColor;
        if(nextSide. x < -1) {
            nextSide.x = COLORS.length - 1;
        }
        if (nextSide.x == COLORS.length) {
            nextSide.x = 0;
            nextSide.y = 2;
        }
        if(COLORS[nextSide.x][nextSide.y] == ' ' && nextSide.x == 3)  {
            nextSide.x = 0;
            nextSide.y = 2;
        }
        if(COLORS[nextSide.x][nextSide.y] == ' ' && nextSide.x != 3) {
            if(direction.y != 0)
                nextColor = OPP_SIDE.get(COLORS[currentSide.x][2]);
            else {
                nextColor = COLORS[currentSide.x][2];
            }
        } else {
            nextColor = COLORS[nextSide.x][nextSide.y];
        }
        if(currColor == nextColor) {
            nextColor = COLORS[nextSide.x][2];
        }
        ////!!!!NOT GENERALIZED!!!!////
        return nextColor;
    }

    private int calcRotations(Point currSide, Point nextSide) {
        int rotations = (currSide.x + 4 - nextSide.x) / 4;
        if(Math.abs(currSide.y - nextSide.y) > 1)
            rotations++;
        return rotations;
    }
}
