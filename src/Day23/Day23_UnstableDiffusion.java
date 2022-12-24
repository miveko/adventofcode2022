package Day23;

import Base.Puzzle;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day23_UnstableDiffusion extends Puzzle {

    int BUFFER_SIZE = 10;
    Point[][] DIRECTION = new Point[][] {{new Point(-1, -1), new Point(-1, 0), new Point(-1, 1)},   //NORTH
            {new Point(1, -1), new Point(1, 0), new Point(1, 1)},         //SOUTH
            {new Point(-1, -1), new Point(0, -1), new Point(1, -1)},     //WEST
            {new Point(-1, 1), new Point(0, 1), new Point(1, 1)}};      //EAST
    int roundsAkaDirIndex;
    List<String> inputMap;
    boolean[][] map;
    List<Point> toPropose;
    Map<Point, Integer> proposedOnes;
    Map<Point, Point> proposedFrom;

    public static void main(String[] args) {
        Puzzle puzzle = new Day23_UnstableDiffusion();
        puzzle.execute(args);
    }

    @Override
    protected void solution(Scanner inputReader) {
        inputMap = new ArrayList<>();
        while (inputReader.hasNextLine()) {
            inputMap.add(inputReader.nextLine());
        }

        map = new boolean[inputMap.size() + 2 * BUFFER_SIZE][inputMap.get(0).length() + 2 * BUFFER_SIZE];
        for(int i = 0; i < inputMap.size(); i++)
            for(int j = 0; j < inputMap.get(i).length(); j++)
                if(inputMap.get(i).charAt(j) == '#')
                    map[i + BUFFER_SIZE][j + BUFFER_SIZE] = true;

        roundsAkaDirIndex = 0;
        boolean anyHasMoved = true;
        while(anyHasMoved) {
            selectToPropose();
            proposeMoves();
            anyHasMoved = move();
//            showMap();
            roundsAkaDirIndex++;
            if(roundsAkaDirIndex == 10) {
                setAnswerPartOne(String.valueOf(calcResultAndExpand()));
            }
            if(roundsAkaDirIndex % 15 == 0)
                calcResultAndExpand();
        }

        setAnswerPartTwo(String.valueOf(roundsAkaDirIndex));
    }

    private void selectToPropose() {
        toPropose = new ArrayList<>();
        for(int i = 1; i < map.length - 1; i++) {
            for(int j = 1; j < map[i].length - 1; j++) {
                if(map[i][j] && hasSurround(i, j)) {
                    toPropose.add(new Point(i, j));
                }
            }
        }
    }

    private boolean hasSurround(int i, int j) {
        return  (map[i-1][j-1] || map[i-1][j] || map[i-1][j+1] || map[i][j-1]
            ||map[i][j +1] || map[i+1][j-1] || map[i+1][j] || map[i+1][j+1]);
    }
    private void proposeMoves() {
        proposedOnes = new HashMap<>();
        proposedFrom = new HashMap<>();
        for(Point p : toPropose) {
            for(int i = roundsAkaDirIndex; i < roundsAkaDirIndex +4; i++) {
                boolean dirFree = true;
                for(Point d : DIRECTION[i % 4]) {
                    if (map[p.x + d.x][p.y + d.y]) {
                        dirFree = false;
                        break;
                    }
                }

                if(dirFree) {
                    Point pr = new Point(p.x + DIRECTION[i % 4][1].x, p.y + DIRECTION[i % 4][1].y);
                    if (proposedOnes.containsKey(pr)) {
                        int count = proposedOnes.get(pr) + 1;
                        proposedOnes.put(pr, count);
                    } else {
                        proposedOnes.put(pr, 1);
                        proposedFrom.put(pr, p);
                    }
                    break;
                }
            }
        }
    }

    private boolean move() {
        boolean anyHasMoved = false;
        for(Point p : proposedOnes.keySet()) {
            if(proposedOnes.get(p) == 1) {
                map[p.x][p.y] = true;
                map[proposedFrom.get(p).x][proposedFrom.get(p).y] = false;
                anyHasMoved = true;
            }
        }
        return anyHasMoved;
    }

    private int calcResultAndExpand() {
        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        int count = 0;
        for(int i = 0; i < map.length; i++)
            for(int j = 0; j < map[i].length; j++) {
                if (map[i][j]) {
                    count++;
                    if (i < xMin) xMin = i;
                    if (i > xMax) xMax = i;
                    if (j < yMin) yMin = j;
                    if (j > yMax) yMax = j;
                }
            }

        int xExpand = 0;
        int yExpand = 0;
        if(xMin < 2 || xMax - map.length < 2)
            xExpand = BUFFER_SIZE;
        if(yMin < 2 || yMax - map[0].length < 2)
            yExpand = BUFFER_SIZE;
        if(xExpand > 0 || yExpand >0) {
            boolean[][] map2 = map;
            map = new boolean[map2.length + 2 * xExpand][map2[0].length + 2 * yExpand];
            for(int i = 0; i < map2.length; i++)
                for(int j = 0; j < map2[0].length; j++)
                    if(map2[i][j])
                        map[i + BUFFER_SIZE][j + BUFFER_SIZE] = true;
        }

        return (xMax - xMin + 1) * (yMax - yMin + 1) - count;
    }

    private void showMap() {
        System.out.println("__________________________________________");
        for (boolean[] booleans : map) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) System.out.print('#');
                else System.out.print('.');
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
    }
}
