package Day12;

import Base.Puzzle;
import java.awt.Point;
import java.util.*;

public class Day12_HillClimbingAlgorithm extends Puzzle {
//    private List<Point>[] map, transitionFrom, transitionTo;
    private char[][] map;
    int width, heigth;
    Point me, goal;
    List<Point> exactPath;
    boolean foundPathToA;
    public static void main(String[] args) {
        Puzzle puzzle = new Day12_HillClimbingAlgorithm();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        List<List<Character>> mapList = new ArrayList<>();
        int i = 0;
        char c;
        while (inputReader.hasNextLine()) {
            String line = inputReader.nextLine();
            mapList.add(new ArrayList<>());
            for(int j = 0; j < line.length(); j++) {
                c = line.charAt(j);
                if (c == 'S') {
                    mapList.get(i).add('a');
                    me = new Point(i, j);
                } else if (c == 'E') {
                    mapList.get(i).add('z');
                    goal = new Point(i, j);
                } else {
                    mapList.get(i).add(c);
                }
            }

            i++;
        }

        //copping the input to an array (quicker access to elements)
        heigth = mapList.size();
        width = mapList.get(0).size();
        map = new char [heigth][width];
        for(i = 0; i < heigth; i++) {
            for(int j = 0; j < width; j++ )
                map[i][j] = mapList.get(i).get(j);
        }

        foundPathToA = false;
        exactPath = new LinkedList<>();
//        findShortestPath(me, goal);
        findExactShortestPath();    //not required by the assignment
    }

    private void findShortestPath(Point start, Point end) {
        int steps = 0;
        Set<Point> current;
        Set<Point> next = new HashSet<>();
        next.add(end);
        while (!next.contains(start)) {
            steps++;
            current = next;
            next = new HashSet<>();
            for(Point p : current) {
                if(p.x > 0 && map[p.x][p.y] - map[p.x - 1][p.y] < 2)
                    next.add(new Point(p.x - 1, p.y));
                if(p.x + 1 < heigth && map[p.x][p.y] - map[p.x + 1][p.y] < 2)
                    next.add(new Point(p.x + 1, p.y));
                if(p.y > 0 && map[p.x][p.y] - map[p.x][p.y - 1] < 2)
                    next.add(new Point(p.x, p.y - 1));
                if(p.y + 1 < width && map[p.x][p.y] - map[p.x][p.y + 1] < 2)
                    next.add(new Point(p.x, p.y + 1));
                if(isStartPointReached(p, next, steps, start))
                    break;
            }

            if(steps >= width * heigth) {
                System.err.println("Path does not exists!");
            }
        }
    }

    private boolean isStartPointReached(Point p, Set<Point> next, int steps, Point start) {
        if(!foundPathToA && map[p.x][p.y] == 'a') { //an 'a' has been reached the previous step
            setAnswerPartTwo(String.valueOf(steps - 1));
            foundPathToA = true;
        }

        if(next.contains(me))
            setAnswerPartOne(String.valueOf(steps));

        if(next.contains(start)) {
            exactPath.add(p);
            return true;
        } else
            return false;
    }

    private void findExactShortestPath() {
        exactPath.add(me);
        Point currentStart = exactPath.get(0);
        while (!currentStart.equals(goal)) {
            findShortestPath(currentStart, goal);
            currentStart = exactPath.get(exactPath.size() - 1);
        }
        //Printing the exact path
        for(Point p : exactPath)
            System.out.printf("[%d, %d] -> ", p.x, p.y);
        System.out.println("Win!");
    }
}
