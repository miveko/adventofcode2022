package Day14;

import Base.Puzzle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day14_RegolithReservoir extends Puzzle {

    Point POUR_SNOW_POINT = new Point(0, 500);
    List<Point> matrix;
    int xMax, yMax, yMin;
    boolean[][] filled_PartOne, filled_partTwo;

    public static void main(String[] args) {
        Puzzle puzzle = new Day14_RegolithReservoir();
        puzzle.execute(args);
    }

    protected void solution(Scanner inputReader) {
        matrix = new ArrayList<>();
        xMax = Integer.MIN_VALUE;
        yMax = Integer.MIN_VALUE;
        yMin = Integer.MAX_VALUE;
        while (inputReader.hasNextLine()) {
            String[] points = inputReader.nextLine().split(" -> ");
            for (int i = 0; i < points.length - 1; i++) {
                setFilled(points[i], points[i + 1]);
            }
        }


        filled_PartOne = new boolean[xMax + 1][yMax - yMin + 1];
        for(Point p : matrix)
            filled_PartOne[p.x][p.y - yMin] = true;
        Point START_POINT = new Point(POUR_SNOW_POINT.x, POUR_SNOW_POINT.y - yMin);
        setAnswerPartOne(String.valueOf(simulateSandPouring(filled_PartOne, START_POINT)));

        xMax += 2;
        yMin = Math.min(yMin, 500 - xMax);
        yMax = Math.max(yMax, 500 + xMax);
        filled_partTwo = new boolean[xMax + 1][yMax - yMin + 1];
        for(Point p : matrix)
            filled_partTwo[p.x][p.y - yMin] = true;
        Arrays.fill(filled_partTwo[xMax], true);
        START_POINT = new Point(POUR_SNOW_POINT.x, POUR_SNOW_POINT.y - yMin);
        setAnswerPartTwo(String.valueOf(simulateSandPouring(filled_partTwo, START_POINT)));
    }

    private int simulateSandPouring(boolean[][] filled, Point START_POINT ) {
        int sandUnitsCount = 0;
        boolean outOfBounds = false;
        while (!outOfBounds && !filled[START_POINT.x][START_POINT.y]) {
            //show(filled);
            Point sandUnit = new Point(START_POINT);
            boolean stuck = false;
            while (!stuck) {
                Point nextStep = getNextStep(sandUnit, filled);
                if(sandUnit.equals(nextStep)) { //sandUnit position established
                    stuck = true;
                    filled[nextStep.x][nextStep.y] = true;
                    sandUnitsCount++;
                } else {
                    sandUnit = nextStep;    //sandUnit position moving
                    if(nextStep.y < 0 || nextStep.y == filled[0].length || nextStep.x > xMax) {
                        outOfBounds = true; //sandUnit flowing into the abyss
                        break;

                    }
                }
            }
        }
        return sandUnitsCount;
    }

    private Point getNextStep(Point sand, boolean[][] filled) {
        if(sand.x + 1 > xMax || !filled[sand.x + 1][sand.y])
            return new Point(sand.x + 1, sand.y);
        else if(sand.y - 1 == -1 || !filled[sand.x + 1][sand.y - 1])
            return new Point(sand.x + 1, sand.y - 1);
        else if(sand.y + 1 == filled[0].length || !filled[sand.x + 1][sand.y + 1])
            return new Point(sand.x + 1, sand.y + 1);
        else
            return sand;
    }

    private void setFilled(String from, String to) {
        String[] xy = from.split(",");
        Point pFrom = new Point(new Point(Integer.parseInt(xy[1]), Integer.parseInt(xy[0])));
        xy = to.split(",");
        Point pTo = new Point(new Point(Integer.parseInt(xy[1]), Integer.parseInt(xy[0])));
        for(int i = Math.min(pFrom.x, pTo.x); i <= Math.max(pFrom.x, pTo.x); i++)
            for(int j = Math.min(pFrom.y, pTo.y); j <= Math.max(pFrom.y, pTo.y); j++) {
                Point p = new Point(i, j);
                matrix.add(p);
                if(p.x > xMax) xMax = p.x;
                if(p.y > yMax) yMax = p.y;
                if(p.y < yMin) yMin = p.y;
            }
    }

    private void show(boolean[][] filled) {
        System.out.println("--------------------------------------");
        for(boolean[] i : filled) {
            for (boolean j : i) {
                if(j) System.out.print("#");
                else System.out.print(".");
            }
            System.out.println();
        }
    }
}
