package Day12;

import Base.Puzzle;
import java.awt.Point;
import java.util.*;

public class Day12_HillClimbingAlgorithm extends Puzzle {
//    private List<Point>[] map, transitionFrom, transitionTo;
    private char[][] map;
    int width, heigth;
    public static void main(String[] args) {
        Puzzle puzzle = new Day12_HillClimbingAlgorithm();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        Point me = new Point();
        Point goal = new Point();
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

        findShortestPath(me, goal);
    }

    private void findShortestPath(Point start, Point end) {
        boolean foundPathToA = false;
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
            }

            if(!foundPathToA)
                for(Point p : next)
                    if(map[p.x][p.y] == 'a') {
                        setAnswerPartTwo(String.valueOf(steps));
                        foundPathToA = true;
                    }

            if(steps == width * heigth) {
                System.out.println("Path does not exists!");
            }
        }

        setAnswerPartOne(String.valueOf(steps));
    }
}
